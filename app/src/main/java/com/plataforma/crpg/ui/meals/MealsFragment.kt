package com.plataforma.crpg.ui.meals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import com.plataforma.crpg.R
import com.plataforma.crpg.R.color.material_blue_200
import com.plataforma.crpg.databinding.MealsFragmentBinding
import com.plataforma.crpg.model.Meal
import kotlinx.android.synthetic.main.meals_fragment.*
import kotlinx.android.synthetic.main.reminder_activity_success.*


class MealsFragment : Fragment() {

    var FLAG_MEAL_CHOSEN = false

    companion object {
        fun newInstance() = MealsFragment()
    }

    private lateinit var mealsViewModel: MealsViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val binding = MealsFragmentBinding.inflate(layoutInflater)
        val view = binding.root


        //view?.findViewById<AppCompatTextView>(R.id.text_opcao_carne)?.text = mealsViewModel.meal.carne
        //view?.findViewById<AppCompatTextView>(R.id.text_opcao_peixe)?.text = mealsViewModel.meal.peixe
        //view?.findViewById<AppCompatTextView>(R.id.text_opcao_carne)?.text = mealsViewModel.meal.dieta
        //view?.findViewById<AppCompatTextView>(R.id.text_opcao_carne)?.text = mealsViewModel.meal.vegetariano

        return view
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mealsViewModel = ViewModelProvider(this).get(MealsViewModel::class.java)

        mealsViewModel.testDB()

        val card_carne: MaterialCardView? = view?.findViewById(R.id.frame_opcao_carne)
        val card_peixe: MaterialCardView? = view?.findViewById(R.id.frame_opcao_peixe)
        val card_dieta: MaterialCardView? = view?.findViewById(R.id.frame_opcao_dieta)
        val card_veg: MaterialCardView? = view?.findViewById(R.id.frame_opcao_vegetariano)

        card_carne?.setOnLongClickListener {
            if(!card_carne.isChecked){ mealsViewModel.selectedOption = 1}else{mealsViewModel.selectedOption = 0}
            card_carne.isChecked = !card_carne.isChecked
            card_peixe?.isChecked = false
            card_dieta?.isChecked = false
            card_veg?.isChecked = false
            FLAG_MEAL_CHOSEN = !FLAG_MEAL_CHOSEN
            println("Selected option: " + mealsViewModel.selectedOption)
            true
        }


        card_peixe?.setOnLongClickListener {
            if(!card_peixe.isChecked){ mealsViewModel.selectedOption = 2}else{mealsViewModel.selectedOption = 0}
            card_peixe.isChecked = !card_peixe.isChecked
            card_carne?.isChecked = false
            card_dieta?.isChecked = false
            card_veg?.isChecked = false
            FLAG_MEAL_CHOSEN = !FLAG_MEAL_CHOSEN
            println("Selected option: " + mealsViewModel.selectedOption)
            true
        }

        card_dieta?.setOnLongClickListener {
            if(!card_dieta.isChecked){ mealsViewModel.selectedOption = 3}else{mealsViewModel.selectedOption = 0}
            card_dieta.isChecked = !card_dieta.isChecked
            card_carne?.isChecked = false
            card_peixe?.isChecked = false
            card_veg?.isChecked = false
            FLAG_MEAL_CHOSEN = !FLAG_MEAL_CHOSEN
            println("Selected option: " + mealsViewModel.selectedOption)
            true
        }

        card_veg?.setOnLongClickListener {
            if(!card_veg.isChecked){ mealsViewModel.selectedOption = 4 }else{mealsViewModel.selectedOption = 0}
            card_veg.isChecked = !card_veg.isChecked
            card_carne?.isChecked = false
            card_peixe?.isChecked = false
            card_dieta?.isChecked = false
            FLAG_MEAL_CHOSEN = !FLAG_MEAL_CHOSEN
            println("Selected option: " + mealsViewModel.selectedOption)
            true
        }

        button_confirm_meal.setOnClickListener(){
            if (mealsViewModel.selectedOption != 0) {
                view?.findViewById<TextView>(R.id.success_text)?.text = "Refeição registada com sucesso!"
                view?.findViewById<View>(R.id.successLayout)?.visibility = View.VISIBLE
                button_ok.setOnClickListener(){
                    view?.findViewById<View>(R.id.successLayout)?.visibility = View.INVISIBLE
                }
            } else {
                view?.findViewById<View>(R.id.aviso_nenhuma_refeicao_checked)?.visibility = View.VISIBLE
            }
        }



    }

}