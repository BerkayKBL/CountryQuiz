package com.berkaykbl.countryquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.room.Room
import com.berkaykbl.countryquiz.database.AppDatabase

private var db: AppDatabase? = null
class Utils {

    fun changeActivity(from: Context, to: Class<*>, historyEnable: Boolean, extra: Bundle? = null) {
        val intent = Intent(from, to)
        if (!historyEnable) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        if (extra != null) {
            intent.putExtras(extra)
        }
        from.startActivity(intent)
    }

    fun getDB(): AppDatabase? = db
    fun setDB(context: Context) {
        db = Room.databaseBuilder(context, AppDatabase::class.java, "countryquiz_database")
            .allowMainThreadQueries()
            .build()
    }

    fun lastMatchesDao() = db!!.lastMatches()
    fun bestScoresDao() = db!!.bestScores()

    fun changeGameModeIndex(gameModeIndex: Int) : Int {
        var gameMode = -1
        if (gameModeIndex in 0..3) {
            gameMode = 0
        } else if (gameModeIndex == 4) {
            gameMode = 1
        } else if (gameModeIndex == 5) {
            gameMode = 2
        } else if (gameModeIndex == 6) {
            gameMode = 3
        }
        return gameMode
    }
}