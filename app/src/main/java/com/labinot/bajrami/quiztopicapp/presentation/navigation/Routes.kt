package com.labinot.bajrami.quiztopicapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes{

    @Serializable
    data object DashboardScreen : Routes()

    @Serializable
    data class QuizScreen(val topicCode: Int) : Routes()

    @Serializable
    data object ResultScreen : Routes()


    @Serializable
    data class IssueReportScreen(val questionId: String) : Routes()


}
