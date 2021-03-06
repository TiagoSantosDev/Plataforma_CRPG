package com.plataforma.crpg.ui.meals

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.databinding.MealsFragmentBinding
import com.plataforma.crpg.ui.MainActivity
import com.plataforma.crpg.ui.agenda.AgendaFragment
import com.plataforma.crpg.ui.agenda.SharedViewModel
import kotlinx.android.synthetic.main.meals_fragment.*
import kotlinx.android.synthetic.main.reminder_activity_success.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*
import kotlin.properties.Delegates


class MealsFragment : Fragment() {

    private var textToSpeech: TextToSpeech? = null
    private var flagMealChosen = false

    private val myLocale = Locale("pt_PT", "POR")
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()

    companion object {
        fun newInstance() = MealsFragment()
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("mealsHasRun", true).apply()

        handler.removeCallbacksAndMessages(null)

        if(handler.hasMessages(0)) {
            handler.removeCallbacks(runnable)
            println("meditation SR shutdown")
        }


        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to shutdown!
        //DEPOIS DE TESTAR NOTIFICACOES TIRAR OS COMENTARIOS DAQUI
        //handler.removeCallbacks(runnable)

        handler.removeCallbacksAndMessages(null)

        if(handler.hasMessages(0)) {
            handler.removeCallbacks(runnable)
            println("meditation SR shutdown")
        }


        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }

    }



    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "REFEI????ES"
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)

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

        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("mealsHasRun", false)

        defineModality(ttsFlag, srFlag, hasRun)

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
                    handler.removeCallbacksAndMessages(null)
                    if(handler.hasMessages(0)) {
                        handler.removeCallbacks(runnable)
                        println("meditation SR shutdown")
                    }
                }
            } else {
                nothingCheckedWarning?.visibility = View.VISIBLE
            }
        }

    }

    private fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun performActionWithVoiceCommand(command: String){
        when {
            command.contains("Carne", true)
                    || (command.contains("Prato", true)
                    && (command.contains("Carne", true))) -> frame_opcao_carne.performClick()
            command.contains("Peixe", true)
                    || (command.contains("Prato", true)
                    && (command.contains("Peixe", true))) -> frame_opcao_peixe.performClick()
            command.contains("Dieta", true)
                    || (command.contains("Prato", true)
                    && (command.contains("Dieta", true)))-> frame_opcao_dieta.performClick()
            command.contains("Vegetariana", true) || command.contains("Vegetariano", true)
                    || (command.contains("Prato", true)
                    && (command.contains("Vegetariano", true))) -> frame_opcao_vegetariano.performClick()
            command.contains("Guardar", true) -> button_confirm_meal.performClick()
        }
    }

    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {

        println("ttsFlag:  " + ttsFlag)
        println("srFlag: " + srFlag)
        println("hasRun: " + hasRun)

        if (!hasRun){
            when{
                ttsFlag && !srFlag -> { startTTS() }
                !ttsFlag && srFlag -> { startVoiceRecognition() }
                ttsFlag && srFlag ->{ multimodalOption() }
            }
        }

        if(hasRun){
            when{
                !ttsFlag && srFlag -> { startVoiceRecognition() }
                ttsFlag && srFlag ->{ startVoiceRecognition() }
            }
        }

    }

    private fun startTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                        || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Linguagem n??o suportada!")
                }
                val speechStatus = textToSpeech!!.speak("Diga Carne, Peixe, Dieta ou Vegetariano " +
                        "para selecionar o seu prato e depois diga confirmar para selecionar a sua op????o", TextToSpeech.QUEUE_FLUSH, null, "ID")
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun multimodalOption() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                        || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language is not supported!")
                } else {
                    Log.i("TTS", "Language Supported.")
                }
                Log.i("TTS", "Initialization success.")

                val speechListener = object : UtteranceProgressListener() {
                    @Override
                    override fun onStart(p0: String?) {
                        println("Iniciou TTS")
                    }

                    override fun onDone(p0: String?) {
                        println("Encerrou TTS")
                        if(activity != null && isAdded) {
                            startVoiceRecognition()
                        }
                    }

                    override fun onError(p0: String?) {
                        TODO("Not yet implemented")
                    }
                }

                textToSpeech?.setOnUtteranceProgressListener(speechListener)

                val speechStatus = textToSpeech!!.speak("Diga Carne, Peixe, Dieta ou Vegetariano para selecionar o seu prato e depois diga confirmar para selecionar a sua op????o", TextToSpeech.QUEUE_FLUSH, null, "ID")

            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun startVoiceRecognition(){
        //MANTER WIFI SEMPRE LIGADO
        //val handler = Handler(Looper.getMainLooper())
        if(isAdded && isVisible && getUserVisibleHint()) {
            runnable = Runnable {
                handler.sendEmptyMessage(0);
                Speech.init(requireActivity())
                //hasInitSR = true
                try {
                    Speech.getInstance().startListening(object : SpeechDelegate {
                        override fun onStartOfSpeech() {
                            Log.i("speech", "meal speech recognition is now active")
                        }

                        override fun onSpeechRmsChanged(value: Float) {
                            Log.d("speech", "rms is now: $value")
                        }

                        override fun onSpeechPartialResults(results: List<String>) {
                            val str = StringBuilder()
                            for (res in results) {
                                str.append(res).append(" ")
                            }
                            performActionWithVoiceCommand(results.toString())
                            Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                        }

                        override fun onSpeechResult(result: String) {
                            Log.d(TimelineView.TAG, "onSpeechResult: " + result.toLowerCase())
                            //Speech.getInstance().stopTextToSpeech()
                            val handler = Handler()
                            if (activity != null && isAdded) {
                                handler.postDelayed({
                                    try {
                                        if (isAdded && isVisible && getUserVisibleHint()) {
                                            Speech.init(requireActivity())
                                            //hasInitSR = true
                                            Speech.getInstance().startListening(this)
                                        }
                                    } catch (speechRecognitionNotAvailable: SpeechRecognitionNotAvailable) {
                                        speechRecognitionNotAvailable.printStackTrace()
                                    } catch (e: GoogleVoiceTypingDisabledException) {
                                        e.printStackTrace()
                                    }
                                }, 100)
                            }
                        }
                    })
                } catch (exc: SpeechRecognitionNotAvailable) {
                    Log.e("speech", "Speech recognition is not available on this device!")
                } catch (exc: GoogleVoiceTypingDisabledException) {
                    Log.e("speech", "Google voice typing must be enabled!")
                }
            }

            handler.post(runnable)
        }


    }


}


/*
Speech.init(context)
println("Current language: " + Speech.getInstance().speechToTextLanguage)

val handler = Handler(Looper.getMainLooper())
val runable = Runnable {
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
handler.post(runable)

 */
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
 //view?.findViewById<TextView>(R.id.success_text)?.text = "Refei????o registada com sucesso!"
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