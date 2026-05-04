package com.programmsoft.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContentDataCategory(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var nameEng: String? = null,
    var nameUzb: String? = null,
)