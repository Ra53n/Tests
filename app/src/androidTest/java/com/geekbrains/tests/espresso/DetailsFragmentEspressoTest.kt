package com.geekbrains.tests.espresso

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.R
import com.geekbrains.tests.view.details.DetailsFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsFragmentEspressoTest {

    private lateinit var scenario: FragmentScenario<DetailsFragment>

    @Before
    fun setup() {
        //Запускаем Fragment в корне Activity
        scenario = launchFragmentInContainer()
    }

    @Test
    fun fragment_testBundle() {
        //Можно передавать аргументы во Фрагмент, но это необязательно
        val fragmentArgs = bundleOf("TOTAL_COUNT_EXTRA" to 10)
        //Запускаем Fragment с аргументами
        val scenario = launchFragmentInContainer<DetailsFragment>(fragmentArgs)
        //Возможность менять стейт Фрагмента
        scenario.moveToState(Lifecycle.State.RESUMED)

        val assertion = matches(withText("Number of results: 10"))
        onView(withId(R.id.totalCountDetailsFragmentTextView)).check(assertion)
    }

    @Test
    fun fragment_testSetCountMethod() {
        scenario.onFragment { fragment ->
            fragment.setCount(10)
        }
        val assertion = matches(withText("Number of results: 10"))
        onView(withId(R.id.totalCountDetailsFragmentTextView))
            .check(matches(isDisplayed()))
            .check(assertion)
    }

    @Test
    fun fragment_testIncrementButton() {
        onView(withId(R.id.incrementButton)).perform(click())
        onView(withId(R.id.totalCountDetailsFragmentTextView)).check(matches(withText("Number of results: 1")))
    }

    @Test
    fun fragment_testBundleInc() {
        val fragmentArgs = bundleOf("TOTAL_COUNT_EXTRA" to 100)
        val scenario = launchFragmentInContainer<DetailsFragment>(fragmentArgs)
        val countTextView = onView(withId(R.id.totalCountDetailsFragmentTextView))

        countTextView.check(matches(withText("Number of results: 100")))
        onView(withId(R.id.incrementButton)).perform(click())
        countTextView.check(matches(withText("Number of results: 101")))
    }

    @Test
    fun fragment_testBundleDec() {
        val fragmentArgs = bundleOf("TOTAL_COUNT_EXTRA" to 100)
        val scenario = launchFragmentInContainer<DetailsFragment>(fragmentArgs)
        val countTextView = onView(withId(R.id.totalCountDetailsFragmentTextView))

        countTextView.check(matches(withText("Number of results: 100")))
        onView(withId(R.id.decrementButton)).perform(click())
        countTextView.check(matches(withText("Number of results: 99")))
    }

    @Test
    fun fragment_testNoArgs() {
        val countTextView = onView(withId(R.id.totalCountDetailsFragmentTextView))
        countTextView.check(matches(withText("Number of results: 0")))
    }
}
