package com.geekbrains.tests

import com.geekbrains.tests.presenter.details.DetailsPresenter
import com.geekbrains.tests.view.details.ViewDetailsContract
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times

class DetailsPresenterTest {

    private lateinit var presenter: DetailsPresenter

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        presenter = DetailsPresenter()
        presenter.onAttach(viewContract)
    }

    @Test
    fun incrementTest() {
        var counter = 0
        presenter.setCounter(counter)
        presenter.onIncrement()
        verify(viewContract).setCount(++counter)
    }

    @Test
    fun decrementTest() {
        var counter = 2
        presenter.setCounter(counter)
        presenter.onDecrement()
        verify(viewContract).setCount(--counter)
    }

    @Test
    fun onDetachPresenterTest() {
        var counter = 0
        presenter.setCounter(counter)
        presenter.onDetach()
        presenter.onIncrement()
        presenter.onDecrement()
        verify(viewContract, Times(0)).setCount(counter)
        verify(viewContract, Times(0)).setCount(++counter)
    }

}