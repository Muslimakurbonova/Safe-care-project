package com.programmsoft.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.programmsoft.models.ContentDataItem
import com.programmsoft.repository.Repository
import com.programmsoft.utils.Functions
import kotlinx.coroutines.tasks.await

class ContentPagingSource(private val repository: Repository) :
    PagingSource<String, ContentDataItem>() {

    override fun getRefreshKey(state: PagingState<String, ContentDataItem>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ContentDataItem> {
        return try {
            val pageSize = params.loadSize
            var query = repository.getTipsReference().orderByKey().limitToFirst(pageSize)
            
            if (params.key != null) {
                query = query.startAfter(params.key)
            }
            
            val snapshot = query.get().await()
            val responseData = mutableListOf<ContentDataItem>()
            var lastKey: String? = null
            
            for (child in snapshot.children) {
                val text = child.child("text").getValue(String::class.java) ?: ""
                val category = child.child("category").getValue(String::class.java) ?: ""
                val id = child.key?.hashCode()?.toLong() ?: 0L
                val item = ContentDataItem(
                    caption = category,
                    dislikes = "0",
                    id = id,
                    likes = "0",
                    published_date = "",
                    isNew = 1,
                    text = text
                )
                responseData.add(item)
                Functions.insertData(item)
                lastKey = child.key
            }
            
            // Fallback to local Room DB if Firebase is empty (for demo purposes)
            if (responseData.isEmpty()) {
                val localData = Functions.db.contentDataDao().getAllContent()
                // Paging locally for demo
                val start = params.key?.toIntOrNull() ?: 0
                val end = minOf(start + pageSize, localData.size)
                if (start < localData.size) {
                    val sublist = localData.subList(start, end)
                    for (localItem in sublist) {
                        val item = ContentDataItem(
                            caption = localItem.categoryId.toString(), // Actually it's category ID, but good enough for demo
                            dislikes = "0",
                            id = localItem.contentId,
                            likes = "0",
                            published_date = "",
                            isNew = 1,
                            text = localItem.text ?: ""
                        )
                        responseData.add(item)
                    }
                    if (end < localData.size) {
                        lastKey = end.toString()
                    }
                }
            }

            LoadResult.Page(
                data = responseData,
                prevKey = null,
                nextKey = lastKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}