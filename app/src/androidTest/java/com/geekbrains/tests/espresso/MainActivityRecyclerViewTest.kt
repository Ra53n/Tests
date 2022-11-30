package com.geekbrains.tests.espresso

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.BuildConfig
import com.geekbrains.tests.R
import com.geekbrains.tests.view.search.MainActivity
import com.geekbrains.tests.view.search.SearchResultAdapter.SearchResultViewHolder
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityRecyclerViewTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activitySearch_ScrollTo() {
        if (BuildConfig.TYPE == "FAKE") {
            loadList()

            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.scrollTo<SearchResultViewHolder>(
                        hasDescendant(withText("FullName: 42"))
                    )
                )
        } else {
            loadRealList()
            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.scrollTo<SearchResultViewHolder>(
                        hasDescendant(withText("ReactiveX/RxJava"))
                    )
                )
        }
    }

    @Test
    fun activitySearch_PerformClickAtPosition() {
        if (BuildConfig.TYPE == "FAKE") {
            loadList()

            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<SearchResultViewHolder>(
                        0,
                        click()
                    )
                )
        } else {
            loadRealList()
            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<SearchResultViewHolder>(
                        0,
                        click()
                    )
                )
        }
    }

    @Test
    fun activitySearch_PerformClickOnItem() {
        val recyclerView = onView(withId(R.id.recyclerView))
        if (BuildConfig.TYPE == "FAKE") {
            loadList()

            recyclerView.perform(
                RecyclerViewActions.scrollTo<SearchResultViewHolder>(
                    hasDescendant(withText("FullName: 50"))
                )
            )

            recyclerView.perform(
                RecyclerViewActions.actionOnItem<SearchResultViewHolder>(
                    hasDescendant(withText("FullName: 42")),
                    click()
                )
            )
        } else {
            loadRealList()
            recyclerView.perform(
                RecyclerViewActions.scrollTo<SearchResultViewHolder>(
                    hasDescendant(withText("ReactiveX/RxKotlin"))
                )
            )
            recyclerView.perform(
                RecyclerViewActions.actionOnItem<SearchResultViewHolder>(
                    hasDescendant(withText("ReactiveX/RxAndroid")),
                    longClick()
                )
            )
        }
    }

    @Test
    fun activitySearch_PerformLongItemClick() {
        loadRealList()
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItem<SearchResultViewHolder>(
                hasDescendant(withText("ReactiveX/RxAndroid")),
                longClickAndWait(R.id.repositoryName)
            )
        )
        onView(withId(R.id.searchEditText))
            .check(matches(withText("ReactiveX/RxAndroid")))

        onView(withId(R.id.totalCountTextView))
            .check(matches(withText("Number of results: 11")))
    }

    @Test
    fun activitySearch_PerformCustomClick() {
        if (BuildConfig.TYPE == "FAKE") {
            loadList()

            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions
                        .actionOnItemAtPosition<SearchResultViewHolder>(
                            0,
                            tapOnItemWithId(R.id.checkbox)
                        )
                )
        }
    }

    private fun loadList() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
    }

    private fun loadRealList() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("RxJava"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
        onView(isRoot()).perform(delay())
    }

    private fun tapOnItemWithId(id: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Нажимаем на view с указанным id"
        }

        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById(id) as View
            v.performClick()
        }
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

    private fun longClickAndWait(id: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String = "click and wait for $2 seconds"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController, view: View) {
                view.findViewById<View>(id).performLongClick()
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}
