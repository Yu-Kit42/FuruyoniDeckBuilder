package sungil.furuyonideckbuilder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sungil.furuyonideckbuilder.data.Card
import sungil.furuyonideckbuilder.data.Save
import sungil.furuyonideckbuilder.databinding.DeckListItemBinding
import java.text.SimpleDateFormat

class DeckListAdapter(
    private val saveList: ArrayList<Save>,
    private val listener: OnDataClickListener<Save>,
) : RecyclerView.Adapter<DeckListAdapter.ViewHolder>() {


    inner class ViewHolder(
        private val binding: DeckListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(save: Save) {

            binding.time.text = SimpleDateFormat("yyyy-MM-dd").format(save.time)
            binding.title.text = save.title
            Glide.with(binding.god1.context)
                .load(save.profile.split(" ").get(0))
                .into(binding.god1)

            Glide.with(binding.god2.context)
                .load(save.profile.split(" ").get(1))
                .into(binding.god2)
            binding.item.setOnClickListener {

            }

            binding.item.setOnClickListener { listener.onClickDeck(save, adapterPosition ) }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckListAdapter.ViewHolder {
        return ViewHolder(
            DeckListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(saveList[position])
    }

    override fun getItemCount(): Int = saveList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
