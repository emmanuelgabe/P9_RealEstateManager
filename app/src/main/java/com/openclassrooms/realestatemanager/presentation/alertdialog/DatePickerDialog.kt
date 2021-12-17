package com.openclassrooms.realestatemanager.presentation.alertdialog

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import com.openclassrooms.realestatemanager.R
import java.util.*

class DatePickerDialog(val context: Context, private val interaction: DatePickerListener) {
    fun openDatePickerDialog() {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                interaction.onUpdateSaleDate("$year.$monthOfYear.$dayOfMonth")
            }, y, m, d
        )
        datePickerDialog.setButton(
            DialogInterface.BUTTON_NEUTRAL,
            context.resources.getString(R.string.all_remove)
        ) { _, _ ->
            interaction.onUpdateSaleDate("")
        }
        datePickerDialog.show()
    }
}

interface DatePickerListener {
    fun onUpdateSaleDate(saleDate: String)
}