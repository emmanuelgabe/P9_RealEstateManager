package com.openclassrooms.realestatemanager.presentation.realestatelist

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateListFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.presentation.MainActivityViewModel
import com.openclassrooms.realestatemanager.utils.KEY_BUNDLE_REAL_ESTATE
import com.openclassrooms.realestatemanager.utils.MAPVIEW_BUNDLE_KEY
import com.openclassrooms.realestatemanager.utils.MAP_ZOOM
import com.openclassrooms.realestatemanager.utils.Util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RealEstateListFragment : Fragment(), RealEstateListAdapter.Interaction, OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var mapView: MapView
    private var isFirstZoom = true
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
        initUi()
        initGoogleMap(savedInstanceState)
        initEvent()
        return view
    }

    private fun initEvent() {
        binding.listFragmentRecyclerView.adapter = realEstateAdapter
        lifecycleScope.launchWhenStarted {
            listViewModel.eventFlow.collect { event ->
                when (event) {
                    is RealEstateListViewModel.UiEvent.SubmitList -> {
                        realEstateAdapter.submitList(event.realEstateList)
                        realEstateAdapter.notifyDataSetChanged()
                        addMarkers()
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

        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.lastKnownLocation.collect { lastLocation ->
                if (::map.isInitialized && lastLocation != null) {
                    val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            lastLocation.latitude,
                            lastLocation.longitude
                        ), MAP_ZOOM
                    )
                    if (isFirstZoom) {
                        map.moveCamera(
                            cameraUpdateFactory
                        )
                        isFirstZoom = false
                    } else {
                        map.animateCamera(
                            cameraUpdateFactory
                        )
                    }
                }
            }
        }
    }

    private fun initGoogleMap(savedInstanceState: Bundle?) {
        val mapViewBundle = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)
        mapView = binding.listFragmentMapView
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
    }

    private fun initUi() {
        realEstateAdapter = RealEstateListAdapter(this)
        binding.listFragmentRecyclerView.layoutManager = LinearLayoutManager(activity)

        binding.listFragmentButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.list_fragment_button_map -> {
                        binding.listFragmentMapView.visibility = View.VISIBLE
                        binding.listFragmentRecyclerView.visibility = View.GONE
                        mainActivityViewModel.updateToggleButtonState(true)
                    }
                    R.id.list_fragment_button_list -> {
                        binding.listFragmentRecyclerView.visibility = View.VISIBLE
                        binding.listFragmentMapView.visibility = View.GONE
                        mainActivityViewModel.updateToggleButtonState(false)
                    }
                }
            }
        }
    }

    private fun addMarkers() {
        if (!listViewModel.realEstates.value.isNullOrEmpty() && ::map.isInitialized) {
            map.clear()
            for (realEstate in listViewModel.realEstates.value!!) {
                if (realEstate.lng != null && realEstate.lat != null) {
                    val icon: BitmapDescriptor =
                        BitmapDescriptorFactory.fromResource(R.drawable.ic_map_location_realestate)
                    map.addMarker(
                        MarkerOptions()
                            .position(
                                LatLng(
                                    realEstate.lat!!,
                                    realEstate.lng!!
                                )
                            )
                            .title(realEstate.id)
                            .icon(icon)
                            .flat(true)
                    )
                }
            }
        }
    }

    override fun onItemSelected(position: Int, item: RealEstate) {
        val bundle = bundleOf(KEY_BUNDLE_REAL_ESTATE to item)
        findNavController().navigate(R.id.action_global_realEstateDetailFragment, bundle)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY) ?: Bundle().also {
            outState.putBundle(MAPVIEW_BUNDLE_KEY, it)
        }
        if (::mapView.isInitialized) {
            mapView.onSaveInstanceState(mapViewBundle)
        }
    }

    @SuppressLint("PotentialBehaviorOverride", "MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.setOnMarkerClickListener { marker ->
            val realEstate =
                listViewModel.realEstates.value!!.single { it.id == marker.title }
            val bundle = bundleOf(KEY_BUNDLE_REAL_ESTATE to realEstate)
            findNavController().navigate(R.id.action_global_realEstateDetailFragment, bundle)
            true
        }

        if (mainActivityViewModel.lastKnownLocation.value == null) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), MAP_ZOOM)
            )
        } else {
            val location = mainActivityViewModel.lastKnownLocation.value
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location!!.latitude, location.longitude), MAP_ZOOM
                )
            )
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = false

        } else {
            map.isMyLocationEnabled = false
            map.uiSettings.isMyLocationButtonEnabled = false
        }
        map.setOnMapClickListener {
            hideKeyboard()
        }
        addMarkers()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        listViewModel.realEstates.value?.let { realEstateAdapter.submitList(it) }
        if (mainActivityViewModel.toggleButtonIsMapState.value) {
            binding.listFragmentButtonToggleGroup.check(R.id.list_fragment_button_map)
        }
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}