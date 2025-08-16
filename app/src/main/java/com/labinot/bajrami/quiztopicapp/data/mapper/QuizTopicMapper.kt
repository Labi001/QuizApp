package com.synac.quiztime.data.mapper

import com.labinot.bajrami.quiztopicapp.domain.models.QuizTopic
import com.labinot.bajrami.quiztopicapp.domain.util.Constant.BASE_URL
import com.synac.quiztime.data.local.entity.QuizTopicEntity
import com.synac.quiztime.data.remote.dto.QuizTopicDto


private fun QuizTopicDto.toQuizTopic() = QuizTopic(
    id = id,
    name = name,
    imageUrl = BASE_URL + imageUrl,
    code = code
)

private fun QuizTopicDto.toQuizTopicEntity() = QuizTopicEntity(
    id = id,
    name = name,
    imageUrl = BASE_URL + imageUrl,
    code = code
)

fun QuizTopicEntity.entityToQuizTopic() = QuizTopic(
    id = id,
    name = name,
    imageUrl = imageUrl,
    code = code
)

fun List<QuizTopicDto>.toQuizTopics() = map { it.toQuizTopic() }

fun List<QuizTopicDto>.toQuizTopicsEntity() = map { it.toQuizTopicEntity() }

fun List<QuizTopicEntity>.entityToQuizTopics() = map { it.entityToQuizTopic() }