package com.roombookingapp.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.roombookingapp.R
/**
 * This class to handle ProgressDialog
 */
class ProgressDialog {
    companion object {
        lateinit var dialog: Dialog
        fun showProgressDialog(context: Context) {
            dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
            dialog.setContentView(inflate)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        fun dismissProgressDialog() {
            dialog.dismiss()
        }


    }
}