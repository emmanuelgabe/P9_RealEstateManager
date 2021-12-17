package com.openclassrooms.realestatemanager.data.local.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.domain.models.*
import com.openclassrooms.realestatemanager.utils.REAL_ESTATE_TABLE_NAME

@Entity(tableName = REAL_ESTATE_TABLE_NAME)
data class RealEstateEntity(
    @PrimaryKey val id: String,
    val address: Address,
    val status: RealEstateStatus,
    val price: Int,
    val size: Int,
    val room: Int,
    val description: String,
    val type: RealEstateType,
    @ColumnInfo(name = "photo_description")
    val photoDescription: List<String>? = null,
    @ColumnInfo(name = "photo_uri") val photoUri: List<Uri>? = null,
    @ColumnInfo(name = "map_photo") val mapPhoto: Uri? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    @ColumnInfo(name = "entry_date") val entryDate: String,
    @ColumnInfo(name = "sale_date") val saleDate: String? = null,
    @ColumnInfo(name = "real_estate_agent") val realEstateAgent: String,
    @ColumnInfo(name = "nearby_interest") val nearbyInterest: List<NearbyInterest>? = null
) {
    fun entityToRealEstate(): RealEstate {
        return RealEstateFactory().createRealEstate(
            id = this.id,
            type = this.type,
            price = this.price,
            size = this.size,
            room = this.room,
            description = this.description,
            photosUri = this.photoUri ?: emptyList(),
            photosDescription = this.photoDescription ?: emptyList(),
            streetNumber = this.address.streetNumber,
            streetName = this.address.streetName,
            postalCode = this.address.postalCode,
            city = this.address.city,
            country = this.address.country,
            status = this.status,
            mapPhoto = this.mapPhoto,
            lat = this.lat,
            lng = this.lng,
            entryDate = this.entryDate,
            saleDate = this.saleDate ?: "",
            realEstateAgent = this.realEstateAgent,
            nearbyInterest = (this.nearbyInterest ?: emptyList()).toMutableList(),
        )
    }

    companion object {
        fun realEstateToRealEStateEntity(realEstate: RealEstate): RealEstateEntity {
            return RealEstateEntity(
                id = realEstate.id,
                type = realEstate.type!!,
                price = realEstate.price!!,
                size = realEstate.size!!,
                room = realEstate.room!!,
                description = realEstate.description!!,
                address = Address(
                    streetNumber = realEstate.address!!.streetNumber,
                    streetName = realEstate.address.streetName,
                    postalCode = realEstate.address.postalCode,
                    city = realEstate.address.city,
                    country = realEstate.address.country
                ),
                status = realEstate.status,
                mapPhoto = realEstate.mapPhoto,
                lat = realEstate.lat,
                lng = realEstate.lng,
                entryDate = realEstate.entryDate,
                saleDate = if (realEstate.saleDate.isNullOrBlank()) null else realEstate.saleDate,
                realEstateAgent = realEstate.realEstateAgent!!,
                nearbyInterest = if (realEstate.nearbyInterest.isEmpty()) null else realEstate.nearbyInterest,
                photoUri = getUrisFromPhotoList(realEstate.photos),
                photoDescription = getDescriptionsFromPhotoList(realEstate.photos)
            )
        }

        private fun getUrisFromPhotoList(photos: List<Photo>): List<Uri> {
            val uriList = mutableListOf<Uri>()
            for (photo in photos) {
                uriList.add(photo.uri)
            }
            return uriList
        }

        private fun getDescriptionsFromPhotoList(photos: List<Photo>): List<String> {
            val descriptionList = mutableListOf<String>()
            for (photo in photos) {
                descriptionList.add(photo.description)
            }
            return descriptionList
        }
    }
}