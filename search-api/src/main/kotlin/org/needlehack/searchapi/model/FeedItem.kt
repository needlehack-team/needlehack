package org.needlehack.searchapi.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class FeedItem(val generatedId: String, val title: String, val uri: String, val creator: String, val content: String?, val topics: Set<Topic>?) : Serializable
