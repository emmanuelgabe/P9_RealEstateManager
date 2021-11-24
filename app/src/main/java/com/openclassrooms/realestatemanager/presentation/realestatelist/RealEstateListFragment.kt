package com.openclassrooms.realestatemanager.presentation.realestatelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateListFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateFactory
import com.openclassrooms.realestatemanager.domain.models.RealEstateStatus
import com.openclassrooms.realestatemanager.domain.models.RealEstateType
import com.openclassrooms.realestatemanager.domain.utils.DateUtil
import java.text.SimpleDateFormat

class RealEstateListFragment : Fragment(), RealEstateListAdapter.Interaction {

    private lateinit var binding: RealEstateListFragmentBinding
    private val viewModelList: RealEstateListViewModel by viewModels()
    private lateinit var mAdapter: RealEstateListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.real_estate_list_fragment, container, false)
        binding = RealEstateListFragmentBinding.bind(view)
        mAdapter = RealEstateListAdapter(this)
        binding.listFragmentRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.listFragmentRecyclerView.adapter = mAdapter

        mAdapter.submitList(fakeRealEstatelist())
        return view
    }

    override fun onItemSelected(position: Int, item: RealEstate) {
        val bundle = bundleOf("realestate" to item)
        findNavController().navigate(R.id.action_global_realEstateDetailFragment, bundle)
    }

    fun fakeRealEstatelist(): List<RealEstate> {
        val fakeRealEstate =
            RealEstateFactory(DateUtil(SimpleDateFormat("yyyy.MM.dd HH:mm:ss"))).createRealEstate(
                id = null,
                type = RealEstateType.APARTMENT,
                price = 300000,
                size = 40,
                room = 1,
                description = "Apartment on the first floor of an elegant period building in Earl's Court.The property consists of a bright room with a kitchenette and bathroom. The location is excellent, with the numerous restaurants and shops of Earl's Court Road and the underground stations of Earl's Court, West Brompton and West Kensington within walking distance from the property.",
                streetNumber = 3,
                streetName = "Nevern Square",
                postalCode = 533420,
                city = "London",
                country = "England",
                status = RealEstateStatus.AVAILABLE,
                lat = 0.0,
                lng = 1.1,
                realEstateAgent = "Sthéphane Dupont"
            )
        return listOf(fakeRealEstate, fakeRealEstate, fakeRealEstate)
    }

}