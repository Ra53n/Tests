package com.geekbrains.tests

import android.content.Context
import android.os.Build
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.search.MainActivity
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper.idleMainLooper
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var context: Context

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun editText_Test() {
        scenario.onActivity {
            val editText = it.findViewById<EditText>(R.id.searchEditText)
            val someText = "some text"
            editText.setText(someText, TextView.BufferType.EDITABLE)
            Assert.assertNotNull(editText.text)
            Assert.assertEquals(someText, editText.text.toString())
        }
    }

    @Test
    fun editTextError_Test() {
        scenario.onActivity {
            val editText = it.findViewById<EditText>(R.id.searchEditText)
            editText.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
            idleMainLooper()
            Assert.assertEquals(
                ShadowToast.getTextOfLatestToast(),
                context.getString(R.string.enter_search_word)
            )
        }
    }

    @After
    fun close() {
        scenario.close()
        stopKoin()
    }
}