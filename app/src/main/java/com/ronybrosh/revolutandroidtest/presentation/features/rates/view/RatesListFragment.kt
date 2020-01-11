package com.ronybrosh.revolutandroidtest.presentation.features.rates.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.ronybrosh.revolutandroidtest.R
import com.ronybrosh.revolutandroidtest.presentation.features.rates.adapter.RatesAdapter
import com.ronybrosh.revolutandroidtest.presentation.util.KeyboardUtil
import com.ronybrosh.revolutandroidtest.presentation.util.observe
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_rates.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesListFragment : DaggerFragment(), RatesAdapter.RateAdapterListener {
    @Inject
    lateinit var viewModel: RatesViewModel

    private val compositeDisposable = CompositeDisposable()
    private val focusPublishSubject = PublishSubject.create<Unit>()
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

    override fun onResume() {
        super.onResume()
        observeNoFocusedAmountField()
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
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

    override fun onNoFocusedAmountField() {
        focusPublishSubject.onNext(Unit)
    }

    private fun observeNoFocusedAmountField() {
        compositeDisposable.clear()
        // Debounce the latest event in case of quick switching between edit text fields.
        // Seems like 10 milliseconds is the sweet spot. Probably need to check on multiple devices.
        val disposable = focusPublishSubject.debounce(10, TimeUnit.MILLISECONDS)
            .subscribe {
                val currentContext = context ?: return@subscribe

                // If we don't have a focused child or the focused child is not of type edit text hide the keyboard.
                val focusedChild = recyclerView.findFocus()
                if (focusedChild == null || focusedChild !is EditText)
                    KeyboardUtil.hideKeyboard(currentContext, recyclerView)
            }
        compositeDisposable.add(disposable)
    }
}