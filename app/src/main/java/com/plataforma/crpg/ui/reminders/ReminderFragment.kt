package com.plataforma.crpg.ui.reminders


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.databinding.ReminderActivityBinding
import com.plataforma.crpg.model.AlarmFrequency
import com.plataforma.crpg.model.AlarmType
import com.plataforma.crpg.model.ReminderType
import kotlinx.android.synthetic.main.layout_second_alerta.*
import kotlinx.android.synthetic.main.layout_second_dia.*
import kotlinx.android.synthetic.main.layout_second_horas.*
import kotlinx.android.synthetic.main.layout_second_lembrar.*
import kotlinx.android.synthetic.main.reminder_activity.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*
import kotlin.properties.Delegates


class ReminderFragment : Fragment() {

    companion object {
        fun newInstance() = ReminderFragment()
    }

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()
    private val myLocale = Locale("pt_PT", "POR")

    private var textToSpeech: TextToSpeech? = null
    private var lembrarButtonPressed = 0
    private var alarmTypeButtonPressed = 0
    private var alarmFreqButtonPressed = 0
    private var startTimeString = ""
    private var hoursMinutesFlag = false

    override fun onPause() {
        super.onPause()
        val sharedPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("remindersHasRun", true).apply()
    }

    override fun onDestroy() {
        // Don't forget to shutdown!

        if(handler.hasMessages(0)) {
            handler.removeCallbacks(runnable)
        }

        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }

        super.onDestroy ();
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("mealsHasRun", false)

        defineModality(ttsFlag, srFlag, hasRun)

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
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.imageButtonSom)
                    .setOnClickListener {
                        cbSom.visibility = View.VISIBLE
                        cbVib.visibility = View.INVISIBLE
                        cbAmbos.visibility = View.INVISIBLE
                        alarmTypeButtonPressed = 1
                    }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.imageButtonVibrar)
                    .setOnClickListener {
                        cbSom.visibility = View.INVISIBLE
                        cbVib.visibility = View.VISIBLE
                        cbAmbos.visibility = View.INVISIBLE
                        alarmTypeButtonPressed = 2
                    }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.imageButtonAmbos)
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

                expandableNotas.secondLayout.findViewById<EditText>(R.id.editTextNotes).setText("")
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
                    1 -> {newViewModel.newReminder.title = "Tomar medicacao"
                        newViewModel.newReminder.reminder_type = ReminderType.MEDICACAO }
                    2 -> {newViewModel.newReminder.title = "Apanhar bus do CRPG"
                        newViewModel.newReminder.reminder_type = ReminderType.TRANSPORTE }
                    3 -> {newViewModel.newReminder.title = "Lembrar escolha de almoço"
                        newViewModel.newReminder.reminder_type = ReminderType.REFEICAO }
                    //definir titulo personalizado
                    4 -> {newViewModel.newReminder.title = textEditPersonalizado.text.toString()
                        newViewModel.newReminder.reminder_type = ReminderType.PERSONALIZADO }
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

                println(">alarm type:" + alarmTypeButtonPressed)
                println(">alarm freq:" + alarmFreqButtonPressed)
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
                    println(">chega ao add Reminder")
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

            fun performActionWithVoiceCommand(command: String){
                when {
                    command.contains("Abrir secção lembrete", true) -> expandableLembrar.parentLayout.performClick()
                    command.contains("Abrir secção horas", true) -> expandableHoras.parentLayout.performClick()
                    command.contains("Abrir secção dia", true) -> expandableDia.parentLayout.performClick()
                    command.contains("Abrir secção alerta", true) -> expandableAlerta.parentLayout.performClick()
                    command.contains("Abrir secção notas", true) -> expandableNotas.parentLayout.performClick()
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

    private fun performActionWithVoiceCommand(command: String){
        checkHoursCommand(command)
        checkMinutesCommand(command)
        when {
            command.contains("Lembrete", true) -> expandable_lembrar.performClick()
            command.contains("Horas", true) -> expandable_horas.performClick()
            command.contains("Dia", true) -> expandable_dia.performClick()
            command.contains("Alerta", true) -> expandable_alerta.performClick()
            command.contains("Cancelar", true) -> button_confirm.performClick()
            command.contains("Confirmar", true) -> button_confirm.performClick()
            command.contains("Todos", true) -> {
                expandable_lembrar.performClick()
                expandable_horas.performClick()
                expandable_dia.performClick()
                expandable_alerta.performClick()
            }
            command.contains("Medicamento", true) -> button0.performClick()
            command.contains("Transporte", true) -> button1.performClick()
            command.contains("Almoço", true)-> button2.performClick()
            command.contains("Lembrete Personalizado", true)-> button3.performClick()
            command.contains("Som", true)-> imageButtonSom.performClick()
            command.contains("Vibração", true) -> imageButtonVibrar.performClick()
            command.contains("Ambos", true) -> imageButtonAmbos.performClick()
            command.contains("Hoje", true) -> button_hoje.performClick()
            command.contains("Sempre", true) -> button_todos_dias.performClick()
            command.contains("Dia Personalizado", true) -> button_personalizado.performClick()
        }
    }

    private fun checkHoursCommand(command: String) {
        when {
            (command.contains("oito", true) || command.contains("8", true)) && command.contains("da manhã", true) ->edit_hours.setText("08")
            (command.contains("oito", true) || command.contains("8", true))  && command.contains("da noite", true) ->edit_hours.setText("20")
            (command.contains("nove", true)  || command.contains("9", true))&& command.contains("da manhã", true) ->edit_hours.setText("09")
            (command.contains("nove", true)  || command.contains("9", true))&& command.contains("da noite", true) ->edit_hours.setText("21")
            (command.contains("dez", true)  || command.contains("10", true))&& command.contains("da manhã", true) ->edit_hours.setText("10")
            (command.contains("dez", true)  || command.contains("10", true))&& command.contains("da noite", true) ->edit_hours.setText("22")
            (command.contains("onze", true)  || command.contains("11", true))&& command.contains("da manhã", true) ->edit_hours.setText("11")
            (command.contains("onze", true)  || command.contains("11", true))&& command.contains("da noite", true) ->edit_hours.setText("23")
            (command.contains("meio-dia", true)  || command.contains("12", true)) ->edit_hours.setText("12")
            (command.contains("meia-noite", true)  || command.contains("0", true)) ->edit_hours.setText("00")
            (command.contains("uma", true)  || command.contains("1", true))&& command.contains("da manhã", true) ->edit_hours.setText("01")
            (command.contains("uma", true)  || command.contains("1", true))&& command.contains("da tarde", true) ->edit_hours.setText("13")
            (command.contains("duas", true) || command.contains("2", true)) && command.contains("da manhã", true) ->edit_hours.setText("02")
            (command.contains("duas", true)  || command.contains("2", true))&& command.contains("da tarde", true) ->edit_hours.setText("14")
            (command.contains("três", true)  || command.contains("3", true))&& command.contains("da manhã", true) ->edit_hours.setText("03")
            (command.contains("três", true)  || command.contains("3", true))&& command.contains("da tarde", true) ->edit_hours.setText("15")
            (command.contains("quatro", true)  || command.contains("4", true))&& command.contains("da manhã", true) ->edit_hours.setText("04")
            (command.contains("quatro", true)  || command.contains("4", true))&& command.contains("da tarde", true) ->edit_hours.setText("16")
            (command.contains("cinco", true)  || command.contains("5", true))&& command.contains("da manhã", true) ->edit_hours.setText("05")
            (command.contains("cinco", true)  || command.contains("5", true))&& command.contains("da tarde", true) ->edit_hours.setText("17")
            (command.contains("seis", true)  || command.contains("6", true))&& command.contains("da manhã", true) ->edit_hours.setText("06")
            (command.contains("seis", true)  || command.contains("6", true))&& command.contains("da tarde", true) ->edit_hours.setText("18")
            (command.contains("sete", true)  || command.contains("7", true))&& command.contains("da manhã", true) ->edit_hours.setText("07")
            (command.contains("sete", true)  || command.contains("7", true))&& command.contains("da tarde", true) ->edit_hours.setText("19")
        }

    }

    private fun checkMinutesCommand(command: String) {
        when {
            command.contains("e cinco", true) || command.contains(":05", true) ->edit_minutes.setText("05")
            command.contains("e um quarto", true) || command.contains(":15", true) ->edit_minutes.setText("15")
            command.contains("e meia", true) || command.contains(":30", true) ->edit_minutes.setText("30")
        }

    }

    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {

        println("ttsFlag:  " + ttsFlag)
        println("srFlag: " + srFlag)
        println("hasRun: " + hasRun)

        if (!hasRun){
            when{
                ttsFlag && !srFlag -> { startTTS() }
                !ttsFlag && srFlag -> { startVoiceRecognition() }
                ttsFlag && srFlag ->{ multimodalOption() }
            }
        }

        if(hasRun){
            when{
                !ttsFlag && srFlag -> { startVoiceRecognition() }
                ttsFlag && srFlag ->{ startVoiceRecognition() }
            }
        }

    }

    private fun startTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                        || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Linguagem não suportada!")
                }
                val speechStatus = textToSpeech!!.speak("Diga o nome da secção para abrir/fechar essa secção" +
                        "e depois diga o nome da opção que quer escolher ou da hora para fazer a seleção", TextToSpeech.QUEUE_FLUSH, null, "ID")
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun multimodalOption() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                        || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language is not supported!")
                } else {
                    Log.i("TTS", "Language Supported.")
                }
                Log.i("TTS", "Initialization success.")

                val speechListener = object : UtteranceProgressListener() {
                    @Override
                    override fun onStart(p0: String?) {
                        println("Iniciou TTS")
                    }

                    override fun onDone(p0: String?) {
                        println("Encerrou TTS")
                        if(activity != null && isAdded) {
                            startVoiceRecognition()
                        }
                    }

                    override fun onError(p0: String?) {
                        TODO("Not yet implemented")
                    }
                }

                textToSpeech?.setOnUtteranceProgressListener(speechListener)

                val speechStatus = textToSpeech!!.speak("Diga o nome da secção para abrir/fechar essa secção" +
                        "e depois diga o nome da opção que quer escolher ou da hora para fazer a seleção  ", TextToSpeech.QUEUE_FLUSH, null, "ID")

            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun startVoiceRecognition(){
        //MANTER WIFI SEMPRE LIGADO
        //val handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            handler.sendEmptyMessage(0);
            Speech.init(requireActivity())
            //hasInitSR = true
            try {
                Speech.getInstance().startListening(object : SpeechDelegate {
                    override fun onStartOfSpeech() {
                        Log.i("speech", "speech recognition is now active")
                    }

                    override fun onSpeechRmsChanged(value: Float) {
                        //Log.d("speech", "rms is now: $value")
                    }

                    override fun onSpeechPartialResults(results: List<String>) {
                        val str = StringBuilder()
                        for (res in results) {
                            str.append(res).append(" ")
                        }
                        performActionWithVoiceCommand(results.toString())
                        Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                    }

                    override fun onSpeechResult(result: String) {
                        Log.d(TimelineView.TAG, "onSpeechResult: " + result.toLowerCase())
                        //Speech.getInstance().stopTextToSpeech()
                        val handler = Handler()
                        if(activity != null && isAdded) {
                            handler.postDelayed({
                                try {
                                    Speech.init(requireActivity())
                                    //hasInitSR = true
                                    Speech.getInstance().startListening(this)
                                } catch (speechRecognitionNotAvailable: SpeechRecognitionNotAvailable) {
                                    speechRecognitionNotAvailable.printStackTrace()
                                } catch (e: GoogleVoiceTypingDisabledException) {
                                    e.printStackTrace()
                                }
                            }, 100)
                        }
                    }
                })
            } catch (exc: SpeechRecognitionNotAvailable) {
                Log.e("speech", "Speech recognition is not available on this device!")
            } catch (exc: GoogleVoiceTypingDisabledException) {
                Log.e("speech", "Google voice typing must be enabled!")
            }
        }

        handler.post(runnable)
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