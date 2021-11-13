package com.example.smartstaffsolutiontask

import android.os.Bundle
import com.example.smartstaffsolutiontask.base.BaseActivity

class MainActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MyFragment.create())
                .commitNow()
        }
    }
}