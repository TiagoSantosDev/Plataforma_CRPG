package com.plataforma.crpg.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import java.util.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityUITest {

    @get:Rule var main2 = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule var main = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun performActionWithVoiceCommand() {
    }
}