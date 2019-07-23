package org.needlehack.searchapi.controller


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders.disMaxQuery
import org.elasticsearch.index.query.QueryBuilders.termQuery
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.needlehack.searchapi.dto.Post
import org.needlehack.searchapi.model.FeedItem
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController

class SearchController(val client: RestHighLevelClient, @Value("\${jest.elasticsearch.index}") val indexName: String, val jacksonObjectMapper: ObjectMapper) {

    val log = LoggerFactory.getLogger(SearchController::class.java)

    @GetMapping("/search")
    fun search(@RequestParam(value = "term", defaultValue = "*") term: String): List<Post> {

        log.info("You want to seach: [$term]")
        val searchSource = SearchSourceBuilder()
                .query(disMaxQuery()
                        .add(termQuery("topics", term))
                        .add(termQuery("title", term)).tieBreaker(0.3f))

        val searchRequest = SearchRequest()
        searchRequest.indices(indexName)
        searchRequest.source(searchSource)

        val resultList = client.search(searchRequest).hits.hits.map {
            it.sourceAsString
        }.map {
            it.asDomainObject()
        }.map {
            it.toDto()
        }.toList()

        log.info("found: ${resultList.size} items")
        return resultList
    }

    fun String.asDomainObject(): FeedItem = jacksonObjectMapper.readValue<FeedItem>(this)

    fun FeedItem.toDto(): Post {
        val topics = (this.topics ?: emptySet()).map { it.tag }.toSet()
        return Post(this.generatedId, this.title, this.uri, this.creator, getContentPreview(), topics)
    }

    fun FeedItem.getContentPreview() : String {
        val content = (this.content ?: "")
        return content.substring(100) + "...";
    }

    fun String.substring(maxIndex : Int) : String = if (this.length < maxIndex) substring(0, this.length) else substring(0 , maxIndex)


}