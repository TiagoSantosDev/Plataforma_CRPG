package com.plataforma.crpg.ui

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class MainActivityTest {

    private lateinit var main: MainActivity

    @Before
    fun setUp() {
        this.main = spy(MainActivity())
    }

    @Test
    fun testMainActivity() {
        assertNotNull(main);
    }

    @Test
    fun performActionWithVoiceCommand() {


    }
}