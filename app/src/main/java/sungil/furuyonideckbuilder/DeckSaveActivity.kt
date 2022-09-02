package sungil.furuyonideckbuilder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import sungil.furuyonideckbuilder.adapter.OnDataClickListener
import sungil.furuyonideckbuilder.adapter.VeListAdapter
import sungil.furuyonideckbuilder.data.Card
import sungil.furuyonideckbuilder.data.CardDatabase
import sungil.furuyonideckbuilder.data.Save
import sungil.furuyonideckbuilder.databinding.ActivityDeckSaveBinding

class DeckSaveActivity : AppCompatActivity(), OnDataClickListener<Card> {

    private lateinit var binding: ActivityDeckSaveBinding
    lateinit var godArr: Array<String>
    private var pick_card: MutableList<Card> = mutableListOf()
    private val test: MutableList<String> = mutableListOf()
    private val db = CardDatabase.getInstance(this@DeckSaveActivity)
    private var cards: List<Card> = listOf(Card("", "", "", 1, false))
    private var biJang: Int = 0;
    private var tongSang: Int = 0;
    private var god1: String = ""
    private var god2: String = ""
    private var gods: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDeckSaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        godArr = resources.getStringArray(R.array.godArray)




        binding.look.setOnClickListener { lookCard() }
        binding.save.setOnClickListener { save() }


        //스피너 부분//
        val spAdapter = ArrayAdapter.createFromResource(this, R.array.godArray, R.layout.spinner)
        spAdapter.setDropDownViewResource(R.layout.spinner_setting)

        val spinner: Spinner = binding.godSpinnerLeft
        spinner.adapter = spAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                loadCard(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        if (intent.getBooleanExtra("ch", false)) {
            val save = intent.getSerializableExtra("save") as Save
            pick_card = intent.getSerializableExtra("pickCard") as MutableList<Card>
            god1 = save.god.split("|")[0]
            god2 = save.god.split("|")[1]
            spinner.setSelection(godArr.indexOf((save.god.split("|")[0]).split(" ")[0]))
            biJang =  pick_card.filter { it.biJang }.size
            tongSang = pick_card.size - pick_card.filter { it.biJang }.size
        }


    }

    private fun save() {
        val Intent = Intent(this, DeckCreateActivity::class.java)
        val arr: ArrayList<Card> = pick_card as ArrayList
        if (tongSang <= 6 || biJang <= 2){
            Toast.makeText(this, "통상패 7장, 비장패 3장을 모두 골라주세요", Toast.LENGTH_SHORT ).show()
            return
        }
        if (god2 == ""){
            Toast.makeText(this, "여신 2명을 골라주세요", Toast.LENGTH_SHORT ).show()
            return
        }

        if (intent.getBooleanExtra("ch", false)) {
            val save = intent.getSerializableExtra("save") as Save
            Intent.putExtra("save", save)
            Intent.putExtra("pickCard", arr)
            Intent.putExtra("ch", true)
            Intent.putExtra("ps", intent.getIntExtra("ps", 0))
        } else {
            Intent.putExtra("pickCard", arr)
            Intent.putExtra("god1", god1)
            Intent.putExtra("god2", god2)
            Intent.putExtra("ch", false)

            Log.e("test", "현재여신: $god2")
            Log.e("test", "현재여신: $god1")
        }
        startActivity(Intent)
    }

    private fun lookCard() {
        pick_card.forEach { test.add(it.name) }
        val dialogView =
            LayoutInflater.from(this@DeckSaveActivity).inflate(R.layout.dialog_pickcard, null)
        val alertDialog = AlertDialog.Builder(this@DeckSaveActivity).setView(dialogView)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.pick_card_view)
        recyclerView.run {
            adapter = VeListAdapter(pick_card.toList(), this@DeckSaveActivity, 1, null)
            setHasFixedSize(true)
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    // 카드 불러오기
    private fun loadCard(index: Int) {
        if (gods)
            god1 = "${godArr.get(index)} $index"
        else
            god2 = "${godArr.get(index)} $index"
        gods = !gods

        cards = db?.cardDao()?.select(godArr.get(index))!!

        val veListAdapter = cards.let { VeListAdapter(it, this@DeckSaveActivity, 0, pick_card) }
        binding.veRecycler.adapter = veListAdapter
    }

    override fun onClickCard(data: Card, check: Boolean) {
        if (pick_card.contains(data)) {
            pick_card.remove(data)
            if (data.biJang) biJang--
            else tongSang--
        } else {
            if (data.biJang) {
                pick_card.add(pick_card.size, data)
                biJang++
            } else {
                tongSang++
                pick_card.add(0, data)
            }

        }
        binding.tongSang.text = "통상패: $tongSang"
        binding.biJang.text = "비장패: $biJang"
    }

    override fun onClickDeck(save: Save, position: Int) {
    }

}