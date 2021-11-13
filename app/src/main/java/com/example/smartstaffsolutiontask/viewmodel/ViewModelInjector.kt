package com.example.smartstaffsolutiontask.viewmodel

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.smartstaffsolutiontask.App
import com.example.smartstaffsolutiontask.base.BaseActivity
import com.example.smartstaffsolutiontask.base.BaseFragment
import com.example.smartstaffsolutiontask.MyFragment

class ViewModelInjector(private val context: Context) {

    companion object {
        fun inject(app: App) {
            app.injector = ViewModelInjector(app)
        }
    }

    val dependencyProvider by lazy {
        ViewModelDependency(context)
    }

    fun handleActivity(activity: Activity) {
        if (activity is BaseActivity) inject(activity)
    }

    fun handleFragment(fragment: Fragment) {
        if (fragment is BaseFragment) {
            inject(fragment)
            when (fragment) {
                is MyFragment -> {
                    inject(fragment)
                }
            }
        }
    }

    private fun inject(activity: BaseActivity){}

    private fun inject(fragment: BaseFragment){}


    private fun inject(fragment: MyFragment) {
        fragment.viewModelFactory = dependencyProvider.viewModelFactory
    }
}