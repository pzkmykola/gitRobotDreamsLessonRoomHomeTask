package com.example.roomongit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.roomongit.dbnew.TodoNew

class TodoListAdapter(var items:List<TodoNew> = emptyList()): RecyclerView.Adapter<TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val listItemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return TodoViewHolder(listItemView)
    }
    fun updateItems(itemsToUpdate:List<TodoNew>){
        items = itemsToUpdate
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.note.text = items[position].note
        holder.date.text = items[position].date
        holder.rootLayout.setOnClickListener {
            if (!holder.checkbox.isChecked) {
                holder.image.setImageResource(R.mipmap.todocheck)
                holder.checkbox.isChecked = true
                //holder.itemView.setTag(1)
            } else {
                holder.image.setImageResource(R.drawable.todo_list_hand_icon)
                holder.checkbox.isChecked = false
                //holder.itemView.setTag(2)
            }
            /*Toast.makeText(
                holder.itemView.context,
                "${items[position].task} check is ${holder.itemView.tag}",
                Toast.LENGTH_SHORT
            ).show()*/
        }
    }
}

class TodoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.title)
    val note: TextView = itemView.findViewById(R.id.note)
    val date: TextView = itemView.findViewById(R.id.date)
    val rootLayout: ConstraintLayout = itemView.findViewById(R.id.rootLayout)
    val checkbox: CheckBox = itemView.findViewById(R.id.checkBox)
    val image: ImageView = itemView.findViewById(R.id.photo)
}