package com.cxyzy.weather.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.cxyzy.weather.viewmodels.BaseViewModel
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    protected abstract fun viewModel(): VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        observeVM()
        prepareBeforeInitView()
        setToolbar()
        initData()
        initViews()
        initListeners()
        startObserve()
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    abstract fun initData()

    private fun setToolbar() {
        providerToolBar()?.let { setSupportActionBar(it) }
    }

    abstract fun layoutId(): Int

    open fun prepareBeforeInitView() {}
    open fun initViews() {}
    open fun initListeners() {}
    open fun startObserve() {}

    private fun observeVM() {
        lifecycle.addObserver(viewModel())
    }

    open fun providerToolBar(): Toolbar? = null

    override fun onDestroy() {
        viewModel().let {
            lifecycle.removeObserver(it)
        }
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    protected open fun isRegisterEventBus(): Boolean {
        return false
    }
}