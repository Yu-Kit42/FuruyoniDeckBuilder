package sungil.furuyonideckbuilder.data

import androidx.room.*

@Dao
interface CardDao {
    @Insert
    fun insert(card: Card)

    @Insert
    fun insertAll(vararg card: Card)

    @Update
    fun update(card: Card)

    @Delete
    fun delete(card: Card)

    @Query("SELECT * FROM Card")
    fun selectAll(): List<Card>

    @Query("SELECT * FROM Card WHERE god = :god1 or :god2")
    fun selectA(god1: String, god2: String): List<Card>

    @Query("SELECT * FROM Card WHERE god = :god")
    fun select(god: String): List<Card>

    @Query("DELETE FROM Card")
    fun deleteAll()

}