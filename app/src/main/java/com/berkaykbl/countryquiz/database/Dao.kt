package com.berkaykbl.countryquiz.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface LastMatchesDao {

    @Insert
    fun insert(lastMatch: LastMatchesEntity)

    @Query("SELECT * FROM last_matches")
    fun getAll(): List<LastMatchesEntity>

}


@Dao
interface BestScoresDao {

    @Insert
    fun insert(bestScoreEntity: BestScoresEntity)

    @Query("SELECT * FROM best_scores")
    fun getAll(): List<BestScoresEntity>

    @Update
    fun update(bestScoreEntity: BestScoresEntity)

    @Query("SELECT * FROM best_scores WHERE gameModeKey = :gameModeKey AND categories = :categories")
    fun getCategoryScore(gameModeKey: String, categories: String): List<BestScoresEntity>

}