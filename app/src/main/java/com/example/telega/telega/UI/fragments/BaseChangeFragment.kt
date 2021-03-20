package com.example.telega.telega.UI.fragments

import android.view.*
import androidx.fragment.app.Fragment
import com.example.telega.telega.utilits.APP_ACTIVITY
import com.example.telega.telega.utilits.hideKeyBoard
import com.example.telegram.R

open class BaseChangeFragment(layout:Int) : Fragment(layout) {

    override fun onStart()
    {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
        hideKeyBoard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        APP_ACTIVITY.menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    open fun change()
    {

    }

}