package com.plataforma.crpg.ui.transports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.github.chrisbanes.photoview.PhotoView
import com.plataforma.crpg.R
import com.plataforma.crpg.ui.MainActivity
import com.plataforma.crpg.ui.agenda.SharedViewModel
import kotlinx.android.synthetic.main.fragment_custom_transport.*


class CustomTransportsFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var transportsViewModel: TransportsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        transportsViewModel = ViewModelProvider(this).get(TransportsViewModel::class.java)
        sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)
        val selectedDate = sharedViewModel.selectedDate
        val customText = transportsViewModel.getCustomText(selectedDate)
        view?.findViewById<TextView>(R.id.custom_transports_text)?.text = customText

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                println(">HandleonBackButtonPressed")
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
        return inflater.inflate(R.layout.fragment_custom_transport, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "TRANSPORTE PERSONALIZADO"
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_return_transports.setOnClickListener {
            val fragment: Fragment = TransportsFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        
    }

    fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}