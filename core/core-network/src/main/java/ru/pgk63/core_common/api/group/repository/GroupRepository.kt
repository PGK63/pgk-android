package ru.pgk63.core_common.api.group.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.group.GroupApi
import ru.pgk63.core_model.group.CreateGroupBody
import ru.pgk63.core_common.api.group.paging.GroupPagingSource
import ru.pgk63.core_model.student.StudentResponse
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.room.database.history.model.History
import ru.pgk63.core_database.room.database.history.model.HistoryType
import ru.pgk63.core_database.room.database.history.dataSource.HistoryDataSource
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val groupApi: GroupApi,
    private val historyDataSource: HistoryDataSource,
) : ApiResponse() {

    fun getAll(
        search: String? = null,
        course: List<Int>? = null,
        number: List<Int>? = null,
        specialityIds: List<Int>? = null,
        departmentIds: List<Int>? = null,
        classroomTeacherIds: List<Int>? = null,
        deputyHeadmaIds: List<Int>? = null,
        headmanIds: List<Int>? = null
    ): Flow<PagingData<Group>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            GroupPagingSource(
                groupApi = groupApi,
                search = search,
                course = course,
                number = number,
                specialityIds = specialityIds,
                departmentIds = departmentIds,
                classroomTeacherIds = classroomTeacherIds,
                deputyHeadmaIds = deputyHeadmaIds,
                headmanIds = headmanIds,
            )
        }.flow
    }

    suspend fun create(body: CreateGroupBody) = safeApiCall {
        groupApi.create(body)
    }

    suspend fun deleteById(id: Int) = safeApiCall {
        groupApi.deleteById(id)
    }

    suspend fun getById(id: Int): Result<Group> {
        val response = safeApiCall { groupApi.getById(id) }

        response.data?.let { group ->
            historyDataSource.add(History(
                contentId = group.id,
                title = group.toString(),
                historyType = HistoryType.GROUP
            ))
        }

        return response
    }

    suspend fun getStudentByGroupId(
        id: Int,
        pageNumber: Int = 1,
        pageSize: Int = PAGE_SIZE
    ): StudentResponse = groupApi.getStudentByGroupId(id, pageNumber, pageSize)

    suspend fun createRaportichka(groupId:Int) = safeApiCall { groupApi.createRaportichka(groupId) }

    suspend fun updateCourse(groupId: Int, course: Int) = safeApiCall {
        groupApi.updateCourse(groupId, course)
    }
}