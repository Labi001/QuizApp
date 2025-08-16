package com.labinot.bajrami.quiztopicapp.presentation.screens.issue_report

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.labinot.bajrami.quiztopicapp.presentation.components.QuestionCard
import com.synac.quiztime.presentation.issue_report.IssueReportAction
import com.synac.quiztime.presentation.issue_report.IssueReportEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun IssueReportScreen(navController: NavController,
                      state: IssueReportState,
                      event: Flow<IssueReportEvent>,
                      onAction: (IssueReportAction) -> Unit,)
{

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        event.collect { event ->
            when (event) {
                is IssueReportEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                IssueReportEvent.NavigateUp -> {
                    navController.navigateUp()
                }
            }
        }
    }

//    val dummyQuestion = QuizQuestion(
//        id = "1",
//        topicCode = 1,
//        question = "What is the language for Android Dev?",
//        allOptions = listOf("Java", "Python", "Dart", "Kotlin"),
//        correctAnswer = "Kotlin",
//        explanation = "Some Explanation"
//    )

    Column (modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        IssueReportScreenTopBar(title = "Issue Report Screen",
            onBackButtonClick = {
                navController.navigateUp()
            })

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {

            QuestionCard(
                modifier = Modifier.fillMaxWidth(),
                question = state.quizQuestion,
                isCardExpanded = state.isQuestionCardExpanded,
                onExpandClick = {
                    onAction(IssueReportAction.ExpandQuestionCard)
                }
            )
            Spacer(modifier = Modifier.height(10.dp))

           IssueTypeSection(
                selectedIssueType = state.issueType,
                otherIssueText = state.otherIssueText,
                onOtherIssueTextChange = {
                    onAction(IssueReportAction.SetOtherIssueText(it))
                },
                onIssueTypeClick = {
                    onAction(IssueReportAction.SetIssueReportType(it))
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                value = state.additionComment,
                onValueChange = {
                    onAction(IssueReportAction.SetAdditionComment(it))
                },
                label = { Text(text = "Additional Comment") },
                supportingText = {
                    Text(text = "Describe the issue in more detail (Optional)")
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.emailForFollowUp,
                onValueChange = {
                    onAction(IssueReportAction.SetEmailForFollowUp(it))
                },
                label = { Text(text = "Email for Follow-up") },
                singleLine = true,
                supportingText = { Text(text = "(Optional)") }
            )



        }

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            onClick = {
                onAction(IssueReportAction.SubmitReport)
            }
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = "Submit Report"
            )
        }





    }




}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueReportScreenTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackButtonClick: () -> Unit
) {
    TopAppBar(
        windowInsets = WindowInsets(0),
        modifier = modifier,
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IssueTypeSection(
    modifier: Modifier = Modifier,
    selectedIssueType: IssueType,
    otherIssueText: String,
    onOtherIssueTextChange: (String) -> Unit,
    onIssueTypeClick: (IssueType) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "ISSUE TYPE",
            style = MaterialTheme.typography.bodySmall
        )
        FlowRow {
            IssueType.entries.forEach { issue ->
                Row(
                    modifier = Modifier
                        .widthIn(250.dp)
                        .clickable { onIssueTypeClick(issue) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = issue == selectedIssueType,
                        onClick = { onIssueTypeClick(issue) }
                    )
                    if (issue == IssueType.OTHER) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = otherIssueText,
                            onValueChange = onOtherIssueTextChange,
                            label = { Text(text = issue.text) },
                            singleLine = true,
                            enabled = selectedIssueType == IssueType.OTHER
                        )
                    } else {
                        Text(
                            text = issue.text
                        )
                    }
                }
            }
        }
    }
}