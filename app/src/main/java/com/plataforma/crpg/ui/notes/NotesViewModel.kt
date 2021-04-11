package com.plataforma.crpg.ui.notes

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.plataforma.crpg.model.*
import java.util.ArrayList

@SuppressLint("StaticFieldLeak")
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    var mReminderList = ArrayList<Note>()
    var newNote = Note(NoteType.TEXT,"", "","", "", "",
            "")


    fun addNewVoiceNote() {



    }


    fun addNewTextNote() {



    }


}