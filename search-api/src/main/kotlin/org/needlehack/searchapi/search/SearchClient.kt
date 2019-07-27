package org.needlehack.searchapi.search

import org.needlehack.searchapi.model.FeedItem
import org.springframework.data.domain.PageRequest

interface SearchClient {

    fun invoke(term: String, pageRequest: PageRequest): Set<FeedItem>

}