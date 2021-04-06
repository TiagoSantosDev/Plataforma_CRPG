package com.plataforma.crpg.ui.transports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.plataforma.crpg.R
import kotlinx.android.synthetic.main.fragment_public_transports.*


class PublicTransportsFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

        val linesSpinner: Spinner? = view?.findViewById(R.id.bus_lines_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                context,
                R.array.location_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            linesSpinner?.adapter = adapter
        }

        linesSpinner?.onItemSelectedListener = this

        return inflater.inflate(R.layout.fragment_public_transports, container, false)
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

        when(pos){
            1 -> textFromBusLines!!.text= "Do Porto para o CRPG"
            2 -> textFromBusLines!!.text = "De Gaia para o CRPG"
            3 -> textFromBusLines!!.text = "De Casa para o CRPG"
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}