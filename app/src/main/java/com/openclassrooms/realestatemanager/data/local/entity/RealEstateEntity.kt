package com.openclassrooms.realestatemanager.data.local.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.domain.models.*

@Entity(tableName = "real_estate_table")
data class RealEstateEntity(
    @PrimaryKey val id: String,
    val address: Address,
    val status: RealEstateStatus? = null,
    val price: Int? = null,
    val size: Int? = null,
    val room: Int? = null,
    val description: String? = null,
    val type: RealEstateType? = null,
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
    public fun entityToRealEstate(): RealEstate {
        return RealEstate(
            id = this.id,
            type = this.type!!,
            price = this.price!!,
            size = this.size!!,
            room = this.room!!,
            description = this.description!!,
            photoUri = this.photoUri,
            photoDescription = this.photoDescription,
            address = Address(
                streetNumber = this.address.streetNumber,
                streetName = this.address.streetName,
                postalCode = this.address.postalCode,
                city = this.address.city,
                country = this.address.country
            ),
            status = this.status!!,
            mapPhoto = this.mapPhoto,
            lat = this.lat,
            lng = this.lng,
            entryDate = this.entryDate,
            saleDate = this.saleDate,
            realEstateAgent = this.realEstateAgent,
            nearbyInterest = this.nearbyInterest
        )
    }

    companion object {
        fun realEstateToRealEStateEntity(realEstate: RealEstate): RealEstateEntity {
            return RealEstateEntity(
                id = realEstate.id,
                type = realEstate.type,
                price = realEstate.price,
                size = realEstate.size,
                room = realEstate.room,
                description = realEstate.description,
                photoUri = realEstate.photoUri,
                photoDescription = realEstate.photoDescription,
                address = Address(
                    streetNumber = realEstate.address.streetNumber,
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
                saleDate = realEstate.saleDate,
                realEstateAgent = realEstate.realEstateAgent,
                nearbyInterest = realEstate.nearbyInterest
            )
        }
    }
}
