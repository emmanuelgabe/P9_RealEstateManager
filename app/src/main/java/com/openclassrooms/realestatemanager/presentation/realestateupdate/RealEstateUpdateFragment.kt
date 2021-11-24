package com.openclassrooms.realestatemanager.presentation.realestateupdate

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateUpdateFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateType
import java.util.*

class RealEstateUpdateFragment : Fragment() {
    private lateinit var mRealEstate: RealEstate
    private lateinit var viewModel: RealEstateUpdateViewModel
    private lateinit var binding: RealEstateUpdateFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.real_estate_update_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRealEstate = requireArguments().getParcelable("realestate")!!
        initUi(view)
    }

    private fun initUi(view: View) {
        binding = RealEstateUpdateFragmentBinding.bind(view)
        binding.realEstateUpdateTilStreetName.editText?.setText(mRealEstate.address.streetName)
        binding.realEstateUpdateTilCity.editText?.setText(mRealEstate.address.city)
        binding.realEstateUpdateTilCountry.editText?.setText(mRealEstate.address.country)
        binding.realEstateUpdateTilDescription.editText?.setText(mRealEstate.description)
        binding.realEstateUpdateTilPostalCode.editText?.setText(mRealEstate.address.postalCode.toString())
        binding.realEstateUpdateTilPrice.editText?.setText(mRealEstate.price.toString())
        binding.realEstateUpdateTilRoom.editText?.setText(mRealEstate.room.toString())
        binding.realEstateUpdateTilSize.editText?.setText(mRealEstate.size.toString())
        binding.realEstateUpdateTilStreetNumber.editText?.setText(mRealEstate.address.streetNumber.toString())
        binding.realEstateUpdateTilType.editText?.setText(mRealEstate.type.toString())
        binding.realEstateUpdateTilRealEstateAgent.editText?.setText(mRealEstate.realEstateAgent)
        binding.realEstateUpdateButtonAddSaleDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)
            val datepickerdialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    //TODO
                }, y, m, d
            )

            datepickerdialog.show()
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
        binding.realEstateUpdateActvType.setAdapter(typeAdapterAdapter)
        binding.realEstateUpdateActvRoom.setAdapter(roomAdapterAdapter)
    }
}