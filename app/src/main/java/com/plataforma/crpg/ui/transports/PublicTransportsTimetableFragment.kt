package com.plataforma.crpg.ui.transports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import kotlinx.android.synthetic.main.fragment_custom_transport.button_return_transports
import kotlinx.android.synthetic.main.timetable_layout.*
import com.plataforma.crpg.databinding.FragmentPublicTransportsTimetablesBinding
import kotlinx.android.synthetic.main.fragment_public_transports_timetables.*


class PublicTransportsTimetableFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var opSelected = 0
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var transportsViewModel: TransportsViewModel

    companion object {
        private const val trindadeString = "Trindade"
        private const val valadaresString = "Valadares"
        private const val francelosString = "Francelos"
        private const val crpgString = "CRPG"
        private const val miramarString = "Miramar"
        private const val joaoDeDeusString = "João de Deus"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view?.findViewById<View>(R.id.photo_view)?.visibility = INVISIBLE
                view?.findViewById<View>(R.id.photo_view_hint)?.visibility = INVISIBLE
                view?.findViewById<View>(R.id.frame_layout_timetables)?.visibility = VISIBLE

                val fragment: Fragment = TransportsSelectionFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentPublicTransportsTimetablesBinding.inflate(layoutInflater)
        val view = binding.root

        val linesSpinner: Spinner? = view.findViewById(R.id.bus_lines_spinner_2)
        ArrayAdapter.createFromResource(
                context,
                R.array.linha_autocarro_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            linesSpinner?.adapter = adapter
        }

        linesSpinner?.onItemSelectedListener = this
        return view
        //return inflater.inflate(R.layout.fragment_public_transports_old, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "VER HORÁRIO"

        transportsViewModel = ViewModelProvider(this).get(TransportsViewModel::class.java)
        sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)
        val selectedDate = sharedViewModel.selectedDate
        val publicText = transportsViewModel.getPublicTransportText(selectedDate)
        println("Public custom text: $publicText")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //val binding = FragmentPublicTransportsTimetablesBinding.inflate(layoutInflater)
        val photoView = photo_view as PhotoView

        fun imageViewerController(){
            println(">Entrou no controller")
            view?.findViewById<View>(R.id.photo_view_hint)?.visibility = VISIBLE
            view?.findViewById<View>(R.id.photo_view)?.visibility = VISIBLE
            view?.findViewById<View>(R.id.frame_layout_timetables)?.visibility = INVISIBLE
            view?.findViewById<View>(R.id.photo_view)?.setOnClickListener {
                println("> Onclick photo view")
                view?.findViewById<View>(R.id.photo_view)?.visibility = INVISIBLE
                view?.findViewById<View>(R.id.photo_view_hint)?.visibility = INVISIBLE
                view?.findViewById<View>(R.id.frame_layout_timetables)?.visibility = VISIBLE
            }
        }

        button_view_timetable_1.setOnClickListener {

            when(opSelected){
                1 ->  { photoView.setImageResource(R.drawable.stcp_901_trindade_valadares)
                    imageViewerController()}
                2 ->  { photoView.setImageResource(R.drawable.zf_valadares_francelos)
                    imageViewerController() }
                3 ->  { photoView.setImageResource(R.drawable.linha35_joaodedeus_crpg)
                    imageViewerController() }
                4 ->  { photoView.setImageResource(R.drawable.linha45_joaodedeus_miramar)
                    imageViewerController() }
            }
        }

        button_view_timetable_2.setOnClickListener {
            when(opSelected){
                1 ->  { photoView.setImageResource(R.drawable.stcp_901_valadares_trindade)
                    imageViewerController() }
                2 ->  { photoView.setImageResource(R.drawable.zf_francelos_valadares)
                    imageViewerController() }
                3 ->  { photoView.setImageResource(R.drawable.linha35_crpg_joaodedeus)
                    imageViewerController() }
                4 ->  { photoView.setImageResource(R.drawable.linha45_miramar_joaodedeus)
                    imageViewerController() }
            }
        }

        button_return_transports.setOnClickListener {
            val fragment: Fragment = PublicTransportsFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
/*
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PublicTransportsTimetableFragment().apply {
                }
    }
*/
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {




        println("Item clicked: $p2")
        when(p2){
            0 -> {
                text_to_from_1.text = trindadeString
                text_to_from_2.text = valadaresString
                text_to_from_3.text = valadaresString
                text_to_from_4.text = trindadeString
                opSelected = 1
            }
            1 -> {
                text_to_from_1.text = valadaresString
                text_to_from_2.text = francelosString
                text_to_from_3.text = francelosString
                text_to_from_4.text = valadaresString
                opSelected = 2
            }
            2 -> {
                text_to_from_1.text = joaoDeDeusString
                text_to_from_2.text = crpgString
                text_to_from_3.text = crpgString
                text_to_from_4.text = joaoDeDeusString
                opSelected = 3
            }
            3 -> {
                text_to_from_1.text = joaoDeDeusString
                text_to_from_2.text = miramarString
                text_to_from_3.text = miramarString
                text_to_from_4.text = joaoDeDeusString
                opSelected = 4
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
            //frame_de_para_1.visibility = GONE
            //frame_de_para_2.visibility = GONE
    }

    private fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}



//val photoView = view?.findViewById(R.id.photo_view) as PhotoView
//textFromBusLines!!.text = "De Casa para o CRPG"
//val selItem: String = p0?.getItemAtPosition(p2).toString()
//var textFromBusLines = view?.rootView?.findViewById<TextView>(R.id.)
/*
    fun ActionBar getActionBar(){
        return ((ActionBarActivity() activity?.getSupport
    }
*/
/*
@Override
public fun boolean onOptionsItemSelected() {
    /*
        ADD WHAT YOU WANT TO DO WHEN ARROW IS PRESSED
    */
    return super.onOptionsItemSelected(item);
}*/

//activity?.setActionBarTitle("TRANSPORTES PUBLICOS")

//(MainActivity getActivity()).setActionBarTitle("TRANSPORTES PUBLICOS")
//
// showBackButton()
//        // This callback will only be called when MyFragment is at least Started.
//        /*
//        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
//            override fun handleOnBackPressed() {
//                val fragment: Fragment = TransportsFragment()
//                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
//                fragmentManager.popBackStack()
//                fragmentTransaction.addToBackStack(null)
//                fragmentTransaction.commit()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)*/
//
//        // The callback can be enabled or disabled here or in handleOnBackPressed()
//
//        /*
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        //enable menu
//        setHasOptionsMenu(true)
//
//        requireActivity()
//                .onBackPressedDispatcher
//                .addCallback(this) {
//                    println("ola")
//                }
//    }
// import kotlinx.android.synthetic.main.fragment_public_transports.*
//*/
// view?.findViewById<TextView>(R.id.public_transports_text)?.text = customText
//
// public_transports_text.text= publicText
//
// layoutInflater.inflate(R.layout.timetable_layout, null).bringToFront()
//                    //view?.findViewById<View>(R.id.timetable
//
// layoutInflater.inflate(R.layout.timetable_layout, null)