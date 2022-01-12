package com.openclassrooms.realestatemanager.databasetest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.data.local.DataConverter
import com.openclassrooms.realestatemanager.data.local.LocalDataSource
import com.openclassrooms.realestatemanager.data.local.RealEstateDao
import com.openclassrooms.realestatemanager.data.local.RealEstateDatabase
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import com.openclassrooms.realestatemanager.domain.models.*
import com.openclassrooms.realestatemanager.utils.MAX_PRICE
import com.openclassrooms.realestatemanager.utils.MAX_SIZE
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class RealEstateDatabaseTest {
    private lateinit var realEstateDao: RealEstateDao
    private lateinit var realEstateDatabase: RealEstateDatabase
    private lateinit var localDataSource: LocalDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        realEstateDatabase = Room.inMemoryDatabaseBuilder(
            context, RealEstateDatabase::class.java
        )
            .addTypeConverter(DataConverter())
            .build()
        realEstateDao = realEstateDatabase.realEstateDao
        localDataSource = LocalDataSource(realEstateDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        realEstateDatabase.close()
    }

    @Test
    fun insertAndGetRealEstates() = runBlocking {
        realEstateDao.insertRealEstate(fakeRealEstate("1"))
        realEstateDao.insertRealEstate(fakeRealEstate("2"))
        // test replace in case of same id
        realEstateDao.insertRealEstate(fakeRealEstate("2"))
        realEstateDao.insertRealEstate(fakeRealEstate("3"))
        val realEstates = realEstateDao.getAllRealEstates().first()
        assertEquals(realEstates.size, 3)
    }

    @Test
    fun updateAndGetRealEstates() = runBlocking {
        realEstateDao.insertRealEstate(fakeRealEstate("1"))
        realEstateDao.updateRealEstate(fakeRealEstate("1", "updateDescription"))
        val realEstates = realEstateDao.getAllRealEstates().first()
        assertEquals(realEstates[0].description, "updateDescription")
    }

    @Test
    fun insertAndGetRealEstatesWithCursor() = runBlocking {
        realEstateDao.insertRealEstate(fakeRealEstate("1"))
        realEstateDao.insertRealEstate(fakeRealEstate("2", "lastAdded"))
        val cursor = realEstateDao.getAllRealEstatesWithCursor()
        assertEquals(cursor.count, 2)
        cursor.moveToLast()
        val descriptionAdded = cursor.getString(6)
        assertEquals(descriptionAdded, "lastAdded")
    }

    @Test
    fun insertAndGetRealEstatesFromLocalDataSource() = runBlocking {
        localDataSource.insertRealEstate(fakeRealEstate("1"))
        localDataSource.insertRealEstate(fakeRealEstate("2"))
        // test replace in case of same id
        localDataSource.insertRealEstate(fakeRealEstate("2"))
        localDataSource.insertRealEstate(fakeRealEstate("3"))
        val realEstates = localDataSource.getAllRealEstates().first()
        assertEquals(realEstates.size, 3)
    }

    @Test
    fun updateAndGetRealEstatesFromLocalDataSource() = runBlocking {
        localDataSource.insertRealEstate(fakeRealEstate("1"))
        localDataSource.updateRealEstate(fakeRealEstate("1", "updateDescription"))
        val realEstates = localDataSource.getAllRealEstates().first()
        assertEquals(realEstates[0].description, "updateDescription")
    }

    @Test
    fun getFilteredRealEstatesFromLocalDataSource_WithoutCriteria() = runBlocking {
        localDataSource.insertRealEstate(fakeRealEstate("1"))
        val realEstates = localDataSource.getFilteredRealEstates(fakeRealEstatesFilter()).first()
        assertEquals(1, realEstates.size)
    }


    @Test
    fun getFilteredRealEstatesFromLocalDataSource_WithCity() = runBlocking {
        localDataSource.insertRealEstate(fakeRealEstate("1"))
        localDataSource.insertRealEstate(fakeRealEstate("2", city = "paris"))
        localDataSource.insertRealEstate(fakeRealEstate("3"))
        val realEstates =
            localDataSource.getFilteredRealEstates(fakeRealEstatesFilter(city = "paris")).first()
        assertEquals(1, realEstates.size)
    }

    @Test
    fun getFilteredRealEstatesFromLocalDataSource_WithMultipleCriteria() = runBlocking {
        localDataSource.insertRealEstate(fakeRealEstate("1"))
        localDataSource.insertRealEstate(fakeRealEstate("2"))
        localDataSource.insertRealEstate(
            fakeRealEstate(
                "3",
                price = 50000,
                nearbyInterest = listOf(
                    NearbyInterest.BANK,
                    NearbyInterest.RESTAURANT
                )
            )
        )

        val realEstates =
            localDataSource.getFilteredRealEstates(
                fakeRealEstatesFilter(
                    minPrice = 10000,
                    maxPrice = 60000,
                    nearbyInterest = listOf(
                        NearbyInterest.BANK
                    )
                )
            ).first()
        assertEquals(1, realEstates.size)
    }

    private fun fakeRealEstate(
        id: String,
        description: String = "fakeDescription",
        city: String = "fakeCity",
        price: Int = 100000,
        nearbyInterest: List<NearbyInterest> = emptyList()
    ): RealEstateEntity {
        return RealEstateEntity(
            id = id,
            address = Address(
                streetNumber = 4,
                streetName = "fakeStreetName",
                postalCode = 7500,
                city = city,
                country = "fakeCountry"
            ),
            price = price,
            size = 80,
            room = 4,
            description = description,
            type = RealEstateType.APARTMENT,
            lat = null,
            lng = null,
            photoDescription = null,
            photoUri = null,
            mapPhoto = null,
            saleDate = null,
            nearbyInterest = nearbyInterest,
            entryDate = Calendar.getInstance().time,
            status = RealEstateStatus.AVAILABLE,
            realEstateAgent = "fakeAgent"
        )
    }

    private fun fakeRealEstatesFilter(
        minSize: Int? = 0,
        maxSize: Int? = MAX_SIZE,
        minPrice: Int? = 0,
        maxPrice: Int? = MAX_PRICE,
        minPhoto: Int? = 0,
        maxPhoto: Int? = null,
        minSaleDate: Long? = null,
        maxSaleDate: Long? = null,
        minEntryDate: Long? = null,
        maxEntryDate: Long? = null,
        nearbyInterest: List<NearbyInterest> = emptyList(),
        city: String? = null
    ): RealEstateFilter {
        return RealEstateFilter(
            minSize, maxSize, minPrice,
            maxPrice, minPhoto, maxPhoto, minSaleDate, maxSaleDate, minEntryDate, maxEntryDate,
            nearbyInterest, city
        )
    }
}