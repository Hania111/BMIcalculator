//package com.example.myapplication2
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//class HistoryAdapter(private val records: List<BmiRecord>) :
//    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
//
//    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val bmiTextView: TextView = itemView.findViewById(R.id.historyTV)
//        // Dodaj inne elementy widoku, jeśli potrzebujesz.
//
//        fun bind(record: BmiRecord) {
//            bmiTextView.text = "BMI: ${record.bmi}"
//            // Ustaw inne wartości z rekordu, jeśli potrzebujesz.
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.list_item, parent, false)
//        return HistoryViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
//        holder.bind(records[position])
//    }
//
//    override fun getItemCount() = records.size
//}

package com.example.myapplication2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class HistoryAdapter(private val recordsJson: List<String?>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    // Deserializacja JSON do obiektów BmiRecord
    private val records: List<BmiRecord> = recordsJson.mapNotNull { json ->
        json?.let { Gson().fromJson(it, BmiRecord::class.java) }
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bmiTextView: TextView = itemView.findViewById(R.id.historyTV)
        // Dodaj inne elementy widoku, jeśli potrzebujesz.

        fun bind(record: BmiRecord) {
            bmiTextView.text = "BMI: ${record.bmi}, Weight: ${record.weight}, Height: ${record.height}"
            // Ustaw inne wartości z rekordu, jeśli potrzebujesz.
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(records[position])
    }

    override fun getItemCount() = records.size
}

