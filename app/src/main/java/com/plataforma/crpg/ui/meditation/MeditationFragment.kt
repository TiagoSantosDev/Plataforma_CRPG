package com.plataforma.crpg.ui.meditation

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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.github.windsekirun.naraeaudiorecorder.extensions.runOnUiThread
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
    private var onResumeFlag = false
    private val myLocale = Locale("pt_PT", "POR")
    private var hasInitSR = false

    companion object {
        fun newInstance() = MeditationFragment()
    }

    override fun onResume() {
        super.onResume()
        val actionBar = (activity as MainActivity?)?.supportActionBar
        actionBar?.title = "MEDITAÇÃO"
        actionBar?.setDisplayHomeAsUpEnabled(false)
        //val colorDrawable = ColorDrawable(Color.parseColor("#00BBF2"))
        //onResumeFlag = true
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMeditationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        //garante que o TTS so e lancado na primeira vez que um Fragment e aberto
        editor.putBoolean("meditationHasRun", true).apply()

        if (textToSpeech != null){
            textToSpeech?.shutdown()
        }
        onResumeFlag = true
    }

    override fun onDestroy() {
        // Don't forget to shutdown!

        println("Has init SR: $hasInitSR")

        if (hasInitSR){
            if (Speech.getInstance() != null) {
                Speech.getInstance().stopListening()
                Speech.getInstance().shutdown()
                println("shutdown Speech")
            }
        }

        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val medViewModel = ViewModelProvider(activity as AppCompatActivity).get(MeditationViewModel::class.java)

        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        //val srPreferences = this.requireActivity().getSharedPreferences("TTS", Context.MODE_PRIVATE)

        println("TTS preferences: " + modalityPreferences.getBoolean("TTS", false))
        println("SR preferences: " + modalityPreferences.getBoolean("SR", false))

        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("meditationHasRun", false)

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

        defineModality(ttsFlag, srFlag, hasRun)


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
                val speechStatus = textToSpeech!!.speak("Selecione uma das opções ou diga o estado" +
                        "em voz alta", TextToSpeech.QUEUE_FLUSH, null, "ID")
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun multimodalOption() {
        println("First Time flag: $onResumeFlag")
        if (!onResumeFlag) {
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
                    //var runable = Runnable {
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
                    println("Chegou aqui pos utterance listener")

                    val speechStatus = textToSpeech!!.speak("Selecione uma das opções ou diga o estado" +
                            "em voz alta", TextToSpeech.QUEUE_FLUSH, null, "ID")

                } else {
                    Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun startVoiceRecognition(){
        //MANTER WIFI SEMPRE LIGADO
        val handler = Handler(Looper.getMainLooper())
        val runable = Runnable {
            Speech.init(requireActivity())
            hasInitSR = true
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
                        hasInitSR = false
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
        val fragment: Fragment = MeditationMediaPlayerFragment()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
        fragmentManager.popBackStack()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}



//editor.putBoolean("RanBefore", true);
//editor.commit();
/*
        if (Speech.getInstance() != null){
            Speech.getInstance().shutdown()
        }
        if (textToSpeech != null) textToSpeech?.shutdown() */
/*
if(Speech.getInstance() != null){
    Speech.getInstance().stopListening()
}*/
//onResumeFlag = true
//handler.post(runable)
//ttsMeditationHint()
//Speech.getInstance().shutdown()
//val runable2 = Runnable{
//}
//handler.post(runable2)
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
//
// /*
//                    if (textToSpeech!!.isSpeaking) {
//                        ttsFlag = true
//                    }
//
//                    if (!textToSpeech!!.isSpeaking) {
//                        ttsFlag = false
//                    }*/
//
//
//                    /*
//        Speech.init(context)
//        try {
//            Speech.getInstance().startListening(object : SpeechDelegate {
//                override fun onStartOfSpeech() {
//                    Log.i("speech", "speech recognition is now active")
//                }
//
//                override fun onSpeechRmsChanged(value: Float) {
//                    Log.d("speech", "rms is now: $value")
//                }
//
//                override fun onSpeechPartialResults(results: List<String>) {
//                    val str = StringBuilder()
//                    for (res in results) {
//                        str.append(res).append(" ")
//                    }
//                    Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
//                }
//
//                override fun onSpeechResult(result: String) {
//                    performActionWithVoiceCommand(result)
//                    Log.i("speech", "result: $result")
//                    //println("on Speech Result")
//                }
//            })
//        } catch (exc: SpeechRecognitionNotAvailable) {
//            Log.e("speech", "Speech recognition is not available on this device!")
//        } catch (exc: GoogleVoiceTypingDisabledException) {
//            Log.e("speech", "Google voice typing must be enabled!")
//        }
//*/