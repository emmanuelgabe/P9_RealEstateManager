package com.openclassrooms.realestatemanager.presentation.real_estate_update

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R

class RealEstateUpdateFragment : Fragment() {

    companion object {
        fun newInstance() = RealEstateUpdateFragment()
    }

    private lateinit var viewModel: RealEstateUpdateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.real_estate_update_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RealEstateUpdateViewModel::class.java)
        // TODO: Use the ViewModel
    }

}