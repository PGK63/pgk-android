package ru.pgk63.core_common.api.teacher.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.teacher.TeacherApi
import ru.pgk63.core_common.api.teacher.model.Teacher
import ru.pgk63.core_common.api.teacher.paging.TeacherPageSource
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.room.database.history.dataSource.HistoryDataSource
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.room.database.history.model.HistoryType
import javax.inject.Inject

class TeacherRepository @Inject constructor(
    private val teacherApi: TeacherApi,
    private val historyDataSource: HistoryDataSource
): ApiResponse() {

    suspend fun registration(body: UserRegistrationBody) = safeApiCall {
        teacherApi.registration(body)
    }

    fun getAll(
        search:String? = null,
        subjectIds:List<Int>? = null
    ): Flow<PagingData<Teacher>> {
        return Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)){
            TeacherPageSource(
                teacherApi = teacherApi,
                search = search,
                subjectIds = subjectIds
            )
        }.flow
    }

    suspend fun getById(teacherId: Int): Result<Teacher> {
        val response = safeApiCall { teacherApi.getById(teacherId) }

        response.data?.let { teacher ->
            historyDataSource.add(
                History(
                    contentId = teacher.id,
                    title = teacher.fioAbbreviated(),
                    historyType = HistoryType.TEACHER
                )
            )
        }

        return response
    }

    suspend fun teacherAddSubject(teacherId: Int, subjectId: Int) = safeApiCall {
        teacherApi.teacherAddSubject(teacherId,subjectId)
    }
}