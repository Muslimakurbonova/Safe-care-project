package com.programmsoft.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.programmsoft.room.entity.ContentDataCategory
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ContentDataCategoryDao {
    @Insert
    fun insert(contentDataCategory: ContentDataCategory)

    @Query("select *from ContentDataCategory")
    fun getAll(): Flowable<List<ContentDataCategory>>

    @Query("select *from ContentDataCategory")
    fun getAllWithoutFlowable(): List<ContentDataCategory>

    @Query("select *from ContentDataCategory where id = :id")
    fun getById(id: Int): ContentDataCategory
}