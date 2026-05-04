package com.programmsoft.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.adapters.ContentDataAdapter
import com.programmsoft.sevgisherlari.R
import com.programmsoft.sevgisherlari.databinding.FragmentBookmarkBinding
import com.programmsoft.room.entity.ContentData
import com.programmsoft.utils.Functions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Consumer

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private val binding: FragmentBookmarkBinding by viewBinding()
    val contentAdapter = ContentDataAdapter()

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Functions.db.contentDataDao().getAllByBookmark(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(@SuppressLint("NewApi")
            object : Consumer<List<ContentData>>,
                io.reactivex.rxjava3.functions.Consumer<List<ContentData>> {
                override fun accept(t: List<ContentData>) {
                    contentAdapter.submitList(t)
                    val isListEmpty = t.isEmpty()
                    binding.imgEmpty.visibility = if (isListEmpty) View.VISIBLE else View.GONE
                    binding.rvBookmark.visibility = if (isListEmpty) View.GONE else View.VISIBLE
                }
            },
                @SuppressLint("NewApi")
                object : Consumer<Throwable>,
                    io.reactivex.rxjava3.functions.Consumer<Throwable> {
                    override fun accept(t: Throwable) {

                    }

                })
        binding.rvBookmark.adapter = contentAdapter
        adapterItemClick()
    }

    private fun adapterItemClick() {
        contentAdapter.setOnItemClickListener { contentId ->
            Functions.showDialogWithArgument(requireActivity().supportFragmentManager, contentId)
        }
    }

    override fun onResume() {
        super.onResume()
        val bookmarkCount = Functions.db.contentDataDao().getAllBookmark()
        binding.imgEmpty.visibility = if (bookmarkCount == 0) View.VISIBLE else View.GONE

    }


}