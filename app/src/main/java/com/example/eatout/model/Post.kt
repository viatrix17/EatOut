package com.example.eatout.model

import com.example.eatout.GlobalData
import kotlin.math.min

class Post {
    companion object {
        fun ignoreCaseOpt(ignoreCase: Boolean) =
            if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet()

        fun String?.indexesOf(pat: String, ignoreCase: Boolean = true): List<Int> =
            pat.toRegex(ignoreCaseOpt(ignoreCase))
                .findAll(this ?: "")
                .map { it.range.first }
                .toList()

        fun FindRestaurants(string: String) {
            var result = ArrayList<String>()
            var indexes = string.indexesOf("\"name\"", false)
            for (i in indexes) {
                var r = ""
                var j = i + 1
                //while(string[j] != '"'){
                //    r += string[j]
                //}
                var a = string.substring(i + 9, min(i + 100, string.length)).indexesOf("\"", false)
                    .first()
                println(a)
                if (a > 0 && a + i + 9 < string.length) {
                    r = string.substring(i + 9, i + 9 + a)
                    println(r)
                    result.add(r)
                }
            }
            println(result.toString())
            GlobalData.ListOfRestaurants = result
            GlobalData.Flag = true
        }
    }
}