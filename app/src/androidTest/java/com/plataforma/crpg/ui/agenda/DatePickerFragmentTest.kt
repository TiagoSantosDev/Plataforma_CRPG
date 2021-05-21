package com.plataforma.crpg.ui.agenda

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.plataforma.crpg.R
import com.plataforma.crpg.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DatePickerFragmentTest{

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun launchFragmentAndVerifyUI(){
        launchFragmentInContainer<DatePickerFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.selecionar_dia_hint)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.selecionar_dia_hint)).check(ViewAssertions.matches(ViewMatchers.withText("MOVA OS CARTÕES LARANJA PARA A ESQUERDA/DIREITA PARA VER MAIS DIAS!")))

    }

}