package com.openclassrooms.realestatemanager.presentation.realestatelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateListFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.presentation.MainActivityViewModel
import com.openclassrooms.realestatemanager.utils.KEY_BUNDLE_REAL_ESTATE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class RealEstateListFragment : Fragment(), RealEstateListAdapter.Interaction {

    private lateinit var binding: RealEstateListFragmentBinding
    private lateinit var realEstateAdapter: RealEstateListAdapter
    private val listViewModel: RealEstateListViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.real_estate_list_fragment, container, false)
        binding = RealEstateListFragmentBinding.bind(view)
        realEstateAdapter = RealEstateListAdapter(this)
        binding.listFragmentRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.listFragmentRecyclerView.adapter = realEstateAdapter
        lifecycleScope.launchWhenStarted {
            listViewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is RealEstateListViewModel.UiEvent.SubmitList -> {
                        realEstateAdapter.submitList(event.realEstateList)
                        realEstateAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.filterEventFlow.collectLatest { event ->
                if (event is MainActivityViewModel.FilterEvent.ApplyFilter) {
                    listViewModel.updateList(event.realEstateFilter)
                }
            }
        }
        return view
    }

    override fun onItemSelected(position: Int, item: RealEstate) {
        val bundle = bundleOf(KEY_BUNDLE_REAL_ESTATE to item)
        findNavController().navigate(R.id.action_global_realEstateDetailFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        listViewModel.realEstates.value?.let { realEstateAdapter.submitList(it) }
    }
}