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
import com.furuyonideckbuilder.databinding.VerticalItemBinding

class VeListAdapter(

    private val dataSet: MutableList<Card>,
//    private val listener: OnDataClickListener<Card>

) : RecyclerView.Adapter<VeListAdapter.ViewHolder>() {


    inner class ViewHolder(
        private val binding: VerticalItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: Card) {
            Glide.with(binding.careImg.context)
                .load(card.imgUrl)
                .into(binding.careImg)
            binding.cardName.text = card.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VerticalItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

}
