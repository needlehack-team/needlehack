package org.needlehack.searchapi.controller


import org.needlehack.searchapi.dto.Post
import org.needlehack.searchapi.model.FeedItem
import org.needlehack.searchapi.search.SearchClient
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController(val client: SearchClient) {

    val log = LoggerFactory.getLogger(SearchController::class.java)

    @GetMapping("/search")
    fun search(@RequestParam(value = "term", defaultValue = "*") term: String): List<Post> {

        log.info("You want to seach: [$term]")

        val resultList = client.invoke(term)
                .map {
                    it.toDto()
                }.toList()

        log.info("found: ${resultList.size} items")
        return resultList
    }


    fun FeedItem.toDto(): Post {
        val topics = (this.topics ?: emptySet()).map { it.tag }.toSet()
        return Post(this.generatedId, this.title, this.uri, this.creator, getContentPreview(), topics)
    }

    fun FeedItem.getContentPreview(): String {
        val content = (this.content ?: "")
        return content.substring(100) + "...";
    }

    fun String.substring(maxIndex: Int): String = if (this.length < maxIndex) substring(0, this.length) else substring(0, maxIndex)


}