package com.plataforma.crpg.ui.transports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.TransportsFragmentBinding


class TransportsFragment : Fragment() {

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

        return view
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        transportsViewModel = ViewModelProvider(this).get(TransportsViewModel::class.java)

        transportsViewModel.nome_motorista = "Jorge"



    }

}