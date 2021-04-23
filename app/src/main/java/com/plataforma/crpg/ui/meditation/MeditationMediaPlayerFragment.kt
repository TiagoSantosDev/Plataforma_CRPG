package com.plataforma.crpg.ui.meditation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.FragmentMeditationMediaPlayerBinding
import com.plataforma.crpg.ui.MainActivity
import com.plataforma.crpg.ui.notes.NotesViewModel
import kotlinx.android.synthetic.main.fragment_meditation.*
import kotlinx.android.synthetic.main.fragment_meditation_media_player.*
import kotlinx.android.synthetic.main.notes_fragment.*


class MeditationMediaPlayerFragment : Fragment(){

    companion object {
        fun newInstance() = MeditationMediaPlayerFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentMeditationMediaPlayerBinding.inflate(layoutInflater)
        return binding.root
        //return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "REPRODUZIR ÁUDIO"
        val medViewModel = ViewModelProvider(activity as AppCompatActivity).get(MeditationViewModel::class.java)

        println(">Sel mood: " + medViewModel.selectedMood)
        medViewModel.getValue()
        text_selected_mood.text = medViewModel.selectedMood

        when(medViewModel.selectedMood){
            "RELAXED" -> { mood_color.setColorFilter(ContextCompat.getColor((activity as AppCompatActivity).applicationContext, R.color.material_blue_200))}
            "HAPPY" ->  {}
            "SLEEPY" ->{}
            "CONFIDENT" ->{}
            "LOVED" ->{}
            "MINDFUL" ->{}
        }

        

        button_return_meditation.setOnClickListener {
            val fragment: Fragment = MeditationFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                val fragment: Fragment = MeditationFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

}
//context?.resources?.getColor(R.color.material_blue_200)?.let { mood_color.setColorFilter(it) }