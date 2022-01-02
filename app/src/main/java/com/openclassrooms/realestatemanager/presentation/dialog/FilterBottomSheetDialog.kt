package com.openclassrooms.realestatemanager.presentation.dialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FilterSheetDialogFragmentBinding
import com.openclassrooms.realestatemanager.domain.models.NearbyInterest
import com.openclassrooms.realestatemanager.presentation.MainActivityViewModel
import com.openclassrooms.realestatemanager.utils.MAX_ENTRY_DATE_TAG
import com.openclassrooms.realestatemanager.utils.MAX_SALE_DATE_TAG
import com.openclassrooms.realestatemanager.utils.MIN_ENTRY_DATE_TAG
import com.openclassrooms.realestatemanager.utils.MIN_SALE_DATE_TAG
import com.openclassrooms.realestatemanager.utils.Util.hideKeyboard
import kotlinx.coroutines.flow.collect
import java.text.NumberFormat
import java.util.*


class FilterBottomSheetDialog : BottomSheetDialogFragment(), DatePickerListener,
    FilterInterestAdapter.InterestListener {

    private lateinit var binding: FilterSheetDialogFragmentBinding
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var interestAdapter: FilterInterestAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FilterSheetDialogFragmentBinding.bind(view)
        binding.mainViewModel = mainActivityViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initUI()
    }

    private fun initUI() {
        binding.filterSheetDialogRsPrice.setLabelFormatter { price: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(price.toDouble())
        }
        binding.filterSheetDialogRsSize.setLabelFormatter { size: Float ->
            val format = NumberFormat.getInstance()
            format.maximumFractionDigits = 0
            "${size.toInt()}m"
        }
        binding.filterSheetDialogButtonMinEntryDate.setOnClickListener {
            DatePickerDialog(requireContext(), this, MIN_ENTRY_DATE_TAG).openDatePickerDialog()
        }
        binding.filterSheetDialogButtonMaxEntryDate.setOnClickListener {
            DatePickerDialog(requireContext(), this, MAX_ENTRY_DATE_TAG).openDatePickerDialog()
        }
        binding.filterSheetDialogButtonMinSaleDate.setOnClickListener {
            DatePickerDialog(requireContext(), this, MIN_SALE_DATE_TAG).openDatePickerDialog()
        }
        binding.filterSheetDialogButtonMaxSaleDate.setOnClickListener {
            DatePickerDialog(requireContext(), this, MAX_SALE_DATE_TAG).openDatePickerDialog()
        }
        binding.filterSheetDialogButtonApply.setOnClickListener {
            mainActivityViewModel.applyFilter()
            this.dismiss()
        }
        binding.filterSheetDialogButtonClose.setOnClickListener {
            this.dismiss()
        }
        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.uiEventFlow.collect { event ->
                when (event) {
                    is MainActivityViewModel.UIEvent.ShowToast -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                    }
                    is MainActivityViewModel.UIEvent.ResetNearByInterestList -> {
                        interestAdapter = FilterInterestAdapter(this@FilterBottomSheetDialog)
                        binding.filterSheetDialogRvInterest.adapter = interestAdapter
                    }
                }
            }
        }
        interestAdapter = FilterInterestAdapter(this)
        binding.filterSheetDialogRvInterest.adapter = interestAdapter
        binding.filterSheetDialogRvInterest.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filterSheetDialogRvInterest.scrollToPosition(2)

        view!!.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog?
            if (dialog != null) {
                val bottomSheet =
                    dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                if (bottomSheet != null) {
                    val bottomSheetBehavior: BottomSheetBehavior<*> =
                        BottomSheetBehavior.from(bottomSheet)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    bottomSheetBehavior.peekHeight = 0
                    bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
                        override fun onStateChanged(@NonNull view1: View, i: Int) {
                            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED || bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                                if (!isStateSaved) dismissAllowingStateLoss()
                            }
                        }

                        override fun onSlide(@NonNull view1: View, v: Float) {}
                    })
                }
            }
        }
        binding.filterSheetDialogEtCity.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@OnKeyListener true
            }
            false
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.filter_sheet_dialog_fragment, container, false)

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onUpdateSaleDate(date: Date?, tag: String?) {
        mainActivityViewModel.updateDate(date, tag)
    }

    override fun onInterestSelected(nearbyInterests: List<NearbyInterest>) {
        mainActivityViewModel.updateNearByInterest(nearbyInterests)
    }

}