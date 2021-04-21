package com.plataforma.crpg.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.plataforma.crpg.R
import java.util.*


class MainActivity : /*BaseActivity()*/ AppCompatActivity() {

    lateinit var dLocale: Locale

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       //setContentViewWithoutInject(R.layout.activity_main)
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

    }

    override fun onSupportNavigateUp(): Boolean {
        println(">OnSupportNavigate called")
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() /*|| super.onSupportNavigateUp()*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home ->{ onBackPressedDispatcher.onBackPressed()
                println(">Back button pressed")
                onSupportNavigateUp()} // click on 'up' button in the action bar, handle it here
            else -> super.onOptionsItemSelected(item)
        }
    }

}


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