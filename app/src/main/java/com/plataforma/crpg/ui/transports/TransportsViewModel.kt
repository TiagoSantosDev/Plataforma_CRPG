package com.plataforma.crpg.ui.transports

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.plataforma.crpg.model.CustomInfoPublicTransport
import java.util.*

@SuppressLint("StaticFieldLeak")
class TransportsViewModel(application: Application) : AndroidViewModel(application) {

    var nome_motorista_para_CRPG = "João"
    var nome_motorista_de_CRPG = "Maria"
    var horario_ida_CRPG = "11h30"
    var horario_volta_CRPG = "12h30"
    private val context = getApplication<Application>().applicationContext
    lateinit var custom_transports_text: String

    fun getCustomText(selectedDate: String): String {

        val customText: String = "Horários para o dia selecionado: \n \n Trindade - 09h30 \n CRPG - 10h30 \n" +
                " \n" +
                " Valadares - 10h30 \n" +
                " CRPG - 11h30 \n"

        return customText
    }

    fun getPublicTransportText(selectedDate: String): HashMap<String,String> {

        println("Selected date: $selectedDate")

        val listaDados = ArrayList<CustomInfoPublicTransport>()
        val placeHolderDataA = CustomInfoPublicTransport("03052021",
                " Horário Linha 901: \n Trindade - 09h30 \n CRPG - 10h30",
                " Horário Linha ZF: \n Trindade - 09h30 \n CRPG - 10h30",
                " Horário linha 35: \n Trindade - 09h30 \n CRPG - 10h30",
                " Horário Linha 45: \n Trindade - 09h30 \n CRPG - 10h30")

        val placeHolderDataB = CustomInfoPublicTransport("06052021",
                "Horário Linha 901: \n Trindade - 09h30 \n CRPG - 10h30",
                "Horário Linha ZF: \n Trindade - 09h30 \n CRPG - 10h30",
                "Horário linha 35: \n Trindade - 09h30 \n CRPG - 10h30",
                "Horário Linha 45: \n Trindade - 09h30 \n CRPG - 10h30")

        val placeHolderDataC = CustomInfoPublicTransport("09052021",
                "Horário Linha 901: \n Trindade - 09h30 \n CRPG - 10h30",
                "Horário Linha ZF: \n Trindade - 09h30 \n CRPG - 10h30",
                "Horário linha 35: \n Trindade - 09h30 \n CRPG - 10h30",
                "Horário Linha 45: \n Trindade - 09h30 \n CRPG - 10h30")

        listaDados.add(placeHolderDataA)
        listaDados.add(placeHolderDataB)
        listaDados.add(placeHolderDataC)

        //primeiro verifica se existe informação para a data selecionada
        var idx = listaDados.indexOfFirst {
            it.data == selectedDate
        }

        //se não existir, assume-se que a informacao em vigor e a data passada mais proxima da data selecionada
        if(idx == -1){
            idx = listaDados.indexOfLast {
                it.data < selectedDate
            }
        }

        println("Idx value: $idx")
        val publicText = hashMapOf<String, String>()

        if (idx > -1) {
            val fetchedCustomInfo = listaDados[idx]
            publicText["Linha 905"] = fetchedCustomInfo.linha901
            publicText["Linha ZF"] = fetchedCustomInfo.linhaZF
            publicText["Linha 35"] = fetchedCustomInfo.linha35
            publicText["Linha 45"] = fetchedCustomInfo.linha45
        }

        return publicText
    }


}

/*
    fun extractDataFromTransportModel(): ArrayList<String> {
        return ArrayList()
    }
    */
