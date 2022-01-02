package com.openclassrooms.realestatemanager.presentation.dialog

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.InterestItemBinding
import com.openclassrooms.realestatemanager.domain.models.NearbyInterest


class FilterInterestAdapter(private val interestListener: InterestListener) :
    RecyclerView.Adapter<FilterInterestAdapter.ViewHolder>() {

    val nearbyInterests = mutableListOf<NearbyInterest>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            InterestItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), interestListener
        )
    }

    override fun onBindViewHolder(holder: FilterInterestAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }


    override fun getItemCount(): Int = NearbyInterest.values().size

    inner class ViewHolder(
        private val binding: InterestItemBinding,
        private val interestListener: InterestListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.interestItemTv.text = NearbyInterest.values()[position].toString()

            binding.interestItemTv.setOnClickListener {
                if (binding.interestItemTv.isSelected) {
                    nearbyInterests.remove(NearbyInterest.values()[position])
                    binding.interestItemTv.isSelected = false
                    binding.interestItemTv.setTypeface(null, Typeface.NORMAL)
                    binding.interestItemTv.setTextColor(Color.BLACK)

                } else {
                    nearbyInterests.add(NearbyInterest.values()[position])
                    binding.interestItemTv.isSelected = true
                    binding.interestItemTv.setTypeface(null, Typeface.BOLD)
                    binding.interestItemTv.setTextColor(Color.WHITE)

                }
                interestListener.onInterestSelected(nearbyInterests = nearbyInterests)
            }
        }
    }

    interface InterestListener {
        fun onInterestSelected(nearbyInterests: List<NearbyInterest>)
    }
}