package com.plataforma.crpg.ui.transports

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.chrisbanes.photoview.PhotoView
import com.plataforma.crpg.R
import com.plataforma.crpg.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_custom_transport.*

//import kotlinx.android.synthetic.main.fragment_public_transports.*


class PublicTransportsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var opSelected = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

        showBackButton()
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                /*
                val a = Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(a)*/

                println("> handleOnBackPressed")

                val fragment: Fragment = TransportsFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        // The callback can be enabled or disabled here or in handleOnBackPressed()

        val linesSpinner: Spinner? = view?.findViewById(R.id.bus_lines_spinner)
        ArrayAdapter.createFromResource(
                context,
                R.array.linha_autocarro_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            linesSpinner?.adapter = adapter
        }

        linesSpinner?.onItemSelectedListener = this
        return inflater.inflate(R.layout.fragment_public_transports_old, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val photoView = view?.findViewById(R.id.photo_view) as PhotoView

        /*
        button_view_timetable_1.setOnClickListener {

            when(opSelected){
                0 ->  { photoView.setImageResource(R.drawable.linha35_joaodedeus_crpg)
                layoutInflater.inflate(R.layout.timetable_layout, null) }
                1 ->  { photoView.setImageResource(R.drawable.linha35_joaodedeus_crpg)
                        layoutInflater.inflate(R.layout.timetable_layout, null) }
                2 ->  { photoView.setImageResource(R.drawable.linha45_joaodedeus_miramar)
                    layoutInflater.inflate(R.layout.timetable_layout, null) }
                3 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
                    layoutInflater.inflate(R.layout.timetable_layout, null) }
                4 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
                    layoutInflater.inflate(R.layout.timetable_layout, null) }
            }
        }

        button_view_timetable_2.setOnClickListener {
            when(opSelected){
                0 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
                    layoutInflater.inflate(R.layout.timetable_layout, null) }
                1 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
                    layoutInflater.inflate(R.layout.timetable_layout, null) }
                2 ->  { photoView.setImageResource(R.drawable.linha45_miramar_joaodedeus)
                    layoutInflater.inflate(R.layout.timetable_layout, null) }
                3 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
                    layoutInflater.inflate(R.layout.timetable_layout, null) }
                4 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
                    layoutInflater.inflate(R.layout.timetable_layout, null) }
            }
        }
        */
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PublicTransportsFragment().apply {
                }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selItem: String = p0?.getItemAtPosition(p2).toString()
        val pos = p2
        var textFromBusLines = view?.rootView?.findViewById<TextView>(R.id.bus_lines_spinner)
        /*
        when(pos){
            1 -> {
                text_to_from_1.text = "CRPG"
                text_to_from_2.text = "S. João de Deus"
                text_to_from_3.text = "S. João de Deus"
                text_to_from_4.text = "CRPG"
                opSelected = 1
            }
            2 -> {
                textFromBusLines!!.text = "De Gaia para o CRPG"
                opSelected = 2
            }
            3 -> {
                textFromBusLines!!.text = "De Casa para o CRPG"
                opSelected = 3
            }
        }*/
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    /*
    @Override
    public fun boolean onOptionsItemSelected() {
        /*
            ADD WHAT YOU WANT TO DO WHEN ARROW IS PRESSED
        */
        return super.onOptionsItemSelected(item);
    }*/
    
}