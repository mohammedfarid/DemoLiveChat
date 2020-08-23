package com.farid.exampleforlivechatzendesk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.farid.exampleforlivechatzendesk.Utils.FragmentUtil
import com.farid.exampleforlivechatzendesk.Utils.ValidationUtil
import com.zopim.android.sdk.api.ZopimChatApi
import com.zopim.android.sdk.model.VisitorInfo
import kotlinx.android.synthetic.main.fragment_guest_live_chat.*

/**
 * A simple [Fragment] subclass.
 */
class GuestLiveChatFragment : Fragment() {
    companion object {
        val TAG: String = GuestLiveChatFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_live_chat, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSubmit?.setOnClickListener {
            if (getString(R.string.zen_desk_key).isNotEmpty()) {
                ZopimChatApi.init(getString(R.string.zen_desk_key))
            } else {
                Toast.makeText(context, "Please write the zendesk key inside String Files", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (etMail.text.toString().trim().isNullOrEmpty()) {
                Toast.makeText(context, "Please write the your Email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!ValidationUtil.validEmail(etMail.text.toString().trim())) {
                Toast.makeText(
                    context,
                    "Please write your Email as correct format",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            ZopimChatApi.setVisitorInfo(
                VisitorInfo.Builder()
                    .name(etNameBody.text.toString().trim())
                    .email(etMail.text.toString().trim())
                    .phoneNumber(etPhone.text.toString().trim())
                    .build()
            )
            FragmentUtil.replaceFragment(
                activity as LiveChatActivity,
                LiveChatFragment(),
                false,
                TAG = LiveChatFragment.TAG
            )

        }
    }
}



