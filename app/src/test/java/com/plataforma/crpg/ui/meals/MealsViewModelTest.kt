package com.plataforma.crpg.ui.meals

import android.app.Application
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class MealsViewModelTest {

    @Mock
    @JvmField
    val application: Application? = null

    @Rule
    @JvmField
    var viewModel: MealsViewModel? = null

    @Before
    fun setUp() {
        viewModel = Mockito.spy(MealsViewModel(application!!))
    }

    @Test
    fun getMealsFromJSON() {
    }

    @Test
    fun convertMealsToJSON() {
    }

    @Test
    fun updateMealChoiceOnLocalStorage() {
    }
}