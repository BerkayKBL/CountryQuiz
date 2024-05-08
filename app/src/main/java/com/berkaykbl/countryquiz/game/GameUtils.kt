package com.berkaykbl.countryquiz.game

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.util.Collections
import java.util.TimerTask
import kotlin.random.Random

class GameUtils {

    private var countryData: JSONArray = JSONArray()
    private var fragmentType: Int = 0

    fun setCountryData(context: Context) {
        var countryJSONData: String? = null
        try {
            val inputStream = context.assets.open("countryData.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            countryJSONData = String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        countryData = JSONArray(countryJSONData)
    }

    fun getCountryData() : JSONArray {
        return countryData
    }

    fun getQuestion(askedQuestions: ArrayList<Int>, category: String) : ArrayList<Any> {
        val options: JSONArray = JSONArray()
        val optionIDS: ArrayList<Int> = ArrayList()

        var i = 0
        while (options.length() < 4) {
            val countryId = Random.nextInt(0, countryData.length())
            if (options.length() == 0) {
                if (!askedQuestions.contains(countryId)) {
                    val countryData = getCountryData().getJSONObject(countryId)
                    optionIDS.add(i)
                    options.put(JSONObject().put(countryData.getString("name"), countryData.getString(category)))
                }
            } else {
                val countryData = getCountryData().getJSONObject(countryId)
                optionIDS.add(i)
                options.put(JSONObject().put(countryData.getString("name"), countryData.getString(category)))
            }
            i++
        }

        var callback = ArrayList<Any>()
        optionIDS.shuffle()
        callback.add(options)
        callback.add(optionIDS)

        return callback

    }





}