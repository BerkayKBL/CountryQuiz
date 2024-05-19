package com.berkaykbl.countryquiz.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "last_matches")
data class LastMatchesEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val gameModeKey: String,
    val gameModeName: String,
    val time: Int,
    val win: Boolean,
    val categories: String,
    val score: Int,
    val playtime: Int
)

@Entity(
    tableName = "best_scores"
)

data class BestScoresEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val gameModeKey: String,
    val gameModeName: String,
    val time: Int,
    val categories: String,
    val score: Int,
    val playtime: Int
)

