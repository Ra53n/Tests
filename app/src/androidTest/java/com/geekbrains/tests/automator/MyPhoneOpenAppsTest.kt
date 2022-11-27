package com.geekbrains.tests.automator

import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyPhoneOpenAppsTest {
    private val uiDevice: UiDevice =
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun test_OpenSettings() {
        uiDevice.pressHome()
        uiDevice.swipe(500, 1500, 500, 0, 5)

        val appViews = UiScrollable(UiSelector().scrollable(false))

        val settingsApp = appViews
            .getChildByText(
                UiSelector()
                    .className(TextView::class.java.name),
                "Settings"
            )
        settingsApp.clickAndWaitForNewWindow()

        val settingsValidation =
            uiDevice.findObject(UiSelector().packageName("com.android.settings"))
        Assert.assertTrue(settingsValidation.exists())
    }

    @Test
    fun test_OpenSettingsSystem() {
        uiDevice.pressHome()
        uiDevice.swipe(500, 1500, 500, 0, 5)

        val appViews = UiScrollable(UiSelector().scrollable(false))

        val settingsApp = appViews
            .getChildByText(
                UiSelector()
                    .className(TextView::class.java.name),
                "Settings"
            )
        settingsApp.clickAndWaitForNewWindow()

        val settingsViews = UiScrollable(UiSelector().scrollable(true))
        val systemSettings =
            settingsViews.getChildByText(UiSelector().className(TextView::class.java), "System")
        systemSettings.clickAndWaitForNewWindow()
        val systemSettingsValidation = uiDevice.findObject(UiSelector().text("Gestures"))
        Assert.assertTrue(systemSettingsValidation.exists())
    }

    @Test
    fun test_OpenPhone() {
        uiDevice.pressHome()
        uiDevice.swipe(500, 1500, 500, 0, 5)
        val appViews = UiScrollable(UiSelector().scrollable(false))

        val phoneApp = appViews
            .getChildByText(
                UiSelector()
                    .className(TextView::class.java.name),
                "Phone"
            )
        phoneApp.clickAndWaitForNewWindow()
        val phoneContactsButton =
            uiDevice.findObject(UiSelector().text("Contacts"))
        Assert.assertTrue(phoneContactsButton.exists())
        phoneContactsButton.click()
        val phoneContactsValidation = uiDevice.findObject(UiSelector().text("CREATE NEW CONTACT"))
        Assert.assertTrue(phoneContactsValidation.exists())
    }
}