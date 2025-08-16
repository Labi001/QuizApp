package com.labinot.bajrami.quiztopicapp.presentation.screens.result

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.labinot.bajrami.quiztopicapp.R
import com.labinot.bajrami.quiztopicapp.presentation.navigation.Routes
import com.labinot.bajrami.quiztopicapp.presentation.theme.CustomGreen
import com.synac.quiztime.domain.model.QuizQuestion
import com.synac.quiztime.domain.model.UserAnswer
import com.synac.quiztime.presentation.result.ResultEvent
import com.synac.quiztime.presentation.result.ResultState
import kotlinx.coroutines.flow.Flow


@Composable
fun ResultScreen(navController: NavController,
                 state: ResultState,
                 event: Flow<ResultEvent>,)
{

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        event.collect { event ->
            when (event) {
                is ResultEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

//    val dummyQuestions = List(size = 10) { index ->
//        QuizQuestion(
//            id = "$index",
//            topicCode = 1,
//            question = "What is the language for Android Dev?",
//            allOptions = listOf("Java", "Python", "Dart", "Kotlin"),
//            correctAnswer = "Kotlin",
//            explanation = "Some Explanation"
//        )
//    }

    val dummyAnswers = listOf(
        UserAnswer(questionId = "1", selectedOption = ""),
        UserAnswer(questionId = "3", selectedOption = ""),
    )

    Column (modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top)
    {

        LazyColumn (
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(10.dp)
        ) {

            item {

                Scorecard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 80.dp, horizontal = 10.dp),
                    scorePercentage = state.scorePercentage,
                    correctAnswerCount = state.correctAnswerCount,
                    totalQuestions = state.totalQuestions)


            }

            item {
                Text(
                    text = "Quiz Questions",
                    style = MaterialTheme.typography.titleLarge,
                    textDecoration = TextDecoration.Underline
                )
            }

            items(items = state.quizQuestions){ question ->

                val userAnswer = state.userAnswers
                    .find { it.questionId == question.id }?.selectedOption

                QuestionItem(
                    question = question,
                    userSelectedAnswer = userAnswer,
                    onReportIconClick = { id ->
                        navController.navigate(Routes.IssueReportScreen(questionId = id))
                       // onReportIconClick(question.id)
                    }
                )


            }



        }

        Button(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                navController.navigate(Routes.DashboardScreen) {
                    popUpTo <Routes.ResultScreen> { inclusive = true }
                }
            }
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = "Start New Quiz"
            )
        }




    }



}

@Composable
private fun Scorecard(
    modifier: Modifier = Modifier,
    scorePercentage: Int,
    correctAnswerCount: Int,
    totalQuestions: Int,
) {

    val resultText = when (scorePercentage) {
        in 71..100 -> "Congratulations!\nA great performance!"
        in 41..70 -> "You did well,\nbut there's room for improvement"
        else -> "You may have struggled this time.\nMistakes are part of learning, keep going!"
    }
    val resultIconResId = when (scorePercentage) {
        in 71..100 -> R.drawable.ic_laugh
        in 41..70 -> R.drawable.ic_smiley
        else -> R.drawable.ic_sad
    }
    Card(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(20.dp)
                .size(100.dp),
            painter = painterResource(resultIconResId),
            contentDescription = null
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "You answered correctly $correctAnswerCount out of $totalQuestions.",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            text = resultText,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun QuestionItem(
    modifier: Modifier = Modifier,
    question: QuizQuestion,
    userSelectedAnswer: String?,
    onReportIconClick: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Q: ${question.question}",
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {onReportIconClick(question.id)}) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Report"
                )
            }
        }
        question.allOptions.forEachIndexed { index, option ->
            val letter = when (index) {
                0 -> "(a) "
                1 -> "(b) "
                2 -> "(c) "
                3 -> "(d) "
                else -> ""
            }
            val optionColor = when (option) {
                question.correctAnswer -> CustomGreen
                userSelectedAnswer -> MaterialTheme.colorScheme.error
                else -> LocalContentColor.current
            }
            Text(
                text = letter + option,
                color = optionColor
            )
        }
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = question.explanation,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        HorizontalDivider()
    }
}