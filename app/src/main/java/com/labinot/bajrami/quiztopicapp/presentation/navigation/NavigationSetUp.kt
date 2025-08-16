package com.labinot.bajrami.quiztopicapp.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.labinot.bajrami.quiztopicapp.presentation.screens.result.ResultScreen
import com.labinot.bajrami.quiztopicapp.presentation.screens.dashboard.DashboardScreen
import com.labinot.bajrami.quiztopicapp.presentation.screens.dashboard.DashboardViewModel
import com.labinot.bajrami.quiztopicapp.presentation.screens.issue_report.IssueReportScreen
import com.labinot.bajrami.quiztopicapp.presentation.screens.issue_report.IssueReportViewModel
import com.labinot.bajrami.quiztopicapp.presentation.screens.quiz.QuizScreen
import com.labinot.bajrami.quiztopicapp.presentation.screens.quiz.QuizViewModel
import com.labinot.bajrami.quiztopicapp.presentation.screens.result.ResultViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationSetUp(){

    val navController = rememberNavController()


    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground)
    { paddingValues ->

        NavHost(
            modifier = Modifier
                .padding(paddingValues),
            navController = navController,
            startDestination = Routes.DashboardScreen,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            popEnterTransition = { fadeIn(animationSpec = tween(100)) },
            popExitTransition = { fadeOut(animationSpec = tween(100)) })
        {

            composable<Routes.DashboardScreen> {

                val viewModel = koinViewModel<DashboardViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                DashboardScreen(
                    navController = navController,
                    state = state,
                    onAction = viewModel::onAction)

            }

            composable<Routes.QuizScreen> {

                val viewModel = koinViewModel<QuizViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                QuizScreen(navController = navController,
                    state = state,
                    event = viewModel.event,
                    onAction = viewModel::onAction)

            }

            composable<Routes.ResultScreen> {

                val viewModel = koinViewModel<ResultViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                ResultScreen(navController = navController,
                    state = state,
                    event = viewModel.event)

            }

            composable<Routes.IssueReportScreen> {

                val viewModel = koinViewModel<IssueReportViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                IssueReportScreen(
                    navController = navController,
                    state = state,
                    event = viewModel.event,
                    onAction = viewModel::onAction
                    )


            }




        }



    }




}