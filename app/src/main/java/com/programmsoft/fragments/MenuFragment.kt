package com.programmsoft.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.adapters.CategoryAdapter
import com.programmsoft.adapters.ContentDataAdapter
import com.programmsoft.sevgisherlari.R
import com.programmsoft.sevgisherlari.databinding.FragmentMenuBinding
import com.programmsoft.room.entity.ContentData
import com.programmsoft.utils.Functions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Consumer


class MenuFragment : Fragment(R.layout.fragment_menu) {
    private val binding: FragmentMenuBinding by viewBinding()

    //private lateinit var categoryAdapter: CategoryAdapter
    val contentAdapter = ContentDataAdapter()

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //   categoryAdapter = CategoryAdapter()
        //   val contentCategoryList = Functions.db.contentDataCategoryDao().getAllWithoutFlowable()
        //  categoryAdapter.submitList(contentCategoryList)
        //binding.rvCategory.adapter = categoryAdapter
        Functions.db.contentDataDao().getAllContentFlowable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(@SuppressLint("NewApi")
            object : Consumer<List<ContentData>>,
                io.reactivex.rxjava3.functions.Consumer<List<ContentData>> {
                override fun accept(t: List<ContentData>) {
                    contentAdapter.submitList(t)
                }
            },
                @SuppressLint("NewApi")
                object : Consumer<Throwable>,
                    io.reactivex.rxjava3.functions.Consumer<Throwable> {
                    override fun accept(t: Throwable) {

                    }

                })
        binding.rvContents.adapter = contentAdapter
        contentAdapter.setOnItemClickListener { contentId ->
            Functions.showDialogWithArgument(requireActivity().supportFragmentManager, contentId)
        }

    }

//    private fun adapterItemClick() {
//        categoryAdapter.setOnItemClickListener { categoryId ->
//            val bundleOf = bundleOf("category_id" to categoryId)
//            findNavController().navigate(R.id.fragment_category, bundleOf)
//        }
//    }
}