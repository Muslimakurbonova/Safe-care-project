package com.programmsoft.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.programmsoft.paging.ContentPagingSource
import com.programmsoft.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val contentList = Pager(PagingConfig(10))
    {
        ContentPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    val contentListRetry = Pager(PagingConfig(10))
    {
        ContentPagingSource(repository)
    }.flow.cachedIn(viewModelScope)
}