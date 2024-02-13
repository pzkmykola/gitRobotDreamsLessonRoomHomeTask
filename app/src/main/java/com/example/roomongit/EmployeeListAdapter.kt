package com.example.roomongit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomongit.db.Employee

class EmployeeListAdapter(var items:List<Employee> = emptyList()): RecyclerView.Adapter<EmployeeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val listItemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return EmployeeViewHolder(listItemView)
    }
    fun updateItems(itemsToUpdate:List<Employee>){
        items = itemsToUpdate
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.name.text = items[position].name
        holder.position.text = items[position].position
    }
}

class EmployeeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.name)
    val position: TextView = itemView.findViewById(R.id.position)
}