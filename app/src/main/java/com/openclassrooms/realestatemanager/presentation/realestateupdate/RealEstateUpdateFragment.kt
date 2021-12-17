package com.openclassrooms.realestatemanager.presentation.realestateupdate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView.CommaTokenizer
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateUpdateFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.NearbyInterest
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstateType
import com.openclassrooms.realestatemanager.presentation.alertdialog.*
import com.openclassrooms.realestatemanager.utils.KEY_BUNDLE_REAL_ESTATE
import com.openclassrooms.realestatemanager.utils.Util.hideKeyboard
import com.openclassrooms.realestatemanager.utils.Util.removalOfDuplicates
import com.openclassrooms.realestatemanager.utils.deletePhoto
import com.openclassrooms.realestatemanager.utils.savePhoto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class RealEstateUpdateFragment : Fragment(), ListPhotoListener, DatePickerListener,
    EditPhotoDialogListener, AddPhotoDialogListener {
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var binding: RealEstateUpdateFragmentBinding
    private val updateViewModel: RealEstateUpdateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.real_estate_update_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RealEstateUpdateFragmentBinding.bind(view)
        binding.updateViewModel = updateViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        updateViewModel.init(requireArguments().getParcelable(KEY_BUNDLE_REAL_ESTATE)!!)
        initUi()
    }

    private fun initUi() {
        binding.realEstateUpdateMactvNearbyInterest.doOnTextChanged { text, _, _, _ ->
            if (!(text.isNullOrBlank() && text!!.contains("["))) {
                val textWithoutDuplicates = removalOfDuplicates(text)
                if (textWithoutDuplicates != text.toString()) {
                    binding.realEstateUpdateMactvNearbyInterest.setText(textWithoutDuplicates)
                }
            }
        }
        updateViewModel.realEstate.observe(this) { realEstate ->
            if (realEstate.saleDate.isNullOrBlank()) {
                binding.realEstateUpdateTvSaleDateInformation.text =
                    resources.getString(R.string.real_estate_update_tv_information_available)
                binding.realEstateUpdateButtonAddSaleDate.text =
                    resources.getString(R.string.real_estate_update_button_add_sale_date)
            } else {
                binding.realEstateUpdateTvSaleDateInformation.text =
                    resources.getString(R.string.real_estate_update_tv_information_sold)
                binding.realEstateUpdateButtonAddSaleDate.text =
                    resources.getString(R.string.real_estate_update_button_edit_sale_date)
            }
        }
        binding.realEstateUpdateButtonAddSaleDate.setOnClickListener {
            DatePickerDialog(requireContext(), this).openDatePickerDialog()
        }
        binding.realEstateUpdateButtonAddPicture.setOnClickListener {
            openGetImageActivity()
        }

        binding.realEstateUpdateButtonUpdate.setOnClickListener {
            if (requireFieldIsNotEmpty()) {
                for ((index, photo) in updateViewModel.realEstate.value!!.photos.withIndex()) {
                    if (photo.uriIsExternal) {
                        val uriInternal: Uri? = savePhoto(
                            UUID.randomUUID().toString(), photo.uri, requireContext()
                        )
                        if (uriInternal != null)
                            updateViewModel.updatePhoto(photo.copy(uri = uriInternal,uriIsExternal = false), index)
                    }
                }
                for (uri in updateViewModel.photosToDelete.value!!){
                    deletePhoto(requireContext(),uri)
                }
            }
            updateViewModel.onEvent(UpdateRealEstateEvent.UpdateRealEstate)
        }
        photoAdapter = PhotoAdapter(updateViewModel.realEstate.value!!.photos, this)
        binding.realEstateUpdateRvPhoto.adapter = photoAdapter
        binding.realEstateUpdateRvPhoto.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        lifecycleScope.launchWhenStarted {
            updateViewModel.eventFlow.collect { event ->
                when (event) {
                    is RealEstateUpdateViewModel.UIEvent.RealEstateUpdated -> {
                        hideKeyboard()
                        val navController = Navigation.findNavController(
                            requireActivity(), R.id.activity_main_fcv_nav_host_fragment)
                        navController.navigateUp()
                    }
                    is RealEstateUpdateViewModel.UIEvent.ShowSnackbar -> {
                        Snackbar.make( binding.root, event.message,Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
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
    override fun onResume() {
        super.onResume()
        val typeAdapterAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, RealEstateType.values())
        val roomAdapterAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_item,
                resources.getStringArray(R.array.room_number)
            )
        val nearbyInterestAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, NearbyInterest.values())
        binding.realEstateUpdateActvType.setAdapter(typeAdapterAdapter)
        binding.realEstateUpdateActvRoom.setAdapter(roomAdapterAdapter)
        binding.realEstateUpdateMactvNearbyInterest.setTokenizer(CommaTokenizer())
        binding.realEstateUpdateMactvNearbyInterest.setAdapter(nearbyInterestAdapter)
    }

    private fun requireFieldIsNotEmpty(): Boolean {
        var fieldsIsNotEmpty = true
        if (TextUtils.isEmpty(binding.realEstateUpdateTilType.editText?.text)) {
            binding.realEstateUpdateActvType.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdateActvType.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateUpdateTilPrice.editText?.text)) {
            binding.realEstateUpdateTietPrice.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdateTietPrice.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateUpdateTilRoom.editText?.text)) {
            binding.realEstateUpdateActvRoom.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdateActvRoom.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateUpdateTilSize.editText?.text)) {
            binding.realEstateUpdateTietSize.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdateTietSize.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateUpdateTilDescription.editText?.text)) {
            binding.realEstateUpdateTietDescription.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdateTietDescription.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateUpdateTilRealEstateAgent.editText?.text)) {
            binding.realEstateUpdateTietRealEstateAgent.error =
                getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdateTietRealEstateAgent.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateUpdateTilStreetNumber.editText?.text)) {
            binding.realEstateUpdateTilStreetNumber.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdateTietStreetNumber.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateUpdateTilStreetName.editText?.text)) {
            binding.realEstateUpdateTilStreetName.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdateTietStreetName.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateUpdateTilPostalCode.editText?.text)) {
            binding.realEstateUpdatePostalTietPostalCode.error =
                getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdatePostalTietPostalCode.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateUpdateTilCity.editText?.text)) {
            binding.realEstateUpdateTietCity.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdateTietCity.error = null
        }
        if (TextUtils.isEmpty(binding.realEstateUpdateTilCountry.editText?.text)) {
            binding.realEstateUpdateTietCountry.error = getString(R.string.all_required_field)
            fieldsIsNotEmpty = false
        } else {
            binding.realEstateUpdateTietCountry.error = null
        }
        return fieldsIsNotEmpty
    }
    override fun onUpdateSaleDate(saleDate: String) {
        updateViewModel.updateSaleDate(saleDate)
    }
    override fun onUpdatePhoto(photo: Photo, position: Int) {
        updateViewModel.updatePhoto(photo,position)
        photoAdapter.notifyItemChanged(position)
    }
    override fun onRemovePhoto(photo: Photo, position: Int) {
        updateViewModel.removePhoto(photo)
        photoAdapter.notifyItemRemoved(position)
    }
    override fun onAddPhoto(photo: Photo) {
        updateViewModel.addPhoto(photo)
        photoAdapter.notifyItemInserted(updateViewModel.realEstate.value!!.photos.lastIndex)
    }
    override fun onPhotoItemSelected(photo: Photo, position: Int) {
        EditPhotoDialog(requireContext(), this).openEditPhotoDialog(photo, position)
    }
}