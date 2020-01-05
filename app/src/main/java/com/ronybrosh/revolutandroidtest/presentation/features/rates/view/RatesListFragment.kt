package com.ronybrosh.revolutandroidtest.presentation.features.rates.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.ronybrosh.revolutandroidtest.R
import com.ronybrosh.revolutandroidtest.presentation.features.rates.adapter.RatesAdapter
import com.ronybrosh.revolutandroidtest.presentation.util.observe
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_rates.*
import javax.inject.Inject

class RatesListFragment : DaggerFragment(), RatesAdapter.RateAdapterListener {
    @Inject
    lateinit var viewModel: RatesViewModel

    private val snackBar: Snackbar by lazy {
        return@lazy Snackbar.make(
            recyclerView,
            "",
            Snackbar.LENGTH_INDEFINITE
        ).setBehavior(object : BaseTransientBottomBar.Behavior() {
            override fun canSwipeDismissView(child: View): Boolean {
                return false
            }
        }).setAction(R.string.rates_screen_snack_bar_action_error) {
            snackBar.dismiss()
        }
    }

    private val adapter = RatesAdapter(this)
    private val layoutManager: LinearLayoutManager =
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            if (it is AppCompatActivity)
                it.supportActionBar?.title = getString(R.string.rates_screen_actionbar_title)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        observe(viewModel.getLoading()) { isLoading ->
            if (isLoading)
                progressBar.show()
            else
                progressBar.hide()
        }

        observe(viewModel.getError()) { uiError ->
            if (uiError == null || snackBar.isShown)
                return@observe

            snackBar.setText(uiError.errorMessageResourceId)
            snackBar.show()
        }

        observe(viewModel.getResult()) {
            if (snackBar.isShown)
                snackBar.dismiss()

            adapter.setContent(it.content)
            if (it.isScrollToTop)
                layoutManager.scrollToPosition(0)
        }
    }

    override fun updateRatesConversion(amountAsString: String, currencyRate: Float) {
        viewModel.updateRatesConversion(amountAsString, currencyRate)
    }

    override fun updateTopItemIndex(oldIndex: Int) {
        viewModel.moveToTopOfList(oldIndex)
    }
}