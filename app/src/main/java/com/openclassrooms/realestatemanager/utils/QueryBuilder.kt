package com.openclassrooms.realestatemanager.utils

import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.domain.models.NearbyInterest
import com.openclassrooms.realestatemanager.domain.models.RealEstateFilter

object QueryBuilder {

    fun getQueryFilteredRealEstate(realEstateFilter: RealEstateFilter): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM $REAL_ESTATE_TABLE_NAME WHERE ")
        if (realEstateFilter.minPrice != 0) {
            simpleQuery.append("AND price >= ${realEstateFilter.minPrice} ")
        }
        if (realEstateFilter.maxPrice != MAX_PRICE) {
            simpleQuery.append("AND price <= ${realEstateFilter.maxPrice} ")
        }
        if (realEstateFilter.city != null) {
            simpleQuery.append("AND address LIKE '%city\":\"${realEstateFilter.city}%' ")
        }
        if (realEstateFilter.minSize != 0) {
            simpleQuery.append("AND size >= ${realEstateFilter.minSize} ")
        }
        if (realEstateFilter.maxSize != MAX_SIZE) {
            simpleQuery.append("AND size <= ${realEstateFilter.maxSize} ")
        }
        if (realEstateFilter.minSaleDate != null) {
            simpleQuery.append("AND sale_date >= ${realEstateFilter.minSaleDate} ")
        }
        if (realEstateFilter.maxSaleDate != null) {
            simpleQuery.append("AND sale_date <= ${realEstateFilter.maxSaleDate} ")
        }
        if (realEstateFilter.minEntryDate != null) {
            simpleQuery.append("AND sale_date >= ${realEstateFilter.minEntryDate} ")
        }
        if (realEstateFilter.maxEntryDate != null) {
            simpleQuery.append("AND entry_date <= ${realEstateFilter.maxEntryDate} ")
        }
        if (!realEstateFilter.nearbyInterest.isNullOrEmpty()) {
            simpleQuery.append(
                "AND nearby_interest LIKE '${
                    interestQueryListFromEnumList(
                        realEstateFilter.nearbyInterest
                    )
                }' "
            )
        }
        if (realEstateFilter.minPhoto != 0) {
            TODO("Not yet implemented")
        }
        if (realEstateFilter.maxPhoto != MAX_PHOTO) {
            TODO("Not yet implemented")
        }
        if (simpleQuery.toString().contains("AND")) {
            simpleQuery.delete(37, 41) // delete firsts AND
        } else {
            simpleQuery.delete(31, 37) // delete WHERE
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

    private fun interestQueryListFromEnumList(nearbyInterests: List<NearbyInterest>): String {
        val nearByInterestListQuery = StringBuilder().append("%")
        for (nearByInterest in nearbyInterests) {
            nearByInterestListQuery.append("${nearByInterest.name}%")
        }
        return nearByInterestListQuery.toString()
    }
}