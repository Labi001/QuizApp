package com.labinot.bajrami.quiztopicapp.domain.repository

import com.labinot.bajrami.quiztopicapp.domain.models.QuizTopic
import com.synac.quiztime.domain.util.DataError
import com.synac.quiztime.domain.util.Result

interface QuizTopicRepository {

    suspend fun getQuizTopics(): Result<List<QuizTopic>, DataError>

    suspend fun getQuizTopicByCode(topicCode: Int): Result<QuizTopic, DataError>

}