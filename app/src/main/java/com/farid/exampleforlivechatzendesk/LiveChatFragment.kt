package com.farid.exampleforlivechatzendesk


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.farid.exampleforlivechatzendesk.Utils.FragmentUtil
import com.farid.exampleforlivechatzendesk.Utils.KeyBoardUtil
import com.zopim.android.sdk.api.ChatApi
import com.zopim.android.sdk.api.ZopimChatApi
import com.zopim.android.sdk.data.observers.AccountObserver
import com.zopim.android.sdk.data.observers.ChatItemsObserver
import com.zopim.android.sdk.data.observers.ConnectionObserver
import com.zopim.android.sdk.model.Account
import com.zopim.android.sdk.model.ChatLog
import com.zopim.android.sdk.model.Connection
import com.zopim.android.sdk.model.items.*
import kotlinx.android.synthetic.main.fragment_live_chat.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class LiveChatFragment : Fragment() {

    private var chat: ChatApi? = null

    private var accountStatus = true
    private var connectionStatus = false

    private var chatItemList = ArrayList<ChatItem>()

    private var liveChatAdapter: LiveChatAdapter? = null

    private var chatStatus: Boolean = false

    private lateinit var leaveCommentBottomSheetFragment: LeaveCommentBottomSheetFragment

    private lateinit var layoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chat = ZopimChatApi.start(activity as LiveChatActivity)

        liveChatAdapter = LiveChatAdapter(context!!,
            chatItemList,
            onRatingClickItem = {
                onRatingClickItem(it)
            },
            onLeaveCommentClick = {
                onLeaveCommentClick()
            })
        layoutManager = LinearLayoutManager(context)
        layoutManager.isSmoothScrollbarEnabled = true
        rvChat?.layoutManager = layoutManager

        rvChat?.adapter = liveChatAdapter

        btnChat?.setOnClickListener {
            addChat()
        }

        ZopimChatApi.getDataSource().addConnectionObserver(object : ConnectionObserver() {
            override fun update(p0: Connection?) {
                if (p0?.status == Connection.Status.CONNECTED) {
                    connectionStatus = true
                    activity?.runOnUiThread {
                        updateUi()
                    }
                } else {
                    connectionStatus = false
                    activity?.runOnUiThread {
                        updateUi()
                    }
                }
            }
        })

        ZopimChatApi.getDataSource().addAccountObserver(object : AccountObserver() {
            override fun update(account: Account?) {
                if (account?.status == Account.Status.OFFLINE) {
                    accountStatus = false
                    activity?.runOnUiThread {
                        updateUi()
                    }
                } else {
                    accountStatus = true
                    activity?.runOnUiThread {
                        updateUi()
                    }
                }
            }
        })

        ZopimChatApi.getDataSource().addChatLogObserver(object : ChatItemsObserver(context) {
            override fun updateChatItems(p0: TreeMap<String, RowItem<RowItem<*>>>) {
                addViewChat(p0)
            }
        })
    }

    private fun addViewChat(p0: TreeMap<String, RowItem<RowItem<*>>>) {
        if (p0.isNotEmpty()) {
            var update = false
            val lastKey = p0.lastKey()
            val item = p0[lastKey]
            val size = p0.size
            activity?.runOnUiThread {
                if (item != null) {
                    if (item.type.name == VISITOR_MESSAGE_TYPE) {
                        val visitorMessage = VisitorMessage()
                        visitorMessage.update(item as VisitorMessage)
                        ChatLog.Type.CHAT_MSG_TRIGGER
                        val chatItem = ChatItem(
                            keyId = visitorMessage.id,
                            message = visitorMessage.message,
                            timeStamp = visitorMessage.timestamp,
                            typeName = visitorMessage.type.name,
                            avatarVisitor = ""
                        )
                        if (!chatItemList.isNullOrEmpty()) {
                            for (i in 0 until chatItemList.size) {
                                if (chatItemList[i].keyId == chatItem.keyId) {
                                    chatItemList[i] = chatItem
                                    update = true
                                    break
                                }
                            }
                            if (!update) {
                                chatItemList.add(chatItem)
                            }
                        } else {
                            chatItemList.add(chatItem)
                        }
                    } else if (item.type.name == AGENT_MESSAGE_TYPE) {
                        val agentMessage = AgentMessage()
                        agentMessage.update(item as AgentMessage)
                        val chatItem = ChatItem(
                            keyId = agentMessage.id,
                            message = agentMessage.message,
                            timeStamp = agentMessage.timestamp,
                            typeName = agentMessage.type.name,
                            avatarUrlAgent = agentMessage.avatarUri
                        )
                        if (!chatItemList.isNullOrEmpty()) {
                            for (i in 0 until chatItemList.size) {
                                if (chatItemList[i].keyId == chatItem.keyId) {
                                    chatItemList[i] = chatItem
                                    update = true
                                    break
                                }
                            }
                            if (!update) {
                                chatItemList.add(chatItem)
                            }
                        } else {
                            chatItemList.add(chatItem)
                        }
                    } else if (item.type.name == CHAT_RATING_TYPE) {
                        edChat.isEnabled = false
                        btnChat.isEnabled = false
                        val charRating = ChatRating()
                        charRating.update(item as ChatRating)
                        val chatItem = ChatItem(
                            keyId = charRating.id,
                            timeStamp = charRating.timestamp,
                            typeName = charRating.type.name,
                            ratingType = charRating.rating.name
                        )
                        if (!chatItemList.isNullOrEmpty()) {
                            for (i in 0 until chatItemList.size) {
                                if (chatItemList[i].keyId == chatItem.keyId) {
                                    chatItemList[i] = chatItem
                                    update = true
                                    break
                                }
                            }
                            if (!update) {
                                chatItemList.add(chatItem)
                            }
                        } else {
                            chatItemList.add(chatItem)
                        }
                    } else if (item.type.name == CHAT_EVENT_TYPE) {
                        val chatEvent = ChatEvent()
                        chatEvent.update(item as ChatEvent)
                        chatEvent.message =
                            "Hello, Thanks for contacting mRAK support. How may I help you?"

                        val chatItem = ChatItem(
                            keyId = chatEvent.id,
                            message = chatEvent.message,
                            timeStamp = chatEvent.timestamp,
                            typeName = chatEvent.type.name,
                            avatarUrlAgent = ""
                        )
                        if (!chatItemList.isNullOrEmpty()) {
                            for (i in 0 until chatItemList.size) {
                                if (chatItemList[i].keyId == chatItem.keyId) {
                                    chatItemList[i] = chatItem
                                    update = true
                                    break
                                }
                            }
                            if (!update) {
                                chatItemList.add(chatItem)
                            }
                        } else {
                            chatItemList.add(chatItem)
                        }
                    } else if (item.type.name == MEMBER_EVENT_TYPE) {
                        val memberEvent = ChatMemberEvent()
                        memberEvent.update(item as ChatMemberEvent)
                        if (memberEvent.message.contains("left") || memberEvent.message.contains("غادَر")) {
                            edChat.isEnabled = false
                            btnChat.isEnabled = false
                            context?.let { KeyBoardUtil.hideKeyboard(it, baseView) }
                        } else {
                            edChat.isEnabled = true
                            btnChat.isEnabled = true
                        }
                        val chatItem = ChatItem(
                            keyId = memberEvent.id,
                            message = memberEvent.message,
                            typeName = memberEvent.type.name
                        )
                        if (!chatItemList.isNullOrEmpty()) {
                            for (i in 0 until chatItemList.size) {
                                if (chatItemList[i].keyId == chatItem.keyId) {
                                    chatItemList[i] = chatItem
                                    update = true
                                    break
                                }
                            }
                            if (!update) {
                                chatItemList.add(chatItem)
                            }
                        } else {
                            chatItemList.add(chatItem)
                        }
                    }
                }
                liveChatAdapter?.addListItems(chatItemList)
                var sizePosition = chatItemList.size
                Handler().postDelayed({
                    rvChat?.smoothScrollToPosition(sizePosition)
                }, 200)

            }
        }
    }

    private fun addChat() {
        chat?.send(edChat.text?.trim().toString())
        edChat?.text?.clear()
    }

    private fun onLeaveCommentClick() {
        leaveCommentBottomSheetFragment = LeaveCommentBottomSheetFragment(
            chatStatus,
            onItemClicked = {
                chat?.sendChatComment(it)
                FragmentUtil.addFragment(
                    (activity as LiveChatActivity),
                    CommentSendDoneFragment(),
                    false,
                    TAG = CommentSendDoneFragment.TAG
                )
            },
            onCancelClicked = {
                if (chatStatus) {
                    chat?.endChat()
                    chat = null
                    activity?.finish()
                }
            }
        )
        if (leaveCommentBottomSheetFragment.isAdded) {
            return
        }
        leaveCommentBottomSheetFragment.isCancelable = false
        activity?.supportFragmentManager?.let {
            leaveCommentBottomSheetFragment.show(
                it,
                LeaveCommentBottomSheetFragment.TAG
            )
        }
    }

    private fun onRatingClickItem(chatRate: ChatLog.Rating) {
        chatStatus = chatRate == ChatLog.Rating.GOOD
        chat?.sendChatRating(chatRate)
    }

    fun onBackPressed() {
        showCancelDialog()
    }

    private fun updateUi() {
        if (accountStatus) {
            layout_chat_offline?.visibility = View.GONE
            llWriteChat?.visibility = View.VISIBLE
            layout_loading?.visibility = View.VISIBLE
            if (connectionStatus) {
                layout_loading?.visibility = View.GONE
                rvChat?.visibility = View.VISIBLE
            } else {
                layout_loading?.visibility = View.VISIBLE
                rvChat?.visibility = View.GONE
            }
        } else {
            layout_chat_offline?.visibility = View.VISIBLE
            llWriteChat?.visibility = View.GONE
            layout_loading?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        chat?.endChat()
        chat = null
        super.onDestroy()
    }

    private fun showCancelDialog() {
        val dialogFragment = AlertDialogFragment.newInstance(
            headMessage = resources.getString(R.string.chat_head_message),
            bodyMessage = resources.getString(R.string.chat_body_message),
            noButtonText = resources.getString(R.string.chat_cancel_title),
            yesButtonText = resources.getString(R.string.chat_close_title)
        )
        activity?.supportFragmentManager?.let { dialogFragment.show(it, AlertDialogFragment.TAG) }
        dialogFragment.setOnAlertDialogListener(object : AlertDialogFragment.OnAlertDialogListener {
            override fun onConfirmClick() {
                chat?.endChat()
                chat = null
                activity?.finish()
            }

            override fun onCancelClick() {
            }
        })
    }


    companion object {
        val TAG: String = LiveChatFragment::class.java.simpleName
        private const val VISITOR_MESSAGE_TYPE = "VISITOR_MESSAGE"
        private const val AGENT_MESSAGE_TYPE = "AGENT_MESSAGE"
        private const val CHAT_RATING_TYPE = "CHAT_RATING"
        private const val MEMBER_EVENT_TYPE = "MEMBER_EVENT"
        private const val CHAT_EVENT_TYPE = "CHAT_EVENT"
    }
}
