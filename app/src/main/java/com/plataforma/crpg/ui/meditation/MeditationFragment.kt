package com.plataforma.crpg.ui.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.FragmentMeditationBinding
import kotlinx.android.synthetic.main.fragment_meditation.*
import kotlinx.android.synthetic.main.notes_fragment.*

class MeditationFragment : Fragment() {

    companion object {
        fun newInstance() = MeditationFragment()
    }

    var selectedMood = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

        val binding = FragmentMeditationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "MEDITAÇÃO"
        val medViewModel = ViewModelProvider(this).get(MeditationViewModel::class.java)
        button_mood_relaxed.setOnClickListener{
            medViewModel.selectedMood = "RELAXED"
            goToMeditationMediaPlayer()
        }

        button_mood_happy.setOnClickListener{
            medViewModel.selectedMood = "HAPPY"
            goToMeditationMediaPlayer()
        }

        button_mood_sleepy.setOnClickListener{
            medViewModel.selectedMood = "CONFIDENT"
            goToMeditationMediaPlayer()
        }

        button_mood_confident.setOnClickListener{
            medViewModel.selectedMood = "CONFIDENT"
            goToMeditationMediaPlayer()
        }

        button_mood_loved.setOnClickListener{
            medViewModel.selectedMood = "CONFIDENT"
            goToMeditationMediaPlayer()
        }

        button_mood_mindful.setOnClickListener{
            medViewModel.selectedMood = "CONFIDENT"
            goToMeditationMediaPlayer()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun goToMeditationMediaPlayer(){
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
