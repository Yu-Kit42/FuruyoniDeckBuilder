package sungil.furuyonideckbuilder.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Card(
    val god: String,
    val imgUrl: String,
    val name: String,
    val num: Int,
    val biJang: Boolean
): Serializable {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
