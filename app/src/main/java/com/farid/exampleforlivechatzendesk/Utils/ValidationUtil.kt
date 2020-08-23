package com.farid.exampleforlivechatzendesk.Utils

object ValidationUtil {

    fun hasLengthFour(str: String): Boolean {
        return str.length >= 4
    }

    fun validEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+"
        return email.trim().matches(emailPattern.toRegex())
    }
}