package com.berkaykbl.countryquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.Locale

class MainActivityBackup : AppCompatActivity() {
    fun ulkeKoduAl(ulkeAdi: String): String? {
        val localeList = Locale.getAvailableLocales()
        for (locale in localeList) {
            if (locale.displayCountry.equals(ulkeAdi, ignoreCase = true)) {
                return locale.country
            }
        }
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val countries = readJSONArrayFromAsset("datas/countries.json")
        val countries2 = readJSONArrayFromAsset("datas/countries2.json")
        val capitals = readJSONFromAsset("datas/capitals.json")
        val noneCapitals = JSONObject()

        val dosyaYolu = "countryData.json" // Yazılacak dosyanın yolu
        val dosya = File(dosyaYolu)
        countries?.let {
            for (i in 0 until it.length()) {
                val jsonObject = it.getJSONObject(i)
                val code = jsonObject.getString("alpha2")
                val capital = capitals!!.getJSONObject(code).getString("capital")
                var countryData = JSONObject()
                for (i in 0 until countries2!!.length()) {
                    // JSON nesnesini al
                    val jsonObject: JSONObject = countries2.getJSONObject(i)

                    // "name" anahtarının değerini kontrol et
                    if (jsonObject.getString("code") == code.uppercase()) {
                        countryData = jsonObject
                        break
                    }
                }
                val newCData = JSONObject()
                println(code)
                newCData.put("name", countryData.getString("name"))
                newCData.put("dialCode", countryData.getString("dialCode"))
                newCData.put("capital", capital)
                noneCapitals.put(code, newCData)

            }

            println(noneCapitals.toString().chunked(4000).joinToString("\n"))

        }
    }

    fun readJSONFromAsset(fileName: String): JSONObject? {
        var json: String? = null
        try {
            val inputStream = this.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return JSONObject(json)
    }

    fun readJSONArrayFromAsset(fileName: String): JSONArray? {
        var json: String?
        try {
            val inputStream = this.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return JSONArray(json)
    }
}