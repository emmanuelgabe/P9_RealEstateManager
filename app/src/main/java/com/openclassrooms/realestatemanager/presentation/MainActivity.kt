package com.openclassrooms.realestatemanager.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.location.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.dialog.FilterBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var addMenuItem: MenuItem
    private lateinit var filterMenuItem: MenuItem
    private var isReadPermissionGranted = false
    private var isWritePermissionGranted = false
    private var isFineLocationPermissionGranted = false
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>
    private var locationCallback: LocationCallback? = null
    private lateinit var locationRequest: LocationRequest
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavController()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        permissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                    ?: isReadPermissionGranted
                isWritePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: isWritePermissionGranted
                isFineLocationPermissionGranted =
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: isReadPermissionGranted
                if (isFineLocationPermissionGranted) {
                    registerLocationUpdate()
                }
            }
        requestPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        addMenuItem = menu.findItem(R.id.add_menu_item)
        filterMenuItem = menu.findItem(R.id.filter_menu_item)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            addMenuItem.isVisible = destination.label != "Add real estate"
            if (!resources.getBoolean(R.bool.is_tablet)) filterMenuItem.isVisible =
                destination.label == "List real estate"
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_menu_item -> {
                Navigation.findNavController(this, R.id.activity_main_fcv_nav_host_fragment)
                    .navigate(R.id.action_global_realEstateAddFragment)
            }
            R.id.filter_menu_item -> {
                supportFragmentManager.let {
                    val modalBottomSheet = FilterBottomSheetDialog()
                    modalBottomSheet.show(supportFragmentManager, FilterBottomSheetDialog.TAG)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            navController.navigate(R.id.action_global_realEstateListFragment)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            navController.navigateUp()
        }
        return super.onSupportNavigateUp()
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_fcv_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun requestPermissions() {
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        isReadPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        isWritePermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED || minSdk29
        isFineLocationPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val permissionsToRequest = mutableListOf<String>()
        if (!isWritePermissionGranted) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!isReadPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!isFineLocationPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            registerLocationUpdate()
        }
        if (permissionsToRequest.isNotEmpty()) {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    @SuppressLint("MissingPermission")
    private fun registerLocationUpdate() {
        if (locationCallback == null) {
            try {
                locationRequest = LocationRequest.create()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                locationRequest.interval = 10
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        if (locationResult.locations.isNotEmpty()) {
                            mainActivityViewModel.saveLocation(locationResult.lastLocation)
                            mFusedLocationClient.removeLocationUpdates(locationCallback!!)
                            locationCallback = null
                        }
                    }
                }
                mFusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback!!,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                Log.e("Exception: %s", e.message, e)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reorganizeFragmentsAfterRotation()
    }

    private fun reorganizeFragmentsAfterRotation() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            && navController.currentDestination?.id == R.id.realEstateListFragment && resources.getBoolean(
                R.bool.is_tablet
            )
        ) {
            navController.navigate(R.id.action_global_realEstateDetailFragment)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            && navController.currentDestination?.id == R.id.realEstateDetailFragment
        ) {
            navController.navigate(R.id.action_global_realEstateListFragment)
        }
    }
}