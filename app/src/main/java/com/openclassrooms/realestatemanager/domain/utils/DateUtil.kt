package com.openclassrooms.realestatemanager.domain.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by Emmanuel gabé on 17/09/2021.
 */
class DateUtil constructor(private val dateFormat: SimpleDateFormat) {

    fun getCurrentTimestamp(): String {
        return dateFormat.format(Date())
    }
}