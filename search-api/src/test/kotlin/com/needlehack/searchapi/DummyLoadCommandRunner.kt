package com.needlehack.searchapi

import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import com.needlehack.searchapi.controller.SearchController
import com.needlehack.searchapi.model.FeedItem
import com.needlehack.searchapi.model.Topic
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*


@Component
class DummyLoadCommandRunner(val highLevelclient: RestHighLevelClient,
                             @Value("\${elasticsearch.index}") val indexName: String,
                             val jacksonObjectMapper: ObjectMapper) : CommandLineRunner {

    val log = LoggerFactory.getLogger(SearchController::class.java)

    override fun run(vararg args: String?) {
        for (feedNumber in 1..15) {
            val item = FeedItem(UUID.randomUUID().toString(), "Dummy article about kotlin $feedNumber", "https://david-romero.github.io//articles/2019-05/jbcnconf-2019", "David Romero",
                    "Kotlin is awesome", setOf(Topic("kotlin")), Date(), Date())
            create(item)
        }

    }

    fun create(feedItem: FeedItem) {
        val feedAsJson = jacksonObjectMapper.writeValueAsString(feedItem)
        val indexRequest = IndexRequest("feed-collector")
                .id(feedItem.generatedId)
                .source(feedAsJson, XContentType.JSON)

        try {
            val indexResponse = highLevelclient.index(indexRequest, RequestOptions.DEFAULT)
            log.debug("New feedItem indexed [{}]", indexResponse)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }


}