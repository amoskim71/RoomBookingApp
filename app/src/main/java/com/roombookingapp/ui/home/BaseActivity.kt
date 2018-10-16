package com.roombookingapp.ui.home

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.roombookingapp.R
import com.roombookingapp.RoomBookingApp
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Class for implement common functionalities to all the activitys
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RoomBookingApp.setTheActivity(this)
    }


    /*
    replace the fragment to container
     */
    fun replaceTheFragment(fragment: Fragment, backStackText: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(backStackText)
                .commit()
    }


    fun addTheFragment(fragment: Fragment, backStackText: String) {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(backStackText)
                .commit()
    }

    /*
    This methode helps to add font to app
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }



}