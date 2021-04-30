package com.plataforma.crpg.ui.transports

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.plataforma.crpg.model.CustomInfoPublicTransport
import java.util.ArrayList

@SuppressLint("StaticFieldLeak")
class TransportsViewModel(application: Application) : AndroidViewModel(application) {

    var nome_motorista_para_CRPG = "João"
    var nome_motorista_de_CRPG = "Maria"
    private val context = getApplication<Application>().applicationContext
    lateinit var custom_transports_text: String


    fun extractDataFromTransportModel(): ArrayList<String> {

        return ArrayList()
    }

    fun getCustomText(selectedDate: String): String {
        val customText: String

        customText = "Horários para o dia X:"

        return customText
    }

    fun getPublicTransportText(selectedDate: String): HashMap<String,String> {

        val listaDados = ArrayList<CustomInfoPublicTransport>()

        val idx = listaDados.indexOfFirst {
            it.data == selectedDate
        }

        val publicText = hashMapOf<String,String>()

        publicText["Linha 401"] = "Horários para a linha 401"
        publicText["Linha ZF"] = "Horários para a linha 401"
        publicText["Linha 35"] = "Horários para a linha 401"
        publicText["Linha 45"] = "Horários para a linha 401"


        return publicText
    }


}