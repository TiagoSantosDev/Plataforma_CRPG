package com.plataforma.crpg.ui

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.plataforma.crpg.R
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainActivityTest {

    var mainActivity: MainActivity? = null


    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun getMyLocale() {

    }

    @Test
    fun onCreate() {
        assertNotNull(mainActivity);
    }

    @Test
    fun testMainActivity() {
        assertNotNull(mainActivity);
    }


    @Test
    fun testBottomNavBar() {
        //var navView: BottomNavigationView = mainActivity!!.findViewById(R.id.nav_view)
        //assertNotNull(navView);
    }

    @Test
    fun onDestroy() {
    }

    @Test
    fun onSupportNavigateUp() {
    }

    @Test
    fun onOptionsItemSelected() {
    }

    @Test
    fun performActionWithVoiceCommand() {
    }
}