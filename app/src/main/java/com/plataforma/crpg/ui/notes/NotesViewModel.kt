package com.plataforma.crpg.ui.notes

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.plataforma.crpg.model.*
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.ArrayList

@SuppressLint("StaticFieldLeak")
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    var mNoteList = ArrayList<Note>()
    var newNote = Note(NoteType.TEXT,"", "","", "", "",
            "")
    private val noteList = listOf(
            Note(NoteType.TEXT, "16042021","1200", "Consultar e-mail e verificar",
                    "Tenho que verificar o meu e-mail hoje","",""),
            Note(NoteType.TEXT, "16042021", "1200","Ir às compras à mercearia",
                    "Tenho de comprar água, pão, entre outras coisas","",""),
            Note(NoteType.VOICE, "16042021", "1200","Nota de voz",
                    "Para me lembrar depois","","")
    )

    var removedPosition : Int ? = null

    private fun populateFile() {
        val filename = "notes.json"
        val fullFilename = context.filesDir.toString() + "/" + filename
        val file = File(fullFilename)

        // create a new file
        val isNewFileCreated : Boolean = file.createNewFile()

        if(isNewFileCreated){
            println("$fullFilename is created successfully.")
        } else{
            println("$fullFilename already exists.")
        }
        val fileContent = convertNoteListToJSON()



        File(fullFilename).writeText(fileContent)
    }

    private fun convertNoteListToJSON(): String {
        val gson = Gson()
        val noteListJSON = gson.toJson(noteList)
        return noteListJSON
    }

    private fun convertAnyNoteListToJSON(list : ArrayList<Note>): String {
        val gson = Gson()
        val noteListJSON = gson.toJson(list)
        return noteListJSON
    }

    fun getNotesCollectionFromJSONWithoutPopulate() {
        val gson = Gson()
        val privateNoteList: ArrayList<Note>
        val filename = "notes.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        val file = File(fullFilename)
        val fileExists = file.exists()

        if (fileExists) {
            print("$fullFilename does exist.")
            populateFile()
        } else {
            print("$fullFilename does not exist.")
            populateFile()
        }

        val type: Type = object : TypeToken<ArrayList<Note>>() {}.type
        mNoteList = gson.fromJson(FileReader(fullFilename), type)

    }


    fun addNewVoiceNote() {
        val gson = Gson()

        mNoteList.add(newNote)
        val filename = "notes.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        val newJSONList = convertAnyNoteListToJSON(mNoteList)

        val file = File(fullFilename)
        val fileExists = file.exists()

        if (fileExists) {
            File(fullFilename).writeText(newJSONList)
        }
    }


    fun addNewTextNote() {
        val gson = Gson()

        mNoteList.add(newNote)
        val filename = "notes.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        val newJSONList = convertAnyNoteListToJSON(mNoteList)

        val file = File(fullFilename)
        val fileExists = file.exists()

        if (fileExists) {
            File(fullFilename).writeText(newJSONList)
        }

    }

}


//println("Note file content: $fileContent")
//println("mNoteList criado aqui: $mNoteList")
//println("Private Note List from JSON Without Populate: $mNoteList")
//println(">mNoteList a entrada do addNewTextNote: $mNoteList")
//println("> New file content: " + File(fullFilename).readText())
/*"""[{"title": "ALMOÇO","info":"test","type": "MEAL",
            |"start_time": "1130","end_time": "1230","date": "2021-03-17"},
            |{"title": "JANTAR","info":"test","type":"MEAL", "start_time": "2000","end_time": "2100","date": "2021-03-17"}]""".trimMargin()*/