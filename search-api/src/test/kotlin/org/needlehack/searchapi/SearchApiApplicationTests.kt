package org.needlehack.searchapi

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.testcontainers.elasticsearch.ElasticsearchContainer
import java.util.concurrent.TimeUnit


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [ElasticsearchInitializer::class, ThrottlingInitializer::class])
class SearchApiApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc


    @Test
    fun `given a searching term when the searching is executed then a json with results is returned`() {
        // given
        val term = "kotlin"

        // when
        val response = mockMvc.perform(MockMvcRequestBuilders
                .get("/search?term=$term")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))


        // then
        response
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content").isArray)
                .andExpect(jsonPath("$.content", hasSize<Any>(10)))
                .andExpect(jsonPath("$.content[0].title").value("Dummy article about kotlin 15"))
                .andExpect(jsonPath("$.content[0].uri").value("https://david-romero.github.io//articles/2019-05/jbcnconf-2019"))
                .andExpect(jsonPath("$.content[0].creator").value("David Romero"))
                .andExpect(jsonPath("$.content[0].content").value("Kotlin is awesome..."))
                .andExpect(jsonPath("$.content[0].topics").isArray)
                .andExpect(jsonPath("$.content[0].topics", hasSize<Any>(1)))
                .andExpect(jsonPath("$.content[0].topics[0]").value("kotlin"))
                .andExpect(jsonPath("$.numberOfElements").value(10))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10))
    }

    @Test
    fun `given a searching term and the second page when the searching is executed then a partial page should be retrieved`() {
        // given
        val term = "kotlin"
        val page = 1

        // when
        val response = mockMvc.perform(MockMvcRequestBuilders
                .get("/search?term=$term&page=$page")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))


        // then
        response
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content").isArray)
                .andExpect(jsonPath("$.content", hasSize<Any>(5)))
                .andExpect(jsonPath("$.content[0].title").value("Dummy article about kotlin 5"))
                .andExpect(jsonPath("$.content[0].uri").value("https://david-romero.github.io//articles/2019-05/jbcnconf-2019"))
                .andExpect(jsonPath("$.content[0].creator").value("David Romero"))
                .andExpect(jsonPath("$.content[0].content").value("Kotlin is awesome..."))
                .andExpect(jsonPath("$.content[0].topics").isArray)
                .andExpect(jsonPath("$.content[0].topics", hasSize<Any>(1)))
                .andExpect(jsonPath("$.content[0].topics[0]").value("kotlin"))
                .andExpect(jsonPath("$.numberOfElements").value(5))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(10))
    }

    @Test
    fun `given 200 requests per minute when a new requests is performed by the same ip then a too many requests is returned`() {
        // given
        performSearchRequest(240) // 4 per second, 240 per minute
        val term = "kotlin"

        // when
        val response = mockMvc.perform(MockMvcRequestBuilders
                .get("/search?term=$term")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))

        // then
        response
                .andExpect(status().isTooManyRequests)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
    }

    @Test
    fun `given an uppercase search term when a search is performed then a json with results is returned`() {
        /// given
        val term = "AWESOME"

        // when
        val response = mockMvc.perform(MockMvcRequestBuilders
                .get("/search?term=$term")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))


        // then
        response
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content").isArray)
                .andExpect(jsonPath("$.content", hasSize<Any>(10)))
                .andExpect(jsonPath("$.content[0].title").value("Dummy article about kotlin 15"))
                .andExpect(jsonPath("$.content[0].uri").value("https://david-romero.github.io//articles/2019-05/jbcnconf-2019"))
                .andExpect(jsonPath("$.content[0].creator").value("David Romero"))
                .andExpect(jsonPath("$.content[0].content").value("Kotlin is awesome..."))
                .andExpect(jsonPath("$.content[0].topics").isArray)
                .andExpect(jsonPath("$.content[0].topics", hasSize<Any>(1)))
                .andExpect(jsonPath("$.content[0].topics[0]").value("kotlin"))
                .andExpect(jsonPath("$.numberOfElements").value(10))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10))
    }

    @Before
    fun setUp() {
        TimeUnit.SECONDS.sleep(1)
    }

    fun performSearchRequest(numberOrRequests: Int) = runBlocking {
        repeat(numberOrRequests) {
            launch {
                performRequest()
                println("coroutine $it")
            }
        }
    }

    suspend fun performRequest() {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/search?term=Test")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
    }


    companion object {
        @ClassRule
        @JvmField
        val elasticsearchContainer = ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.2.0")
    }

}


