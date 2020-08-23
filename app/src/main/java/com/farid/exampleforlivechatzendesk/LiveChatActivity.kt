package com.farid.exampleforlivechatzendesk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.farid.exampleforlivechatzendesk.Utils.FragmentUtil

class LiveChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_chat)

        FragmentUtil.replaceFragment(
            this,
            GuestLiveChatFragment(),
            false,
            TAG = GuestLiveChatFragment.TAG
        )

    }

    companion object {
        val TAG: String = LiveChatActivity::class.java.simpleName
    }

    override fun onBackPressed() {
        val fragments: List<Fragment> =
            supportFragmentManager.fragments
        for (f in fragments) {
            when (f) {
                is LiveChatFragment -> f.onBackPressed()
                is CommentSendDoneFragment -> f.onBackPressed()
                else -> {
                    super.onBackPressed()
                }
            }
        }
    }
}
