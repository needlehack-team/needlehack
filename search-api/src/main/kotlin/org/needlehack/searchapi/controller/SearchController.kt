package org.needlehack.searchapi.controller


import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders.disMaxQuery
import org.elasticsearch.index.query.QueryBuilders.termQuery
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.needlehack.searchapi.model.FeedItem
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController

class SearchController(val client: RestHighLevelClient, @Value("\${jest.elasticsearch.index}") val indexName: String, val mapper: ObjectMapper) {

    val log = LoggerFactory.getLogger(SearchController::class.java)

    @GetMapping("/search")
    fun search(@RequestParam(value = "term", defaultValue = "*") term: String) : List<FeedItem> {

        log.info("You want to seach: [$term]")
        val searchSource = SearchSourceBuilder()
                .query(disMaxQuery()
                        .add(termQuery("topics", term))
                        .add(termQuery("title", term)).tieBreaker(0.3f))

        val searchRequest = SearchRequest()
        searchRequest.indices(indexName)
        searchRequest.source(searchSource)

        val resultList = client.search(searchRequest, RequestOptions.DEFAULT).hits.hits.map {
            it.sourceAsString
        }.map {
            mapper.readValue(it, FeedItem::class.java)
        }.toList()

        log.info("found: ${resultList.size} items")
        //TODO: Returns a DTO instead of a JEST Object
        return resultList
    }
}