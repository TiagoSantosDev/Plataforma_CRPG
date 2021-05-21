package com.plataforma.crpg.ui.transports

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.databinding.FragmentTransportSelectionBinding
import com.plataforma.crpg.ui.MainActivity
import com.plataforma.crpg.ui.agenda.AgendaFragment
import com.plataforma.crpg.ui.agenda.SharedViewModel
import kotlinx.android.synthetic.main.fragment_transport_selection.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*
import kotlin.properties.Delegates


class TransportsSelectionFragment : Fragment() {

    companion object {
        fun newInstance() = TransportsSelectionFragment()
    }

    private val myLocale = Locale("pt_PT", "POR")
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()

    private var textToSpeech: TextToSpeech? = null
    private lateinit var transportsViewModel: TransportsViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onPause() {
        super.onPause()
        val sharedPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("selectionHasRun", true).apply()
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

        super.onDestroy ()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("selectionHasRun", false)

        defineModality(ttsFlag, srFlag, hasRun)

        val binding = FragmentTransportSelectionBinding.inflate(layoutInflater)
        val view = binding.root

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment: Fragment = AgendaFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "SELEÇÃO DE TRANSPORTES"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)
        transportsViewModel = ViewModelProvider(this).get(TransportsViewModel::class.java)

        botao_escolha_transportes_fixos.setOnClickListener {
            val fragment: Fragment = TransportsFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        botao_escolha_transportes_personalizados.setOnClickListener {
            val fragment: Fragment = CustomTransportsFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        botao_escolha_transportes_publicos.setOnClickListener {
            val fragment: Fragment = PublicTransportsFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    fun performActionWithVoiceCommand(command: String){
        when {
            command.contains("Camioneta", true) -> botao_escolha_transportes_fixos.performClick()
            command.contains("Personalizado", true) -> botao_escolha_transportes_personalizados.performClick()
            command.contains("Transportes Públicos", true) -> botao_escolha_transportes_publicos.performClick()
        }
    }

    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {

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
                val speechStatus = textToSpeech!!.speak("Diga Camioneta, Personalizado ou Transportes Públicos para" +
                        "selecionar uma das opções ", TextToSpeech.QUEUE_FLUSH, null, "ID")
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

                val speechStatus = textToSpeech!!.speak("Diga Camioneta, Personalizado ou Transportes Públicos para" +
                        "selecionar uma das opções ", TextToSpeech.QUEUE_FLUSH, null, "ID")

            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun startVoiceRecognition(){
        //MANTER WIFI SEMPRE LIGADO
        //val handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            handler.sendEmptyMessage(0)
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

    private fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}