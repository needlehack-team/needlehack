package com.needlehack.searchapi.search

import com.needlehack.searchapi.model.FeedItem
import org.springframework.data.domain.PageRequest

interface SearchClient {

    fun invoke(term: String, pageRequest: PageRequest): Set<FeedItem>

}