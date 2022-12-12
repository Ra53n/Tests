package com.geekbrains.tests.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.geekbrains.tests.R
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DetailsFragment : Fragment(), ViewDetailsContract {

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAttach(this)
        viewModel.countLiveData.observe(viewLifecycleOwner) {
            setCountText(it)
        }
        setUI()
    }

    private fun setUI() {
        val counter = arguments?.getInt(TOTAL_COUNT_EXTRA, 0)
        viewModel.setCounter(counter ?: 0)
        setCountText(counter ?: 0)
        decrementButton.setOnClickListener { viewModel.onDecrement() }
        incrementButton.setOnClickListener { viewModel.onIncrement() }
    }

    override fun setCount(count: Int) {
        setCountText(count)
    }

    private fun setCountText(count: Int) {
        totalCountDetailsFragmentTextView.text =
            String.format(Locale.getDefault(), getString(R.string.results_count), count)
    }

    companion object {

        private const val TOTAL_COUNT_EXTRA = "TOTAL_COUNT_EXTRA"

        @JvmStatic
        fun newInstance(counter: Int) =
            DetailsFragment().apply { arguments = bundleOf(TOTAL_COUNT_EXTRA to counter) }
    }
}
