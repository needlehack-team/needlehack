package org.needlehack.searchapi.controller

import io.searchbox.client.JestClient
import io.searchbox.core.Search
import io.searchbox.core.SearchResult
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

class SearchController(val client: JestClient, @Value("\${jest.elasticsearch.index}") val indexName: String) {

    val log = LoggerFactory.getLogger(SearchController::class.java)

    @GetMapping("/search")
    fun search(@RequestParam(value = "term", defaultValue = "*") term: String): List<SearchResult.Hit<FeedItem, Void>> {

        log.info("You want to seach: [$term]")
        val searchSource = SearchSourceBuilder()
                .query(disMaxQuery()
                        .add(termQuery("topics", term))
                        .add(termQuery("title", term)).tieBreaker(0.3f))
                .toString()

        val search = Search.Builder(searchSource).addIndex(indexName)

        val resultList = client.execute(search.build()).getHits(FeedItem::class.java)

        log.info("found: ${resultList.size} items")
        //TODO: Returns a DTO instead of a JEST Object
        return resultList
    }
}