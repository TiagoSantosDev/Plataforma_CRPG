package com.plataforma.crpg.ui.transports

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.plataforma.crpg.model.Meal

class TransportsRepository {

    private val TAG = "MealsRepository"
    private lateinit var database: DatabaseReference

    fun initializeDbRef() {
        //referencia da base de dados esta errada
        database = Firebase.database.reference
        println(">Reference: $database")
        //database = Firebase.database.getReferenceFromUrl("https://crpg-1a3d5-default-rtdb.europe-west1.firebasedatabase.app/")
        println("> Firebase Database initialized ")
    }

    fun writeNewMeal(data: String, carne: String, peixe: String, dieta: String, vegetariano: String) {
        //val meal = Meal(data,carne,peixe,dieta,vegetariano)
        val meal = Meal("01042021","bife","atum","massa","salada")
        database.child("meals").child(data).setValue(meal)
    }


    fun writeNewMealWithTaskListeners(data: String, carne: String, peixe: String, dieta: String, vegetariano: String) {
        //val meal = Meal(data,carne,peixe,dieta,vegetariano)
        val meal = Meal("01042021","bife","atum","massa","salada")

        database.child("meals").child(data).setValue(meal)
                .addOnSuccessListener {
                    println("Write was successful!")
                }
                .addOnFailureListener {
                    println("Error during write to DB :(")
                }

    }

    fun addPostEventListener() {

        print("> Started addPostEventListener")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue<Meal>()
                println("Valor post: $post")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)
    }

}



/*
    fun getMealFromSpecificDate(data: String,snapshot: DataSnapshot){

        /*
        database.child("meals").child(data).get().then(function(snapshot) {
            if (snapshot.exists()) {
                console.log(snapshot.val());
            }
            else {
                console.log("No data available");
            }
        }).catch(function(error) {
            console.error(error);
        });
        */




/*
    private fun writeNewPost(userId: String, username: String, title: String, body: String) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        val key = database.child("posts").push().key
        if (key == null) {
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }

        val post = Post(userId, username, title, body)
        val postValues = post.toMap()

        val childUpdates = hashMapOf<String, Any>(
                "/posts/$key" to postValues,
                "/user-posts/$userId/$key" to postValues
        )

        database.updateChildren(childUpdates)
    }
    // [END write_fan_out]

}*/