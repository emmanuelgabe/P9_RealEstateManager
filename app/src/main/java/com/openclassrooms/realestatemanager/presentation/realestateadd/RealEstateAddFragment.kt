package com.openclassrooms.realestatemanager.presentation.realestateadd

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateAddFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.NearbyInterest
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstateType
import com.openclassrooms.realestatemanager.presentation.alertdialog.AddPhotoDialog
import com.openclassrooms.realestatemanager.presentation.alertdialog.AddPhotoDialogListener
import com.openclassrooms.realestatemanager.presentation.alertdialog.EditPhotoDialog
import com.openclassrooms.realestatemanager.presentation.alertdialog.EditPhotoDialogListener
import com.openclassrooms.realestatemanager.utils.Util
import com.openclassrooms.realestatemanager.utils.Util.hideKeyboard
import com.openclassrooms.realestatemanager.utils.savePhoto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class RealEstateAddFragment : Fragment(), AddPhotoDialogListener, ListPhotoListener,
    EditPhotoDialogListener {

    private lateinit var binding: RealEstateAddFragmentBinding
    private lateinit var photoAdapter: PhotoAdapter
    private val addViewModel: RealEstateAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.real_estate_add_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RealEstateAddFragmentBinding.bind(view)
        binding.addViewModel = addViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initUi()
    }

    private fun initUi() {
        lifecycleScope.launchWhenStarted {
            addViewModel.eventFlow.collect { event ->
                when (event) {
                    is RealEstateAddViewModel.UIEvent.RealEstateAdded -> {
                        hideKeyboard()
                        val navController = Navigation.findNavController(
                            requireActivity(),R.id.activity_main_fcv_nav_host_fragment)
                        navController.navigateUp()
                    }
                    is RealEstateAddViewModel.UIEvent.ShowSnackBar -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.realEstateAddButtonAdd.setOnClickListener {
            if (requireFieldIsNotEmpty()) {
                for ((index, photo) in addViewModel.realEstate.value!!.photos.withIndex()) {
                    val uriInternal: Uri? = savePhoto(UUID.randomUUID().toString(), photo.uri, requireContext())
                    if (uriInternal != null)
                        addViewModel.updatePhoto(photo.copy( uri = uriInternal, uriIsExternal = false ), index)
                }
                addViewModel.onEvent(AddRealEstateEvent.SaveRealEstate)
            }
        }
        binding.realEstateAddButtonAddPicture.setOnClickListener {
            openGetImageActivity()
        }
        binding.realEstateAddMactvNearbyInterest.doOnTextChanged { text, _, _, _ ->
            if (!(text.isNullOrBlank() && text!!.contains("["))) {
                val textWithoutDuplicates = Util.removalOfDuplicates(text)
                if (textWithoutDuplicates != text.toString()) {
                    binding.realEstateAddMactvNearbyInterest.setText(textWithoutDuplicates)
                }
            }
        }
        photoAdapter = PhotoAdapter(addViewModel.realEstate.value!!.photos,this)
        binding.realEstateAddRvPhoto.adapter = photoAdapter
        binding.realEstateAddRvPhoto.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


    override fun onResume() {
        super.onResume()
        val typeAdapterAdapter =
            ArrayAdapter(requireContext(),R.layout.dropdown_item,RealEstateType.values())
        val roomAdapterAdapter =
            ArrayAdapter(requireContext(),R.layout.dropdown_item,resources.getStringArray(R.array.room_number))
        val nearbyInterestAdapter =
            ArrayAdapter(requireContext(),R.layout.dropdown_item,NearbyInterest.values())
        binding.realEstateAddActvType.setAdapter(typeAdapterAdapter)
        binding.realEstateAddActvRoom.setAdapter(roomAdapterAdapter)
        binding.realEstateAddMactvNearbyInterest.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        binding.realEstateAddMactvNearbyInterest.setAdapter(nearbyInterestAdapter)
    }

    private fun openGetImageActivity() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launchGetContentActivity.launch(chooser)
    }

    private var launchGetContentActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                AddPhotoDialog(requireContext(),this
                ).openAddDescriptionDialog(it.data!!.data as Uri)
            }
        }
    }

    override fun onAddPhoto(photo: Photo) {
        addViewModel.addPhoto(photo)
        photoAdapter.notifyItemInserted(addViewModel.realEstate.value!!.photos.lastIndex)
    }

    override fun onPhotoItemSelected(photo: Photo, position: Int) {
        EditPhotoDialog(requireContext(),this).openEditPhotoDialog(photo,position)
    }

    override fun onUpdatePhoto(photo: Photo, position: Int) {
        addViewModel.updatePhoto(photo,position)
        photoAdapter.notifyItemChanged(position)
    }

    override fun onRemovePhoto(photo: Photo, position: Int) {
        addViewModel.removePhoto(photo)
        photoAdapter.notifyItemRemoved(position)
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