package com.plataforma.crpg.ui.transports

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.util.ArrayList

@SuppressLint("StaticFieldLeak")
class TransportsViewModel(application: Application) : AndroidViewModel(application) {


    var nome_motorista_para_CRPG = "Jorge"
    var nome_motorista_de_CRPG = "Eliseu"
    private val context = getApplication<Application>().applicationContext
    lateinit var custom_transports_text: String
    lateinit var public_transports_text: String



    fun extractDataFromTransportModel(): ArrayList<String> {



        return ArrayList()
    }

    fun getCustomText(selectedDate: String): String {
        val customText: String

        customText = "Horários para o dia X:"

        return customText
    }

    fun getPublicTransportText(selectedDate: String): String {
        val publicText: String

        publicText = "Horários para o dia X:"

        return publicText
    }


}