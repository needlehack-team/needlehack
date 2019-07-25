package org.needlehack.searchapi.search

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.needlehack.searchapi.model.FeedItem
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SearchClient(val client: RestHighLevelClient, @Value("\${jest.elasticsearch.index}") val indexName: String, val jacksonObjectMapper: ObjectMapper) {


    fun invoke(term: String): Set<FeedItem> {
        val searchSource = SearchSourceBuilder()
                .query(QueryBuilders.disMaxQuery()
                        .add(QueryBuilders.termQuery("topics", term))
                        .add(QueryBuilders.termQuery("title", term)).tieBreaker(0.3f))

        val searchRequest = SearchRequest()
        searchRequest.indices(indexName)
        searchRequest.source(searchSource)

        return client.search(searchRequest).hits.hits.map {
            it.sourceAsString
        }.map {
            it.asDomainObject()
        }.toSet()
    }

    fun String.asDomainObject(): FeedItem = jacksonObjectMapper.readValue<FeedItem>(this)

}