package com.plataforma.crpg.ui.reminders


import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.ReminderActivityBinding
import com.plataforma.crpg.model.AlarmFrequency
import com.plataforma.crpg.model.AlarmType


class ReminderFragment : Fragment() {

    companion object {
        fun newInstance() = ReminderFragment()
    }

    private var lembrarButtonPressed = 0
    private var alarmTypeButtonPressed = 0
    private var alarmFreqButtonPressed = 0
    private var startTimeString = ""
    private var hoursMinutesFlag = false

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = ReminderActivityBinding.inflate(layoutInflater)
        val view = binding.root
        val newViewModel = ViewModelProvider(activity as AppCompatActivity).get(ReminderViewModel::class.java)

        with(binding){
            root.findViewById<View>(R.id.reminderIntroHintLayout).visibility = View.VISIBLE
            root.findViewById<FloatingActionButton>(R.id.createReminderActionButton).setOnClickListener{
                root.findViewById<View>(R.id.reminderIntroHintLayout).visibility = View.GONE
            }

            fun setButtonColorsReminder(pos: Int){
                expandableLembrar.secondLayout.findViewById<Button>(R.id.button0).setBackgroundResource(R.drawable.layout_button_round_top)
                expandableLembrar.secondLayout.findViewById<Button>(R.id.button1).setBackgroundResource(R.color.md_blue_100)
                expandableLembrar.secondLayout.findViewById<Button>(R.id.button2).setBackgroundResource(R.color.md_blue_100)
                expandableLembrar.secondLayout.findViewById<Button>(R.id.button3).setBackgroundResource(R.drawable.layout_button_round_bottom)

                when(pos){
                    1 ->  expandableLembrar.secondLayout.findViewById<Button>(R.id.button0).setBackgroundResource(R.drawable.layout_button_round_top_selected)
                    2 ->  expandableLembrar.secondLayout.findViewById<Button>(R.id.button1).setBackgroundResource(R.color.md_blue_200)
                    3 ->  expandableLembrar.secondLayout.findViewById<Button>(R.id.button2).setBackgroundResource(R.color.md_blue_200)
                    4 ->  expandableLembrar.secondLayout.findViewById<Button>(R.id.button3).setBackgroundResource(R.drawable.layout_button_round_bottom_selected)
                }

            }

            fun setButtonColorsDays(pos: Int){
                expandableDia.secondLayout.findViewById<Button>(R.id.button_hoje).setBackgroundResource(R.drawable.layout_button_round_top)
                expandableDia.secondLayout.findViewById<Button>(R.id.button_todos_dias).setBackgroundResource(R.color.md_blue_100)
                expandableDia.secondLayout.findViewById<Button>(R.id.button_personalizado).setBackgroundResource(R.drawable.layout_button_round_bottom)

                when(pos){
                    1 ->  expandableDia.secondLayout.findViewById<Button>(R.id.button_hoje).setBackgroundResource(R.drawable.layout_button_round_top_selected)
                    2 ->  expandableDia.secondLayout.findViewById<Button>(R.id.button_todos_dias).setBackgroundResource(R.color.md_blue_200)
                    3 ->  expandableDia.secondLayout.findViewById<Button>(R.id.button_personalizado).setBackgroundResource(R.drawable.layout_button_round_bottom_selected)
                }

            }

            val textEditPersonalizado = root.findViewById<EditText>(R.id.text_edit_personalizado)

            expandableLembrar.parentLayout.setOnClickListener {
                expandableLembrar.toggleLayout() }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button0)
                    .setOnClickListener {
                        lembrarButtonPressed = 1
                        setButtonColorsReminder(lembrarButtonPressed)
                        root.findViewById<TextView>(R.id.inserir_titulo_lembrete_personalizado).visibility = View.INVISIBLE
                        textEditPersonalizado.visibility = View.INVISIBLE
                    }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button1)
                    .setOnClickListener {
                        lembrarButtonPressed = 2
                        setButtonColorsReminder(lembrarButtonPressed)
                        root.findViewById<TextView>(R.id.inserir_titulo_lembrete_personalizado).visibility = View.INVISIBLE
                        textEditPersonalizado.visibility = View.INVISIBLE
                    }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button2)
                    .setOnClickListener {
                        lembrarButtonPressed = 3
                        setButtonColorsReminder(lembrarButtonPressed)
                        root.findViewById<TextView>(R.id.inserir_titulo_lembrete_personalizado).visibility = View.INVISIBLE
                        textEditPersonalizado.visibility = View.INVISIBLE
                    }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button3)
                    .setOnClickListener {
                        lembrarButtonPressed = 4
                        setButtonColorsReminder(lembrarButtonPressed)
                        root.findViewById<TextView>(R.id.inserir_titulo_lembrete_personalizado).visibility = View.VISIBLE
                        textEditPersonalizado.visibility = View.VISIBLE
                    }

            expandableHoras.parentLayout.setOnClickListener { expandableHoras.toggleLayout() }

            val et = expandableHoras.secondLayout.findViewById(R.id.edit_hours) as EditText
            et.filters = arrayOf(InputFilterMinMax("00", "23"), InputFilter.LengthFilter(2))

            val etMin = expandableHoras.secondLayout.findViewById(R.id.edit_minutes) as EditText
            etMin.filters = arrayOf(InputFilterMinMax("00", "59"),InputFilter.LengthFilter(2))

            val cbSom = expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_som)
            val cbVib = expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_vibrar)
            val cbAmbos = expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_ambos)

            expandableDia.parentLayout.setOnClickListener { expandableDia.toggleLayout() }
            expandableDia.secondLayout.findViewById<Button>(R.id.button_hoje)
                    .setOnClickListener {
                        alarmFreqButtonPressed = 1
                        setButtonColorsDays(alarmFreqButtonPressed)
                        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = View.INVISIBLE
                        root.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup).visibility = View.INVISIBLE
                    }
            expandableDia.secondLayout.findViewById<Button>(R.id.button_todos_dias)
                    .setOnClickListener {
                        alarmFreqButtonPressed = 2
                        setButtonColorsDays(alarmFreqButtonPressed)
                        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = View.INVISIBLE
                        root.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup).visibility = View.INVISIBLE
                    }
            expandableDia.secondLayout.findViewById<Button>(R.id.button_personalizado)
                    .setOnClickListener {
                        alarmFreqButtonPressed = 3
                        setButtonColorsDays(alarmFreqButtonPressed)
                        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = View.VISIBLE
                        root.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup).visibility = View.VISIBLE
                    }

            expandableAlerta.parentLayout.setOnClickListener { expandableAlerta.toggleLayout() }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.ImageButtonSom)
                    .setOnClickListener {
                        cbSom.visibility = View.VISIBLE
                        cbVib.visibility = View.INVISIBLE
                        cbAmbos.visibility = View.INVISIBLE
                        alarmTypeButtonPressed = 1
                    }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.ImageButtonVibrar)
                    .setOnClickListener {
                        cbSom.visibility = View.INVISIBLE
                        cbVib.visibility = View.VISIBLE
                        cbAmbos.visibility = View.INVISIBLE
                        alarmTypeButtonPressed = 2
                    }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.ImageButtonAmbos)
                    .setOnClickListener {
                        cbSom.visibility = View.INVISIBLE
                        cbVib.visibility = View.INVISIBLE
                        cbAmbos.visibility = View.VISIBLE
                        alarmTypeButtonPressed = 3
                    }

            expandableNotas.parentLayout.setOnClickListener { expandableNotas.toggleLayout() }

            //--------------------- CANCELAR ---------------------------------------
            val avisoCampos = root.findViewById<TextView>(R.id.aviso_campos)

            root.findViewById<Button>(R.id.button_cancel).setOnClickListener {

                avisoCampos.visibility = View.GONE

                lembrarButtonPressed = 0
                alarmTypeButtonPressed = 0
                alarmFreqButtonPressed = 0

                //reset set Hours section
                expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_hours).setText("")
                expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_minutes).setText("")

                // reset alarmType section
                cbSom.visibility = View.INVISIBLE
                cbVib.visibility = View.INVISIBLE
                cbAmbos.visibility = View.INVISIBLE

                expandableNotas.secondLayout.findViewById<EditText>(R.id.editTextTextPersonName).setText("")
            }

            //------------------------- CONFIRMAR -------------------------------------------------

            root.findViewById<Button>(R.id.button_confirm).setOnClickListener {

                var hoursInt = 1
                var minsInt = 1

                if (et.text.toString().length == 2 && etMin.text.toString().length == 2) {
                    println("> Validacao das horas com sucesso")
                    newViewModel.startTimeHours = et.text.toString()
                    newViewModel.startTimeMin = etMin.text.toString()
                    startTimeString = newViewModel.startTimeHours.plus(newViewModel.startTimeMin)
                    hoursInt = root.findViewById<EditText>(R.id.edit_minutes).text.toString().toInt()
                    minsInt = root.findViewById<EditText>(R.id.edit_minutes).text.toString().toInt()
                    hoursMinutesFlag = true
                } else {
                    avisoCampos.text = getString(R.string.valor_horas_minutos_falta)
                    avisoCampos.visibility = View.VISIBLE
                }

                newViewModel.newReminder.start_time = startTimeString

                when (lembrarButtonPressed) {
                    1 -> newViewModel.newReminder.title = "Tomar medicacao"
                    2 -> newViewModel.newReminder.title = "Apanhar bus do CRPG"
                    3 -> newViewModel.newReminder.title = "Lembrar escolha de almoço"
                    //definir titulo personalizado
                    4 -> newViewModel.newReminder.title = textEditPersonalizado.text.toString()
                    else -> {
                        println("lembrarButtonPressed is neither one of the values")
                    }
                }

                //println(">Titulo personalizado do reminder: " + newViewModel.newReminder.title)

                val materialButtonToggleGroup =
                        expandableDia.secondLayout.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup)

                val ids: List<Int> = materialButtonToggleGroup.checkedButtonIds
                for (id in ids) {
                    val materialButton: MaterialButton = materialButtonToggleGroup.findViewById(id)
                    val resourceName: String =
                            expandableDia.secondLayout.resources.getResourceName(materialButton.id).takeLast(3)
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
                    1 -> newViewModel.newReminder.alarm_type = AlarmType.SOM
                    2 -> newViewModel.newReminder.alarm_type = AlarmType.VIBRAR
                    3 -> newViewModel.newReminder.alarm_type = AlarmType.AMBOS
                    else -> { // Note the block
                        println("alarmTypeButtonPressed is neither one of the values")
                    }
                }

                when (alarmFreqButtonPressed) {
                    1 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.HOJE
                    2 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.TODOS_OS_DIAS
                    3 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.PERSONALIZADO

                    else -> { // Note the block
                        println("alarmFreqButtonPressed is neither one of the values")
                    }
                }

                if (alarmFreqButtonPressed != 0 && alarmTypeButtonPressed != 0
                        && lembrarButtonPressed != 0 && hoursMinutesFlag) {

                    newViewModel.addReminder()
                    if (newViewModel.flagReminderAdded) {

                        avisoCampos.visibility = View.GONE
                        root.findViewById<View>(R.id.successLayout).visibility = View.VISIBLE
                        root.findViewById<Button>(R.id.button_ok).setOnClickListener {
                            root.findViewById<View>(R.id.successLayout).visibility = View.GONE
                        }
                        if (activity?.packageManager?.let { it1 -> newViewModel.alarmIntent.resolveActivity(it1) } != null) {
                            startActivity(newViewModel.alarmIntent)
                        }
                    }
                } else if (hoursInt > 23 || minsInt > 59) {
                    avisoCampos.text = getString(R.string.hora_minutos_invalido)
                    avisoCampos.visibility = View.VISIBLE
                } else {
                    avisoCampos.text = getString(R.string.campos_obrigatorios_falta)
                    avisoCampos.visibility = View.VISIBLE
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


/*
//println("Dates picked: " + ids.toString())
//et.filters = arrayOf<InputFilter>(InputFilterMinMax("00", "23"), InputFilter.LengthFilter(2))
//println("entrou na condicao")
//etMin.filters = arrayOf<InputFilter>(InputFilterMinMax("00", "59"),InputFilter.LengthFilter(2) )
//2 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.AMANHA
           //println("Entrou aqui")
           expandableLembrar.secondLayout.findViewById<Button>(R.id.button0).setBackgroundResource(R.drawable.layout_button_round_top_selected)
                        expandableLembrar.secondLayout.findViewById<Button>(R.id.button1).setBackgroundResource(R.color.md_blue_100)
                        expandableLembrar.secondLayout.findViewById<Button>(R.id.button2).setBackgroundResource(R.color.md_blue_100)
                        expandableLembrar.secondLayout.findViewById<Button>(R.id.button3).setBackgroundResource(R.drawable.layout_button_round_bottom)
                        */




/*

//expandableLembrar.secondLayout.findViewById<Button>(R.id.button0).setBackgroundColor() = "@color/white"
//newViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
private var min:Int = 1
private var max:Int = 24
constructor(min:Int, max:Int) {
    this.min = min
    this.max = max
}
constructor(min:String, max:String) {
    this.min = Integer.parseInt(min)
    this.max = Integer.parseInt(max)
}
fun filter(source:CharSequence, start:Int, end:Int, dest: Spanned, dstart:Int, dend:Int): CharSequence? {
    try
    {
        val input = Integer.parseInt(dest.toString() + source.toString())
        if (isInRange(min, max, input))
            return null
    }
    catch (nfe:NumberFormatException) {}
    return ""
}
private fun isInRange(a:Int, b:Int, c:Int):Boolean {
    return if (b > a) c in a..b else c in b..a
}
*/
//et.filters = arrayOf(InputFilter.LengthFilter(2))
//etMin.filters = arrayOf(InputFilter.LengthFilter(2))
// val et = expandableHoras.secondLayout.findViewById(R.id.edit_hours) as EditText
//                //et.filters = arrayOf<InputFilter>(InputFilterMinMax("00", "23"))
//
//                //val etMin = expandableHoras.secondLayout.findViewById(R.id.edit_minutes) as EditText
//                //etMin.filters = arrayOf<InputFilter>(InputFilterMinMax("00", "59"))
//
//                /* if (expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_hours).text.toString().length == 2
//                       && expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_minutes).text.toString().length == 2) {
//                    newViewModel.startTimeHours = expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_hours).text.toString()
//                    newViewModel.startTimeMin = expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_minutes).text.toString()
//                    startTimeString = newViewModel.startTimeHours.plus(newViewModel.startTimeMin)
//                    hoursInt = root.findViewById<EditText>(R.id.edit_minutes).text.toString().toInt()
//                    minsInt = root.findViewById<EditText>(R.id.edit_minutes).text.toString().toInt()
//
//                } else {
//                    avisoCampos.text = getString(R.string.valor_horas_minutos_falta)
//                    avisoCampos.visibility = View.VISIBLE
//                }*/