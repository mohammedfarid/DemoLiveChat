package com.farid.exampleforlivechatzendesk

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_alert.*


class AlertDialogFragment : DialogFragment() {

    private var onAlertDialogListener: OnAlertDialogListener? = null

    private var titleText: String = ""
    private var bodyText = ""
    private var noButtonText = ""
    private var yesButtonText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        fetchBundle(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvHead?.movementMethod = ScrollingMovementMethod()
        tvBody?.movementMethod = ScrollingMovementMethod()

        if (titleText.isEmpty())
            tvHead?.visibility = View.GONE
        else
            tvHead?.text = titleText

        if (bodyText.isEmpty())
            tvBody?.visibility = View.GONE
        else
            tvBody?.text = bodyText

        if (noButtonText.isEmpty())
            tvNo?.visibility = View.GONE
        else
            tvNo?.text = noButtonText

        tvNo?.setOnClickListener {
            if (onAlertDialogListener != null) {
                onAlertDialogListener?.onCancelClick()
                dismiss()
            }
        }

        tvYes?.text = yesButtonText
        tvYes?.setOnClickListener {
            if (onAlertDialogListener != null) {
                onAlertDialogListener?.onConfirmClick()
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setupDialog()
    }

    private fun fetchBundle(arguments: Bundle?) {
        if (arguments != null) {
            titleText = arguments.getString(KEY_TITLE, "")
            bodyText = arguments.getString(KEY_BODY, "")
            noButtonText = arguments.getString(KEY_NO, "")
            yesButtonText = arguments.getString(KEY_YES, "")
        }
    }

    private fun setupDialog() {
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun setOnAlertDialogListener(onAlertDialogListener: OnAlertDialogListener) {
        this.onAlertDialogListener = onAlertDialogListener
    }

    interface OnAlertDialogListener {
        fun onConfirmClick()
        fun onCancelClick()
    }

    companion object {
        val TAG: String = AlertDialogFragment::class.java.simpleName

        private const val KEY_TITLE = "key_title"
        private const val KEY_BODY = "key_body"
        private const val KEY_NO = "key_no"
        private const val KEY_YES = "key_yes"

        fun newInstance(
            headMessage: String = "",
            bodyMessage: String = "",
            noButtonText: String = "",
            yesButtonText: String = ""
        ) = run {
            val fragment = AlertDialogFragment()
            fragment.arguments = bundleOf(
                KEY_TITLE to headMessage,
                KEY_BODY to bodyMessage,
                KEY_NO to noButtonText,
                KEY_YES to yesButtonText
            )
            return@run fragment
        }
    }
}