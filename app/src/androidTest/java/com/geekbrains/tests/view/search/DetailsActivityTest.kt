package com.geekbrains.tests.view.search


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.geekbrains.tests.R
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailsActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun detailsActivityTest() {
        onView(
            allOf(
                withId(R.id.toDetailsActivityButton), withText("to details"),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.totalCountDetailsTextView), withText("Number of results: 0"),
                isDisplayed()
            )
        ).check(matches(withText("Number of results: 0")))

        onView(
            allOf(
                withId(R.id.incrementButton), withText("+"),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.totalCountDetailsTextView), withText("Number of results: 1"),
                isDisplayed()
            )
        ).check(matches(withText("Number of results: 1")))


        pressBack()

        onView(
            allOf(
                withId(R.id.toDetailsActivityButton), withText("to details"),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.totalCountDetailsTextView), withText("Number of results: 0"),
                isDisplayed()
            )
        ).check(matches(withText("Number of results: 0")))
    }
}
