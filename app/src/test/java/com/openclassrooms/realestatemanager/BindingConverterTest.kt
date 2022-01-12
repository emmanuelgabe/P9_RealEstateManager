package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.domain.models.NearbyInterest
import com.openclassrooms.realestatemanager.domain.models.RealEstateType
import com.openclassrooms.realestatemanager.utils.BindingConverter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BindingConverterTest {

    private lateinit var dataConverter: BindingConverter

    @Before
    fun setUp() {
        dataConverter = BindingConverter
    }

    @Test
    fun intToString() {
        val string = dataConverter.intToString(4)
        Assert.assertEquals("4", string)
    }

    @Test
    fun stringToInt() {
        val int = dataConverter.stringToInt("582")
        Assert.assertEquals(582, int)
    }

    @Test
    fun nearbyInterestToString() {
        val string = dataConverter.nearbyInterestToString(
            listOf(
                NearbyInterest.RESTAURANT,
                NearbyInterest.SHOPPINGMALL,
                NearbyInterest.TRAINSTATION
            )
        )
        Assert.assertEquals("Restaurant, Shopping mall, Train station, ", string)
        val stringEmptyList = dataConverter.nearbyInterestToString(emptyList())
        Assert.assertEquals("", stringEmptyList)
    }

    @Test
    fun stringToNearbyInterest() {
        val nearbyInterestList = dataConverter.stringToNearbyInterest("Bank, Park, Train station, ")
        val expectedList = listOf(
            NearbyInterest.BANK,
            NearbyInterest.PARK,
            NearbyInterest.TRAINSTATION
        )
        Assert.assertEquals(expectedList, nearbyInterestList)
    }

    @Test
    fun realEstateTypeToString() {
        val string = dataConverter.realEstateTypeToString(RealEstateType.PENTHOUSE)
        Assert.assertEquals("Penthouse", string)
    }

    @Test
    fun stringToRealEstateType() {
        val realEstateType = dataConverter.stringToRealEstateType("Studio")
        Assert.assertEquals(RealEstateType.STUDIO, realEstateType)
    }
}