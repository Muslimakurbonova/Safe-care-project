package com.programmsoft.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.programmsoft.sevgisherlari.App
import com.programmsoft.sevgisherlari.R
import com.programmsoft.sevgisherlari.databinding.ItemRvContentBinding
import com.programmsoft.room.entity.ContentData


class ContentDataAdapter : ListAdapter<ContentData, ContentDataAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var itemClick: OnItemClickListener
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    fun interface OnItemClickListener {
        fun onClick(id: Long)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

    inner class ViewHolder(var binding: ItemRvContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(contentData: ContentData) {
            binding.tvContent.text = contentData.text
            binding.logo.isVisible = contentData.news == 1
            binding.root.animation = AnimationUtils.loadAnimation(App.instance, R.anim.item_anim)
            binding.cvContent.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition
            //  notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                itemClick.onClick(contentData.contentId)
            }
            if (bindingAdapterPosition == selectedPosition) {
                binding.logo.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<ContentData>() {
        override fun areItemsTheSame(oldItem: ContentData, newItem: ContentData): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ContentData, newItem: ContentData): Boolean {
            return when {
                oldItem.bookmark != newItem.bookmark -> {
                    false
                }

                oldItem.news != newItem.news -> {
                    false
                }

                else -> true
            }
        }

    }
}