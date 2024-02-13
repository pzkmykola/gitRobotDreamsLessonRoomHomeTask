package com.example.roomongit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomongit.db.Todo

class EmployeeListAdapter(var items:List<Todo> = emptyList()): RecyclerView.Adapter<TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val listItemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return TodoViewHolder(listItemView)
    }
    fun updateItems(itemsToUpdate:List<Todo>){
        items = itemsToUpdate
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.name.text = items[position].task
        holder.position.text = items[position].progress
    }
}

class TodoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.task)
    val position: TextView = itemView.findViewById(R.id.progress)
}