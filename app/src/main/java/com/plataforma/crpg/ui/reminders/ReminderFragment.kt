package com.plataforma.crpg.ui.reminders


import android.os.Build
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.plataforma.crpg.R

import com.plataforma.crpg.databinding.ReminderActivityBinding
import com.plataforma.crpg.model.AlarmFrequency
import com.plataforma.crpg.model.AlarmType


class ReminderFragment : Fragment() {

    companion object {
        fun newInstance() = ReminderFragment()
    }

    var lembrarButtonPressed = 0
    var alarmTypeButtonPressed = 0
    var alarmFreqButtonPressed = 0
    var startTimeString = ""

    private lateinit var newViewModel: ReminderViewModel

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = ReminderActivityBinding.inflate(layoutInflater)
        val view = binding.root

        newViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        with(binding){
            expandableLembrar.parentLayout.setOnClickListener {
                expandableLembrar.toggleLayout() }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button0)
                    .setOnClickListener {
                        lembrarButtonPressed = 1
                    }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button1)
                    .setOnClickListener {
                        lembrarButtonPressed = 2
                    }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button2)
                    .setOnClickListener {
                        lembrarButtonPressed = 3
                    }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button3)
                    .setOnClickListener {
                        lembrarButtonPressed = 4
                    }

            expandableHoras.parentLayout.setOnClickListener { expandableHoras.toggleLayout() }

            expandableDia.parentLayout.setOnClickListener { expandableDia.toggleLayout() }
            expandableDia.secondLayout.findViewById<Button>(R.id.button_hoje)
                    .setOnClickListener {

                        alarmFreqButtonPressed = 1
                        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = View.INVISIBLE
                        root.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup).visibility = View.INVISIBLE
                    }
            expandableDia.secondLayout.findViewById<Button>(R.id.button_amanha)
                    .setOnClickListener {

                        alarmFreqButtonPressed = 2
                        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = View.INVISIBLE
                        root.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup).visibility = View.INVISIBLE
                    }
            expandableDia.secondLayout.findViewById<Button>(R.id.button_personalizado)
                    .setOnClickListener {
                        alarmFreqButtonPressed = 3
                        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = View.VISIBLE
                        root.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup).visibility = View.VISIBLE
                    }

            expandableAlerta.parentLayout.setOnClickListener { expandableAlerta.toggleLayout() }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.ImageButtonSom)
                    .setOnClickListener {

                        expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_som).visibility = View.VISIBLE
                        expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_vibrar).visibility = View.INVISIBLE
                        expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_ambos).visibility = View.INVISIBLE
                        alarmTypeButtonPressed = 1
                    }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.ImageButtonVibrar)
                    .setOnClickListener {

                        expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_som).visibility = View.INVISIBLE
                        expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_vibrar).visibility = View.VISIBLE
                        expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_ambos).visibility = View.INVISIBLE
                        alarmTypeButtonPressed = 2
                    }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.ImageButtonAmbos)
                    .setOnClickListener {

                        expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_som).visibility = View.INVISIBLE
                        expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_vibrar).visibility = View.INVISIBLE
                        expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_ambos).visibility = View.VISIBLE
                        alarmTypeButtonPressed = 3
                    }

            expandableNotas.parentLayout.setOnClickListener { expandableNotas.toggleLayout() }

            //--------------------- CANCELAR ---------------------------------------

            root.findViewById<Button>(R.id.button_cancel).setOnClickListener {

                root.findViewById<TextView>(R.id.aviso_campos).visibility = View.GONE

                lembrarButtonPressed = 0

                //reset set Hours section
                expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_hours).setText("")
                expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_minutes).setText("")

                // reset alarmType section
                expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_som).visibility = View.INVISIBLE
                expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_vibrar).visibility = View.INVISIBLE
                expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_ambos).visibility = View.INVISIBLE
                alarmTypeButtonPressed = 0

                alarmFreqButtonPressed = 0

                expandableNotas.secondLayout.findViewById<EditText>(R.id.editTextTextPersonName).setText("")
            }

            //------------------------- CONFIRMAR -------------------------------------------------

            root.findViewById<Button>(R.id.button_confirm).setOnClickListener {

                var hoursInt = 24
                var minsInt = 24

                if (expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_hours).text.toString().length == 2 && expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_minutes).text.toString().length == 2) {
                    newViewModel.startTimeHours = expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_hours).text.toString()
                    newViewModel.startTimeMin = expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_minutes).text.toString()
                    startTimeString = newViewModel.startTimeHours.plus(newViewModel.startTimeMin)
                    //As horas nao podem ser null, tem que ser preenchidas
                    hoursInt = root.findViewById<EditText>(R.id.edit_minutes).text.toString().toInt()
                    minsInt = root.findViewById<EditText>(R.id.edit_minutes).text.toString().toInt()
                } else {
                    root.findViewById<TextView>(R.id.aviso_campos).text = "Valor das horas ou minutos em falta!"
                    root.findViewById<TextView>(R.id.aviso_campos).visibility = View.VISIBLE
                }

                newViewModel.newReminder.start_time = startTimeString

                when (lembrarButtonPressed) {
                    1 -> newViewModel.newReminder.title = "Tomar medicacao"
                    2 -> newViewModel.newReminder.title = "Apanhar bus do CRPG"
                    3 -> newViewModel.newReminder.title = "Lembrar escolha de almoço"
                    4 -> newViewModel.newReminder.title = "Personalizado"
                    else -> { // Note the block
                        println("lembrarButtonPressed is neither one of the values")
                    }
                }

                val materialButtonToggleGroup = expandableDia.secondLayout.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup)
                val ids: List<Int> = materialButtonToggleGroup.checkedButtonIds
                for (id in ids) {
                    val materialButton: MaterialButton = materialButtonToggleGroup.findViewById(id)
                    val resourceName: String = expandableDia.secondLayout.resources.getResourceName(materialButton.id).takeLast(3)

                    when (resourceName) {
                        "Seg" -> newViewModel.weekDaysBoolean[0] = true
                        "Ter" -> newViewModel.weekDaysBoolean[1] = true
                        "Qua" -> newViewModel.weekDaysBoolean[2] = true
                        "Qui" -> newViewModel.weekDaysBoolean[3] = true
                        "Sex" -> newViewModel.weekDaysBoolean[4] = true
                        "Sab" -> newViewModel.weekDaysBoolean[5] = true
                        "Dom" -> newViewModel.weekDaysBoolean[6] = true
                    }
                }


                when (alarmTypeButtonPressed) {
                    1 -> newViewModel.newReminder.alarm_type = AlarmType.SOM;
                    2 -> newViewModel.newReminder.alarm_type = AlarmType.VIBRAR
                    3 -> newViewModel.newReminder.alarm_type = AlarmType.AMBOS
                    else -> { // Note the block
                        println("alarmTypeButtonPressed is neither one of the values")
                    }
                }

                when (alarmFreqButtonPressed) {
                    1 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.HOJE
                    2 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.AMANHA
                    3 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.TODOS_OS_DIAS
                    4 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.PERSONALIZADO
                    else -> { // Note the block
                        println("alarmFreqButtonPressed is neither one of the values")
                    }
                }


                if (alarmFreqButtonPressed != 0 && alarmTypeButtonPressed != 0 && lembrarButtonPressed != 0 && hoursInt <= 23 && minsInt <= 59 ) {
                    root.findViewById<TextView>(R.id.aviso_campos).visibility = View.GONE
                    root.findViewById<View>(R.id.successLayout).visibility = View.VISIBLE
                    root.findViewById<Button>(R.id.button_ok).setOnClickListener {
                        root.findViewById<View>(R.id.successLayout).visibility = View.GONE
                    }

                    newViewModel.addReminder()

                    if (activity?.packageManager?.let { it1 -> newViewModel.alarmIntent.resolveActivity(it1) } != null) {
                        startActivity(newViewModel.alarmIntent)
                    }


                } else if (hoursInt > 23 || minsInt > 59) {
                    root.findViewById<TextView>(R.id.aviso_campos).text = "Valor de Hora ou minutos inválido!"
                    root.findViewById<TextView>(R.id.aviso_campos).visibility = View.VISIBLE
                } else {
                    root.findViewById<TextView>(R.id.aviso_campos).text = "Campos obrigatórios em falta."
                    root.findViewById<TextView>(R.id.aviso_campos).visibility = View.VISIBLE
                }
            }

        }

        return view

    }

    //binding must be done on onCreateView, not on onActivityCreated
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //newViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
        // TODO: Use the ViewModel

    }

}