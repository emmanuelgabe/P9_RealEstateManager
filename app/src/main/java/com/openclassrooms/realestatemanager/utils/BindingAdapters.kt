package com.openclassrooms.realestatemanager.utils

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.slider.RangeSlider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.utils.DateUtil
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {

    @InverseBindingAdapter(attribute = "values")
    @JvmStatic
    fun getRangeSlider(slider: RangeSlider): List<Float> {
        return slider.values
    }

    @BindingAdapter("app:valuesAttrChanged")
    @JvmStatic
    fun setListeners(
        slider: RangeSlider,
        attrChange: InverseBindingListener
    ) {
        val listener = RangeSlider.OnChangeListener { _, _, _ -> attrChange.onChange() }
        slider.addOnChangeListener(listener)
    }

    @BindingAdapter("app:setPrice")
    @JvmStatic
    fun setPrice(view: TextView, price: Float) {
        val priceFormat = NumberFormat.getCurrencyInstance()
        priceFormat.currency = Currency.getInstance("USD")
        priceFormat.maximumFractionDigits = 0
        priceFormat.format(price.toDouble())
        if (price == MAX_PRICE.toFloat()) {
            view.text = view.context.getString(R.string.filter_most_max_price,priceFormat.format(price.toInt()))
        } else {
            view.text = priceFormat.format(price)
        }
    }

    @BindingAdapter("app:setSize")
    @JvmStatic
    fun setSize(view: TextView, size: Float) {
        if (size == MAX_SIZE.toFloat()) {
            view.text = view.context.getString(R.string.filter_most_max_size,size.toInt())
        } else {
            view.text = view.context.getString(R.string.filter_max_size,size.toInt())
        }
    }

    @BindingAdapter("app:photo_count")
    @JvmStatic
    fun setPhoto(view: TextView, photoCount: Float) {
        if (photoCount == MAX_PHOTO.toFloat()) {
            view.text = view.context.getString(R.string.filter_most_max_photo,photoCount.toInt())
        } else {
            view.text = "${photoCount.toInt()}"
        }
    }

    @BindingAdapter("app:setDate")
    @JvmStatic
    fun setDate(view: TextView, date: Date?) {
        if (date != null) {
            view.text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date)
        } else {
            view.text = "N/A"
        }
    }

    @BindingAdapter("app:textMinEntryDateButton")
    @JvmStatic
    fun textMinEntryDateButton(view: Button, date: Date?) {
        if (date == null) {
            view.text = view.context.getString(R.string.filter_add_min_entry_date)
        } else {
            view.text = view.context.getString(R.string.filter_edit_min_entry_date)
        }
    }

    @BindingAdapter("app:textMaxEntryDateButton")
    @JvmStatic
    fun textMaxEntryDateButton(view: Button, date: Date?) {
        if (date == null) {
            view.text = view.context.getString(R.string.filter_add_max_entry_date)
        } else {
            view.text =view.context.getString(R.string.filter_edit_max_entry_date)
        }
    }

    @BindingAdapter("app:textMinSaleDateButton")
    @JvmStatic
    fun textMinSaleDateButton(view: Button, date: Date?) {
        if (date == null) {
            view.text = view.context.getString(R.string.filter_add_min_sale_date)
        } else {
            view.text = view.context.getString(R.string.filter_edit_min_sale_date)
        }
    }

    @BindingAdapter("app:textMaxSaleDateButton")
    @JvmStatic
    fun textMaxSaleDateButton(view: Button, date: Date?) {
        if (date == null) {
            view.text = view.context.getString(R.string.filter_add_max_sale_date)
        } else {
            view.text = view.context.getString(R.string.filter_edit_max_sale_date)
        }
    }

    @BindingAdapter("app:textSaleDate")
    @JvmStatic
    fun textSaleDate(view: TextView, date: Date?) {
        if (date == null) {
            view.text = ""
        } else {
            view.text = DateUtil.formatDate(date)
        }
    }
}