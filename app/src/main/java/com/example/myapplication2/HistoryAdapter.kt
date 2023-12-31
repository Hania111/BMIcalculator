package com.example.myapplication2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class HistoryAdapter (private val BmiList : List<BmiRecord>) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val weightTV : TextView = itemView.findViewById<TextView>(R.id.weightTv)
        val heightTV : TextView = itemView.findViewById<TextView>(R.id.heightTV)
        val bmiTV : TextView = itemView.findViewById<TextView>(R.id.bmiTV)
        val bmiDateTV : TextView = itemView.findViewById<TextView>(R.id.bmiDateTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent,
            false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return BmiList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = BmiList[position]
        val context = holder.itemView.context
        holder.weightTV.text = context.getString(R.string.weight_text, currentItem.weight.toString(), currentItem.weightUnit)
        holder.heightTV.text = context.getString(R.string.height_text, currentItem.height.toString(), currentItem.heightUnit)
        holder.bmiTV.text = context.getString(R.string.bmi_text, currentItem.bmi.toString())
        holder.bmiDateTV.text = currentItem.date
    }
}


