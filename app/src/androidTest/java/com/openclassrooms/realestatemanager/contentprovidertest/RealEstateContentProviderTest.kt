package com.openclassrooms.realestatemanager.contentprovidertest

import android.content.ContentResolver
import androidx.core.net.toUri
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.data.local.provider.RealEstateContentProvider.Companion.URI_REAL_ESTATE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class RealEstateContentProviderTest {

    private var contentResolver: ContentResolver? = null
    private val uriRealEstate = "${URI_REAL_ESTATE}/${UUID.randomUUID()}".toUri()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        contentResolver = context.contentResolver
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getRealEstates() = runBlocking {
        val cursor = contentResolver!!.query(
            uriRealEstate, null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.columnNames[2], `is`("status"))
        assertThat(cursor.columnCount, `is`(17))
    }
}