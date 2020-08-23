package com.farid.exampleforlivechatzendesk.Utils


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.farid.exampleforlivechatzendesk.R

object FragmentUtil {

    fun addFragment(
        activity: AppCompatActivity,
        fragment: Fragment,
        addToBackstack: Boolean,
        TAG: String
    ) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.container_fragment, fragment, TAG)
            if (addToBackstack) {
                transaction.addToBackStack(TAG)
            }
        }

        transaction.commit()
    }

    fun replaceFragment(
        activity: AppCompatActivity,
        fragment: Fragment,
        addToBackstack: Boolean,
        TAG: String
    ) {
        val transaction = activity.supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container_fragment, fragment, TAG)

        if (addToBackstack) {
            transaction.addToBackStack(TAG)
        }

        transaction.commit()
    }
}