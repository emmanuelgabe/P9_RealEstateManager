package com.openclassrooms.realestatemanager.utils

import androidx.databinding.InverseMethod
import com.openclassrooms.realestatemanager.domain.models.NearbyInterest
import com.openclassrooms.realestatemanager.domain.models.RealEstateType

object BindingConverter {

    @InverseMethod("stringToRealEstateType")
    @JvmStatic
    fun realEstateTypeToString(
        value: RealEstateType?
    ): String {
        return value?.toString() ?: return ""
    }

    @JvmStatic
    fun stringToRealEstateType(
        value: String?
    ): RealEstateType {
        return if (value.isNullOrBlank()) RealEstateType.APARTMENT else
            RealEstateType.valueOf(value.uppercase())
    }

    @InverseMethod("stringToInt")
    @JvmStatic
    fun intToString(
        value: Int?
    ): String {
       return if (value == null || value == 0) "" else value.toString()
    }

    @JvmStatic
    fun stringToInt(
        value: String
    ): Int? {
        return if (value.isBlank()) null else value.toInt()
    }

    @InverseMethod("stringToNearbyInterest")
    @JvmStatic
    fun nearbyInterestToString(
        value: List<NearbyInterest>?
    ): String {
        return if (value.isNullOrEmpty()) "" else {
            value.toString().replace("[", "").replace("]", ", ")
        }
    }

    @JvmStatic
    fun stringToNearbyInterest(
        value: String
    ): List<NearbyInterest> {
        if (value.isNotEmpty()) {
            val nearbyInterestList: MutableList<NearbyInterest> = mutableListOf()
            val strList =
                value.substring(0, value.length - 2).replace(" ", "").split(",").toMutableList()
            for (nearbyInterest in strList) {
                nearbyInterestList.add(
                    NearbyInterest.valueOf(nearbyInterest.uppercase())
                )
            }
            return nearbyInterestList
        }
        return emptyList()
    }
}