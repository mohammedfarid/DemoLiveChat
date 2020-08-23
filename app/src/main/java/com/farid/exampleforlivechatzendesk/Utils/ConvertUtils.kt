package com.farid.exampleforlivechatzendesk.Utils

import android.content.Context

object ConvertUtils {

    fun ConvertDPToPixel(dpValue: Int, context: Context): Int {
        var d = context.resources.displayMetrics.density
        return (dpValue * d).toInt()
    }
}