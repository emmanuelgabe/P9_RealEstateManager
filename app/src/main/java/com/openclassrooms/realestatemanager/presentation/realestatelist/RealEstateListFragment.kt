package com.openclassrooms.realestatemanager.presentation.realestatelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateListFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.presentation.MainActivityViewModel

class RealEstateListFragment : Fragment(), RealEstateListAdapter.Interaction {

    private lateinit var binding: RealEstateListFragmentBinding
    private lateinit var mAdapter: RealEstateListAdapter
    private val viewModelMainActivity: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.real_estate_list_fragment, container, false)
        binding = RealEstateListFragmentBinding.bind(view)
        mAdapter = RealEstateListAdapter(this)
        binding.listFragmentRecyclerView.layoutManager = LinearLayoutManager(activity)
        subscribeObservers()
        binding.listFragmentRecyclerView.adapter = mAdapter
        mAdapter.submitList(viewModelMainActivity.listViewState.value?.realEstates!!)
        return view
    }

    override fun onItemSelected(position: Int, item: RealEstate) {
        val bundle = bundleOf("realestate" to item)
        findNavController().navigate(R.id.action_global_realEstateDetailFragment, bundle)
    }

    private fun subscribeObservers() {
        viewModelMainActivity.listViewState.observe(
            viewLifecycleOwner,
            { listViewState ->
                listViewState.realEstates.let { realEstates ->
                    mAdapter.submitList(realEstates)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        viewModelMainActivity.getRealEstate()
    }
}