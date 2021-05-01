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

        val customText: String = "Horários para o dia X:"

        return customText
    }

    fun getPublicTransportText(selectedDate: String): HashMap<String,String> {

        println("Selected date: $selectedDate")

        val listaDados = ArrayList<CustomInfoPublicTransport>()
        val placeHolderDataA = CustomInfoPublicTransport("03052021",
                " A Horário Linha 901: \n Trindade - 09h30 \n CRPG - 10h30",
                " A Horário Linha ZF: \n Trindade - 09h30 \n CRPG - 10h30",
                " A Horário linha 35: \n Trindade - 09h30 \n CRPG - 10h30",
                " A Horário Linha 45: \n Trindade - 09h30 \n CRPG - 10h30")

        val placeHolderDataB = CustomInfoPublicTransport("06052021",
                "B Horário Linha 901: \n Trindade - 09h30 \n CRPG - 10h30",
                "B Horário Linha ZF: \n Trindade - 09h30 \n CRPG - 10h30",
                "B Horário linha 35: \n Trindade - 09h30 \n CRPG - 10h30",
                "B Horário Linha 45: \n Trindade - 09h30 \n CRPG - 10h30")

        val placeHolderDataC = CustomInfoPublicTransport("09052021",
                "C Horário Linha 901: \n Trindade - 09h30 \n CRPG - 10h30",
                "C Horário Linha ZF: \n Trindade - 09h30 \n CRPG - 10h30",
                "C Horário linha 35: \n Trindade - 09h30 \n CRPG - 10h30",
                "C Horário Linha 45: \n Trindade - 09h30 \n CRPG - 10h30")


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