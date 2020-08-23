package com.farid.exampleforlivechatzendesk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.farid.exampleforlivechatzendesk.Utils.DateAndTimeUtilKT
import com.squareup.picasso.Picasso
import com.zopim.android.sdk.model.ChatLog

class LiveChatAdapter(
    var context: Context,
    var chatItemList: ArrayList<ChatItem>,
    var onRatingClickItem: (ChatLog.Rating) -> Unit,
    var onLeaveCommentClick: () -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var VISITOR_MESSAGE = 0
    private var AGENT_MESSAGE = 1
    private var CHAT_RATING = 2
    private var MEMBER_CHAT_EVENT = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VISITOR_MESSAGE -> {
                return VisitorViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.visitor_chat_item, parent, false)
                )
            }
            AGENT_MESSAGE -> {
                return AgentViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.agent_chat_item, parent, false)
                )
            }
            CHAT_RATING -> {
                return RateViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.rate_chat_item, parent, false)
                )
            }
            MEMBER_CHAT_EVENT -> {
                return MemberChatViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.member_chat_item, parent, false)
                )
            }
            else -> {
                return VisitorViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.visitor_chat_item, parent, false)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return chatItemList.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (chatItemList[position].typeName) {
            "VISITOR_MESSAGE" -> {
                VISITOR_MESSAGE
            }
            "AGENT_MESSAGE" -> {
                AGENT_MESSAGE
            }
            "CHAT_EVENT" -> {
                AGENT_MESSAGE
            }
            "CHAT_RATING" -> {
                CHAT_RATING
            }
            "MEMBER_EVENT" -> {
                MEMBER_CHAT_EVENT
            }
            else -> {
                0
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VISITOR_MESSAGE -> {
                val vHolder = holder as VisitorViewHolder
                vHolder.bind(chatItemList[position])
            }
            AGENT_MESSAGE -> {
                val aHolder = holder as AgentViewHolder
                aHolder.bind(chatItemList[position])
            }
            CHAT_RATING -> {
                val rHolder = holder as RateViewHolder
                rHolder.bind(chatItemList[position])
            }
            MEMBER_CHAT_EVENT -> {
                val mHolder = holder as MemberChatViewHolder
                mHolder.bind(chatItemList[position])
            }
        }
    }

    inner class MemberChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msg: AppCompatTextView = itemView.findViewById(R.id.msg)

        fun bind(chatItem: ChatItem) {
            msg.text = chatItem.message
        }
    }

    inner class VisitorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivVisitor: AppCompatImageView = itemView.findViewById(R.id.ivVisitor)
        val tvVisitor: AppCompatTextView = itemView.findViewById(R.id.tvVisitor)
        val tvVisitorTime: AppCompatTextView = itemView.findViewById(R.id.tvVisitorTime)

        fun bind(chatItem: ChatItem) {
            tvVisitor.text = chatItem.message
            tvVisitorTime.text = DateAndTimeUtilKT.convertLongToTime(chatItem.timeStamp ?: 0L)
        }
    }

    inner class AgentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAgent: AppCompatImageView = itemView.findViewById(R.id.ivAgent)
        val tvAgent: AppCompatTextView = itemView.findViewById(R.id.tvAgent)
        val tvAgentTime: AppCompatTextView = itemView.findViewById(R.id.tvAgentTime)

        fun bind(chatItem: ChatItem) {
            if (!chatItem.avatarUrlAgent.isNullOrEmpty()) {
                Picasso.get().load(chatItem.avatarUrlAgent)
                    .placeholder(R.drawable.ic_profle_chat).into(ivAgent)
            }
            tvAgent.text = chatItem.message
            tvAgentTime.text = DateAndTimeUtilKT.convertLongToTime(chatItem.timeStamp ?: 0L)
        }
    }

    inner class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivGoodRate: AppCompatImageView = itemView.findViewById(R.id.ivGoodRate)
        val ivBadRate: AppCompatImageView = itemView.findViewById(R.id.ivBadRate)
        val leaveCommentTv: AppCompatTextView = itemView.findViewById(R.id.tvRateComment)

        fun bind(chatItem: ChatItem) {
            ivGoodRate.setOnClickListener {
                if (it.isSelected) {
                    it.isSelected = false
                    onRatingClickItem(ChatLog.Rating.UNRATED)
                    leaveCommentTv.isEnabled = false
                } else {
                    ivBadRate.isSelected = false
                    it.isSelected = true
                    onRatingClickItem(ChatLog.Rating.GOOD)
                    leaveCommentTv.isEnabled = true
                }
            }
            ivBadRate.setOnClickListener {
                if (it.isSelected) {
                    it.isSelected = false
                    onRatingClickItem(ChatLog.Rating.UNRATED)
                    leaveCommentTv.isEnabled = false
                } else {
                    ivGoodRate.isSelected = false
                    it.isSelected = true
                    onRatingClickItem(ChatLog.Rating.BAD)
                    leaveCommentTv.isEnabled = true
                    onLeaveCommentClick()
                }
            }
            leaveCommentTv.setOnClickListener {
                onLeaveCommentClick()
            }
        }
    }

    fun addItem(chatItem: ChatItem) {
        chatItemList.add(chatItem)
        notifyDataSetChanged()
    }

    fun addListItems(chatItem: ArrayList<ChatItem>) {
        chatItemList = chatItem
        notifyItemInserted(chatItemList.size)
    }
}