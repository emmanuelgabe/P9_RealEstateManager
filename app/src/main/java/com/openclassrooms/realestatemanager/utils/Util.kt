package com.openclassrooms.realestatemanager.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

object Util {
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun removalOfDuplicates(text: CharSequence): String {
        return if (text.isNotEmpty()) {
            val textList: List<String> =
                text.substring(0, text.length - 2).replace(", ", ",").split(",")
            val newList = mutableListOf<String>()
            newList.addAll(textList)
            for (i in textList) {
                if ((textList.count { it == i }) > 1) {
                    newList.remove(i)
                }
            }
            if (newList.size > 0) {
                newList.toString().replace("[", "").replace("]", ", ")
            } else ""
        } else ""
    }
}