package com.openclassrooms.realestatemanager.presentation.realestateadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateAddFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.RealEstateType

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
        binding.realEstateAddActvType.setAdapter(typeAdapterAdapter)
        binding.realEstateAddActvRoom.setAdapter(roomAdapterAdapter)
    }
}