package com.labinot.bajrami.quiztopicapp.presentation.screens.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.labinot.bajrami.quiztopicapp.domain.repository.QuizQuestionRepository
import com.labinot.bajrami.quiztopicapp.domain.repository.QuizTopicRepository
import com.labinot.bajrami.quiztopicapp.domain.repository.UserPreferenceRepository
import com.labinot.bajrami.quiztopicapp.domain.util.getErrorMessage
import com.labinot.bajrami.quiztopicapp.presentation.navigation.Routes
import com.synac.quiztime.domain.model.UserAnswer
import com.synac.quiztime.domain.util.onFailure
import com.synac.quiztime.domain.util.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    savedStateHandle: SavedStateHandle,
    private val questionRepository: QuizQuestionRepository,
    private val topicRepository: QuizTopicRepository,
    private val userPreferencesRepository: UserPreferenceRepository

    ) : ViewModel() {

    private val topicCode = savedStateHandle.toRoute<Routes.QuizScreen>().topicCode

    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()

    private val _event = Channel<QuizEvent>()
    val event = _event.receiveAsFlow()


    init {

        setupQuiz()
    }


    fun onAction(action: QuizAction) {

        when(action){

            QuizAction.NextQuestionButtonClick -> {

                val newIndex = (state.value.currentQuestionIndex + 1)
                    .coerceAtMost(state.value.questions.lastIndex)
                _state.update { it.copy(currentQuestionIndex = newIndex) }


            }

            QuizAction.PrevQuestionButtonClick -> {

                val newIndex = (state.value.currentQuestionIndex - 1)
                    .coerceAtLeast(0)
                _state.update { it.copy(currentQuestionIndex = newIndex) }


            }

            QuizAction.ExitQuizButtonClick -> {

                _state.update { it.copy(isExitQuizDialogOpen = true) }

            }
            QuizAction.ExitQuizConfirmButtonClick -> {

                _state.update { it.copy(isExitQuizDialogOpen = false) }
                _event.trySend(QuizEvent.NavigateToDashboardScreen)

            }

            QuizAction.ExitQuizDialogDismiss -> {
                _state.update { it.copy(isExitQuizDialogOpen = false) }
            }
            is QuizAction.JumpToQuestion -> {

                _state.update { it.copy(currentQuestionIndex = action.index) }

            }
            is QuizAction.OnOptionSelected -> {

                val newAnswer = UserAnswer(action.questionId, action.answer)
                val currentAnswers = state.value.answers.toMutableList()
                val existingAnswerIndex = currentAnswers
                    .indexOfFirst { it.questionId == action.questionId }
                if (existingAnswerIndex != -1) {
                    currentAnswers[existingAnswerIndex] = newAnswer
                } else {
                    currentAnswers.add(newAnswer)
                }
                _state.update { it.copy(answers = currentAnswers) }

            }

            QuizAction.Refresh -> {

                setupQuiz()
            }
            QuizAction.SubmitQuizButtonClick -> {
                _state.update { it.copy(isSubmitQuizDialogOpen = true) }
            }
            QuizAction.SubmitQuizConfirmButtonClick -> {
                _state.update { it.copy(isSubmitQuizDialogOpen = false) }
                submitQuiz()
            }
            QuizAction.SubmitQuizDialogDismiss -> {

                _state.update { it.copy(isSubmitQuizDialogOpen = false) }

            }
        }



    }




    private fun setupQuiz() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, loadingMessage = "Setting up Quiz...")
            }
            getQuizTopicName(topicCode)
            delay(1000)
            getQuizQuestions(topicCode)
            _state.update {
                it.copy(isLoading = false, loadingMessage = null)
            }
        }
    }

    private suspend fun getQuizTopicName(topicCode: Int) {
        topicRepository.getQuizTopicByCode(topicCode)
            .onSuccess { topic ->
                _state.update { it.copy(topBarTitle = topic.name + " Quiz") }
            }
            .onFailure { error ->
                _event.send(QuizEvent.ShowToast(error.getErrorMessage()))
            }
    }

    private suspend fun getQuizQuestions(topicCode: Int) {
        questionRepository.fetchAndSaveQuizQuestions(topicCode)
            .onSuccess { questions ->
                _state.update {
                    it.copy(
                        questions = questions,
                        errorMessage = null
                    )
                }
            }
            .onFailure { error ->
                _state.update {
                    it.copy(
                        questions = emptyList(),
                        errorMessage = error.getErrorMessage()
                    )
                }
            }
    }

    private fun submitQuiz() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, loadingMessage = "Submitting Quiz...")
            }
            saveUserAnswers()
            updateScore()
            _state.update {
                it.copy(isLoading = false, loadingMessage = null)
            }
            _event.send(QuizEvent.NavigateToResultScreen)
        }
    }

    private suspend fun saveUserAnswers() {
        questionRepository.saveUserAnswers(state.value.answers)
            .onFailure { error ->
                _event.send(QuizEvent.ShowToast(error.getErrorMessage()))
            }
    }

    private suspend fun updateScore() {
        val quizQuestions = state.value.questions
        val userAnswers = state.value.answers

        val correctAnswersCount = userAnswers.count { answer ->
            val question = quizQuestions.find { it.id == answer.questionId }
            question?.correctAnswer == answer.selectedOption
        }

        val previousAttempted = userPreferencesRepository.getQuestionsAttempted().first()
        val previousCorrect = userPreferencesRepository.getCorrectAnswers().first()

        val totalAttempted = previousAttempted + userAnswers.size
        val totalCorrect = previousCorrect + correctAnswersCount

        userPreferencesRepository.saveScore(
            questionAttempted = totalAttempted,
            correctAnswers = totalCorrect
        )
    }

}