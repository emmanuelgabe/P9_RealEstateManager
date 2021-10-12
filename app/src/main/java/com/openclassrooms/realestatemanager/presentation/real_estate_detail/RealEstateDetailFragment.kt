package com.openclassrooms.realestatemanager.presentation.real_estate_detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R

class RealEstateDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RealEstateDetailFragment()
    }

    private lateinit var viewModel: RealEstateDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.real_estate_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RealEstateDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}