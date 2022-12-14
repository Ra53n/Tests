package com.geekbrains.tests.view.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geekbrains.tests.R

class DetailsActivity : AppCompatActivity() {

    //private val presenter: PresenterDetailsContract by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_second)
        supportFragmentManager.beginTransaction()
            .add(
                R.id.detailsFragmentContainer,
                DetailsFragment.newInstance(intent.getIntExtra(TOTAL_COUNT_EXTRA, 0))
            )
            .commitAllowingStateLoss()
    }

    companion object {

        private const val TOTAL_COUNT_EXTRA = "TOTAL_COUNT_EXTRA"

        fun getIntent(context: Context, totalCount: Int): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(TOTAL_COUNT_EXTRA, totalCount)
            }
        }
    }
}

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_details)
//        presenter.onAttach(this)
//        setUI()
//    }
//
//    private fun setUI() {
//        val count = intent.getIntExtra(TOTAL_COUNT_EXTRA, 0)
//        presenter.setCounter(count)
//        setCountText(count)
//        decrementButton.setOnClickListener { presenter.onDecrement() }
//        incrementButton.setOnClickListener { presenter.onIncrement() }
//    }
//
//    override fun onDestroy() {
//        presenter.onDetach()
//        super.onDestroy()
//    }
//
//    override fun setCount(count: Int) {
//        setCountText(count)
//    }
//
//    private fun setCountText(count: Int) {
//        totalCountDetailsTextView.text =
//            String.format(Locale.getDefault(), getString(R.string.results_count), count)
//    }
//
//    companion object {
//
//        const val TOTAL_COUNT_EXTRA = "TOTAL_COUNT_EXTRA"
//
//        fun getIntent(context: Context, totalCount: Int): Intent {
//            return Intent(context, DetailsActivity::class.java).apply {
//                putExtra(TOTAL_COUNT_EXTRA, totalCount)
//            }
//        }
//    }
