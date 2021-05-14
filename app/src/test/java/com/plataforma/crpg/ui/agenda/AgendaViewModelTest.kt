package com.plataforma.crpg.ui.agenda

import android.app.Application
import com.plataforma.crpg.ui.reminders.ReminderViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito

class AgendaViewModelTest {

    @Mock
    @JvmField
    val application: Application? = null

    @Rule
    @JvmField
    var viewModel: AgendaViewModel? = null

    @Before
    fun setUp() {
        viewModel = Mockito.spy(AgendaViewModel(application!!))
    }

    @Test
    fun getEventCollectionFromJSON() {
    }

    @Test
    fun getEventCollectionFromJSONWithoutPopulate() {
    }
}