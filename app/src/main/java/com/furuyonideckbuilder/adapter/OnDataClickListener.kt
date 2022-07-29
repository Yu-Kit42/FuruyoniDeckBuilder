package com.furuyonideckbuilder.adapter

import androidx.recyclerview.widget.RecyclerView

interface OnDataClickListener<in T> {

    fun onClickPlus(data: T)

    fun onClickMinus(data: T)

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, data: T)

}
