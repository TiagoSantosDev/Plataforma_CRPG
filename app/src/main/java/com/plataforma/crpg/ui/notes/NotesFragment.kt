package com.plataforma.crpg.ui.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.NotesFragmentBinding
import kotlinx.android.synthetic.main.fragment_date_picker.*
import kotlinx.android.synthetic.main.transports_fragment.*


class NotesFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = NotesFragment()
    }

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val binding = NotesFragmentBinding.inflate(layoutInflater)

        return view
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        button_new_voice_note.setOnClickListener {
            val fragment: Fragment = NewVoiceNoteFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        button_new_text_note.setOnClickListener {
            val fragment: Fragment = NewTextNoteFragmentFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        val selItem: String = p0?.getItemAtPosition(p2).toString()
        val textFromLocation = view?.rootView?.findViewById<TextView>(R.id.location_to_CRPG_title)



    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}