package com.openclassrooms.realestatemanager.presentation.real_estate_add

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R

class RealEstateAddFragment : Fragment() {

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RealEstateAddViewModel::class.java)
        // TODO: Use the ViewModel
    }

}