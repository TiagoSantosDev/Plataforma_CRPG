package com.plataforma.crpg.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.plataforma.crpg.R
import com.plataforma.crpg.services.NotificationsHandler
import com.plataforma.crpg.services.Notifier
import com.plataforma.crpg.ui.meals.MealsViewModel
import com.plataforma.crpg.ui.meditation.MeditationMediaPlayerFragment
import com.plataforma.crpg.ui.transports.TransportsSelectionFragment
import kotlinx.android.synthetic.main.activity_main.*
import net.gotev.speech.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {

    private var textToSpeech: TextToSpeech? = null
    private var ttsFlag = false

    private val LOG_TAG = MainActivity::class.java.simpleName
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    val myLocale = Locale("pt_PT", "POR")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resetSharedPreferences()

        requestMealDataForNotification()

        requestMultiModalityOptions()
        //checkUserPermissions()
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.navigation_escolha_data, R.id.navigation_agenda, R.id.navigation_reminders,
                        R.id.navigation_transports, R.id.navigation_meals, R.id.navigation_notes
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val notifier = Notifier(this)
        notifier.sendNotification("ola", "")
        startService(intent)

        val intent = Intent(this, NotificationsHandler::class.java)

        //launchNotification()

    }

    private fun launchNotification() {
        // Create an explicit intent for an Activity in your app
        //Esta a ser corrido mesmo quando o utilizador nao clicou na notificacao
        val intent = Intent(this, MainActivity::class.java).apply {
            println("Entrou no intent")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            val fragment: Fragment = TransportsSelectionFragment()
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
            mNotificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_notification_bus)
                .setContentTitle("Não se esqueça de apanhar o transporte!")
                .setContentText("Clique aqui para abrir a aplicação e ver os horários!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            println("Entrou no Notification Manager Compat")
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }

    private fun requestMealDataForNotification() {
        val mealsViewModel = ViewModelProvider(this).get(MealsViewModel::class.java)

        val dish = mealsViewModel.fetchMealChoiceOnLocalStorage()

        if(dish.isNullOrEmpty()){
            startMealSelectNotification()
        }else{
            startMealRemindNotification(dish)
        }

    }

    private fun startMealRemindNotification(dish: String) {

    }

    private fun startMealSelectNotification() {

    }


    private fun resetSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("meditationHasRun", false).apply()
        editor.putBoolean("notesHasRun", false).apply()
        editor.putBoolean("selectionHasRun", false).apply()
        editor.putBoolean("transportsHasRun", false).apply()
        editor.putBoolean("remindersHasRun", false).apply()
        editor.putBoolean("agendaHasRun", false).apply()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu, this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun requestMultiModalityOptions() {
        val sharedPreferences = getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        MaterialAlertDialogBuilder(this, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Permitir Sugestões de Áudio")
                .setMessage("A aplicação possui uma voz virtual que poder dar-lhe indicações de como" +
                        "utilizar a plataforma. Prima o botão \"Permitir\" para ativar esta funcionalidade.")
                .setPositiveButton("Permitir") { dialog, which ->
                    editor.putBoolean("TTS", true).apply()
                }
                .setNegativeButton("Recusar"){ dialog, which ->
                    editor.putBoolean("TTS", false).apply()
                }.show()

        MaterialAlertDialogBuilder(this, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Permitir Comandos de Voz")
                .setMessage("A aplicação pode ser usada utilizando " +
                        "utilizar a plataforma. Prima o botão \"Permitir\" para ativar esta funcionalidade.")
                .setPositiveButton("Permitir"){ dialog, which ->
                    editor.putBoolean("SR", true)
                }
                .setNegativeButton("Recusar"){ dialog, which ->
                    editor.putBoolean("SR", false)
                }.show()
    }

    fun ttsDatePickerHint(){

        textToSpeech = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                        || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language is not supported!")
                } else {
                    Log.i("TTS", "Language Supported.")
                }
                Log.i("TTS", "Initialization success.")

                if (textToSpeech!!.isSpeaking) {
                    ttsFlag = true
                }

                if (!textToSpeech!!.isSpeaking) {
                    ttsFlag = false
                }

                val speechStatus = textToSpeech!!.speak("Por favor selecione um dia movendo os quadrados amarelos para a esquerda e direita e premindo aquele que pretender selecionar", TextToSpeech.QUEUE_FLUSH, null)
            } else {
                Toast.makeText(applicationContext, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // prevent memory leaks when activity is destroyed
        //Speech.getInstance().shutdown()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                onSupportNavigateUp()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shoutDatePickerHint() {
        Speech.getInstance().say("Selecione um dia para ver os seus eventos")
    }

    fun performActionWithVoiceCommand(command: String) {
        when {
            command.contains("Navegar Meditação", true) -> nav_view.selectedItemId = R.id.navigation_meditation
            command.contains("Navegar Notas", true) -> nav_view.selectedItemId = R.id.navigation_notes
            command.contains("Navegar Lembretes", true) -> nav_view.selectedItemId = R.id.navigation_reminders
        }
    }

    private fun onSpeakClick() {
        println("is Speaking: " + Speech.getInstance().isSpeaking)
        println("text to speech voice: " + Speech.getInstance().textToSpeechVoice)

        Speech.getInstance().say("Hello", object : TextToSpeechCallback {
            override fun onStart() {
                Toast.makeText(this@MainActivity, "TTS onStart", Toast.LENGTH_SHORT).show()
            }

            override fun onCompleted() {
                Toast.makeText(this@MainActivity, "TTS onCompleted", Toast.LENGTH_SHORT).show()
            }

            override fun onError() {
                Toast.makeText(this@MainActivity, "TTS onError", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


/*
var isLunch  = false
val sdf = SimpleDateFormat("ddMMyyyy", myLocale)
val currentDate = sdf.format(Date())
val sdfh = SimpleDateFormat("HH", myLocale)
val currentHour = sdfh.format(Date())
var selOption = 0
System.out.println(" C DATE is  $currentDate")
System.out.println(" C HOUR is  $currentHour")

//verificar se esta na hora de mostrar notificacao ao utilizador
if (currentHour.toString().toInt() in 9..12){
    println("entre 9 e 12")
    selOption = mealsViewModel.verifyMealChoiceOnLocalStorage(currentDate, true)
    isLunch = true
}else if(currentHour.toString().toInt() in 14..20){
    println("entre 14 e 20")
    selOption = mealsViewModel.verifyMealChoiceOnLocalStorage(currentDate, false)
}

println("Selected option: $selOption")

*/
//val currentDateTime = LocalDateTime.now()
//val currentDate = currentDateTime.format(DateTimeFormatter.ofPattern("ddMMyyyy"))
//val currentHour = currentDateTime.format(DateTimeFormatter.ofPattern("HH"))
//editor.putBoolean("meditationHasRun", false).apply()
//editor.putBoolean("notesHasRun", false).apply()
/*
    private fun checkUserPermissions(): Boolean {
        editor.putBoolean("isAdmin", true).apply()

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val isStaff = sharedPreferences.getBoolean("isAdmin", false)
        //if (isStaff) onCreateOptionsMenu(toolbar)

        return isStaff
    }
*/
/*
        Speech.init(this, packageName, mTttsInitListener);
        Speech.getInstance().setLocale(myLocale)
        onSpeakClick()
        */

//ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

//Speech.init(applicationContext)
//println("Current language: " + Speech.getInstance().speechToTextLanguage)
//Speech.getInstance().say("Hello")
// shoutDatePickerHint()

/*
        try {
            // you must have android.permission.RECORD_AUDIO granted at this point
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
                    //println("on Speech Result")
                }
            })
        } catch (exc: SpeechRecognitionNotAvailable) {
            Log.e("speech", "Speech recognition is not available on this device!")
            // You can prompt the user if he wants to install Google App to have
            // speech recognition, and then you can simply call:
            //
            // SpeechUtil.redirectUserToGoogleAppOnPlayStore(this);
            //
            // to redirect the user to the Google App page on Play Store
        } catch (exc: GoogleVoiceTypingDisabledException) {
            Log.e("speech", "Google voice typing must be enabled!")
        }*/

/*
     var dks = Dks(application, supportFragmentManager, object: DksListener {
         fun onDksLiveSpeechResult(liveSpeechResult: String) {
             Log.d("DKS", "Speech result - $liveSpeechResult")
         }

         fun onDksFinalSpeechResult(speechResult: String) {
             Log.d("DKS", "Final speech result - $speechResult")
         }

         fun onDksLiveSpeechFrequency(frequency: Float) {
             Log.d("DKS", "frequency - $frequency")
         }

         fun onDksLanguagesAvailable(defaultLanguage: String?, supportedLanguages: ArrayList<String>?) {
             Log.d("DKS", "defaultLanguage - $defaultLanguage")
             Log.d("DKS", "supportedLanguages - $supportedLanguages")
         }

         fun onDksSpeechError(errMsg: String) {
             Log.d("DKS", "errMsg - $errMsg")
         }
     })
     */
//println("Suported languages: " + Speech.getInstance().supportedTextToSpeechVoices.size)
//Speech.init(this, packageName, mTttsInitListener);
//lateinit var dLocale: Locale
//setContentViewWithoutInject(R.layout.activity_main)
/*BaseActivity()*/
//println(">OnSupportNavigate called")
// click on 'up' button in the action bar, handle it here
//println(">Back button pressed")
/*|| super.onSupportNavigateUp()*/
/*//supportActionBar?.setDisplayHomeAsUpEnabled(true)
    override fun onStart() {
        super.onStart()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //Both navigation bar back press and title bar back press will trigger this method
    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
            //supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    fun setActionBarTitle(title: String){
        supportActionBar?.title = title
    }
*/



//Teste a base de dados
/*
val database = FirebaseDatabase.getInstance("https://crpg-1a3d5-default-rtdb.europe-west1.firebasedatabase.app/")
val myRef = database.getReference("message")

myRef.setValue("Hello, World!")

// Read from the database
myRef.addValueEventListener(object : ValueEventListener {
    override fun onDataChange(dataSnapshot: DataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        val value = dataSnapshot.getValue(String::class.java)
        println("Value is: $value")
    }

    override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Log.w("Failed to read value.", error.toException())
    }
})


 override fun onBackPressed() {
    if (fragmentManager.backStackEntryCount > 0) {
        fragmentManager.popBackStack()
    } else {
        super.onBackPressed()
    }
}*/
/*
fun showUpButton() {
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
}

fun hideUpButton() {
    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
}

override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment)
    return navController.navigateUp() || super.onSupportNavigateUp()
}
        dLocale = Locale("pt")
        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        this.applyOverrideConfiguration(configuration)*/


//Speech.getInstance().setLocale(myLocale)
//
//
// /*
//    private val mTttsInitListener = TextToSpeech.OnInitListener { status ->
//        when (status) {
//            TextToSpeech.SUCCESS -> {Logger.info(LOG_TAG, "TextToSpeech engine successfully started")
//            }
//            TextToSpeech.ERROR -> Logger.error(LOG_TAG, "Error while initializing TextToSpeech engine!")
//            else -> Logger.error(LOG_TAG, "Unknown TextToSpeech status: $status")
//        }
//    }*/
//
//    /*
//    private val mTttsInitListener = TextToSpeech.OnInitListener { status ->
//        when (status) {
//            TextToSpeech.SUCCESS -> Logger.info(LOG_TAG, "TextToSpeech engine successfully started")
//            TextToSpeech.ERROR -> Logger.error(LOG_TAG, "Error while initializing TextToSpeech engine!")
//            else -> Logger.error(LOG_TAG, "Unknown TextToSpeech status: $status")
//        }
//    }*/    //ttsDatePickerHint()
//
//        //TER SEMPRE WIFI LIGADO OU A VOICE RECOGNITION NAO FUNCIONA
//
///*
//        Speech.init(applicationContext)
//
//        try {
//            // you must have android.permission.RECORD_AUDIO granted at this point
//            Speech.getInstance().startListening(object : SpeechDelegate {
//                override fun onStartOfSpeech() {
//                    Log.i("speech", "speech recognition is now active")
//                }
//
//                override fun onSpeechRmsChanged(value: Float) {
//                    Log.d("speech", "rms is now: $value")
//                }
//
//                override fun onSpeechPartialResults(results: List<String>) {
//                    val str = StringBuilder()
//                    for (res in results) {
//                        str.append(res).append(" ")
//                    }
//                    Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
//                }
//
//                override fun onSpeechResult(result: String) {
//                    //performActionWithVoiceCommand(result)
//                    Log.i("speech", "result: $result")
//                    //println("on Speech Result")
//                }
//            })
//        } catch (exc: SpeechRecognitionNotAvailable) {
//            Log.e("speech", "Speech recognition is not available on this device!")
//            // You can prompt the user if he wants to install Google App to have
//            // speech recognition, and then you can simply call:
//            //
//            // SpeechUtil.redirectUserToGoogleAppOnPlayStore(this);
//            //
//            // to redirect the user to the Google App page on Play Store
//        } catch (exc: GoogleVoiceTypingDisabledException) {
//            Log.e("speech", "Google voice typing must be enabled!")
//        }
//*/