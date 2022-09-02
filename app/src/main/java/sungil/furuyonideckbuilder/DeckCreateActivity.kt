package sungil.furuyonideckbuilder

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.gson.Gson
import sungil.furuyonideckbuilder.adapter.OnDataClickListener
import sungil.furuyonideckbuilder.adapter.VeListAdapter
import sungil.furuyonideckbuilder.data.Card
import sungil.furuyonideckbuilder.data.CardDatabase
import sungil.furuyonideckbuilder.data.Save
import sungil.furuyonideckbuilder.databinding.ActivityDeckCreateBinding
import java.nio.file.Files.delete

class DeckCreateActivity : AppCompatActivity(), OnDataClickListener<Card> {

    private lateinit var binding: ActivityDeckCreateBinding
    lateinit var godArr: Array<String>
    lateinit var sp: SharedPreferences
    lateinit var edit: SharedPreferences.Editor

    var gods = true
    var cards: List<Card> = listOf(Card("", "", "", 1, false))
    lateinit var god1: String
    lateinit var god2: String
    lateinit var save: Save

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeckCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        godArr = resources.getStringArray(R.array.godArray)
        sp = getSharedPreferences("deck", MODE_PRIVATE)
        edit = sp.edit()

        if (intent.getBooleanExtra("ch", false)) {
            save = intent.getSerializableExtra("save") as Save
            Log.e("test", save.title)
            binding.title.setText(save.title)
            binding.comment.setText(save.comment)
            val tp = save.god.split("|")
            god1 = tp[0]
            god2 = tp[1]
            binding.gods.text = "$god1 $god2"
        } else {
            god1 = intent.getStringExtra("god1").toString()
            god2 = intent.getStringExtra("god2").toString()
            binding.gods.text = "$god1 $god2"
        }
        val pickCard = intent.getSerializableExtra("pickCard") as ArrayList<Card>
//        pickCard.forEach { Log.e("test", "저장된 카드 이름: ${it.name}") }
        cards = pickCard.toList()

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
                if (position == 0) return
                if (gods) {
                    binding.gods.text = "${godArr[position]}"
                    god1 = "${godArr[position]} $position"
                    gods = false
                } else if (binding.gods.text.toString().length <= 5) {
                    binding.gods.text = "${binding.gods.text} ${godArr[position]}"
                    god2 = "${godArr[position]} $position"
                } else {
                    binding.gods.text = "${godArr[position]}"
                    god1 = "${godArr[position]} $position"
                }
                Log.e("test", god1 + god2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }


        val veListAdapter =
            cards.let { VeListAdapter(cards, this@DeckCreateActivity, 1, null) }
        binding.veRecycler.adapter = veListAdapter

        binding.save.setOnClickListener { deckCreate(pickCard) }
        binding.modify.setOnClickListener { deckModify() }
        binding.delete.setOnClickListener { delete() }


    }

    private fun deckModify() {
        val Intent = Intent(this, DeckSaveActivity::class.java)

        if (intent.getBooleanExtra("ch", false)) {
            Intent.putExtra("save", save)
            Log.e("test", save.title)
            Intent.putExtra("pickCard", save.cards)
            Intent.putExtra("ch", true)
            Intent.putExtra("ps", intent.getIntExtra("ps", 0))
        } else {
            Toast.makeText(this, "저장 후 수정해 주세요", Toast.LENGTH_SHORT).show()
            return
//            val pickCard = cards as ArrayList<Card>
//            Intent.putExtra("ch", true)
//            Intent.putExtra("ch1", true)
//            Intent.putExtra("pickCard", pickCard)
//            Intent.putExtra("god1", god1)
//            Intent.putExtra("god1", god2)
        }
        startActivity(Intent)

    }

    private fun delete() {
        val builder = AlertDialog.Builder(this)
            .setTitle("삭제 확인!")
            .setMessage("덱을 지우시겠습니까?")
            .setNegativeButton("확인") { dialogInterface: DialogInterface, i: Int ->
                val ps = intent.getIntExtra("ps", 999)
                val last = sp.getInt("last", 0)
                for (i in ps until last) {
                    edit.putString("$i", sp.getString("${i + 1}", null))
                    Log.e("test", "${i}, ${last} ")
                }
                edit.remove("$last")
                edit.putInt("last", last - 1)
                edit.commit()
                startActivity(Intent(this, DeckLookActivity::class.java))
            }
            .setPositiveButton("취소") { dialogInterface: DialogInterface, i: Int ->
                return@setPositiveButton
            }.show()
    }

    private fun deckCreate(pickCard: ArrayList<Card>) {
        val profile = resources.getStringArray(R.array.godProfile)

        if (binding.title.text.isEmpty()) {
            Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        var last: Int = 0
        var json = Gson().toJson("")

        if (intent.getIntExtra("ps", 999) != 999)
            last = intent.getIntExtra("ps", 0)
        else {
            last = sp.getInt("last", 0) + 1
            edit.putInt("last", last)
        }

        if (intent.getBooleanExtra("ch", false)) {
            save.title = binding.title.text.toString()
            save.comment = binding.comment.text.toString()
            save.cards = pickCard
            if (!gods) {
                save.god = "${god1}|${god2}"
                val pf1 = profile[god1?.split(" ")?.get(1)?.toInt()!! - 1]
                val pf2 = profile[god2?.split(" ")?.get(1)?.toInt()!! - 1]
                save.profile = "$pf1 $pf2"
            }
            json = Gson().toJson(save)
        } else {
            val time: Long = System.currentTimeMillis()
            val pf1 = profile[god1?.split(" ")?.get(1)?.toInt()!! - 1]
            val pf2 = profile[god2?.split(" ")?.get(1)?.toInt()!! - 1]
            json = Gson().toJson(
                Save(
                    time, binding.title.text.toString(),
                    "${god1}|${god2}",
                    "$pf1 $pf2",
                    binding.comment.text.toString(),
                    pickCard
                )
            )
        }

        edit.putString("$last", json)
        edit.commit()


        startActivity(Intent(this, DeckLookActivity::class.java))

    }

    override fun onClickCard(data: Card, check: Boolean) {
    }

    override fun onClickDeck(save: Save, position: Int) {

    }
}