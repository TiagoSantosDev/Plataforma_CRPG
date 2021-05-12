package com.plataforma.crpg.ui.meals

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.meals_fragment.*
import kotlinx.android.synthetic.main.reminder_activity_success.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable


class MealsFragment : Fragment() {

    private var flagMealChosen = false

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
    ): View {

        val binding = MealsFragmentBinding.inflate(layoutInflater)
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

        return view
        //return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mealsViewModel = ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)

        text_opcao_carne.text = mealsViewModel.retrievedMeal.carne
        text_opcao_peixe.text = mealsViewModel.retrievedMeal.peixe
        text_opcao_dieta.text = mealsViewModel.retrievedMeal.dieta
        text_opcao_vegetariano.text = mealsViewModel.retrievedMeal.vegetariano

    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mealsViewModel = ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)
        val sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)

        val cardCarne: MaterialCardView? = view?.findViewById(R.id.frame_opcao_carne)
        val cardPeixe: MaterialCardView? = view?.findViewById(R.id.frame_opcao_peixe)
        val cardDieta: MaterialCardView? = view?.findViewById(R.id.frame_opcao_dieta)
        val cardVeg: MaterialCardView? = view?.findViewById(R.id.frame_opcao_vegetariano)

        val isLunch = requireArguments().getBoolean("isLunch")

        cardCarne?.setOnClickListener {

            if (!cardCarne.isChecked) {
                mealsViewModel.selectedOption = 1
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardCarne.isChecked = !cardCarne.isChecked
            cardPeixe?.isChecked = false
            cardDieta?.isChecked = false
            cardVeg?.isChecked = false
            flagMealChosen = !flagMealChosen
        }

        cardPeixe?.setOnClickListener {

            if (!cardPeixe.isChecked) {
                mealsViewModel.selectedOption = 2
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardPeixe.isChecked = !cardPeixe.isChecked
            cardCarne?.isChecked = false
            cardDieta?.isChecked = false
            cardVeg?.isChecked = false
            flagMealChosen = !flagMealChosen
        }

        cardDieta?.setOnClickListener {

            if (!cardDieta.isChecked) {
                mealsViewModel.selectedOption = 3
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardDieta.isChecked = !cardDieta.isChecked
            cardCarne?.isChecked = false
            cardPeixe?.isChecked = false
            cardVeg?.isChecked = false
            flagMealChosen = !flagMealChosen
        }

        cardVeg?.setOnClickListener {

            if (!cardVeg.isChecked) {
                mealsViewModel.selectedOption = 4
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardVeg.isChecked = !cardVeg.isChecked
            cardCarne?.isChecked = false
            cardPeixe?.isChecked = false
            cardDieta?.isChecked = false
            flagMealChosen = !flagMealChosen
        }

        val mealSuccessView = view?.findViewById<View>(R.id.meal_choice_success)
        val nothingCheckedWarning = view?.findViewById<View>(R.id.aviso_nenhuma_refeicao_checked)

        button_confirm_meal.setOnClickListener {
            if (mealsViewModel.selectedOption != 0) {
                mealSuccessView?.visibility = View.VISIBLE
                mealSuccessView?.bringToFront()
                nothingCheckedWarning?.visibility = View.GONE
                mealsViewModel.updateMealChoiceOnLocalStorage(sharedViewModel.selectedDate, mealsViewModel.selectedOption, isLunch)
                button_ok.setOnClickListener {
                    mealSuccessView?.visibility = View.GONE
                }
            } else {
                nothingCheckedWarning?.visibility = View.VISIBLE
            }
        }

        fun performActionWithVoiceCommand(command: String){
            when {
                command.contains("Carne", true)
                        || (command.contains("Prato", true)
                        && (command.contains("Carne", true))) -> cardCarne?.performClick()
                command.contains("Peixe", true)
                        || (command.contains("Prato", true)
                        && (command.contains("Peixe", true))) -> cardPeixe?.performClick()
                command.contains("Dieta", true)
                        || (command.contains("Prato", true)
                        && (command.contains("Dieta", true)))-> cardDieta?.performClick()
                command.contains("Vegetariana", true) || command.contains("Vegetariano", true)
                        || (command.contains("Prato", true)
                        && (command.contains("Vegetariano", true))) -> cardVeg?.performClick()
            }
        }

        Speech.init(context)
        println("Current language: " + Speech.getInstance().speechToTextLanguage)

        try {
            Speech.getInstance().startListening(object : SpeechDelegate {
                override fun onStartOfSpeech() {
                    Log.i("speech", "speech recognition is now active")
                }
                override fun onSpeechRmsChanged(value: Float) {
                    Log.d("speech", "rms is now: $value")
                }
                override fun onSpeechPartialResults(results: List<String>) {
                    val str = StringBuilder()
                    for (res in results) {
                        str.append(res).append(" ")
                    }
                    Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                }

                override fun onSpeechResult(result: String) {
                    performActionWithVoiceCommand(result)
                    Log.i("speech", "result: $result")
                }
            })
        } catch (exc: SpeechRecognitionNotAvailable) {
            Log.e("speech", "Speech recognition is not available on this device!")
        } catch (exc: GoogleVoiceTypingDisabledException) {
            Log.e("speech", "Google voice typing must be enabled!")
        }

    }

    private fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

}

/*
//cardVeg?.setOnLongClickListener {
//cardPeixe?.setOnLongClickListener {
//cardDieta?.setOnLongClickListener {
        //println("Opcao carne text: " + text_opcao_carne.text)
        //text_opcao_carne.bringToFront()
//println("Selected option: " + mealsViewModel.selectedOption)
        view?.findViewById<AppCompatTextView>(R.id.text_opcao_carne)?.text = mealsViewModel.retrievedMeal.carne

        println("Opcao carne text: " + view?.findViewById<AppCompatTextView>(R.id.text_opcao_carne)?.text)

        view?.findViewById<AppCompatTextView>(R.id.text_opcao_carne)?.bringToFront()
        view?.findViewById<AppCompatTextView>(R.id.text_opcao_peixe)?.text = mealsViewModel.retrievedMeal.peixe
        view?.findViewById<AppCompatTextView>(R.id.text_opcao_dieta)?.text = mealsViewModel.retrievedMeal.dieta
        view?.findViewById<AppCompatTextView>(R.id.text_opcao_vegetariano)?.text = mealsViewModel.retrievedMeal.vegetariano
*/
//cardCarne?.setOnLongClickListener {
//mealsViewModel.testDB()
//val mealsViewModel = ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)
//println("Selected option: " + mealsViewModel.selectedOption)
//println("Selected option: " + mealsViewModel.selectedOption)
//println("Selected option: " + mealsViewModel.selectedOption)
//println("> Bundle isLunch: $isLunch")
//view?.findViewById<View>(R.id.frag)?.visibility = View.GONE
//view?.visibility = View.VISIBLE
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

//println("Prato carne:" + mealsViewModel.retrievedMeal.carne)
//println("Meal selected date: " + sharedViewModel.selectedDate)
//println("""On Create View foods: ${mealsViewModel.retrievedMeal.carne}, ${mealsViewModel.retrievedMeal.peixe},
//    |${mealsViewModel.retrievedMeal.dieta}, ${mealsViewModel.retrievedMeal.vegetariano}""".trimMargin())
// println("""On Create View foods: ${mealsViewModel.retrievedMeal.carne}, ${mealsViewModel.retrievedMeal.peixe},
//        //    |${mealsViewModel.retrievedMeal.dieta}, ${mealsViewModel.retrievedMeal.vegetariano}""".trimMargin())
//
// println("IHDIDHIDHID: " + mealsViewModel.retrievedMeal.carne)
//
//       //error here
//        /*
//       view.findViewById<AppCompatTextView>(R.id.text_opcao_carne)?.text = mealsViewModel.retrievedMeal.carne
//        text_opcao_carne.text = mealsViewModel.retrievedMeal.carne
//
//        println("Opcao carne text: " + view.findViewById<AppCompatTextView>(R.id.text_opcao_carne)?.text)
//
//        text_opcao_carne.bringToFront()
//        view.findViewById<AppCompatTextView>(R.id.text_opcao_carne)?.bringToFront()
//
//       view.findViewById<AppCompatTextView>(R.id.text_opcao_peixe)?.text = mealsViewModel.retrievedMeal.peixe
//       view.findViewById<AppCompatTextView>(R.id.text_opcao_dieta)?.text = mealsViewModel.retrievedMeal.dieta
//       view.findViewById<AppCompatTextView>(R.id.text_opcao_vegetariano)?.text = mealsViewModel.retrievedMeal.vegetariano        fun selectMeatOption(){
//
//            if (cardCarne != null) {
//                if (!cardCarne.isChecked) {
//                    mealsViewModel.selectedOption = 1
//                } else {
//                    mealsViewModel.selectedOption = 0
//                }
//            }
//            cardCarne?.isChecked = !cardCarne?.isChecked!!
//            cardPeixe?.isChecked = false
//            cardDieta?.isChecked = false
//            cardVeg?.isChecked = false
//            flagMealChosen = !flagMealChosen
//        }
//
//        fun selectFishOption(){
//            if (!cardPeixe!!.isChecked) {
//                mealsViewModel.selectedOption = 2
//            } else {
//                mealsViewModel.selectedOption = 0
//            }
//            cardPeixe.isChecked = !cardPeixe.isChecked
//            cardCarne?.isChecked = false
//            cardDieta?.isChecked = false
//            cardVeg?.isChecked = false
//            flagMealChosen = !flagMealChosen
//        }
//
//        fun selectDietOption(){
//            if (!cardDieta!!.isChecked) {
//                mealsViewModel.selectedOption = 3
//            } else {
//                mealsViewModel.selectedOption = 0
//            }
//            cardDieta.isChecked = !cardDieta.isChecked
//            cardCarne?.isChecked = false
//            cardPeixe?.isChecked = false
//            cardVeg?.isChecked = false
//            flagMealChosen = !flagMealChosen
//        }
//
//        fun selectVegOption(){
//            if (!cardVeg?.isChecked!!) {
//                mealsViewModel.selectedOption = 4
//            } else {
//                mealsViewModel.selectedOption = 0
//            }
//            cardVeg.isChecked = !cardVeg.isChecked
//            cardCarne?.isChecked = false
//            cardPeixe?.isChecked = false
//            cardDieta?.isChecked = false
//            flagMealChosen = !flagMealChosen
//        }