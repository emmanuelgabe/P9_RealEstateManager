package com.openclassrooms.realestatemanager.presentation.alertdialog

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.FrameLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.Photo

class EditPhotoDialog(val context: Context, private val interaction: EditPhotoDialogListener) {

    fun openEditPhotoDialog(photo: Photo, position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(context.resources.getString(R.string.all_photo_description))

        val layout = FrameLayout(context)
        layout.setPaddingRelative(45, 15, 45, 0)
        val input = EditText(context)
        input.setText(photo.description)
        input.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(input)
        builder.setView(layout)
        builder.setPositiveButton(context.resources.getString(R.string.all_ok)) { _, _ ->
            photo.description = input.text.toString()
            interaction.onUpdatePhoto(photo, position)
        }
        builder.setNegativeButton(
            context.resources.getString(R.string.all_cancel)
        ) { dialog, _ ->
            dialog.cancel()
        }

        builder.setNeutralButton(context.resources.getString(R.string.all_delete)) { _, _ ->
            interaction.onRemovePhoto(photo, position)
        }
        builder.show()
    }
}

interface EditPhotoDialogListener {
    fun onUpdatePhoto(photo: Photo, position: Int)
    fun onRemovePhoto(photo: Photo, position: Int)
}