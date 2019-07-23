package org.needlehack.searchapi.dto

import java.io.Serializable

data class Post(val generatedId: String, val title: String, val uri: String, val creator: String, val content: String, val topics: Set<String>) : Serializable