package com.synac.quiztime.di


import com.labinot.bajrami.quiztopicapp.data.remote.HttpClientFactory
import com.labinot.bajrami.quiztopicapp.data.remote.KtorRemoteQuizDataSource
import com.labinot.bajrami.quiztopicapp.data.remote.RemoteQuizDataSource
import com.labinot.bajrami.quiztopicapp.data.repositoryImpl.IssueReportRepositoryImp
import com.labinot.bajrami.quiztopicapp.data.repositoryImpl.QuizQuestionRepoImpl
import com.labinot.bajrami.quiztopicapp.domain.repository.QuizQuestionRepository
import com.labinot.bajrami.quiztopicapp.data.repositoryImpl.QuizTopicRepoImpl
import com.labinot.bajrami.quiztopicapp.data.repositoryImpl.UserPreferenceRepositoryImpl
import com.labinot.bajrami.quiztopicapp.domain.repository.IssueReportRepository
import com.labinot.bajrami.quiztopicapp.domain.repository.QuizTopicRepository
import com.labinot.bajrami.quiztopicapp.domain.repository.UserPreferenceRepository
import com.labinot.bajrami.quiztopicapp.presentation.screens.dashboard.DashboardViewModel
import com.labinot.bajrami.quiztopicapp.presentation.screens.issue_report.IssueReportViewModel
import com.labinot.bajrami.quiztopicapp.presentation.screens.quiz.QuizViewModel
import com.labinot.bajrami.quiztopicapp.presentation.screens.result.ResultViewModel
import com.synac.quiztime.data.local.DataStoreFactory
import com.synac.quiztime.data.local.DatabaseFactory
import com.synac.quiztime.data.local.QuizDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinModule = module {

    //remote
    single { HttpClientFactory.create() }
    singleOf(::KtorRemoteQuizDataSource).bind<RemoteQuizDataSource>()

    //local
    single { DataStoreFactory.create(get()) }
    single { DatabaseFactory.create(get()) }
    single { get<QuizDatabase>().quizTopicDao() }
    single { get<QuizDatabase>().quizQuestionDao() }
    single { get<QuizDatabase>().userAnswerDao() }

    //Repository
    singleOf(::QuizQuestionRepoImpl).bind<QuizQuestionRepository>()
    singleOf(::QuizTopicRepoImpl).bind<QuizTopicRepository>()
    singleOf(::IssueReportRepositoryImp).bind<IssueReportRepository>()
    singleOf(::UserPreferenceRepositoryImpl).bind<UserPreferenceRepository>()

    //ViewModel
    viewModelOf(::QuizViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::ResultViewModel)
    viewModelOf(::IssueReportViewModel)

}