package org.needlehack.searchapi.search

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.SortOrder
import org.needlehack.searchapi.model.FeedItem
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class SearchClient(val client: RestHighLevelClient, @Value("\${elasticsearch.index}") val indexName: String, val jacksonObjectMapper: ObjectMapper) {


    fun invoke(term: String, pageRequest: PageRequest): Set<FeedItem> {
        val searchSource = SearchSourceBuilder()
                .query(QueryBuilders.disMaxQuery()
                        .add(QueryBuilders.termQuery("topics", term))
                        .add(QueryBuilders.termQuery("title", term))
                        .tieBreaker(0.3f))
                .from(pageRequest.pageNumber.times(pageRequest.pageSize))
                .size(pageRequest.pageSize)
                .sort("collectAt", SortOrder.DESC)

        val searchRequest = SearchRequest()
        searchRequest.indices(indexName)
        searchRequest.source(searchSource)

        return client.search(searchRequest, RequestOptions.DEFAULT).hits.hits.map {
            it.sourceAsString
        }.map {
            it.asDomainObject()
        }.toSet()
    }

    fun String.asDomainObject(): FeedItem = jacksonObjectMapper.readValue<FeedItem>(this)

}