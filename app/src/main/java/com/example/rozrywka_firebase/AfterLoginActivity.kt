package com.example.rozrywka_firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AfterLoginActivity: AppCompatActivity(){

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var user_email = ""
    lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var listOfBook: MutableList<Book>

    var flagActivity = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)
        flagActivity = 1

        recyclerView = findViewById(R.id.recyclerView_books)

        user_email = intent.getStringExtra("EMAIL_NAME")!!
        val login = user_email.split("@",".")

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.myToolbar)
        setSupportActionBar(toolbar)
        //getSupportActionBar()?.setTitle(user_email);
        getSupportActionBar()?.setTitle("Already seen");

        val firebase = FirebaseDatabase.getInstance()
        databaseReference = firebase.getReference(login[0] + login[1] + login[2])
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)


        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError){
                Toast.makeText(applicationContext, "Database Error", Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(dataSnapshot: DataSnapshot){
                listOfBook = ArrayList()

                for(row in dataSnapshot.children){
                    val newRow = row.getValue(Book::class.java)
                    listOfBook.add(newRow!!)
                }
                setupAdapter(listOfBook)
            }
        })

        Log.e("", firebaseAuth.toString())
    }

    private fun setupAdapter(mutableData:MutableList<Book>){
        recyclerView.adapter = Adapter(mutableData, this)
    }


    //Menu options
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;
        //Logout button clicked on the top bar
        if (id == R.id.logout_action){
            firebaseAuth.signOut()
            firebaseAuth.addAuthStateListener {
                if(firebaseAuth.currentUser == null){
                    finish()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            return true
        }

        //Add button clicked on the top bar
        else if(id == R.id.add_action){
            // var intent = Intent(this, AddActivity::class.java)
            finish()
            val intent = Intent(this, AddActivity::class.java).apply {

                putExtra("EMAIL_NAME", user_email)
            }

            startActivity(intent)
            return true
        }

        //Already seen button option on the top bar in the menu option
        else{
            if(flagActivity != 1){
                finish()
                val intent = Intent(this, AfterLoginActivity::class.java)
                startActivity(intent)
                return true
            }
            else{
                Toast.makeText(applicationContext, "You are on this page", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}