package com.openclassrooms.realestatemanager.presentation.real_estate_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R

class RealEstateListFragment : Fragment() {

    companion object {
        fun newInstance() = RealEstateListFragment()
    }

    private lateinit var viewModel: RealEstateListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.real_estate_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RealEstateListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}