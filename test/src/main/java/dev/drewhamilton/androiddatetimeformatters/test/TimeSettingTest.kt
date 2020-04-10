package dev.drewhamilton.androiddatetimeformatters.test

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assume.assumeTrue
import org.junit.Before
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * A base test class that facilitates using and changing the [Settings.System.TIME_12_24] setting by caching the current
 * setting before each test and resetting it after.
 *
 * Currently it appears that even though changing this setting requires the [android.Manifest.permission.WRITE_SETTINGS]
 * permission, tests work just fine without explicitly requesting it as long as the permission is declared in the test
 * manifest.
 *
 * Known issues:
 *  - Tests fail on APIs 23-27 because null is an unsupported value by the setter, but is returned by the
 *    getter on new devices. Further, `assumeTrue(false)` causes failures instead of the expected ignores.
 *  - 12-hour format tests in e.g. Italy locale fail on API 16. For some reason the Android formatter outputs "4:44 PM"
 *    while the ThreeTenBP formatter outputs "4:44 p.", despite an identical format pattern of "h:mm a".
 */
abstract class TimeSettingTest {

    private var originalTimeSetting: String? = null

    @Before fun cacheOriginalTimeSetting() {
        originalTimeSetting = systemTimeSetting
        Log.d(TAG, "cached original setting: $originalTimeSetting")
        // Ignore the test if the device won't allow resetting to the original hour setting:
        ignoreIfThrowing<IllegalArgumentException> {
            resetTimeSetting()
        }
    }

    @After fun resetTimeSetting() {
        Log.d(TAG, "Resetting original setting: $originalTimeSetting")
        systemTimeSetting = originalTimeSetting
    }

    protected val testContext get(): Context = InstrumentationRegistry.getInstrumentation().context

    protected val androidTimeFormatInUtc: DateFormat
        get() = android.text.format.DateFormat.getTimeFormat(testContext).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    protected var systemTimeSetting: String?
        get() = Settings.System.getString(testContext.contentResolver, Settings.System.TIME_12_24)
        set(value) {
            Settings.System.putString(testContext.contentResolver, Settings.System.TIME_12_24, value)
        }

    protected var testLocale: Locale
        get() = testContext.resources.configuration.locale
        set(value) {
            testContext.resources.configuration.locale = value
        }

    /**
     * Ignore the test in progress if [block] throws an instance of [E].
     */
    private inline fun <reified E : Exception> ignoreIfThrowing(block: () -> Unit) {
        try {
            block()
        } catch (exception: Exception) {
            if (exception is E)
                assumeTrue("Test ignored: ${exception.message}", false)
            else
                throw exception
        }
    }

    protected companion object {
        private val TAG = TimeSettingTest::class.java.simpleName

        @JvmStatic val timeFormat24InUtc: DateFormat
            get() = SimpleDateFormat("HH:mm", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
    }
}