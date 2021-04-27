package com.plataforma.crpg.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.plataforma.crpg.model.*
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    var selectedDate = ""

}

