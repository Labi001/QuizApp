package com.labinot.bajrami.quiztopicapp.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.labinot.bajrami.quiztopicapp.domain.models.QuizTopic
import com.labinot.bajrami.quiztopicapp.presentation.components.ErrorScreen
import com.labinot.bajrami.quiztopicapp.presentation.components.NameEditDialog
import com.labinot.bajrami.quiztopicapp.presentation.components.ShimmerEffect
import com.labinot.bajrami.quiztopicapp.presentation.components.TopicCard
import com.labinot.bajrami.quiztopicapp.presentation.components.UserStatisticCards
import com.labinot.bajrami.quiztopicapp.presentation.navigation.Routes

@Composable
fun DashboardScreen(
    navController: NavController,
    state: DashboardState,
    onAction: (DashboardAction) -> Unit,)
{


    NameEditDialog(
        isOpen = state.isNameEditDialogOpen,
        textFieldValue = state.usernameTextFieldValue,
        usernameError = state.usernameError,
        onDialogDismiss = {
            onAction(DashboardAction.NameEditDialogDismiss)
                          },
        onConfirmButtonClick = {
            onAction(DashboardAction.NameEditDialogConfirm)
                               },
        onTextFieldValueChange = {
            onAction(DashboardAction.SetUsername(it))
        }
    )

    Column(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {

        HeaderSection(
            modifier = Modifier.fillMaxWidth(),
            username = state.username,
            questionsAttempted = state.questionsAttempted,
            correctAnswers = state.correctAnswers,
            onEditNameClick = {
                onAction(DashboardAction.NameEditIconClick)
            }
        )


      QuizTopicSection(
            modifier = Modifier.fillMaxWidth(),
            quizTopics = state.quizTopics,
            isTopicsLoading = state.isLoading,
            errorMessage = state.errorMessage,
            onRefreshIconClick = {
                onAction(DashboardAction.RefreshIconClick)
            },
            onTopicCardClick = { code ->
                navController.navigate(Routes.QuizScreen(topicCode = code))
            }
        )


    }


}



@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeaderSection(
    modifier: Modifier = Modifier,
    username: String,
    questionsAttempted: Int,
    correctAnswers: Int,
    onEditNameClick: () -> Unit
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp, start = 10.dp, end = 10.dp)
        ) {
            Text(
                text = "Hello",
                style = MaterialTheme.typography.bodyMedium
            )
            Row {
                Text(
                    text = username,
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(
                    modifier = Modifier.offset(x = (-10).dp, y = (-20).dp),
                    onClick = onEditNameClick
                ) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Name"
                    )
                }
            }
        }
        UserStatisticCards(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(10.dp),
            questionsAttempted = questionsAttempted,
            correctAnswers = correctAnswers
        )
    }
}

@Composable
private fun QuizTopicSection(
    modifier: Modifier = Modifier,
    quizTopics: List<QuizTopic>,
    isTopicsLoading: Boolean,
    errorMessage: String?,
    onRefreshIconClick: () -> Unit,
    onTopicCardClick: (Int) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "What topic do you want to improve today?",
            style = MaterialTheme.typography.titleLarge
        )
        if (errorMessage != null) {
            ErrorScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                errorMessage = errorMessage,
                onRefreshIconClick = onRefreshIconClick
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(15.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                if (isTopicsLoading) {
                    items(count = 6) {
                        ShimmerEffect(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                } else {
                    items(items = quizTopics) { topic ->
                        TopicCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            topic,
                            topicName = topic.name,
                            imageUrl = topic.imageUrl,
                            onClick = { topiCode ->
                                onTopicCardClick(topiCode)
                            }
                        )
                    }
                }
            }
        }
    }
}