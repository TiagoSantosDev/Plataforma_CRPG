package com.plataforma.crpg.ui.notes

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.util.ArrayList

@SuppressLint("StaticFieldLeak")
class NotesViewModel(application: Application) : AndroidViewModel(application) {


    var nome_motorista_para_CRPG = "Jorge"
    var nome_motorista_de_CRPG = "Eliseu"
    private val context = getApplication<Application>().applicationContext

    fun extractDataFromTransportModel(): ArrayList<String> {



        return ArrayList()
    }


}