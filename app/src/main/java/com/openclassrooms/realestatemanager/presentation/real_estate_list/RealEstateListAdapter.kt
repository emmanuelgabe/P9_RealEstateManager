package com.openclassrooms.realestatemanager.presentation.real_estate_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.RealEstate

/**
 * Create by Emmanuel gabé on 12/10/2021.
 */
class RealEstateListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val diffCallback = object : DiffUtil.ItemCallback<RealEstate>() {

        override fun areItemsTheSame(oldItem: RealEstate, newItem: RealEstate): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RealEstate,
            newItem: RealEstate
        ): Boolean {
            return oldItem.equals(newItem)
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return RealEstateViewHolder(
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
            is RealEstateViewHolder -> {
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

    class RealEstateViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: RealEstate) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(layoutPosition, item)
            }

            TODO("bind view with data")
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: RealEstate)
    }
}
