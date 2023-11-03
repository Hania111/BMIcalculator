package com.example.myapplication2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class history : AppCompatActivity() {

    private lateinit var historrRV : RecyclerView
    private lateinit var returnButton: Button

    private val PREFS_NAME = "bmi_prefs"
    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val historyList = getHistoryFromPreferences()

        historrRV = findViewById<RecyclerView>(R.id.historyRV)
        historrRV.layoutManager = LinearLayoutManager(this)
        historrRV.adapter = MyAdapter(historyList)

        returnButton = findViewById<Button>(R.id.buttonReturn)
        returnButton.setOnClickListener {
            openMainActivity()
        }
    }

    private fun getHistoryFromPreferences(): List<String> {
        return sharedPref.getStringSet("history", emptySet())?.toList() ?: emptyList()
    }

    public fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }




}

class MyAdapter(private val items: List<String>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // ViewHolder definiuje elementy widoku dla jednego elementu w RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewItem)
    }

    // Tworzy nowe widoki (wywoływane przez LayoutManager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    // Zastępuje zawartość widoku (wywoływane przez LayoutManager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    // Zwraca rozmiar danych (wywoływane przez LayoutManager)
    override fun getItemCount() = items.size
}
