package com.labinot.bajrami.quiztopicapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {

    fun getQuestionsAttempted(): Flow<Int>

    fun getCorrectAnswers(): Flow<Int>

    fun getUsername(): Flow<String>

    suspend fun saveScore(questionAttempted: Int, correctAnswers: Int)

    suspend fun saveUsername(name: String)


}