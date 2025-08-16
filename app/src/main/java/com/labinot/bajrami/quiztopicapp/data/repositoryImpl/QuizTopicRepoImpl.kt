package com.labinot.bajrami.quiztopicapp.data.repositoryImpl

import com.labinot.bajrami.quiztopicapp.data.remote.RemoteQuizDataSource
import com.labinot.bajrami.quiztopicapp.domain.models.QuizTopic
import com.labinot.bajrami.quiztopicapp.domain.repository.QuizTopicRepository
import com.synac.quiztime.data.local.dao.QuizTopicDao
import com.synac.quiztime.data.mapper.entityToQuizTopic
import com.synac.quiztime.data.mapper.entityToQuizTopics
import com.synac.quiztime.data.mapper.toQuizTopics
import com.synac.quiztime.data.mapper.toQuizTopicsEntity
import com.synac.quiztime.domain.util.DataError
import com.synac.quiztime.domain.util.Result

class QuizTopicRepoImpl(
    private val remoteDataSource: RemoteQuizDataSource,
    private val topicDao: QuizTopicDao
): QuizTopicRepository {

    override suspend fun getQuizTopics(): Result<List<QuizTopic>, DataError> {

        return when (val result = remoteDataSource.getQuizTopics()) {
            is Result.Success -> {
                val quizTopicsDto = result.data
                topicDao.clearAllQuizTopics()
                topicDao.insertQuizTopics(quizTopicsDto.toQuizTopicsEntity())
                Result.Success(quizTopicsDto.toQuizTopics())
            }

            is Result.Failure -> {

                val cachedTopic = topicDao.getAllQuizTopics()
                if (cachedTopic.isNotEmpty()) {
                    Result.Success(cachedTopic.entityToQuizTopics())
                } else {
                    result
                }


            }
        }



    }

    override suspend fun getQuizTopicByCode(topicCode: Int): Result<QuizTopic, DataError> {

        return try {
            val topicEntity = topicDao.getQuizTopicByCode(topicCode)
            if (topicEntity != null) {
                Result.Success(topicEntity.entityToQuizTopic())
            } else {
                Result.Failure(DataError.Unknown(errorMessage = "Quiz Topic not found."))
            }
        } catch (e: Exception) {
            Result.Failure(DataError.Unknown(e.message))
        }


    }


}