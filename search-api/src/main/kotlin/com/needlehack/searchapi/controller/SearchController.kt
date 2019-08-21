package com.needlehack.searchapi.controller


import com.needlehack.searchapi.dto.Post
import com.needlehack.searchapi.model.FeedItem
import com.needlehack.searchapi.search.SearchClient
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class SearchController(val client: SearchClient) {

    val log = LoggerFactory.getLogger(SearchController::class.java)

    companion object {
        const val MAX_CONTENT_SIZE = 150
        const val CONTENT_DECORATOR = "..."
    }

    @GetMapping("/search")
    fun search(@RequestParam(value = "term", defaultValue = "*") term: String,
               @RequestParam(value = "page", defaultValue = "0") page: Int): Page<Post> {

        log.info("You want to search: [$term] in the page $page")
        val pageRequest = PageRequest.of(page, 10)
        val resultList = client.invoke(term, pageRequest)
                .map {
                    it.toDto()
                }.toList()

        log.info("found: ${resultList.size} items in the page $page")
        return PageImpl(resultList, pageRequest, resultList.size.toLong())
    }


    fun FeedItem.toDto(): Post {
        val topics = (this.topics ?: emptySet()).map { it.tag }.toSet()
        return Post(this.generatedId, this.title, this.uri, this.creator, getContentPreview(), topics, this.publicationAt
                ?: Date())
    }

    fun FeedItem.getContentPreview(): String {
        val content: String? = this.content?.substring(MAX_CONTENT_SIZE)?.plus(CONTENT_DECORATOR)
        return content ?: ""
    }

    fun String.substring(maxIndex: Int): String = if (this.length < maxIndex) substring(0, this.length) else substring(0, maxIndex)


}