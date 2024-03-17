package com.example.roomongit

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roomongit.databinding.ListItemLayoutBinding

class PlaceListAdapter(var items:List<PlaceFB> = emptyList()): RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = ListItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return PlaceViewHolder(view)
    }

    fun updateItems(itemsToUpdate:List<PlaceFB>){
        items = itemsToUpdate
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
    inner class PlaceViewHolder(private val binding: ListItemLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        private val image: ImageView = binding.image
        fun bind(place: PlaceFB) {
            binding.title.text = place.title
            binding.location.text = place.location
            if(place.urlImage != "") {
                Glide.with(this.itemView.context)
                    .load(place.urlImage)
                    .into(image)
            }
            binding.rootLayout.setOnClickListener {
                if (!binding.checkBox.isChecked) {
                    binding.checkBox.isChecked = true
                    binding.apply {
                        image.setImageResource(R.mipmap.todocheck)
                    }
                } else {
                    binding.checkBox.isChecked = false
                    if(place.urlImage != "") {
                        Glide.with(this.itemView.context)
                            .load(place.urlImage)
                            .into(image)
                    }
                }
            }
        }
    }
}

