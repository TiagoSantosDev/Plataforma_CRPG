package com.plataforma.crpg.ui.meditation

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel

@SuppressLint("StaticFieldLeak")
class MeditationViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    var selectedMood = ""
    
    fun getValue(){
        println(">Selected mood no VM:$selectedMood")
    }

}