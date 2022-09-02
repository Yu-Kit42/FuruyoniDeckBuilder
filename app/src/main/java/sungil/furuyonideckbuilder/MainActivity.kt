package sungil.furuyonideckbuilder

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sungil.furuyonideckbuilder.adapter.DeckListAdapter
import sungil.furuyonideckbuilder.adapter.OnDataClickListener
import sungil.furuyonideckbuilder.data.Card
import sungil.furuyonideckbuilder.data.CardDatabase
import sungil.furuyonideckbuilder.data.Save
import sungil.furuyonideckbuilder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnDataClickListener<Save> {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = CardDatabase.getInstance(this)
        val items = resources.getStringArray(R.array.godArray)

        if (db!!.cardDao().select("유리나").isEmpty()) {
            Toast.makeText(this, "데이터 저장을 시작합니다", Toast.LENGTH_SHORT).show()
            insert(this)
        }

        val sp = getSharedPreferences("deck", MODE_PRIVATE)
        val last = sp.getInt("last", 0)
//        val edit = sp.edit()
//        edit.putInt("last", last - 1)
//        edit.commit()
//        Log.e("test", "현재 저장된 덱 개수: $last")

        var saves: MutableList<Save> = mutableListOf()

        if (last != 0) {
            for (i in last downTo 1) {
                val json = sp.getString("$i", null)
//            Log.e("test", "덱리스트 생성중(현재 덱 개수:$last, 현재:$i \n$json")
                val test: Save = Gson().fromJson(json, object : TypeToken<Save>() {}.type)
                saves.add(test)
            }
        }
        val deckListAdapter = DeckListAdapter(saves as ArrayList<Save>, this@MainActivity)
        binding.deckList.adapter = deckListAdapter
//        saves.forEach {
//            Log.e("test", it.title)
//        }

        Glide.with(this).load("http://furuyonideck.com/images/profile/22.jpg").into(binding.kururu)

        binding.deckCreate.setOnClickListener {
            startActivity(Intent(this@MainActivity, DeckSaveActivity::class.java))
        }
        binding.deckLook.setOnClickListener {
            startActivity(Intent(this@MainActivity, DeckLookActivity::class.java))
        }
        binding.kururu.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setTitle("사용설명을 보시겠습니까??")
                .setMessage("외부링크(깃허브)로 이동합니다")
                .setNegativeButton("확인") { dialogInterface: DialogInterface, i: Int ->
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/Yu-Kit42/FuruyoniDeckBuilder")
                        )
                    )
                }
                .setPositiveButton("취소") { dialogInterface: DialogInterface, i: Int ->
                    return@setPositiveButton
                }.show()
        }

//        db!!.cardDao().deleteAll()


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
//        Log.e("test", "현재 클릭된 포지션: $ps")
        startActivity(Intent)
    }

}