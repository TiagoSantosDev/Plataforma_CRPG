package com.plataforma.crpg.ui.meals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.MealsFragmentBinding
import com.plataforma.crpg.model.Meal


class MealsFragment : Fragment() {

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mealsViewModel = ViewModelProvider(this).get(MealsViewModel::class.java)

        mealsViewModel.testDB()

        val card_carne: MaterialCardView? = view?.findViewById(R.id.frame_opcao_carne)
        //val card_peixe: MaterialCardView? = view?.findViewById(R.id.frame_opcao_peixe)
        //val card_dieta: MaterialCardView? = view?.findViewById(R.id.frame_opcao_dieta)
        //val card_veg: MaterialCardView? = view?.findViewById(R.id.vegetariano)

        card_carne?.setOnLongClickListener {
            card_carne.isChecked = !card_carne.isChecked
            mealsViewModel.selectedOption = 1
            true
        }

        /*
        card_peixe?.setOnLongClickListener {
            card_peixe.isChecked = !card_peixe.isChecked
            mealsViewModel.selectedOption = 2
            true
        }

        card_carne?.setOnLongClickListener {
            card_carne.isChecked = !card_carne.isChecked
            mealsViewModel.selectedOption = 3
            true
        }

        card_carne?.setOnLongClickListener {
            card_carne.isChecked = !card_carne.isChecked
            mealsViewModel.selectedOption = 4
            true
        }*/



    }

}