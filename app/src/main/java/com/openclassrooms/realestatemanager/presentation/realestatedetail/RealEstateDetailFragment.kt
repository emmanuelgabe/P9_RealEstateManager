package com.openclassrooms.realestatemanager.presentation.realestatedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.domain.models.RealEstateFactory
import com.openclassrooms.realestatemanager.domain.models.RealEstateStatus
import com.openclassrooms.realestatemanager.domain.models.RealEstateType
import com.openclassrooms.realestatemanager.domain.utils.DateUtil
import java.text.SimpleDateFormat

class RealEstateDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RealEstateDetailFragment()
    }

    private lateinit var viewModel: RealEstateDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
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
                    )
                RealEstateDetailScreen(fakeRealEstate)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RealEstateDetailViewModel::class.java)
    }
}