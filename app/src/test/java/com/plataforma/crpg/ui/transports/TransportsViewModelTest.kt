package com.plataforma.crpg.ui.transports

import android.app.Application
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

//@RunWith(AndroidJUnit4::class)
class TransportsViewModelTest {

    @get:Rule
    val application: Application? = null

    @get:Rule
    var viewModel: TransportsViewModel? = null

    @Before
    fun setup() {
        viewModel = Mockito.spy(TransportsViewModel(application!!))
    }

    @Test
    fun getCustom_transports_text() {
    }

    @Test
    fun getCustomText() {
    }

    @Test
    fun getPublicTransportText() {
    }

    @Test(expected = Exception::class)
    fun test_getPublicTransportInvalidString() {
        val invalid_date = "23423425345"
        viewModel?.getPublicTransportText(invalid_date)
    }
}