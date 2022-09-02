package sungil.furuyonideckbuilder.data

import java.io.Serializable
data class Save(
    val time: Long,
    var title: String,
    var god: String,
    var profile: String,
    var comment: String,
    var cards: ArrayList<Card>
): Serializable
