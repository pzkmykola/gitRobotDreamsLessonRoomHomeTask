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
import com.example.roomongit.databinding.ListItemLayoutBinding
import com.example.roomongit.dbnew.TodoFB

class TodoListAdapter(var items:List<TodoFB> = emptyList(), val doneSet: ((TodoFB) -> Unit)? = null): RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = ListItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return TodoViewHolder(view)
    }

    fun updateItems(itemsToUpdate:List<TodoFB>){
        items = itemsToUpdate
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }


    inner class TodoViewHolder(private val binding:ListItemLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        private val image: ImageView = binding.photo
        fun bind(todo: TodoFB) {
            binding.title.text = todo.title
            binding.note.text = todo.note
            binding.date.text = todo.date
            binding.rootLayout.setOnClickListener {
                if (!binding.checkBox.isChecked) {
                    binding.checkBox.isChecked = true
                    binding.apply {
                        image.setImageResource(R.mipmap.todocheck)
                    }
                } else {
                    binding.checkBox.isChecked = false
                    binding.apply {
                        image.setImageResource(R.drawable.todo_list_hand_icon)
                    }
                }
                binding.checkBox.isChecked = todo.completed
            }
        }
    }
}


