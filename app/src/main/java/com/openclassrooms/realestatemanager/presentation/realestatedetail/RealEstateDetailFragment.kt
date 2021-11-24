package com.openclassrooms.realestatemanager.presentation.realestatedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.presentation.ui.theme.RealEstateManagerComposeTheme

class RealEstateDetailFragment : Fragment() {

    private var realEstate: RealEstate? = null
    private val viewModelDetail: RealEstateDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        realEstate = requireArguments().getParcelable("realestate")
        if (realEstate != null) {
            return ComposeView(requireContext()).apply {
                setContent {
                    RealEstateDetailScreen(
                        realEstate!!,
                        onNavigate = { dest ->
                            findNavController().navigate(
                                dest,
                                bundleOf("realestate" to realEstate)
                            )
                        },
                        viewModelDetail,
                    )
                }
            }
        } else {
            return ComposeView(requireContext()).apply {
                setContent {
                    RealEstateManagerComposeTheme {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "Select a property to display the information")
                        }
                    }
                }
            }
        }
    }
}