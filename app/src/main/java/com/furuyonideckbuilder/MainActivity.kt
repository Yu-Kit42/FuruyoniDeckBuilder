package com.furuyonideckbuilder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furuyonideckbuilder.adapter.HoListAdapter
import com.furuyonideckbuilder.adapter.VeListAdapter
import com.furuyonideckbuilder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val Card: MutableList<Card> = mutableListOf(
        Card(0, "http://furuyonideck.com/images/ge/1/0.png", "참", 1, 1, 0),
        Card(0, "http://furuyonideck.com/images/ge/1/1.png", "일섬", 2, 1, 1),
        Card(0, "http://furuyonideck.com/images/ge/1/2.png", "자루 치기", 3, 1, 0),
        Card(0, "http://furuyonideck.com/images/ge/1/3.png", "거합", 4, 1, 1),
        Card(0, "http://furuyonideck.com/images/ge/1/4.png", "기백", 5, 2, 0),
        Card(0, "http://furuyonideck.com/images/ge/1/5.png", "압도", 6, 3, 0),
        Card(0, "http://furuyonideck.com/images/ge/1/6.png", "기염만장", 6, 2, 1),
    )

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hoRecycler.adapter = HoListAdapter(Card)

        binding.veRecycler.adapter = VeListAdapter(Card)

    }
}