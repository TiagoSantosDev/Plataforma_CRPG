package com.plataforma.crpg.ui.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.plataforma.crpg.R
import com.plataforma.crpg.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_meditation.*
import kotlinx.android.synthetic.main.notes_fragment.*


class MeditationMediaPlayerFragment : Fragment(){

    companion object {
        fun newInstance() = MeditationMediaPlayerFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val binding = MeditationMediaPlayerFragment.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "REPRODUZIR ÁUDIO"

        button_return_meditation.setOnClickListener {
            val fragment: Fragment = MeditationMediaPlayerFragment()
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
                val fragment: Fragment = TransportsFragment()
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
