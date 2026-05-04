package com.programmsoft.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.programmsoft.room.entity.ContentData
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ContentDataDao {
    @Insert
    fun insert(contentData: ContentData)

    @Update
    fun update(contentData: ContentData)

    @Query("select *from  ContentData")
    fun getAll(): Flowable<List<ContentData>>

    @Query("select *from  ContentData")
    fun getAllContent(): List<ContentData>

    @Query("select *from  ContentData")
    fun getAllContentFlowable(): Flowable<List<ContentData>>

    @Query("select *from ContentData where id = :id")
    fun getById(id: Long): ContentData

    @Query("select text from ContentData where id = :id")
    fun getContentTextById(id: Long): String

    @Query("SELECT *FROM ContentData WHERE news = :news")
    fun getAllNews(news: Int): Flowable<List<ContentData>>

    @Query("select *from ContentData where categoryId = :categoryId")
    fun getAllByCategories(categoryId: Int): Flowable<List<ContentData>>

    @Query("select *from ContentData where bookmark = :bookmark")
    fun getAllByBookmark(bookmark: Int): Flowable<List<ContentData>>

    @Query("SELECT COUNT(*) FROM ContentData WHERE bookmark = 1")
    fun getAllBookmark(): Int

    @Query("SELECT COUNT(*) FROM ContentData WHERE contentId = :contentId")
    fun isContentExist(contentId: Long): Int

    @Query("SELECT COUNT(*) FROM ContentData WHERE id = :id")
    fun isContentExistById(id: Long): Int

    @Query("UPDATE ContentData SET bookmark = :bookmark WHERE contentId = :contentId")
    fun updateBookmark(contentId: Long, bookmark: Int)

    @Query("UPDATE ContentData SET news = 0 WHERE contentId = :contentId")
    fun updateNew(contentId: Long)

    @Query("select *from  ContentData where contentId = :contentId")
    fun getContentByContentId(contentId: Long): ContentData

    @Query("select contentId from  ContentData where id = :id")
    fun getContentById(id: Long): Long

    @Query("select news from  ContentData where contentId = :contentId")
    fun getNewOfContentByContentId(contentId: Long): Int

    @Query("SELECT id FROM ContentData WHERE categoryId = :categoryId ORDER BY RANDOM() LIMIT 1")
    fun getRandomTipIdByCategory(categoryId: Int): Long?
}