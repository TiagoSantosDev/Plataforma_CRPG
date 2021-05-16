package com.plataforma.crpg.ui.meditation

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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.FragmentMeditationBinding
import com.plataforma.crpg.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_meditation.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*

class MeditationFragment : Fragment() {

    private var textToSpeech: TextToSpeech? = null
    private var firstTimeFlag = false
    private var ttsFlag = false
    val myLocale = Locale("pt_PT", "POR")

    companion object {
        fun newInstance() = MeditationFragment()
    }

    override fun onResume() {
        super.onResume()
        //val colorDrawable = ColorDrawable(Color.parseColor("#00BBF2"))
        val actionBar = (activity as MainActivity?)?.supportActionBar
        actionBar?.title = "MEDITAÇÃO"
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMeditationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val medViewModel = ViewModelProvider(activity as AppCompatActivity).get(MeditationViewModel::class.java)

        button_mood_relaxed.setOnClickListener{
            medViewModel.selectedMood = "RELAXADO"
            medViewModel.getValue()
            goToMeditationMediaPlayer()
        }

        button_mood_happy.setOnClickListener{
            medViewModel.selectedMood = "FELIZ"
            goToMeditationMediaPlayer()
        }

        button_mood_sleepy.setOnClickListener{
            medViewModel.selectedMood = "SONOLENTO"
            goToMeditationMediaPlayer()
        }

        button_mood_confident.setOnClickListener{
            medViewModel.selectedMood = "CONFIANTE"
            goToMeditationMediaPlayer()
        }

        button_mood_loved.setOnClickListener{
            medViewModel.selectedMood = "QUERIDO"
            goToMeditationMediaPlayer()
        }

        button_mood_mindful.setOnClickListener{
            medViewModel.selectedMood = "MENTE SÃ"
            goToMeditationMediaPlayer()
        }

        ttsMeditationHint()
/*
        Speech.init(context)
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
                    Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                }

                override fun onSpeechResult(result: String) {
                    performActionWithVoiceCommand(result)
                    Log.i("speech", "result: $result")
                    //println("on Speech Result")
                }
            })
        } catch (exc: SpeechRecognitionNotAvailable) {
            Log.e("speech", "Speech recognition is not available on this device!")
        } catch (exc: GoogleVoiceTypingDisabledException) {
            Log.e("speech", "Google voice typing must be enabled!")
        }
*/
    }

    fun performActionWithVoiceCommand(command: String) {
        when {
            command.contains("Relaxado", true) -> button_mood_relaxed.performClick()
            command.contains("Feliz", true) -> button_mood_happy.performClick()
            command.contains("Com Sono", true) -> button_mood_sleepy.performClick()
            command.contains("Confiante", true) -> button_mood_confident.performClick()
            command.contains("Querido", true) -> button_mood_loved.performClick()
            command.contains("Mente Sã", true) -> button_mood_mindful.performClick()
        }
    }


    private fun ttsMeditationHint() {
        Speech.init(context)
        println("First Time flag: $firstTimeFlag")
        if (!firstTimeFlag) {
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

                    var handler = Handler(Looper.getMainLooper())
                    //
                    //var runable = Runnable {
                        val speechListener = object : UtteranceProgressListener() {
                            @Override
                            override fun onStart(p0: String?) {
                                //Speech.getInstance().shutdown()
                                println("Iniciou TTS")
                            }

                            override fun onDone(p0: String?) {
                                //Speech.init(context)
                                println("Encerrou TTS")
                                //val runable2 = Runnable{
                                startVoiceRecognition()
                                //}
                                //handler.post(runable2)
                            }

                            override fun onError(p0: String?) {
                                TODO("Not yet implemented")
                            }
                        }

                    textToSpeech?.setOnUtteranceProgressListener(speechListener)

                    //handler.post(runable)

                    if (textToSpeech!!.isSpeaking) {
                        ttsFlag = true
                    }

                    if (!textToSpeech!!.isSpeaking) {
                        ttsFlag = false
                    }

                    val speechStatus = textToSpeech!!.speak("Selecione uma das opções ou diga o estado" +
                            "em voz alta", TextToSpeech.QUEUE_FLUSH, null, "ID")
                    firstTimeFlag = true
                } else {
                    Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun startVoiceRecognition(){

        val handler = Handler(Looper.getMainLooper())
        val runable = Runnable {
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
                        Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                    }

                    override fun onSpeechResult(result: String) {
                        performActionWithVoiceCommand(result)
                        Log.i("speech", "result: $result")
                        println("on Speech Result")
                    }
                })
            } catch (exc: SpeechRecognitionNotAvailable) {
                Log.e("speech", "Speech recognition is not available on this device!")
            } catch (exc: GoogleVoiceTypingDisabledException) {
                Log.e("speech", "Google voice typing must be enabled!")
            }

        }

        handler.post(runable)

    }

    private fun goToMeditationMediaPlayer(){
        Speech.getInstance().shutdown()
        textToSpeech?.shutdown()
        val fragment: Fragment = MeditationMediaPlayerFragment()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
        fragmentManager.popBackStack()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}

/*
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
            Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
        }

        override fun onSpeechResult(result: String) {
            performActionWithVoiceCommand(result)
            Log.i("speech", "result: $result")
            //println("on Speech Result")
        }
    })
} catch (exc: SpeechRecognitionNotAvailable) {
    Log.e("speech", "Speech recognition is not available on this device!")
} catch (exc: GoogleVoiceTypingDisabledException) {
    Log.e("speech", "Google voice typing must be enabled!")
}*/
/*
      button.setOnClickListener {
          val fragment: Fragment = MeditationMediaPlayerFragment()
          val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
          val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
          fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
          fragmentManager.popBackStack()
          fragmentTransaction.addToBackStack(null)
          fragmentTransaction.commit()
      }*/

/*
    fun buttonClicked(view: View) {
        println("View ID:" +  view.id)
        println("Button ID:" + button_mood_relaxed.id)
        when(view.id){
            button_mood_relaxed.id -> {selectedMood= "RELAXED"}
            button_mood_happy.id -> {selectedMood= "HAPPY"}
            button_mood_sleepy.id -> {selectedMood= "SLEEPY"}
            button_mood_confident.id -> {selectedMood= "CONFIDENT"}
            button_mood_loved.id -> {selectedMood= "LOVED"}
            button_mood_mindful.id -> {selectedMood= "MINDFUL"}
        }

        /*
        if (view.id == android.R.id.) {
            // button1 action
        } else if (view.id == android.R.id.button2) {
            //button2 action
        } else if (view.id == android.R.id.button3) {
            //button3 action
        }*/

        val fragment: Fragment = MeditationMediaPlayerFragment()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
        fragmentManager.popBackStack()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }*/

/*
        view.findViewById<Button>(R.id.button_mood_relaxed).setOnClickListener(mListener);
        view.findViewById<Button>(R.id.button_mood_happy).setOnClickListener(mListener);
        view.findViewById<Button>(R.id.button_mood_sleepy).setOnClickListener(mListener);
        view.findViewById<Button>(R.id.button_mood_confident).setOnClickListener(mListener);
        view.findViewById<Button>(R.id.button_mood_loved).setOnClickListener(mListener);
        view.findViewById<Button>(R.id.button_mood_mindful).setOnClickListener(mListener);
        */
/*
(activity as AppCompatActivity).supportActionBar?.title = "MEDITAÇÃO"
(activity as MainActivity?)?.supportActionBar?.
(activity as MainActivity?)?.supportActionBar?.
(activity as MainActivity?)?.nav_view?.setBackgroundDrawable(colorDrawable)
*/

//actionBar?.setBackgroundDrawable(colorDrawable)
//(activity as MainActivity?)?.nav_view?.setBackgroundDrawable(colorDrawable)