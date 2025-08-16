package com.labinot.bajrami.quiztopicapp.domain.repository

import com.labinot.bajrami.quiztopicapp.domain.models.IssueReport
import com.synac.quiztime.domain.util.DataError
import com.synac.quiztime.domain.util.Result

interface IssueReportRepository {

    suspend fun insertIssueReport(report: IssueReport): Result<Unit, DataError>

}