package com.plataforma.crpg.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class SharedViewModel(application: Application) : AndroidViewModel(application) {


    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    var selectedDate = ""
    var isLunch = false







}

