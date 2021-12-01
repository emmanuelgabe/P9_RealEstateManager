package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build.VERSION
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.utils.Utils.*
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.util.ReflectionHelpers
import java.util.*


/**
 * Create by Emmanuel gabé on 12/08/2021.
 */

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class UtilsUnitTest {

    @Test
    fun eurosToDollar_returnCorrectConversion() {
        val dollar = 1000
        val euros = convertDollarToEuro(dollar)
        assertEquals(844, euros)
    }

    @Test
    fun dollarToEuros_returnCorrectConversion() {
        val euros = 100
        val dollar = convertEurosToDollar(euros)
        assertEquals(119, dollar)
    }

    @Test
    fun getDayToDate_returnCorrectDateWithRightFormat() {
        val currentDate = getTodayDate().split("/")
        assertEquals(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), currentDate[0].toInt())
        assertEquals(Calendar.getInstance().get(Calendar.MONTH) + 1, currentDate[1].toInt())
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), currentDate[2].toInt())

    }

    @Test
    fun getInternetAvailability_returnCorrectState() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        Assert.assertTrue(isInternetAvailable(context))
        val shadowOfActiveCapabilities = Shadows.shadowOf(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork))
        shadowOfActiveCapabilities.removeTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowOfActiveCapabilities.removeTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        Assert.assertFalse(isInternetAvailable(context))

        ReflectionHelpers.setStaticField(VERSION::class.java, "SDK_INT", 19)
        val shadowOfActiveNetworkInfo = Shadows.shadowOf(connectivityManager.activeNetworkInfo)
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED)
        Assert.assertTrue(isInternetAvailable(context))
    }
}