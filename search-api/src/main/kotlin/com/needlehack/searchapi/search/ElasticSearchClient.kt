package com.needlehack.searchapi.search

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.weddini.throttling.Throttling
import com.weddini.throttling.ThrottlingType
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.ScoreSortBuilder
import org.elasticsearch.search.sort.SortOrder
import com.needlehack.searchapi.model.FeedItem
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class ElasticSearchClient(val client: RestHighLevelClient,
                          @Value("\${elasticsearch.index}") val indexName: String,
                          val jacksonObjectMapper: ObjectMapper) : SearchClient {

    // https://medium.com/teamarimac/implementing-throttling-in-java-spring-boot-ec4723cfce9f
    @Throttling(type = ThrottlingType.RemoteAddr, expression = "\${app.limit}", timeUnit = TimeUnit.SECONDS)
    override fun invoke(term: String, pageRequest: PageRequest): Set<FeedItem> {
        val searchSource = SearchSourceBuilder()
                .query(QueryBuilders.disMaxQuery()
                        .add(QueryBuilders.matchQuery("topics", term))
                        .add(QueryBuilders.matchQuery("content", term))
                        .add(QueryBuilders.matchQuery("title", term))
                        .tieBreaker(0.3f))
                .from(pageRequest.pageNumber.times(pageRequest.pageSize))
                .size(pageRequest.pageSize)
                .sort(ScoreSortBuilder().order(SortOrder.DESC))
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

    fun String.asDomainObject(): FeedItem = jacksonObjectMapper.readValue(this)

}