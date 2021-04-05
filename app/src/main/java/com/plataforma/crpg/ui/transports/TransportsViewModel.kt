package com.plataforma.crpg.ui.transports

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent.getActivity
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.plataforma.crpg.model.Meal
import java.io.File
import java.io.FileOutputStream

@SuppressLint("StaticFieldLeak")
class TransportsViewModel(application: Application) : AndroidViewModel(application) {


    lateinit var nome_motorista: String
    private val context = getApplication<Application>().applicationContext


}