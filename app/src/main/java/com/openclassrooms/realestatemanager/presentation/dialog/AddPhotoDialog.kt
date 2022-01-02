package com.openclassrooms.realestatemanager.presentation.dialog

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.text.InputType
import android.widget.EditText
import android.widget.FrameLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.Photo

class AddPhotoDialog(val context: Context, private val interaction: AddPhotoDialogListener) {
    fun openAddDescriptionDialog(externalPhotoUri: Uri) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(context.resources.getString(R.string.all_photo_description))
        val layout = FrameLayout(context)
        layout.setPaddingRelative(45, 15, 45, 0)
        val input = EditText(context)
        input.hint = context.resources.getString(R.string.all_description)
        input.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(input)
        builder.setView(layout)
        builder.setPositiveButton(context.resources.getString(R.string.all_ok)) { _, _ ->
            interaction.onAddPhoto(Photo(externalPhotoUri, input.text.toString(), true))
        }
        builder.setNegativeButton(
            context.resources.getString(R.string.all_cancel)
        ) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }
}

interface AddPhotoDialogListener {
    fun onAddPhoto(photo: Photo)
}