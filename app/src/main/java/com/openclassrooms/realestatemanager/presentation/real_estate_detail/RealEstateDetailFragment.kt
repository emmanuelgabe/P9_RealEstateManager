package com.openclassrooms.realestatemanager.presentation.real_estate_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class RealEstateDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RealEstateDetailFragment()
    }

    private lateinit var viewModel: RealEstateDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
               //TODO RealEstateDetailScreen()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RealEstateDetailViewModel::class.java)
    }
}