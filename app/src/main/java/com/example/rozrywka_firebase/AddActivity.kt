package com.example.rozrywka_firebase

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity: AppCompatActivity() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val email = intent?.getStringExtra("EMAIL")!!
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.myToolbar)
        setSupportActionBar(toolbar)

        getSupportActionBar()?.setTitle("Add new book");

        buttonAdd.setOnClickListener {

            val book_Title = bookTitle.text.toString()
            val book_Author = bookAuthor.text.toString()
            val book_ISBN = bookISBN.text.toString()
            val book_Year = bookYear.text.toString()
            val book_Pages = bookPages.text.toString()
            val book_Language = bookLanguage.text.toString()
            val book = Book(book_Title, book_Author, book_ISBN, book_Year,book_Pages,book_Language)

            val login = email.split("@", ".")

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(login[0] + login[1] + login[2])
            if(book_Title != "" && book_Title.isNotEmpty()){
                if(book_Author != "" && book_Author.isNotEmpty()){
                    if(book_ISBN != "" && book_ISBN.isNotEmpty() && (book_ISBN.length == 10 || book_ISBN.length == 13)){
                        if(book_Year != "" && book_Year.isNotEmpty()){
                            if(book_Pages != "" && book_Title.isNotEmpty() && book_Pages.toInt() > 0){
                                if(book_Language != "" && book_Language.isNotEmpty()){

                                    myRef.push().setValue(book)
                                    bookTitle.text?.clear()
                                    bookAuthor.text?.clear()
                                    bookISBN.text?.clear()
                                    bookYear.text?.clear()
                                    bookPages.text?.clear()
                                    bookLanguage.text?.clear()
                                    Toast.makeText(applicationContext, "Book added", Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    Toast.makeText(applicationContext, "LANGUAGE - Wrong format", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                Toast.makeText(applicationContext, "PAGES - Wrong format", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(applicationContext, "YEAR - Wrong format", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(applicationContext, "ISBN - Wrong format", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(applicationContext, "AUTHOR - Wrong format", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(applicationContext, "TITLE - Wrong format", Toast.LENGTH_SHORT).show()
            }
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
