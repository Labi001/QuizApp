package com.labinot.bajrami.quiztopicapp.data.repositoryImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.labinot.bajrami.quiztopicapp.domain.repository.UserPreferenceRepository
import com.labinot.bajrami.quiztopicapp.domain.util.Constant.CORRECT_ANSWERS_PREF_KEY
import com.labinot.bajrami.quiztopicapp.domain.util.Constant.QUESTIONS_ATTEMPTED_PREF_KEY
import com.labinot.bajrami.quiztopicapp.domain.util.Constant.USERNAME_PREF_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceRepositoryImpl(
   private val prefs: DataStore<Preferences>
): UserPreferenceRepository {

    companion object {
        private val QUESTIONS_ATTEMPTED_KEY = intPreferencesKey(QUESTIONS_ATTEMPTED_PREF_KEY)
        private val CORRECT_ANSWER_KEY = intPreferencesKey(CORRECT_ANSWERS_PREF_KEY)
        private val USERNAME_KEY = stringPreferencesKey(USERNAME_PREF_KEY)
    }


    override fun getQuestionsAttempted(): Flow<Int> {

        return prefs.data.map { preferences ->
            preferences[QUESTIONS_ATTEMPTED_KEY] ?: 0
        }

    }

    override fun getCorrectAnswers(): Flow<Int> {

        return prefs.data.map { preferences ->
            preferences[CORRECT_ANSWER_KEY] ?: 0
        }

    }

    override fun getUsername(): Flow<String> {

        return prefs.data.map { preferences ->
            preferences[USERNAME_KEY] ?: "Android Developer"
        }

    }

    override suspend fun saveScore(
        questionAttempted: Int,
        correctAnswers: Int)
    {

        prefs.edit { preferences ->
            preferences[QUESTIONS_ATTEMPTED_KEY] = questionAttempted
            preferences[CORRECT_ANSWER_KEY] = correctAnswers
        }


    }

    override suspend fun saveUsername(name: String) {

        prefs.edit { preferences ->
            preferences[USERNAME_KEY] = name
        }

    }




}