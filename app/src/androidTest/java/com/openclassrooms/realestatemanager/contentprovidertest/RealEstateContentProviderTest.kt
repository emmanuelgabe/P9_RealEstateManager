package com.openclassrooms.realestatemanager.contentprovidertest

import android.content.ContentResolver
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.net.toUri
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.data.local.DataConverter
import com.openclassrooms.realestatemanager.data.local.RealEstateDatabase
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import com.openclassrooms.realestatemanager.data.local.provider.RealEstateContentProvider.Companion.URI_REAL_ESTATE
import com.openclassrooms.realestatemanager.domain.models.Address
import com.openclassrooms.realestatemanager.domain.models.RealEstateStatus
import com.openclassrooms.realestatemanager.domain.models.RealEstateType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class RealEstateContentProviderTest {

    private var contentResolver: ContentResolver? = null
    private lateinit var realEstateDatabase: RealEstateDatabase
    private val uriRealEstate = "${URI_REAL_ESTATE}/${UUID.randomUUID()}".toUri()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        realEstateDatabase = Room.inMemoryDatabaseBuilder(
            context,
            RealEstateDatabase::class.java
        )
            .allowMainThreadQueries()
            .addTypeConverter(DataConverter())
            .build()
        contentResolver = context.contentResolver
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = realEstateDatabase.close()

    @Test
    fun getRealEstateWhenNoItemInserted() {
        val cursor = contentResolver!!.query(
            uriRealEstate, null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(0))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getRealEstates() = runBlocking {

        realEstateDatabase.realEstateDao.insertRealEstate(fakeRealEstate())
        realEstateDatabase.realEstateDao.insertRealEstate(fakeRealEstate())

        val cursor = contentResolver!!.query(
            uriRealEstate, null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(2))
    }

    private fun fakeRealEstate(): RealEstateEntity {
        return RealEstateEntity(
            id = "fakeId",
            address = Address(
                streetNumber = 4,
                streetName = "fakeStreetName",
                postalCode = 7500,
                city = "fakeCity",
                country = "fakeCountry"
            ),
            price = 100000,
            size = 80,
            room = 4,
            description = "fakeDescription",
            type = RealEstateType.APARTMENT,
            lat = null,
            lng = null,
            entryDate = Calendar.getInstance().time,
            status = RealEstateStatus.AVAILABLE,
            realEstateAgent = "fakeAgent"
        )
    }
}