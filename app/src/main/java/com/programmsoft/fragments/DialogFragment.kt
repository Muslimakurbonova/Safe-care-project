package com.programmsoft.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.like.LikeButton
import com.like.OnLikeListener
import com.like.view.BuildConfig
import com.programmsoft.sevgisherlari.databinding.DialogAboutAppBinding
import com.programmsoft.sevgisherlari.databinding.DialogContentBinding
import com.programmsoft.utils.Functions

class DialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        when (tag) {
            "about_app" -> {
                setupAboutApp(dialog)
            }

            "content_dialog" -> {
                createContentDialog(dialog)
            }
        }
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.show()
        return dialog
    }

    private fun createContentDialog(dialog: Dialog) {
        val contentId = requireArguments().getLong("content_id")
        val content = Functions.db.contentDataDao().getContentByContentId(contentId)
        val view = DialogContentBinding.inflate(layoutInflater, null, false)
        dialog.setContentView(view.root)
        view.btnBookmark.isLiked = content.bookmark == 1
        view.text.text = content.text
        if (content.news == 1) {
            Functions.db.contentDataDao().updateNew(contentId)
        }
        view.cvSend.setOnClickListener {
            Functions.sendData(requireActivity(), content.text)
        }
        view.btnBookmark.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                view.btnBookmark.isLiked = true
                Functions.db.contentDataDao().updateBookmark(contentId, 1)
            }

            override fun unLiked(likeButton: LikeButton?) {
                view.btnBookmark.isLiked = false
                Functions.db.contentDataDao().updateBookmark(contentId, 0)
            }
        })
        if (content.text.length > 400) {
            view.scrollView.layoutParams =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1000
                )

        }
        view.cvShare.setOnClickListener {
            Functions.shareData(requireContext(), content.text)
        }
        view.cvCopy.setOnClickListener {
            Functions.copyData(requireContext(), content.text)
        }
        view.root.setOnClickListener {
            dialog.dismiss()
        }
        view.root.setOnClickListener {
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupAboutApp(dialog: Dialog) {
        val dialogView = DialogAboutAppBinding.inflate(layoutInflater, null, false)
        dialog.setCancelable(true)
        dialogView.appVersion.text = "v${BuildConfig.VERSION_NAME}"
        dialog.setContentView(dialogView.root)
    }
}