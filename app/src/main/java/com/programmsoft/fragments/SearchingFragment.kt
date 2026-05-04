package com.programmsoft.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.adapters.ContentDataAdapter
import com.programmsoft.sevgisherlari.R
import com.programmsoft.sevgisherlari.databinding.FragmentSearchingBinding
import com.programmsoft.room.entity.ContentData
import com.programmsoft.utils.Functions

class SearchingFragment : Fragment(R.layout.fragment_searching) {
    private val binding: FragmentSearchingBinding by viewBinding()
    private val contentDataAdapter = ContentDataAdapter()
    val list = Functions.db.contentDataDao().getAllContent()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.close.setOnClickListener {
            if (binding.editText.text.toString().isNotEmpty()) {
                binding.editText.setText("")
            } else {
                findNavController().popBackStack()
            }
        }
        contentDataAdapter.submitList(list)
        binding.rvSearch.adapter = contentDataAdapter
        textChanged()
        adapterItemClick()
    }

    private fun adapterItemClick() {
        contentDataAdapter.setOnItemClickListener { contentId ->
            Functions.showDialogWithArgument(requireActivity().supportFragmentManager, contentId)

        }
    }

    private fun textChanged() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }

    fun filter(text: String) {
        val filteredList = ArrayList<ContentData>()
        val contentList = Functions.db.contentDataDao().getAllContent()
        for (content in contentList) {
            if (content.text.lowercase().contains(text.lowercase())) {
                filteredList.add(content)
            }
        }
        contentDataAdapter.submitList(filteredList)
    }
}