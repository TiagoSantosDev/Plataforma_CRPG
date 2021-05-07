package com.plataforma.crpg.ui.transports

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.TransportsFragmentBinding
import com.plataforma.crpg.ui.MainActivity
import com.plataforma.crpg.ui.agenda.AgendaFragment
import com.plataforma.crpg.ui.agenda.SharedViewModel
import kotlinx.android.synthetic.main.transports_fragment.*


class TransportsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = TransportsFragment()
    }

    private lateinit var transportsViewModel: TransportsViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var phone = "00351912193034"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = TransportsFragmentBinding.inflate(layoutInflater)
        val view = binding.root

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                val fragment: Fragment = AgendaFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        val spinner: Spinner = view.findViewById(R.id.locations_spinner)
        println("Context:" + context.toString())
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "TRANSPORTES"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)
        transportsViewModel = ViewModelProvider(this).get(TransportsViewModel::class.java)

        call_button_1.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            val temp = "tel:$phone"
            intent.data = Uri.parse(temp)
            startActivity(intent)
        }

        call_button_2.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            val temp = "tel:$phone"
            intent.data = Uri.parse(temp)
            startActivity(intent)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        val selItem: String = p0?.getItemAtPosition(p2).toString()
        val textFromLocation = view?.rootView?.findViewById<TextView>(R.id.location_to_CRPG_title)
        val textToLocation = view?.rootView?.findViewById<TextView>(R.id.CRPG_to_location_title)
        val driverFromLocation = view?.rootView?.findViewById<TextView>(R.id.text_driver_name_location_to_CRPG)
        val driverToLocation = view?.rootView?.findViewById<TextView>(R.id.text_driver_name_CRPG_to_location)

        when(selItem){
            "Porto" -> {
                textFromLocation!!.text = "Do Porto para o CRPG"
                textToLocation!!.text = "Do CRPG para o Porto"
                text_time_location_to_CRPG.text = transportsViewModel.horario_ida_CRPG
                text_time_location_from_CRPG.text = transportsViewModel.horario_volta_CRPG
                driverFromLocation?.text = transportsViewModel.nome_motorista_de_CRPG
                driverToLocation?.text = transportsViewModel.nome_motorista_para_CRPG
            }

            "Gaia" -> {
                textFromLocation!!.text = "De Gaia para o CRPG"
                textToLocation!!.text = "Do CRPG para Gaia"
                text_time_location_to_CRPG.text = transportsViewModel.horario_ida_CRPG
                text_time_location_from_CRPG.text = transportsViewModel.horario_volta_CRPG
                driverFromLocation?.text = transportsViewModel.nome_motorista_de_CRPG
                driverToLocation?.text = transportsViewModel.nome_motorista_para_CRPG
            }

            "Casa" -> {
                textFromLocation!!.text = "De Casa para o CRPG"
                textToLocation!!.text = "Do CRPG para Casa"
                text_time_location_to_CRPG.text = transportsViewModel.horario_ida_CRPG
                text_time_location_from_CRPG.text = transportsViewModel.horario_volta_CRPG
                driverFromLocation?.text = transportsViewModel.nome_motorista_de_CRPG
                driverToLocation?.text = transportsViewModel.nome_motorista_para_CRPG
            }
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}
//println("Item selecionado: $selItem")
// /*
//        button_consult_custom_transport.setOnClickListener {
//            val fragment: Fragment = CustomTransportsFragment()
//            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
//            fragmentManager.popBackStack()
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }
//
//        button_consult_public_transport.setOnClickListener {
//            val fragment: Fragment = PublicTransportsFragment()
//            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
//            fragmentManager.popBackStack()
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }
//
//*/