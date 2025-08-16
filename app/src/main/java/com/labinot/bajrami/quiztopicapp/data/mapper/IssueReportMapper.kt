package com.labinot.bajrami.quiztopicapp.data.mapper

import com.labinot.bajrami.quiztopicapp.domain.models.IssueReport
import com.labinot.bajrami.quiztopicapp.domain.util.toFormattedDateTimeString
import com.synac.quiztime.data.remote.dto.IssueReportDto


fun IssueReport.toIssueReportDto() = IssueReportDto(
    questionId = questionId,
    issueType = issueType,
    additionalComment = additionalComment,
    userEmail = userEmail,
    timestamp = timestampMillis.toFormattedDateTimeString()
)