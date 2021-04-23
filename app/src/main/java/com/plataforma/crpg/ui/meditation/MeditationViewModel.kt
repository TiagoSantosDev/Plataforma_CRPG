package com.plataforma.crpg.ui.meditation

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.plataforma.crpg.model.*
import java.util.ArrayList

@SuppressLint("StaticFieldLeak")
class MeditationViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    var mNoteList = ArrayList<Note>()

    var selectedMood = ""





}