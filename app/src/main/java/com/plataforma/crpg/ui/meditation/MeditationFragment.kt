package com.plataforma.crpg.ui.meditation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.FragmentMeditationBinding
import com.plataforma.crpg.ui.MainActivity
import kotlinx.android.synthetic.main.custom_player_control_view.*
import kotlinx.android.synthetic.main.fragment_meditation.*
import kotlinx.android.synthetic.main.fragment_meditation_media_player.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable

class MeditationFragment : Fragment() {

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