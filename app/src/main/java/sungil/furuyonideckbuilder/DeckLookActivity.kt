package sungil.furuyonideckbuilder

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sungil.furuyonideckbuilder.adapter.DeckListAdapter
import sungil.furuyonideckbuilder.adapter.OnDataClickListener
import sungil.furuyonideckbuilder.data.Card
import sungil.furuyonideckbuilder.data.Save
import sungil.furuyonideckbuilder.databinding.ActivityDeckLookBinding

class DeckLookActivity : AppCompatActivity(), OnDataClickListener<Save> {

    private lateinit var binding: ActivityDeckLookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeckLookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = getSharedPreferences("deck", MODE_PRIVATE)
        var saves: MutableList<Save> = mutableListOf()
        val last = sp.getInt("last", 0)

        if (last != 0) {
            for (i in last downTo 1) {
                val json = sp.getString("$i", null)
            Log.e("test", "덱리스트 생성중(현재 덱 개수:$last, 현재:$i")
                val test: Save = Gson().fromJson(json, object : TypeToken<Save>() {}.type)
                saves.add(test)
            }
        }




        val deckListAdapter = DeckListAdapter(saves as ArrayList<Save>, this@DeckLookActivity)
        binding.deckList.adapter = deckListAdapter
        saves.forEach {
            Log.e("test", it.title)
        }

        binding.deckClear.setOnClickListener {
            deckClear(last, sp)
        }
        binding.home.setOnClickListener {
            startActivity(
                Intent(
                    this@DeckLookActivity,
                    MainActivity::class.java
                )
            )
        }

    }

    private fun deckClear(last: Int, sp: SharedPreferences) {
        var check = false;
        val edit = sp.edit()
        val builder = AlertDialog.Builder(this)
            .setTitle("삭제 확인!")
            .setMessage("정말 모든 덱을 지우시겠습니까??")
            .setNegativeButton("확인") { dialogInterface: DialogInterface, i: Int ->
                for (i in last downTo 1) {
                    edit.remove("$i")
                    Log.e("test", "$i")
                    startActivity(Intent(this, DeckLookActivity::class.java))
                }
                edit.putInt("last", 0)
                edit.commit()
            }
            .setPositiveButton("취소") { dialogInterface: DialogInterface, i: Int ->
                check = false
            }.show()

    }

    override fun onClickCard(data: Card, check: Boolean) {
    }

    override fun onClickDeck(save: Save, position: Int) {
        val sp = getSharedPreferences("deck", MODE_PRIVATE)

        val Intent = Intent(this, DeckCreateActivity::class.java)
        val arr: ArrayList<Card> = save.cards
        val ps = sp.getInt("last", 0) - position

        Intent.putExtra("save", save)
        Intent.putExtra("pickCard", arr)
        Intent.putExtra("ch", true)
        Intent.putExtra("ps", ps)
        Log.e("test", "현재 클릭된 포지션: $ps")
        startActivity(Intent)

    }
}