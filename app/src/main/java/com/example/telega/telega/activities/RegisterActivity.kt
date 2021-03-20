package com.example.telega.telega.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.telega.telega.UI.fragments.EnterPhoneNumberFragment
import com.example.telega.telega.utilits.replaceFragment
import com.example.telega.telega.utilits.REGISTER_ACTIVITY
import com.example.telega.telega.utilits.initFireBase
import com.example.telegram.R
import com.example.telegram.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityRegisterBinding
    private lateinit var mToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        REGISTER_ACTIVITY = this
        initFireBase()
    }

    override fun onStart()
    {
        super.onStart()
        mToolbar = mainBinding.RegisterToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.register_title_your_phone)
        replaceFragment(EnterPhoneNumberFragment(),false)
    }
}