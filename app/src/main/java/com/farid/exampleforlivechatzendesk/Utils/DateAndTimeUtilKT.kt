package com.farid.exampleforlivechatzendesk.Utils

import java.text.SimpleDateFormat
import java.util.*

object DateAndTimeUtilKT {

    //    live chat dates formats - start
    const val CHAT_FORMAT_DATE_FORMAT = "MMM dd, hh:mm aa"
    //    live chat dates formats - end

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat(CHAT_FORMAT_DATE_FORMAT)
        return format.format(date)
    }
}