package com.programmsoft.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.programmsoft.sevgisherlari.App
import com.programmsoft.sevgisherlari.R
import com.programmsoft.sevgisherlari.databinding.ItemRvContentBinding
import com.programmsoft.models.ContentDataItem
import com.programmsoft.utils.Functions
import javax.inject.Inject

class HomeAdapter @Inject constructor() :
    PagingDataAdapter<ContentDataItem, HomeAdapter.ViewHolder>(differCallback) {
    lateinit var itemClick: OnItemClickListener
    private lateinit var context: Context
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    fun interface OnItemClickListener {
        fun onClick(id: Long)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

    inner class ViewHolder(var binding: ItemRvContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(contentData: ContentDataItem) {
            binding.tvContent.text = contentData.text
            @Suppress("SENSELESS_COMPARISON")
            binding.logo.isVisible = Functions.db.contentDataDao().getNewOfContentByContentId(contentData.id) == 1
            binding.root.animation = AnimationUtils.loadAnimation(App.instance, R.anim.item_anim)
            binding.cvContent.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition
//                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                itemClick.onClick(contentData.id)
            }
            if (bindingAdapterPosition == selectedPosition) {
                binding.logo.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return ViewHolder(
            ItemRvContentBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position)!!)
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<ContentDataItem>() {
            override fun areItemsTheSame(
                oldItem: ContentDataItem,
                newItem: ContentDataItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ContentDataItem,
                newItem: ContentDataItem
            ): Boolean {
                return when {
                    oldItem.id != newItem.id -> {
                        false
                    }

                    else -> true
                }
            }
        }
    }
}