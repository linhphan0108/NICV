package com.linhphan.presentation.feature.popup

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.linhphan.presentation.R

open class SingleActionConfirmationPopup(builder: Builder) {

    private var mDialog: Dialog? = null
    private var title: String? = null
    private var message: CharSequence? = null
    private var confirmLabel: String? = null
    private var cancelable: Boolean = true

    private var confirmActionCallback: (() -> Unit)? = null

    val isShowing: Boolean
        get() = mDialog != null && mDialog!!.isShowing

    init {
        title = builder.title
        message = builder.message
        cancelable = builder.cancelable
        confirmLabel = builder.confirmLabel
        confirmActionCallback = builder.confirmActionCallback
    }


    fun show(context: Context) {
        mDialog = createDialog(context).apply {
            setCancelable(cancelable)
        }
        val tvDialogTitle = mDialog!!.findViewById(R.id.tv_title) as TextView
        val tvDialogContent = mDialog!!.findViewById(R.id.tv_message) as TextView
        val btnOK = mDialog!!.findViewById(R.id.btn_ok) as Button

        btnOK.setOnClickListener {
            confirmActionCallback?.invoke()
            dismiss()
        }

        tvDialogTitle.text = title
        tvDialogTitle.visibility = if (TextUtils.isEmpty(title)) View.GONE else View.VISIBLE
        tvDialogContent.text = message
        if (!(context as Activity).isFinishing && !context.isDestroyed) {
            mDialog!!.show()
        }
    }

    private fun createDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.window!!.setBackgroundDrawable(
//            ColorDrawable(Color.TRANSPARENT)
//        )
        dialog.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setContentView(R.layout.lp_layout_popup_single_action_confirmation)

        return dialog
    }

    fun dismiss() {
        if (mDialog != null && mDialog!!.isShowing)
            mDialog!!.dismiss()
    }

    class Builder {
        internal var title: String? = null
        internal var message: String? = null
        internal var confirmLabel: String? = null
        internal var cancelable: Boolean = true
        internal var confirmActionCallback: (() -> Unit)? = null

        fun build(): SingleActionConfirmationPopup {
            return SingleActionConfirmationPopup(this)
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setConfirm(confirmLabel: String, callback: (() -> Unit)? = null): Builder {
            this.confirmLabel = confirmLabel
            this.confirmActionCallback = callback
            return this
        }
    }
}
