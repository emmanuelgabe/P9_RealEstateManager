package com.openclassrooms.realestatemanager.presentation.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import com.openclassrooms.realestatemanager.R
import java.util.*

class DatePickerDialog(
    val context: Context,
    private val interaction: DatePickerListener,
    private val tag: String? = null
) {
    fun openDatePickerDialog() {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                val c = Calendar.getInstance()
                c[Calendar.YEAR] = year
                c[Calendar.MONTH] = monthOfYear
                c[Calendar.DAY_OF_MONTH] = dayOfMonth
                if (tag != null && tag.contains("MIN")) {
                    c[Calendar.HOUR_OF_DAY] = 0
                    c[Calendar.MINUTE] = 0
                    c[Calendar.SECOND] = 0
                } else if (tag != null && tag.contains("MAX")) {
                    c[Calendar.HOUR_OF_DAY] = 23
                    c[Calendar.MINUTE] = 59
                    c[Calendar.SECOND] = 59
                }
                interaction.onUpdateSaleDate(c.time, tag)
            }, y, m, d
        )
        datePickerDialog.setButton(
            DialogInterface.BUTTON_NEUTRAL,
            context.resources.getString(R.string.all_remove)
        ) { _, _ ->
            interaction.onUpdateSaleDate(null, tag)
        }
        datePickerDialog.show()
    }
}

interface DatePickerListener {
    fun onUpdateSaleDate(date: Date?, tag: String? = null)
}