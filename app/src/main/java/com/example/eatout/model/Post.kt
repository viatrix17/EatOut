package com.example.eatout.model

import android.R
import android.util.Log
import com.example.eatout.GlobalData
import com.example.eatout.viewmodel.Restaurant
import com.example.eatout.viewmodel.RestaurantViewModel
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

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
        }
        fun FindLantiudes(string: String) {
            var add = false
            var result = ArrayList<Double>()
            var indexes = string.indexesOf("\"lat\"", false)
            for (i in indexes) {
                var r = 0.0
                var j = i + 1
                //while(string[j] != '"'){
                //    r += string[j]
                //}
                var a = string.substring(i + 7, min(i + 100, string.length)).indexesOf("\n", false)
                    .first()
                println(a)
                if (a > 0 && a + i + 6 < string.length) {
                    r = string.substring(i + 7, i + 6 + a).toDouble()
                    println(r)
                    if(add==true){
                        result.add(r)
                    }else{
                        add=true
                    }

                }
            }
            println(result.toString())
            GlobalData.RestaurantsLantitudes = result
        }
        fun FindLontitudes(string: String) {
            var add = false
            var result = ArrayList<Double>()
            var indexes = string.indexesOf("\"lon\"", false)
            for (i in indexes) {
                var r: Double = 0.0
                var j = i + 1
                //while(string[j] != '"'){
                //    r += string[j]
                //}
                var a = string.substring(i + 7, min(i + 100, string.length)).indexesOf("\n", false)
                    .first()
                println(a)
                if (a > 0 && a + i + 6 < string.length) {
                    r = string.substring(i + 7, i + 6 + a).toDouble()
                    Log.d("TAG", r.toString())
                    if(add==true){
                        result.add(r)
                    }else{
                        add=true
                    }

                }
            }
            println(result.toString())
            GlobalData.RestaurantsLontitudes = result

        }
        fun CalculateDistance(){
            Log.d("TAG", "calculating something")
            var i : Int = 0
            while(i<GlobalData.Companion.RestaurantsLontitudes.size){
                var tmp : Double  =sqrt((GlobalData.Companion.longitude - GlobalData.Companion.RestaurantsLontitudes[i].toDouble()).pow(2) + sqrt((GlobalData.Companion.latitude - GlobalData.Companion.RestaurantsLantitudes[i].toDouble()).pow(2)));
                GlobalData.Companion.Distances.add(tmp);
                i+=1
                Log.d("TAG", tmp.toString())
            }

            ConvertToViewModel()
        }
        fun ConvertToViewModel(){
            var i : Int = 0
            while(i<GlobalData.Companion.RestaurantsLontitudes.size){
                var restauracja : Restaurant = Restaurant(i,GlobalData.Companion.ListOfRestaurants[i],"adres",0.0,"",returnTag(GlobalData.Companion.ListOfRestaurants[i]),false,false,GlobalData.Companion.RestaurantsLantitudes[i],GlobalData.Companion.RestaurantsLontitudes[i])
                RestaurantViewModel.allRestaurants.add(restauracja)
                i+=1
            }
            GlobalData.Flag = true
        }
        fun returnTag(RestaurantName : String): List<String> {
            var TAGS = listOf("fast", "expensive", "vegan")
            var sum : Int = 0
            for(i in RestaurantName){
                sum += i.code
            }
            return listOf(TAGS[sum % 3], TAGS[((sum % 3) + (sum % 2 + 1)) % 3])
        }
    }
}