package org.needlehack.searchapi.config

import org.elasticsearch.ElasticsearchStatusException
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.get.GetIndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class CreationIndexCommandRunner(val highLevelclient: RestHighLevelClient,
                                 @Value("\${elasticsearch.index}") val indexName: String) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val createIndexRequest = CreateIndexRequest(indexName)
        try {
            highLevelclient.indices().create(createIndexRequest, RequestOptions.DEFAULT)
        } catch (e : ElasticsearchStatusException){
            if (!existsIndex(e))
                throw e
        }
    }

    private fun existsIndex(e : ElasticsearchStatusException?): Boolean {
        val contains = e?.message?.contains("resource_already_exists_exception")
        return contains?:false
    }
}