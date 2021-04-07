package com.plataforma.crpg.ui.transports

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.TransportsFragmentBinding
import com.plataforma.crpg.ui.agenda.AgendaFragment
import kotlinx.android.synthetic.main.fragment_date_picker.*
import kotlinx.android.synthetic.main.fragment_public_transports.*
import kotlinx.android.synthetic.main.transports_fragment.*
import kotlinx.android.synthetic.main.transports_fragment.view.*


class TransportsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = TransportsFragment()
    }

    private lateinit var transportsViewModel: TransportsViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val binding = TransportsFragmentBinding.inflate(layoutInflater)
        val view = binding.root

        val spinner: Spinner = view.findViewById(R.id.locations_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                context,
                R.array.location_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this

        return view
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        transportsViewModel = ViewModelProvider(this).get(TransportsViewModel::class.java)

        button_consult_transport.setOnClickListener {
            val fragment: Fragment = PublicTransportsFragment()
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
        var textFromLocation = view?.rootView?.findViewById<TextView>(R.id.location_to_CRPG_title)
        var textToLocation = view?.rootView?.findViewById<TextView>(R.id.CRPG_to_location_title)
        var driverFromLocation = view?.rootView?.findViewById<TextView>(R.id.text_driver_name_location_to_CRPG)
        var driverToLocation = view?.rootView?.findViewById<TextView>(R.id.text_driver_name_CRPG_to_location)

        when(selItem){
            "Porto" ->  {textFromLocation!!.text= "Do Porto para o CRPG"
                textToLocation!!.text="Do CRPG para o Porto"
                driverFromLocation?.text = transportsViewModel.nome_motorista_de_CRPG
                driverToLocation?.text = transportsViewModel.nome_motorista_para_CRPG
            }

            "Gaia" -> {textFromLocation!!.text = "De Gaia para o CRPG"
            textToLocation!!.text="Do CRPG para Gaia"}

            "Casa" -> {textFromLocation!!.text = "De Casa para o CRPG"
            textToLocation!!.text="Do CRPG para Casa"}
        }

        println("Item selecionado: $selItem")

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}