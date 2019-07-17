package org.needlehack.searchapi.model

import java.io.Serializable

data class FeedItem(val generatedId: String, val title: String, val uri: String, val creator: String, val origin: String, val topics: Set<String>) : Serializable 
