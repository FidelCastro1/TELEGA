package com.example.telega.telega

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.telega.telega.UI.fragments.ChatsFragment
import com.example.telega.telega.UI.objects.AppDrawer
import com.example.telega.telega.activities.RegisterActivity
import com.example.telega.telega.utilits.*
import com.example.telegram.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?)
    {
        /* Функция запускается один раз, при создании активити */
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFireBase()
        initUser {

           CoroutineScope(Dispatchers.IO).launch { initContacts() }
            initFields()
            initFunc()
        }
    }



    private fun initFunc() {
        /* Функция инициализирует функциональность приложения */
        if (AUTH.currentUser != null)
        {
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatsFragment(), false)
        }
        else
        {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initFields()
    {
        /* Функция инициализирует переменные */
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
    }

    override fun onStart()
    {
        super.onStart()
        AppStatus.updateStates(AppStatus.ONLINE)
    }

    override fun onStop()
    {
        super.onStop()
        AppStatus.updateStates(AppStatus.OFFLINE)
    }

    override fun onPause()
    {
        super.onPause()
        AppStatus.updateStates(AppStatus.OFFLINE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
            {
                initContacts()
            }
        }
    }
