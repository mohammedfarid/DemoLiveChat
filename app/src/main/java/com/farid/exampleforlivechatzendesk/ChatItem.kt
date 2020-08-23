package com.farid.exampleforlivechatzendesk

data class ChatItem(
    var keyId: String? = null,
    var message: String? = null,
    var timeStamp: Long? = null,
    var typeName: String? = null,
    var avatarUrlAgent: String? = null,
    var avatarVisitor: String? = null,
    var commentRate: String? = null,
    var ratingType: String? = null
)