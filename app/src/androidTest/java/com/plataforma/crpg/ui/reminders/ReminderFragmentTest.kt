package com.plataforma.crpg.ui.reminders

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.plataforma.crpg.R
import com.plataforma.crpg.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ReminderFragmentTest {

    /*
    @Rule @JvmField
    var main2 = ActivityScenarioRule(MainActivity::class.java)
    */

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

/*
    @Rule @JvmField
    var main = ActivityTestRule(MainActivity::class.java)
*/

    @Test
    fun launchFragmentAndVerifyUI(){
        launchFragmentInContainer<ReminderFragment>()

        onView(withId(R.id.adicione_lembretes_hint)).check(matches(withText("Adicione lembretes para não se esquecer do que é realmente importante!")))
        //onView(R.id.adicione_lembretes_hint).check(matches(withText(R.string.adicione_lembretes_hint)))
        //onView(R.id.clique_icone_lapis_hint).check(matches(withText(R.string.clique_icone_lapis_hint)))
        onView(withId(R.id.createReminderActionButton)).perform(click())
        onView(withId(R.id.createReminderActionButton)).check(matches(isDisplayed()))

    }

    @Test
    fun listGoesOverTheFold() {
        onView(withText("Hello world!")).check(matches(isDisplayed()))
    }
/*
    @Test
    @Throws(Exception::class)
    fun clickButton() {
        onView(withText(R.string.button)).perform(click())
        onView(withText(R.string.button_clicked)).check(matches(isDisplayed()))
    }*/

    @Test
    fun onCreateView() {
    }

    @Test
    fun onActivityCreated() {
    }
}

//@Rule
//var fragmentTestRule: FragmentTestRule<*, FragmentWithoutActivityDependency> = FragmentTestRule.create(FragmentWithoutActivityDependency::class.java)