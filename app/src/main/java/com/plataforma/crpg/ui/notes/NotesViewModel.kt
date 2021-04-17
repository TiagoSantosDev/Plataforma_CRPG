package com.plataforma.crpg.ui.notes

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.plataforma.crpg.model.*
import java.util.ArrayList

@SuppressLint("StaticFieldLeak")
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    var mNoteList = ArrayList<Note>()
    var newNote = Note(NoteType.TEXT,"", "","", "", "",
            "")
    val noteList = listOf(
            Note(NoteType.TEXT, "16042021","1200", "Nota 1",
                    "a","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_214302371.jpg"),
            Note(NoteType.TEXT, "16042021", "1200","Nota 2",
                    "b","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_214302371.jpg"),
            Note(NoteType.TEXT, "16042021", "1200","Nota 3",
                    "c","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_214302371.jpg")
    )


    fun addNewVoiceNote() {



    }


    fun addNewTextNote() {



    }


}