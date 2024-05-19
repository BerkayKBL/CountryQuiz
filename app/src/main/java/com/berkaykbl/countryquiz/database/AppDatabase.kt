package com.berkaykbl.countryquiz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LastMatchesEntity::class, BestScoresEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lastMatches(): LastMatchesDao
    abstract fun bestScores(): BestScoresDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "countryquiz_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}