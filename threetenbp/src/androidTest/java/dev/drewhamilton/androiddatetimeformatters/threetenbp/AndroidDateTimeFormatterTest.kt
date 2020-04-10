package dev.drewhamilton.androiddatetimeformatters.threetenbp

import android.os.Build
import android.util.Log
import dev.drewhamilton.androiddatetimeformatters.test.TimeSettingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalTime
import java.util.Date
import java.util.Locale

class AndroidDateTimeFormatterTest : TimeSettingTest() {

    @Test fun ofLocalizedTime_nullSystemSettingUsLocale_uses12HourFormat() {
        if (Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING) {
            Log.i(TAG, "Time setting is not nullable in API ${Build.VERSION.SDK_INT}")
            return
        }

        systemTimeSetting = null
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingUsLocale_uses12HourFormat() {
        systemTimeSetting = "12"
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingUsLocale_uses24HourFormat() {
        systemTimeSetting = "24"
        testLocale = Locale.US

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_nullSystemSettingItalyLocale_uses24HourFormat() {
        if (Build.VERSION.SDK_INT < SDK_INT_NULLABLE_TIME_SETTING) {
            Log.i(TAG, "Time setting is not nullable in API ${Build.VERSION.SDK_INT}")
            return
        }

        systemTimeSetting = null
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_12SystemSettingItalyLocale_uses12HourFormat() {
        systemTimeSetting = "12"
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    @Test fun ofLocalizedTime_24SystemSettingItalyLocale_uses24HourFormat() {
        systemTimeSetting = "24"
        testLocale = Locale.ITALY

        val formatter = AndroidDateTimeFormatter.ofLocalizedTime(testContext)
        val formattedTime = formatter.format(TIME)
        assertEquals(expectedFormattedTime, formattedTime)
    }

    private val expectedFormattedTime get(): String = androidTimeFormatInUtc.format(LEGACY_TIME)

    private companion object {
        private val TAG = AndroidDateTimeFormatterTest::class.java.simpleName

        private const val SDK_INT_NULLABLE_TIME_SETTING = 28

        private val TIME = LocalTime.of(16, 44)
        @JvmStatic private val LEGACY_TIME: Date = timeFormat24InUtc.parse("16:44")
    }
}