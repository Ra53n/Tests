package com.geekbrains.tests.view.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.tests.presenter.details.PresenterDetailsContract
import com.geekbrains.tests.view.ViewContract

class DetailsViewModel : PresenterDetailsContract, ViewModel() {

    private val _countLiveData = MutableLiveData<Int>()
    val countLiveData: LiveData<Int> = _countLiveData
    private var viewContract: ViewDetailsContract? = null
    private var count: Int = 0

    override fun setCounter(count: Int) {
        this.count = count
    }

    override fun onIncrement() {
        count++
        _countLiveData.value = count
    }

    override fun onDecrement() {
        count--
        _countLiveData.value = count
    }

    override fun onAttach(view: ViewContract) {
        viewContract = view as ViewDetailsContract
    }

    override fun onDetach() {
        viewContract = null
    }
}