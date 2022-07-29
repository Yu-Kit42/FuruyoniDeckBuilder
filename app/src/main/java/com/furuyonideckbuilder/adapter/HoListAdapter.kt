package com.furuyonideckbuilder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.furuyonideckbuilder.Card
import com.furuyonideckbuilder.R
import com.furuyonideckbuilder.databinding.HorizontalItemBinding

class HoListAdapter(

    private val dataSet: MutableList<Card>,
//    private val listener: OnDataClickListener<Card>

) : RecyclerView.Adapter<HoListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: HorizontalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(Card: Card) {
            binding.name.text = Card.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HorizontalItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

}
