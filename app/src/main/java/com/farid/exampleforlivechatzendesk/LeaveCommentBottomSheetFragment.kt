package com.farid.exampleforlivechatzendesk

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment
import com.farid.exampleforlivechatzendesk.Utils.ConvertUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_leave_comment_bottom_sheet.*


/**
 * A simple [Fragment] subclass.
 */
class LeaveCommentBottomSheetFragment(
    var status: Boolean,
    val onItemClicked: (String) -> Unit,
    val onCancelClicked: () -> Unit
) : RoundedBottomSheetDialogFragment() {

    companion object {
        val TAG: String = LeaveCommentBottomSheetFragment::class.java.simpleName
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leave_comment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (status) {
            iv_like_dilike.setImageDrawable(
                context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_like_feedback
                    )
                }
            )
            ed_comments.hint = getString(R.string.is_optional)

        } else {
            iv_like_dilike.setImageDrawable(
                context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_dislike_feedback
                    )
                }
            )
            ed_comments.hint = getString(R.string.is_required)
        }
        btnClose.setOnClickListener {
            onCancelClicked()
        }
        btnSubmit.setOnClickListener {
            dialog?.cancel()
            onItemClicked(ed_comments.text.toString().trim())
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet) as FrameLayout?
        if (bottomSheet != null) {
            val behavior: BottomSheetBehavior<FrameLayout?> = BottomSheetBehavior.from(bottomSheet)
            val layoutParams = bottomSheet.layoutParams
            val windowHeight = getWindowHeight()
            if (layoutParams != null) {
                val margin = ConvertUtils.ConvertDPToPixel(43, context!!)
                layoutParams.height = windowHeight - margin
            }
            bottomSheet.layoutParams = layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}
