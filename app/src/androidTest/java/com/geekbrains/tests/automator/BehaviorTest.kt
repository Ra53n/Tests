package com.geekbrains.tests.automator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {

    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val packageName = context.packageName

    @Before
    fun setup() {4
        uiDevice.pressHome()

        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    @Test
    fun test_MainActivityIsStarted() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        Assert.assertNotNull(editText)
    }

    @Test
    fun test_SearchIsPositive() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        editText.text = "UiAutomator"

        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        searchButton.click()
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )
        Assert.assertEquals(changedText.text.toString(), "Number of results: 734")
    }

    @Test
    fun test_OpenDetailsScreen() {
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(
                packageName,
                "toDetailsActivityButton"
            )
        )
        toDetails.click()

        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountDetailsTextView")),
                TIMEOUT
            )
        Assert.assertEquals(changedText.text, "Number of results: 0")
    }

    @Test
    fun test_IncrementDetailsScreenTest() {
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(
                packageName,
                "toDetailsActivityButton"
            )
        )

        toDetails.click()

        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountDetailsTextView")),
                TIMEOUT
            )

        Assert.assertEquals(changedText.text, "Number of results: 0")

        val incrementButton = uiDevice.findObject(By.res(packageName, "incrementButton"))
        incrementButton.click()
        Assert.assertEquals(changedText.text, "Number of results: 1")
    }

    @Test
    fun test_DecrementDetailsScreenTest() {
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(
                packageName,
                "toDetailsActivityButton"
            )
        )

        toDetails.click()

        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountDetailsTextView")),
                TIMEOUT
            )

        Assert.assertEquals(changedText.text, "Number of results: 0")

        val incrementButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        incrementButton.click()
        Assert.assertEquals(changedText.text, "Number of results: -1")
    }

    @Test
    fun test_SearchDetailsScreen() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        editText.text = "UiAutomator"

        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        searchButton.click()

        val toDetails: UiObject2 = uiDevice.wait(
            Until.findObject(By.res(packageName, "toDetailsActivityButton")), TIMEOUT
        )

        val resultText = "Number of results: 734"
        val totalCountTextView =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )
        Assert.assertEquals(totalCountTextView.text.toString(), resultText)

        toDetails.click()

        val totalCountDetailsTextView =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountDetailsTextView")),
                TIMEOUT
            )

        Assert.assertEquals(totalCountDetailsTextView.text.toString(), resultText)
    }

    @Test
    fun backPressTest_DetailsScreen() {
        val toDetails: UiObject2 = uiDevice.wait(
            Until.findObject(By.res(packageName, "toDetailsActivityButton")), TIMEOUT
        )

        toDetails.click()

        val changedTextFirst =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountDetailsTextView")),
                TIMEOUT
            )

        val defaultValue = "Number of results: 0"
        Assert.assertEquals(changedTextFirst.text, defaultValue)

        val incrementButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        incrementButton.click()
        Assert.assertEquals(changedTextFirst.text, "Number of results: -1")
        uiDevice.pressBack()
        toDetails.click()
        val changedTextLast =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountDetailsTextView")),
                TIMEOUT
            )
        Assert.assertEquals(changedTextLast.text, defaultValue)
    }

    companion object {
        private const val TIMEOUT = 5000L
    }
}
