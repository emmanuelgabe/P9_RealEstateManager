package com.openclassrooms.realestatemanager.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.dialog.FilterBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var addMenuItem: MenuItem
    private lateinit var filterMenuItem: MenuItem
    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavController()
        permissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                readPermissionGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
                writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: writePermissionGranted
            }
        updateOrRequestPermissions()
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

    private fun updateOrRequestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk29

        val permissionsToRequest = mutableListOf<String>()
        if (writePermissionGranted) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (readPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionsToRequest.isNotEmpty()) {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
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