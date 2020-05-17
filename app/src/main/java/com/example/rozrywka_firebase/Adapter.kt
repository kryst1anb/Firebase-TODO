package com.example.rozrywka_firebase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class Adapter (private var bookList: MutableList<Book>, private val context: Context, var email: String): RecyclerView.Adapter<Adapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val book : Book = bookList[position]

        holder.bookTitle.text = bookList[holder.adapterPosition].title
        holder.bookAuthor.text = bookList[holder.adapterPosition].author
        holder.bookISBN.text = bookList[holder.adapterPosition].isbn
        holder.bookYear.text = bookList[holder.adapterPosition].year
        holder.bookPages.text = bookList[holder.adapterPosition].pages
        holder.bookLanguage.text = bookList[holder.adapterPosition].language

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("EMAIL", email)
            }

            intent.putExtra("position",position)
            intent.putExtra("title", book.title)
            intent.putExtra("author", book.author)
            intent.putExtra("isbn", book.isbn)
            intent.putExtra("year", book.year)
            intent.putExtra("pages", book.pages)
            intent.putExtra("language", book.language)

            ContextCompat.startActivity(context, intent,null)
        }
    }

    class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val bookTitle : TextView = view.findViewById(R.id.BookTitle)
        val bookAuthor : TextView = view.findViewById(R.id.BookAuthor)
        val bookISBN : TextView = view.findViewById(R.id.BookISBN)
        val bookYear : TextView = view.findViewById(R.id.BookYear)
        val bookPages : TextView = view.findViewById(R.id.BookPages)
        val bookLanguage : TextView = view.findViewById(R.id.BookLanguage)

    }
}