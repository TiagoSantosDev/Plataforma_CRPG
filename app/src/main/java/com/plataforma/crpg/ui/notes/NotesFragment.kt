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
import androidx.recyclerview.widget.LinearLayoutManager
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.NotesFragmentBinding
import com.plataforma.crpg.model.Note
import com.plataforma.crpg.model.NoteType
import kotlinx.android.synthetic.main.notes_fragment.*


class NotesFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = NotesFragment()
    }

    private lateinit var notesViewModel: NotesViewModel

    private val noteList = listOf(
            Note(NoteType.TEXT, "16042021","1200", "Nota 1",
                    "a","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_214302371.jpg"),
            Note(NoteType.TEXT, "16042021", "1200","Nota 2",
                    "b","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_214302371.jpg"),
            Note(NoteType.TEXT, "16042021", "1200","Nota 3",
                    "c","","file:///storage/emulated/0/DCIM/Camera/IMG_20210417_214302371.jpg")
    )

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val binding = NotesFragmentBinding.inflate(layoutInflater)


        return binding.root
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ListAdapter(noteList)
        }
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
            val fragment: Fragment = NewTextNoteFragment()
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