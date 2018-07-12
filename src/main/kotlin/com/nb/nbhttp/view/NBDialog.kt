package com.nb.nbhttp.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import com.nb.nbhttp.NBHttpUtil
import com.nb.nbhttp.R
import com.nb.nbhttp.param.NBaseParam
import com.nb.nbhttp.param.NDialogType
import kotlinx.android.synthetic.main.nb_dialog.view.*

/**
 * Created by NieBin on 2018-07-10
 * Github: https://github.com/nb312
 * Email: niebin312@gmail.com
 */

//object NBDialog {
//    fun showPopWin(view: View) {
//        var lay_view = LayoutInflater.from(NBHttpUtil.publicContext).inflate(R.layout.nb_dialog, null, false)
//        var ppWin = PopupWindow(lay_view, 300, 470, true)
//        ppWin.setIgnoreCheekPress()
//        ppWin.isOutsideTouchable = false
//        ppWin.isTouchable = false
//        ppWin.isSplitTouchEnabled = false
//        ppWin.isClippingEnabled = false
//        ppWin.showAtLocation(view, Gravity.CENTER, 0, 0)
//
//
//    }
//}


class NBDialog(var ctx: Context?, var param: NBaseParam) : Dialog(ctx, R.style.custom_dialog_translucent) {

    init {
        init()
    }

    private fun init() {
        val view = View.inflate(ctx, R.layout.nb_dialog, null)
        setContentView(view)
        val window = window
        val displayMetrics = DisplayMetrics()
        val layoutParams = window!!.attributes
        getWindow()!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        layoutParams.width = displayMetrics.widthPixels
        layoutParams.height = displayMetrics.heightPixels
        window.attributes = layoutParams
        if (param.dialogType == NDialogType.CANCELLABLE) {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        } else {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

}
