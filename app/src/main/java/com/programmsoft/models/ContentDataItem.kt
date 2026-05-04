package com.programmsoft.models

data class ContentDataItem(
    val caption: String,
    val dislikes: String,
    val id: Long,
    val likes: String,
    val published_date: String,
    var isNew: Int = 1,
    val text: String
)