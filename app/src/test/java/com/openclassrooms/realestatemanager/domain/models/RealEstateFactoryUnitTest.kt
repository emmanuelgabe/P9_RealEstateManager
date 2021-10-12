package com.openclassrooms.realestatemanager.domain.models

import com.openclassrooms.realestatemanager.domain.utils.DateUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

import org.junit.Test
import java.text.SimpleDateFormat

/**
 * Create by Emmanuel gabé on 08/10/2021.
 */
class RealEstateFactoryUnitTest {

    @Test
    fun `create RealEstate with factory, RealEstate successfully created`() {

        val realEstate =
            RealEstateFactory(DateUtil(SimpleDateFormat("yyyy.MM.dd HH:mm:ss"))).createRealEstate(
                id = null,
                type = RealEstateType.APARTMENT,
                price = 300000,
                size = 40,
                room = 1,
                description = "FakeRealEstateDescription",
                streetNumber = 4,
                streetName = "FakeRealEstateStreetName",
                postalCode = 451,
                city = "paris",
                country = "france",
                status = RealEstateStatus.AVAILABLE,
                lat = 0.0,
                lng = 1.1,
            )
        assert(realEstate is RealEstate)
        assertTrue(realEstate.id != null)
        assertTrue( realEstate.entryDate != null)
        assertEquals("paris",realEstate.address.city)
        assertEquals(RealEstateType.APARTMENT,realEstate.type)
        assertEquals(300000,realEstate.price)
    }
}