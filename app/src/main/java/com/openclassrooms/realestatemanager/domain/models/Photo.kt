package com.openclassrooms.realestatemanager.domain.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    var uri: Uri,
    var description: String,
    var uriIsExternal: Boolean // photo uri external to the application folder
) : Parcelable