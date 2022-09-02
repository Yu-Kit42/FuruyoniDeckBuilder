package sungil.furuyonideckbuilder.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Card::class], version = 2)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao


    companion object {
        private var INSTANCE: CardDatabase? = null

        @Synchronized
        fun getInstance(context: Context): CardDatabase? {
            if (INSTANCE == null) {
                synchronized(CardDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CardDatabase::class.java,
                        "user-database"
                    ).allowMainThreadQueries().build()
                }
            }
            Log.e("test", "$context 에서 데이터 베이스 생성됨")
            return INSTANCE
        }
    }

}