package com.openclassrooms.realestatemanager.data.local.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.data.local.RealEstateDatabase
import com.openclassrooms.realestatemanager.utils.AUTHORITY
import com.openclassrooms.realestatemanager.utils.REAL_ESTATE_TABLE_NAME


class RealEstateContentProvider : ContentProvider() {

    companion object {
        val URI_REAL_ESTATE: Uri = Uri.parse("content://$AUTHORITY/$REAL_ESTATE_TABLE_NAME")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        if (context != null) {
            val cursor: Cursor =
                RealEstateDatabase.getRealEstateDatabase(context!!).realEstateDao.getAllRealEstatesWithCursor()
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(getType: Uri): String {
        return "vnd.android.cursor.dir/$AUTHORITY.$REAL_ESTATE_TABLE_NAME"
    }

    override fun insert(uri: Uri, p1: ContentValues?): Uri? {
        throw IllegalArgumentException("Impossible to insert $uri")
    }

    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {
        throw IllegalArgumentException("Impossible to delete $uri")
    }

    override fun update(uri: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        throw IllegalArgumentException("Impossible to update $uri")
    }
}
