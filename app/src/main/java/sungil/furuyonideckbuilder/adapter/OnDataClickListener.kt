package sungil.furuyonideckbuilder.adapter

import sungil.furuyonideckbuilder.data.Card
import sungil.furuyonideckbuilder.data.Save


interface OnDataClickListener<in T> {

    fun onClickCard(data: Card, check: Boolean)
    fun onClickDeck(save: Save, position: Int)


}
