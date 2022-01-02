package com.openclassrooms.realestatemanager.domain.utils

import com.openclassrooms.realestatemanager.utils.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by Emmanuel gabé on 17/09/2021.
 */
object DateUtil  {

    fun formatDate(date: Date): String {
        return SimpleDateFormat(DATE_FORMAT, Locale.US).format(date)
    }
}