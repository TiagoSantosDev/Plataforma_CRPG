package com.plataforma.crpg.ui.transports

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.databinding.TransportsFragmentBinding
import com.plataforma.crpg.ui.MainActivity
import com.plataforma.crpg.ui.agenda.SharedViewModel
import kotlinx.android.synthetic.main.meals_fragment.*
import kotlinx.android.synthetic.main.notes_fragment.*
import kotlinx.android.synthetic.main.transports_fragment.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*
import kotlin.properties.Delegates


class TransportsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = TransportsFragment()
    }

    private lateinit var transportsViewModel: TransportsViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var phone = "00351912193034"

    private var textToSpeech: TextToSpeech? = null
    private val myLocale = Locale("pt_PT", "POR")
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()

    override fun onPause() {
        super.onPause()
        val sharedPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("transportsHasRun", true).apply()
    }

    override fun onDestroy() {
        // Don't forget to shutdown!

        handler.removeCallbacks(runnable)

        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }

        super.onDestroy ();
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = TransportsFragmentBinding.inflate(layoutInflater)
        val view = binding.root

        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("transportsHasRun", false)

        defineModality(ttsFlag, srFlag, hasRun)

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                val fragment: Fragment = TransportsSelectionFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        val spinner: Spinner = view.findViewById(R.id.locations_spinner)
        println("Context:" + context.toString())
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                context,
                R.array.location_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this

        return view
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "TRANSPORTES"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)
        transportsViewModel = ViewModelProvider(this).get(TransportsViewModel::class.java)

        call_button_1.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            val temp = "tel:$phone"
            intent.data = Uri.parse(temp)
            startActivity(intent)
        }

        call_button_2.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            val temp = "tel:$phone"
            intent.data = Uri.parse(temp)
            startActivity(intent)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        val selItem: String = p0?.getItemAtPosition(p2).toString()
        val textFromLocation = view?.rootView?.findViewById<TextView>(R.id.location_to_CRPG_title)
        val textToLocation = view?.rootView?.findViewById<TextView>(R.id.CRPG_to_location_title)
        val driverFromLocation = view?.rootView?.findViewById<TextView>(R.id.text_driver_name_location_to_CRPG)
        val driverToLocation = view?.rootView?.findViewById<TextView>(R.id.text_driver_name_CRPG_to_location)

        when(selItem){
            "Porto" -> {
                textFromLocation!!.text = "Do Porto para o CRPG"
                textToLocation!!.text = "Do CRPG para o Porto"
                text_time_location_to_CRPG.text = transportsViewModel.horario_ida_CRPG
                text_time_location_from_CRPG.text = transportsViewModel.horario_volta_CRPG
                driverFromLocation?.text = transportsViewModel.nome_motorista_de_CRPG
                driverToLocation?.text = transportsViewModel.nome_motorista_para_CRPG
            }

            "Gaia" -> {
                textFromLocation!!.text = "De Gaia para o CRPG"
                textToLocation!!.text = "Do CRPG para Gaia"
                text_time_location_to_CRPG.text = transportsViewModel.horario_ida_CRPG
                text_time_location_from_CRPG.text = transportsViewModel.horario_volta_CRPG
                driverFromLocation?.text = transportsViewModel.nome_motorista_de_CRPG
                driverToLocation?.text = transportsViewModel.nome_motorista_para_CRPG
            }

            "Casa" -> {
                textFromLocation!!.text = "De Casa para o CRPG"
                textToLocation!!.text = "Do CRPG para Casa"
                text_time_location_to_CRPG.text = transportsViewModel.horario_ida_CRPG
                text_time_location_from_CRPG.text = transportsViewModel.horario_volta_CRPG
                driverFromLocation?.text = transportsViewModel.nome_motorista_de_CRPG
                driverToLocation?.text = transportsViewModel.nome_motorista_para_CRPG
            }
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun performActionWithVoiceCommand(command: String){
        when {
            command.contains("Porto", true) -> locations_spinner.setSelection(0)
            command.contains("Casa", true) -> locations_spinner.setSelection(1)
            command.contains("Gaia", true) -> locations_spinner.setSelection(2)
            command.contains("ligar ida", true) -> call_button_1.performClick()
            command.contains("ligar volta", true) -> call_button_2.performClick()
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
                val speechStatus = textToSpeech!!.speak("Diga a paragem que pretende para selecionar",
                        TextToSpeech.QUEUE_FLUSH, null, "ID")
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

                val speechStatus = textToSpeech!!.speak("Diga a paragem que pretende para selecionar", TextToSpeech.QUEUE_FLUSH, null, "ID")

            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun startVoiceRecognition(){
        //MANTER WIFI SEMPRE LIGADO
        //val handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            Speech.init(requireActivity())
            //hasInitSR = true
            try {
                Speech.getInstance().startListening(object : SpeechDelegate {
                    override fun onStartOfSpeech() {
                        Log.i("speech", "speech recognition is now active")
                    }

                    override fun onSpeechRmsChanged(value: Float) {
                        Log.d("speech", "rms is now: $value")
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




//println("Item selecionado: $selItem")
// /*
//        button_consult_custom_transport.setOnClickListener {
//            val fragment: Fragment = CustomTransportsFragment()
//            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
//            fragmentManager.popBackStack()
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }
//
//        button_consult_public_transport.setOnClickListener {
//            val fragment: Fragment = PublicTransportsFragment()
//            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
//            fragmentManager.popBackStack()
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }
//
//*/