package org.needlehack.searchapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.searchbox.client.JestClient
import io.searchbox.core.Search
import io.searchbox.core.SearchResult
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@RunWith(SpringRunner::class)
@WebMvcTest
class SearchApiApplicationTests {

    @MockBean
    lateinit var client: JestClient
    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var mapper: ObjectMapper

    @Test
    fun `given a searching term when the searching is executed then a json with results is returned`() {
        // given
        val term = "testing with kotlin"
        val result = buildSearchResult()
        given(client.execute(any(Search::class.java))).willReturn(result)

        // when
        var response = mockMvc.perform(MockMvcRequestBuilders
                .get("/search")
                .param("term", term)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))


        // then
        response
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(expectedJson))
    }

    private fun buildSearchResult(): SearchResult {
        val result = SearchResult(GsonBuilder().create())
        result.jsonObject = buildJsonObject()
        result.jsonString = json
        result.isSucceeded = true
        result.pathToResult = "hits/hits/_source"
        return result
    }

    private fun buildJsonObject(): JsonObject {
        return JsonParser().parse(json).getAsJsonObject();
    }

    val expectedJson = "[{\"source\":{\"generatedId\":\"1\",\"title\":\"My Awesome post\",\"uri\":\"http://www.david-romero.github.com/awesome-post\",\"creator\":\"David Romero\",\"origin\":\"Twitter\",\"topics\":[\"Kotlin\",\"10x Engineers\"]},\"explanation\":null,\"highlight\":null,\"sort\":null,\"index\":\"twitter\",\"type\":\"tweet\",\"id\":\"1\",\"score\":null,\"parent\":null,\"routing\":null,\"matchedQueries\":[]}]"

    val json = """
        {
            "_shards" : {
                "total" : 5,
                "successful" : 5,
                "failed" : 0
            },
            "hits" : {
                "max_score" : 0.028130025,
                "total" : 1,
                "hits" : [
                    {
                        "_index" : "twitter",
                        "_type" : "tweet",
                        "_id" : "1",
                        "_source" : {
                                "generatedId" : "1",
                                "title" : "My Awesome post",
                                "uri" : "http://www.david-romero.github.com/awesome-post",
                                "creator" : "David Romero",
                                "origin" : "Twitter",
                                "topics" : [ "Kotlin","10x Engineers" ]
                        }
                    }
                ]
            }
        }
        """.trimIndent()

    /*private fun doTest(input: Request, expectation: Response) {
        mockMvc.perform {
            it.set
        }
                .get("/mockmvc/validate") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(expectation)) }
        }
    }*/

}
