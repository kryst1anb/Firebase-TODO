package com.example.rozrywka_firebase

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var databaseReference: DatabaseReference
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.myToolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setTitle("Edit");

        email = intent?.getStringExtra("EMAIL_NAME")!!
        val login = email.split("@",".")

        val mTitle = intent.getStringExtra("title")
        val mAuthor = intent.getStringExtra("author")
        val mISBN = intent.getStringExtra("isbn")
        val mYear = intent.getStringExtra("year")
        val mPages = intent.getStringExtra("pages")
        val mLanguage = intent.getStringExtra("language")

        details_name.setText(mTitle)
        details_author.setText(mAuthor)
        details_isbn.setText(mISBN)
        details_year.setText(mYear)
        details_pages.setText(mPages)
        details_language.setText(mLanguage)

        val firebase = FirebaseDatabase.getInstance()

        databaseReference = firebase.getReference(login[0] + login[1] + login[2])

        val search: Query = databaseReference.orderByChild("isbn").equalTo(mISBN)
        buttonDeleteEdit.setOnClickListener{
            search.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError){
                    Toast.makeText(applicationContext, "Database Error", Toast.LENGTH_SHORT).show()
                }
                override fun onDataChange(dataSnapshot: DataSnapshot){
                    for (record in dataSnapshot.children) {
                        record.ref.removeValue()
                    }
                    Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;
        if (id == R.id.logout_action){
            firebaseAuth.signOut()
            firebaseAuth.addAuthStateListener {
                if(firebaseAuth.currentUser == null){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            return true
        }

        else if(id == R.id.add_action){
            val intent = Intent(this, AddActivity::class.java).apply{
                putExtra("EMAIL", email)
            }
            startActivity(intent)
            return true
        }
        else{
            val intent = Intent(this, AfterLoginActivity::class.java).apply{
                putExtra("EMAIL", email)
            }
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
