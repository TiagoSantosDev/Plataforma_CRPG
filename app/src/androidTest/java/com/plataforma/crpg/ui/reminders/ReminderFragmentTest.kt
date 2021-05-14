package com.plataforma.crpg.ui.reminders

import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.runner.AndroidJUnit4
import kotlinx.android.synthetic.main.reminder_activity_intro.view.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ReminderFragmentTest {

    @Test
    fun launchFragmentAndVerifyUI(){
        launchFragmentInContainer<ReminderFragment>()
/*
        onView(R.id.adicione_lembretes_hint).check(matches(withText(R.string.adicione_lembretes_hint)))
        onView(R.id.clique_icone_lapis_hint).check(matches(withText(R.string.clique_icone_lapis_hint)))
        onView(R.id.createReminderActionButton).check(matches(withText(R.string.adicione_lembretes_hint)))
*/
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