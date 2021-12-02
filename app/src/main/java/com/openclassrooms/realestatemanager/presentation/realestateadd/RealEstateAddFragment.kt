package com.openclassrooms.realestatemanager.presentation.realestateadd

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.MultiAutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateAddFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.NearbyInterest
import com.openclassrooms.realestatemanager.domain.models.RealEstateFactory
import com.openclassrooms.realestatemanager.domain.models.RealEstateStatus
import com.openclassrooms.realestatemanager.domain.models.RealEstateType
import com.openclassrooms.realestatemanager.domain.utils.DateUtil
import com.openclassrooms.realestatemanager.utils.sdk29AndUp
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RealEstateAddFragment : Fragment() {

    private lateinit var binding: RealEstateAddFragmentBinding
    private val viewModel: RealEstateAddViewModel by viewModels()
    private val photoUriList = mutableListOf<Uri>()
    private val photoDescriptionList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.real_estate_add_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RealEstateAddFragmentBinding.bind(view)
        binding.realEstateAddButtonAdd.setOnClickListener {
            if (requireFieldIsNotEmpty()) {
                val newRealEstate =
                    RealEstateFactory(DateUtil(SimpleDateFormat("yyyy.MM.dd HH:mm:ss"))).createRealEstate(
                        type = RealEstateType.valueOf(
                            binding.realEstateAddActvType.text!!.toString().uppercase()
                        ),
                        price = Integer.parseInt(binding.realEstateAddTilPrice.editText!!.text.toString()),
                        size = Integer.parseInt(binding.realEstateAddTilSize.editText!!.text.toString()),
                        room = Integer.parseInt(binding.realEstateAddTilRoom.editText!!.text.toString()),
                        description = binding.realEstateAddTilDescription.editText!!.text.toString(),
                        streetNumber = Integer.parseInt(binding.realEstateAddTilStreetNumber.editText!!.text.toString()),
                        streetName = binding.realEstateAddTilStreetName.editText!!.text.toString(),
                        postalCode = Integer.parseInt(binding.realEstateAddTilPostalCode.editText!!.text.toString()),
                        city = binding.realEstateAddTilCity.editText!!.text.toString(),
                        country = binding.realEstateAddTilCountry.editText!!.text.toString(),
                        status = RealEstateStatus.AVAILABLE,
                        lat = 0.0,
                        lng = 1.1,
                        realEstateAgent = binding.realEstateAddTilRealEstateAgent.editText!!.text.toString(),
                        nearbyInterest = nearByInterestStringToNearByInterestEnum(),
                        photoUri = photoUriList,
                        photoDescription = photoDescriptionList
                    )

                viewModel.onEvent(AddRealEstateEvent.SaveRealEstate(newRealEstate))
                Snackbar.make(binding.root, "real estate added", Snackbar.LENGTH_LONG).show()
                val navController = Navigation.findNavController(
                    requireActivity(),
                    R.id.activity_main_fcv_nav_host_fragment
                )
                navController.navigateUp()
            }
            binding.realEstateAddButtonAddPicture.setOnClickListener {
                openGetContentActivity()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val typeAdapterAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_item,
                RealEstateType.values()
            )
        val roomAdapterAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_item,
                resources.getStringArray(R.array.room_number)
            )
        val nearbyInterestAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_item,
                NearbyInterest.values()
            )
        binding.realEstateAddActvType.setAdapter(typeAdapterAdapter)
        binding.realEstateAddActvRoom.setAdapter(roomAdapterAdapter)
        binding.realEstateAddMactvNearbyInterest.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        binding.realEstateAddMactvNearbyInterest.setAdapter(nearbyInterestAdapter)
    }

    private fun nearByInterestStringToNearByInterestEnum(): List<NearbyInterest> {
        val nearbyInterestList: MutableList<NearbyInterest> = mutableListOf()
        val str = binding.realEstateAddMactvNearbyInterest.text.toString()
        val strList = str.substring(0, str.length - 2).split(",")
        for (nearbyInterest in strList) {
            nearbyInterestList.add(
                NearbyInterest.valueOf(
                    nearbyInterest.uppercase().replace(" ", "")
                )
            )
        }
        return nearbyInterestList
    }

    private fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap): Boolean {
        val imageCollection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
            sdk29AndUp {
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator + "RealEstateManager"
                )
            }
        }
        return try {
            requireContext().contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                requireContext().contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
                openDescriptionDialog(uri)
            } ?: throw IOException("Couldn't create MediaStore entry")
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private var launchGetContentActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    val imageUri: Uri = it.data!!.data as Uri
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(requireContext().contentResolver, imageUri)
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            imageUri
                        )
                    }
                    savePhotoToExternalStorage(UUID.randomUUID().toString(), bitmap)
                }
            }
        }

    private fun openGetContentActivity() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launchGetContentActivity.launch(chooser)
    }

    private fun openDescriptionDialog(uri: Uri) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Photo description")

        val layout = FrameLayout(requireContext())
        layout.setPaddingRelative(45, 15, 45, 0)
        val input = EditText(requireContext())
        input.hint = "description"
        input.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(input)
        builder.setView(layout)
        builder.setPositiveButton("OK") { _, _ ->
            photoDescriptionList.add(input.text.toString())
            photoUriList.add(uri)
            Snackbar.make(
                binding.root,
                "photo added : ${input.text}",
                Snackbar.LENGTH_LONG
            ).show()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun requireFieldIsNotEmpty(): Boolean {
        var fieldsIsNotEmpty = true
        if (TextUtils.isEmpty(binding.realEstateAddTilType.editText?.text)) {
            binding.realEstateAddActvType.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddActvType.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateAddTilPrice.editText?.text)) {
            binding.realEstateAddTietPrice.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddTietPrice.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateAddTilRoom.editText?.text)) {
            binding.realEstateAddActvRoom.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddActvRoom.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateAddTilSize.editText?.text)) {
            binding.realEstateAddTietSize.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddTietSize.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateAddTilDescription.editText?.text)) {
            binding.realEstateAddTietDescription.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddTietDescription.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateAddTilRealEstateAgent.editText?.text)) {
            binding.realEstateAddTietRealEstateAgent.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddTietRealEstateAgent.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateAddTilStreetNumber.editText?.text)) {
            binding.realEstateAddStreetTietNumber.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddStreetTietNumber.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateAddTilStreetName.editText?.text)) {
            binding.realEstateAddStreetTietName.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddStreetTietName.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateAddTilPostalCode.editText?.text)) {
            binding.realEstateAddPostalTietPostalCode.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddPostalTietPostalCode.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateAddTilCity.editText?.text)) {
            binding.realEstateAddTietCity.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddTietCity.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateAddTilCountry.editText?.text)) {
            binding.realEstateAddTietCountry.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateAddTietCountry.error = null
        }
        return fieldsIsNotEmpty
    }
}