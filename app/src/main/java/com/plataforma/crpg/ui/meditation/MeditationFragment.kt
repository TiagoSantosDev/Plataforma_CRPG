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
        //return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "MEDITAÇÃO"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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