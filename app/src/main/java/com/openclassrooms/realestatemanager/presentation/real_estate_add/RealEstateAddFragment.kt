package com.openclassrooms.realestatemanager.presentation.real_estate_add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateAddFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.RealEstateType

class RealEstateAddFragment : Fragment() {

    private lateinit var binding: RealEstateAddFragmentBinding

    companion object {
        fun newInstance() = RealEstateAddFragment()
    }

    private lateinit var viewModel: RealEstateAddViewModel

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RealEstateAddViewModel::class.java)
        // TODO: Use the ViewModel
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