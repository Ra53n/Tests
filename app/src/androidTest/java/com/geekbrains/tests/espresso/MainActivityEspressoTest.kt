package com.geekbrains.tests.espresso

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.BuildConfig
import com.geekbrains.tests.R
import com.geekbrains.tests.view.search.MainActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        if (BuildConfig.TYPE == "FAKE") {
            onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 42")))
        } else {
            onView(isRoot()).perform(delay())
            onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 3420")))
        }
    }

    @Test
    fun editText_isCorrect() {
        val searchEditTextInteraction = onView(withId(R.id.searchEditText))
        searchEditTextInteraction.check(matches(isDisplayed()))
        searchEditTextInteraction.check(matches(withHint("Enter keyword e.g. android")))
    }

    @Test
    fun toDetailsButton_isCorrect(){
        val toDetailsButton = onView(withId(R.id.toDetailsActivityButton))
        toDetailsButton.check(matches(isDisplayed()))
        toDetailsButton.check(matches(withText("to details")))
    }

    @Test
    fun resultText_isCorrect(){
        val resultTextView = onView(withId(R.id.totalCountTextView))
        resultTextView.check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("text"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        onView(isRoot()).perform(delay())
        resultTextView.check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    private fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $2 seconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}
