package com.plataforma.crpg.ui.transports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.TransportsFragmentBinding
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

        transportsViewModel.nome_motorista = "Jorge"





    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        val selItem: String = p0?.getItemAtPosition(p2).toString()
        var textFromLocation = view?.rootView?.findViewById<TextView>(R.id.location_to_CRPG_title)

        when(selItem){
            "Porto" -> textFromLocation!!.text= "Do Porto para o CRPG"
            "Gaia" -> textFromLocation!!.text = "De Gaia para o CRPG"
            "Casa" -> textFromLocation!!.text = "De Casa para o CRPG"
        }

        println("Item selecionado: $selItem")

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}