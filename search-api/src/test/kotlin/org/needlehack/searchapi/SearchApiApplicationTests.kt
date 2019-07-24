package org.needlehack.searchapi

import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.elasticsearch.ElasticsearchContainer


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [ElasticsearchInitializer::class])
class SearchApiApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc


    @Test
    fun `given a searching term when the searching is executed then a json with results is returned`() {
        //TODO: Add TestContainers https://www.testcontainers.org/modules/elasticsearch/
        // given
        val term = "kotlin"

        // when
        var response = mockMvc.perform(MockMvcRequestBuilders
                .get("/search?term=$term")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))


        // then
        response
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(expectedJson))
    }

    val expectedJson = "[{\"generatedId\":\"C5FEA1359B85A72B180D4BEEB8D0AE4A\",\"title\":\"Kotlin 1.3.30 released\",\"uri\":\"https://blog.jetbrains.com/kotlin/2019/04/kotlin-1-3-30-released/\",\"creator\":\"Svetlana Isakova\",\"content\":\"<p>We’re happy to announce the release of Kotlin 1.3.30, a new bug fix and tooling update for Kotlin...\",\"topics\":[\"newsletter\",\"Releases\"]},{\"generatedId\":\"7855D1FCE58D58DC2811683D579ED869\",\"title\":\"Kotlin at Trello\",\"uri\":\"http://tech.trello.com/kotlin-at-trello/\",\"creator\":\"\",\"content\":\"...\",\"topics\":[]},{\"generatedId\":\"3239739DFA20D145E6F9F927E06D2CCA\",\"title\":\"Kotlin first impressions\",\"uri\":\"https://blog.pchudzik.com/201711/kotlin-first-impressions/\",\"creator\":\"\",\"content\":\"...\",\"topics\":[]},{\"generatedId\":\"60C9BC57987E5A0F4833456E9372B183\",\"title\":\"Kotlin Census 2018\",\"uri\":\"https://blog.jetbrains.com/kotlin/2018/12/kotlin-census-2018/\",\"creator\":\"Alina Dolgikh\",\"content\":\"<p>Our Kotlin community is growing fast; the number of users has increased by almost 3 times this ye...\",\"topics\":[\"sendtoall\",\"census\",\"community\"]},{\"generatedId\":\"6FA70B4C794DCC7309155D35C7CEC152\",\"title\":\"Kotlin 1.3.40 released\",\"uri\":\"https://blog.jetbrains.com/kotlin/2019/06/kotlin-1-3-40-released/\",\"creator\":\"Eugene Petrenko\",\"content\":\"<p>We’re happy to present the new release today, Kotlin 1.3.40. In addition to the quality and tooli...\",\"topics\":[\"newsletter\",\"Releases\"]},{\"generatedId\":\"8494EFBBD94D8656751260A29D371475\",\"title\":\"Kotlin 1.3.20 released\",\"uri\":\"https://blog.jetbrains.com/kotlin/2019/01/kotlin-1-3-20-released/\",\"creator\":\"Hadi Hariri\",\"content\":\"<p>We’re happy to announce the release of Kotlin 1.3.20, a new bug fix and tooling update for Kotlin...\",\"topics\":[\"newsletter\",\"Releases\",\"sendtoall\"]},{\"generatedId\":\"122CA6AB56EC4B08B223DEECA978E286\",\"title\":\"Gradle Kotlin DSL 1.0\",\"uri\":\"https://blog.jetbrains.com/kotlin/2018/12/gradle-kotlin-dsl-1-0/\",\"creator\":\"Roman Belov\",\"content\":\"<p><center></p>\\n<div style=\\\"background-color: #f1f6fe; margin-bottom: 40px; padding: 15px; line-heig...\",\"topics\":[\"newsletter\",\"guestpost\",\"Tools\",\"community\"]},{\"generatedId\":\"39E45122F1514DEA9B1DB5D462D869E8\",\"title\":\"Lovin’ Kotlin since 2015\",\"uri\":\"http://engineering.vinted.com//2017/05/29/kotlin/\",\"creator\":\"Martynas Jurkus\",\"content\":\"<p>At Google I/O ‘17 the Android team <a href=\\\"https://youtu.be/Y2VF8tmLFHw?t=1h27m\\\">announced first...\",\"topics\":[]},{\"generatedId\":\"2A279766181A0B14D64A53BBFB8986E7\",\"title\":\"Kotlin 1.2.70 is Out!\",\"uri\":\"https://blog.jetbrains.com/kotlin/2018/09/kotlin-1-2-70-is-out/\",\"creator\":\"Sergey Igushkin\",\"content\":\"<p>We’re happy to announce the release of Kotlin 1.2.70, a new bugfix and tooling update for Kotlin ...\",\"topics\":[\"newsletter\",\"Releases\"]},{\"generatedId\":\"FF8D3AD17B7C7A5E5ED2207D6991026E\",\"title\":\"Kotlin 1.3 Released with Coroutines, Kotlin/Native Beta, and more\",\"uri\":\"https://blog.jetbrains.com/kotlin/2018/10/kotlin-1-3/\",\"creator\":\"Roman Belov\",\"content\":\"<p><center></p>\\n<div style=\\\"margin:-30px 0px 10px 0px;padding:10px;border-color:#f0f0f0;display:inli...\",\"topics\":[\"Native\",\"Releases\",\"sendtoall\",\"Events\",\"Tools\",\"Android\"]}]"

    companion object {
        @ClassRule @JvmField
        val elasticsearchContainer = ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.2.0")
    }

}
