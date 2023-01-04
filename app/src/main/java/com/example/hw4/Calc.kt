package com.example.hw4

import java.text.DecimalFormat


object Calc {
    fun intToText(likes: Int): String {
        return when (likes) {
            in 0..999 -> likes.toString()
            in 1000..1099 -> "1K"
            in 1100..9999 -> return calcLike(likes, 1) + "K"
            in 10000..999999 -> return calcLike(likes, 0) + "K"
            in 1000000..999999999 -> return calcLike(likes, 1) + "M"
            else -> return "Более 1 Billion"
        }

    }

    fun calcLike(like: Int, places: Int): String {
        val dfor: DecimalFormat
        if (places == 1) {
            dfor = DecimalFormat("###.#")
        } else {
            dfor = DecimalFormat("###")
        }
        val liked: Double
        liked = like.toDouble() / 1000
        return dfor.format(liked)
    }

    fun intToShareText(shares: Int): String {
        return when (shares) {
            in 0..999 -> shares.toString()
            in 1000..1099 -> "1K"
            in 1100..9999 -> return calcShare(shares, 1) + "K"
            in 10000..999999 -> return calcShare(shares, 0) + "K"
            in 1000000..999999999 -> return calcShare(shares, 1) + "M"
            else -> return "Более 1 Billion"
        }

    }
    fun calcShare(shares: Int, places2: Int): String {
        val dfor2: DecimalFormat
        if (places2==1){
            dfor2 = DecimalFormat("###.#")
        }else{
            dfor2 = DecimalFormat("###")
        }
        val liked: Double
        liked = shares.toDouble() / 1000
        return dfor2.format(liked)
    }
}
