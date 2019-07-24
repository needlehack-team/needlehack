package org.needlehack.searchapi

import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.ElasticsearchStatusException
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.get.GetIndexRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.needlehack.searchapi.model.FeedItem
import org.needlehack.searchapi.model.Topic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*


@Component
class DummyLoadCommandRunner(val highLevelclient: RestHighLevelClient,
                             @Value("\${elasticsearch.index}") val indexName: String,
                             val jacksonObjectMapper: ObjectMapper) : CommandLineRunner {

    override fun run(vararg args: String?) {
        for (feedNumber in 1..15 step 2){
            val item = FeedItem(UUID.randomUUID().toString(),"Dummy article about kotlin", "https://david-romero.github.io//articles/2019-05/jbcnconf-2019", "David Romero",
                    "Kotlin is awesome", setOf(Topic("kotlin")), Date(), Date())
            val item2 = FeedItem(UUID.randomUUID().toString(),"Dummy article about kotlin", "https://david-romero.github.io//articles/2019-05/jbcnconf-2019", "David Romero",
                    "Kotlin is awesome", setOf(Topic("kotlin")), Date(), Date())
            create(item, item2)
        }

    }

    fun create(firstItem: FeedItem, second: FeedItem) {
        val json1 = jacksonObjectMapper.writeValueAsString(firstItem)
        val json2 = jacksonObjectMapper.writeValueAsString(second)
        val indexRequest = IndexRequest("feed-collector").type("feedItem")
                .source(XContentType.JSON, json1, json2)

        highLevelclient.index(indexRequest, RequestOptions.DEFAULT)

    }

    private fun existsIndex(e : ElasticsearchStatusException?): Boolean {
        val contains = e?.message?.contains("resource_already_exists_exception")
        return contains?:false
    }
}