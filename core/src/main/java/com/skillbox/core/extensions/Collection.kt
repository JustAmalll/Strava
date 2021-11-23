package com.skillbox.core.extensions

import com.hannesdorfmann.adapterdelegates4.AbsDelegationAdapter

fun <T> AbsDelegationAdapter<T>.setData(data: T) {
    items = data
    notifyDataSetChanged()
}

fun <T> AbsDelegationAdapter<T>.setDataInsertedPosition(
    data: T,
    startPosition: Int,
    itemCount: Int
) {
    items = data
    notifyItemRangeInserted(startPosition, itemCount)
}

fun <T> AbsDelegationAdapter<T>.setDataUpdatePosition(data: T, position: Int) {
    items = data
    notifyItemChanged(position)
}