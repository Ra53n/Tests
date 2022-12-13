package com.geekbrains.tests

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.details.DetailsViewModel
import com.geekbrains.tests.view.details.ViewDetailsContract
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class DetailsViewModelTest {

    private lateinit var viewModel: DetailsViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = DetailsViewModel()
        viewModel.onAttach(viewContract)
    }

    @Test
    fun incrementTest() {
        val observer = Observer<Int> {}
        try {
            viewModel.countLiveData.observeForever(observer)
            var counter = 0
            viewModel.setCounter(counter)
            viewModel.onIncrement()
            Assert.assertEquals(++counter, viewModel.countLiveData.value)
        } finally {
            viewModel.countLiveData.removeObserver(observer)
        }
    }

    @Test
    fun decrementTest() {
        val observer = Observer<Int> {}
        try {
            viewModel.countLiveData.observeForever(observer)
            var counter = 2
            viewModel.setCounter(counter)
            viewModel.onDecrement()
            Assert.assertEquals(--counter, viewModel.countLiveData.value)
        } finally {
            viewModel.countLiveData.removeObserver(observer)
        }
    }

    @Test
    fun onDetachPresenterTest() {
        val observer = Observer<Int> {}
        try {
            viewModel.countLiveData.observeForever(observer)
            val counter = 0
            viewModel.setCounter(counter)
            viewModel.onDetach()
            viewModel.onIncrement()
            viewModel.onDecrement()
            Assert.assertEquals(counter, viewModel.countLiveData.value)
        } finally {
            viewModel.countLiveData.removeObserver(observer)
        }
    }

    @After
    fun close() {
        stopKoin()
    }
}