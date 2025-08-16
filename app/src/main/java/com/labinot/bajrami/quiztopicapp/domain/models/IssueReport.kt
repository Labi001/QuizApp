package com.labinot.bajrami.quiztopicapp.domain.models

data class IssueReport(
    val questionId: String,
    val issueType: String,
    val additionalComment: String?,
    val userEmail: String?,
    val timestampMillis: Long
)