package com.labinot.bajrami.quiztopicapp.data.repositoryImpl

import com.labinot.bajrami.quiztopicapp.data.mapper.toIssueReportDto
import com.labinot.bajrami.quiztopicapp.data.remote.RemoteQuizDataSource
import com.labinot.bajrami.quiztopicapp.domain.models.IssueReport
import com.labinot.bajrami.quiztopicapp.domain.repository.IssueReportRepository
import com.synac.quiztime.domain.util.DataError
import com.synac.quiztime.domain.util.Result

class IssueReportRepositoryImp(
    private val remoteDataSource: RemoteQuizDataSource
): IssueReportRepository {

    override suspend fun insertIssueReport(report: IssueReport): Result<Unit, DataError> {
        val reportDto = report.toIssueReportDto()
        return remoteDataSource.insertIssueReport(reportDto)
    }

}