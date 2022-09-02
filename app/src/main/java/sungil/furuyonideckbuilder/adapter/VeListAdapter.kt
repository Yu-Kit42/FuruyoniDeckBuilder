package sungil.furuyonideckbuilder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sungil.furuyonideckbuilder.R
import sungil.furuyonideckbuilder.data.Card
import sungil.furuyonideckbuilder.databinding.VerticalItemBinding

class VeListAdapter(

    private val dataSet: List<Card>,
    private val listener: OnDataClickListener<Card>,
    private val type: Int,
    private val pickCard: MutableList<Card>?

) : RecyclerView.Adapter<VeListAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: VerticalItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: Card) {

            var pick: Boolean = false

            Glide.with(binding.cardImg.context)
                .load(card.imgUrl)
                .into(binding.cardImg)

            binding.cardImg.setOnLongClickListener {

                val dialogView =
                    LayoutInflater.from(binding.cardImg.context).inflate(R.layout.dialog_long, null)
                val alertDialog = AlertDialog.Builder(binding.cardImg.context).setView(dialogView)
                val view = dialogView.findViewById<ImageView>(R.id.dl_card)
                Glide.with(dialogView.context).load(card.imgUrl).into(view)
                val dialog = alertDialog.create()
                dialog.show()
                false

            }
            if (type == 1) return
            if (pickCard?.contains(card) == true) {
                binding.cardImg.alpha = 0.5F
                pick = !pick
            } else binding.cardImg.alpha = 1F
            binding.cardImg.setOnClickListener {
                pick = !pick
                if (pick) {
                    binding.cardImg.alpha = 0.5F
                    listener.onClickCard(card, true)
                } else {
                    binding.cardImg.alpha = 1F
                    listener.onClickCard(card, false)
                }
            }

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

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
