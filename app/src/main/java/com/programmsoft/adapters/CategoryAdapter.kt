package com.programmsoft.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.programmsoft.sevgisherlari.App
import com.programmsoft.sevgisherlari.R
import com.programmsoft.sevgisherlari.databinding.ItemCategoryBinding
import com.programmsoft.room.entity.ContentDataCategory

class CategoryAdapter : ListAdapter<ContentDataCategory, CategoryAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var itemClick: OnItemClickListener

    fun interface OnItemClickListener {
        fun onClick(id: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

    inner class ViewHolder(private var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DiscouragedApi")
        fun onBind(contentDataCategory: ContentDataCategory) {
            binding.tvName.text = contentDataCategory.nameUzb
            binding.root.animation = AnimationUtils.loadAnimation(App.instance, R.anim.item_anim)
            binding.cvCategory.setOnClickListener {
                itemClick.onClick(contentDataCategory.id!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<ContentDataCategory>() {
        override fun areItemsTheSame(oldItem: ContentDataCategory, newItem: ContentDataCategory): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }

                else -> true
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ContentDataCategory, newItem: ContentDataCategory): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }

                else -> true
            }
        }

    }
}