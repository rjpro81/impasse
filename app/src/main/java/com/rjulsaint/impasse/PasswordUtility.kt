package com.rjulsaint.impasse

import java.security.SecureRandom
import kotlin.streams.asSequence


class PasswordUtility {

    fun validate(password : String): Boolean{
        val specialsRegex = Regex("\\W+")
        val upperRegex = Regex("[A-Z]+")
        val lowerRegex = Regex("[a-z]+")

        return !(!specialsRegex.containsMatchIn(password) || !upperRegex.containsMatchIn(password) || !lowerRegex.containsMatchIn(password) || password.length < 8)
    }

    fun generate(): String{
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + '!' + '@'+ '#' + '$' + '%' + '^' + '&' + '*' + '?'
        val random = SecureRandom()
        val randomNumberForPasswordGenerator = random.nextInt(50-10) + 10

        return random.ints(randomNumberForPasswordGenerator.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")
    }
}