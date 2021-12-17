package com.openclassrooms.realestatemanager.presentation.realestatelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.RealEstateItemBinding
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateStatus

class RealEstateListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<RealEstate>() {

        override fun areItemsTheSame(oldItem: RealEstate, newItem: RealEstate): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RealEstate,
            newItem: RealEstate
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.real_estate_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<RealEstate>) {
        differ.submitList(list)
    }

    inner class ViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = RealEstateItemBinding.bind(itemView)

        fun bind(realEstate: RealEstate) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(layoutPosition, realEstate)
            }
            binding.realEstateItemImageView.clipToOutline = true
            if (realEstate.photos.isNotEmpty()) {
                binding.realEstateItemImageView.load(realEstate.photos[0].uri)
            } else {
                binding.realEstateItemImageView.load(R.drawable.ic_no_photography_24)
            }
            binding.realEstateItemTextViewDescription.text = context.getString(
                R.string.real_estate_list_adapter_description,
                realEstate.type,
                realEstate.size,
                realEstate.room
            )
            binding.realEstateItemTextViewPrice.text =
                context.getString(R.string.real_estate_list_adapter_price, realEstate.price)
            binding.realEstateItemTextViewPlace.text = context.getString(
                R.string.real_estate_list_place,
                realEstate.address!!.postalCode,
                realEstate.address.city,
                realEstate.address.country
            )
            if (realEstate.status == RealEstateStatus.SOLD) {
                binding.realEstateItemIvSoldBanner.visibility = View.VISIBLE
                binding.realEstateItemIvSoldBanner.setImageResource(R.drawable.sold_banner)
            } else {
                binding.realEstateItemIvSoldBanner.visibility = View.INVISIBLE
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: RealEstate)
    }
}