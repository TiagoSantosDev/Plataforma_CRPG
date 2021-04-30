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

        println("Selected date: $selectedDate")

        val listaDados = ArrayList<CustomInfoPublicTransport>()
        val placeHolderData = CustomInfoPublicTransport("01042021","sde","deded","dede","deded")

        listaDados.add(placeHolderData)

        var idx = listaDados.indexOfFirst {
            it.data == selectedDate
        }

        if(idx == -1){
            idx = listaDados.indexOfFirst {
                it.data > selectedDate
            }
        }


        println("Idx value: " + idx)
        val publicText = hashMapOf<String, String>()

        if (idx > -1) {

            val fetchedCustomInfo = listaDados[idx]
            publicText["Linha 405"] = fetchedCustomInfo.linha405
            publicText["Linha ZF"] = fetchedCustomInfo.linhaZF
            publicText["Linha 35"] = fetchedCustomInfo.linha35
            publicText["Linha 45"] = fetchedCustomInfo.linha45
        }

        return publicText
    }


}