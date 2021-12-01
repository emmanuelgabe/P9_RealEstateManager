package com.openclassrooms.realestatemanager.data.local

import android.net.Uri
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.domain.models.Address
import com.openclassrooms.realestatemanager.domain.models.NearbyInterest

@ProvidedTypeConverter
class DataConverter {
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return if (value == null) null
        else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return if (value == null) null
        else {
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(value, type)
        }
    }

    @TypeConverter
    fun fromUri(value: Uri?): String? {
        return if (value == null) null
        else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun toUri(value: String?): Uri? {
        return if (value == null) null
        else {
            val type = object : TypeToken<Uri>() {}.type
            Gson().fromJson(value, type)
        }
    }

    @TypeConverter
    fun fromUriList(value: List<Uri>?): String? {
        return if (value == null) null
        else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun toUriList(value: String?): List<Uri>? {
        return if (value == null) null
        else {
            val type = object : TypeToken<List<Uri>>() {}.type
            Gson().fromJson(value, type)
        }
    }

    @TypeConverter
    fun fromAddress(value: Address?): String? {
        return if (value == null) null
        else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun toAddress(value: String?): Address? {
        return if (value == null) null
        else {
            val type = object : TypeToken<Address>() {}.type
            Gson().fromJson(value, type)
        }
    }

    @TypeConverter
    fun fromNearbyInterest(value: List<NearbyInterest>?): String? {
        return if (value == null) null
        else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun toNearbyInterest(value: String?): List<NearbyInterest>? {
        return if (value == null) null
        else {
            val type = object : TypeToken<List<NearbyInterest>>() {}.type
            Gson().fromJson(value, type)
        }
    }
}