package com.plataforma.crpg.ui.meals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.MealsFragmentBinding
import com.plataforma.crpg.ui.MainActivity
import com.plataforma.crpg.ui.agenda.AgendaFragment
import com.plataforma.crpg.ui.agenda.SharedViewModel
import kotlinx.android.synthetic.main.meals_fragment.*
import kotlinx.android.synthetic.main.reminder_activity_success.*


class MealsFragment : Fragment() {

    var FLAG_MEAL_CHOSEN = false
    var SELECTED_DATE = ""

    companion object {
        fun newInstance() = MealsFragment()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "REFEIÇÕES"
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

        val mealsViewModel = ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)
        val binding = MealsFragmentBinding.inflate(layoutInflater)
        val view = binding.root

        val isLunch = requireArguments().getBoolean("isLunch")
        println("> Bundle isLunch: $isLunch")

        println("""On Create View foods: ${mealsViewModel.retrievedMeal.carne}, ${mealsViewModel.retrievedMeal.peixe}, 
            |${mealsViewModel.retrievedMeal.dieta}, ${mealsViewModel.retrievedMeal.vegetariano}""".trimMargin())

        view.findViewById<AppCompatTextView>(R.id.text_opcao_carne)?.text = mealsViewModel.retrievedMeal.carne
        view.findViewById<AppCompatTextView>(R.id.text_opcao_peixe)?.text = mealsViewModel.retrievedMeal.peixe
        view.findViewById<AppCompatTextView>(R.id.text_opcao_dieta)?.text = mealsViewModel.retrievedMeal.dieta
        view.findViewById<AppCompatTextView>(R.id.text_opcao_vegetariano)?.text = mealsViewModel.retrievedMeal.vegetariano

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

        return view
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mealsViewModel = ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)
        val sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)

        mealsViewModel.testDB()
        val cardCarne: MaterialCardView? = view?.findViewById(R.id.frame_opcao_carne)
        val cardPeixe: MaterialCardView? = view?.findViewById(R.id.frame_opcao_peixe)
        val cardDieta: MaterialCardView? = view?.findViewById(R.id.frame_opcao_dieta)
        val cardVeg: MaterialCardView? = view?.findViewById(R.id.frame_opcao_vegetariano)

        println("Prato carne:" + mealsViewModel.retrievedMeal.carne)
        println("Meal selected date: " + sharedViewModel.selectedDate)

        cardCarne?.setOnLongClickListener {
            if(!cardCarne.isChecked){ mealsViewModel.selectedOption = 1}else{mealsViewModel.selectedOption = 0}
            cardCarne.isChecked = !cardCarne.isChecked
            cardPeixe?.isChecked = false
            cardDieta?.isChecked = false
            cardVeg?.isChecked = false
            FLAG_MEAL_CHOSEN = !FLAG_MEAL_CHOSEN
            println("Selected option: " + mealsViewModel.selectedOption)
            true
        }


        cardPeixe?.setOnLongClickListener {
            if(!cardPeixe.isChecked){ mealsViewModel.selectedOption = 2}else{mealsViewModel.selectedOption = 0}
            cardPeixe.isChecked = !cardPeixe.isChecked
            cardCarne?.isChecked = false
            cardDieta?.isChecked = false
            cardVeg?.isChecked = false
            FLAG_MEAL_CHOSEN = !FLAG_MEAL_CHOSEN
            println("Selected option: " + mealsViewModel.selectedOption)
            true
        }

        cardDieta?.setOnLongClickListener {
            if(!cardDieta.isChecked){ mealsViewModel.selectedOption = 3}else{mealsViewModel.selectedOption = 0}
            cardDieta.isChecked = !cardDieta.isChecked
            cardCarne?.isChecked = false
            cardPeixe?.isChecked = false
            cardVeg?.isChecked = false
            FLAG_MEAL_CHOSEN = !FLAG_MEAL_CHOSEN
            println("Selected option: " + mealsViewModel.selectedOption)
            true
        }

        cardVeg?.setOnLongClickListener {
            if(!cardVeg.isChecked){ mealsViewModel.selectedOption = 4 }else{mealsViewModel.selectedOption = 0}
            cardVeg.isChecked = !cardVeg.isChecked
            cardCarne?.isChecked = false
            cardPeixe?.isChecked = false
            cardDieta?.isChecked = false
            FLAG_MEAL_CHOSEN = !FLAG_MEAL_CHOSEN
            println("Selected option: " + mealsViewModel.selectedOption)
            true
        }

        button_confirm_meal.setOnClickListener(){
            if (mealsViewModel.selectedOption != 0) {
                view?.findViewById<View>(R.id.meal_choice_success)?.visibility = View.VISIBLE
                view?.findViewById<View>(R.id.aviso_nenhuma_refeicao_checked)?.visibility = View.GONE
                mealsViewModel.updateMealChoiceOnLocalStorage(sharedViewModel.selectedDate, true)
                button_ok.setOnClickListener(){
                    view?.findViewById<View>(R.id.meal_choice_success)?.visibility = View.GONE
                }
            } else {
                view?.findViewById<View>(R.id.aviso_nenhuma_refeicao_checked)?.visibility = View.VISIBLE
            }
        }

    }

    private fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

}

/*override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.getItemId()) {
        android.R.id.home -> {
            onBackPressed()
            return true
        }
    }
    return super.onOptionsItemSelected(item)
}
//val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
 //view?.findViewById<TextView>(R.id.success_text)?.text = "Refeição registada com sucesso!"
*/