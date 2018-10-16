package com.roombookingapp.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import com.roombookingapp.R
import com.roombookingapp.ui.rooms.RoomsListFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Class for implement Home Screen
 */
class HomeActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        if (savedInstanceState == null) {
            addTheFragment(RoomsListFragment.newInstance(), RoomsListFragment::class.java.name)
        }

    }
}
