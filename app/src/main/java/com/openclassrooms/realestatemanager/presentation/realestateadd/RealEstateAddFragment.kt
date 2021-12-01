package com.openclassrooms.realestatemanager.presentation.realestateadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateAddFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.NearbyInterest
import com.openclassrooms.realestatemanager.domain.models.RealEstateFactory
import com.openclassrooms.realestatemanager.domain.models.RealEstateStatus
import com.openclassrooms.realestatemanager.domain.models.RealEstateType
import com.openclassrooms.realestatemanager.domain.utils.DateUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class RealEstateAddFragment : Fragment() {

    private lateinit var binding: RealEstateAddFragmentBinding
    private val viewModel: RealEstateAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.real_estate_add_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RealEstateAddFragmentBinding.bind(view)
        binding.realEstateAddButtonAdd.setOnClickListener {

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
                    nearbyInterest = nearByInterestStringToNearByInterestEnum()
                )

            viewModel.onEvent(AddRealEstateEvent.SaveRealEstate(newRealEstate))
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
}