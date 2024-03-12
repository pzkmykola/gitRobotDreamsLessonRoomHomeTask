package com.example.roomongit

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomongit.databinding.ListItemLayoutBinding
import com.example.roomongit.dbnew.PlaceFB

class PlaceListAdapter(var items:List<PlaceFB> = emptyList()): RecyclerView.Adapter<PlaceListAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = ListItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return TodoViewHolder(view)
    }
    fun updateItems(itemsToUpdate:List<PlaceFB>){
        items = itemsToUpdate
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
    inner class TodoViewHolder(private val binding: ListItemLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        private val image: ImageView = binding.photo
        fun bind(todo: PlaceFB) {
            binding.title.text = todo.title
            binding.note.text = todo.location
            binding.date.text = todo.urlImage
            binding.rootLayout.setOnClickListener {
                if (!binding.checkBox.isChecked) {
                    binding.checkBox.isChecked = true
                    if(todo.location == "") todo.location = "completed"
                    binding.apply {
                        image.setImageResource(R.mipmap.todocheck)
                    }
                } else {
                    binding.checkBox.isChecked = true
                    binding.apply {
                        image.setImageResource(R.drawable.todo_list_hand_icon)
                    }
                }
            }
        }
    }
}

