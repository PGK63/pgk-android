package ru.pgk63.core_common.api.raportichka.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.raportichka.RaportichkaApi
import ru.pgk63.core_common.api.raportichka.model.Raportichka
import ru.pgk63.core_common.api.raportichka.model.RaportichkaAddRowBody
import ru.pgk63.core_common.api.raportichka.model.RaportichkaUpdateRowBody
import ru.pgk63.core_common.api.raportichka.pageSource.RaportichkaPageSource
import ru.pgk63.core_common.common.response.ApiResponse
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

class RaportichkaRepository @Inject constructor(
    private val raportichkaApi: RaportichkaApi
): ApiResponse() {

    fun getRaportichkaAll(
        confirmation:Boolean? = null ,
        onlyDate:String? = null,
        startDate:String? = null,
        endDate:String? = null,
        groupIds:List<Int>? = null,
        subjectIds:List<Int>? = null,
        classroomTeacherIds:List<Int>? = null,
        numberLessons:List<Int>? = null,
        teacherIds:List<Int>? = null,
        studentIds:List<Int>? = null
    ): Flow<PagingData<Raportichka>> {

        return Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)){
            RaportichkaPageSource(
                raportichkaApi = raportichkaApi,
                confirmation = confirmation,
                onlyDate = onlyDate,
                startDate = startDate,
                endDate = endDate,
                groupIds = groupIds,
                subjectIds = subjectIds,
                classroomTeacherIds = classroomTeacherIds,
                numberLessons = numberLessons,
                teacherIds = teacherIds,
                studentIds = studentIds
            )
        }.flow
    }

    suspend fun raportichkaAddRow(
        raportichkaId:Int,
        body: RaportichkaAddRowBody
    ): Result<Unit?> = safeApiCall { raportichkaApi.raportichkaAddRow(raportichkaId, body) }

    suspend fun updateRow(
        rowId:Int,
        body: RaportichkaUpdateRowBody
    ) = safeApiCall { raportichkaApi.updateRow(rowId, body) }

    suspend fun updateConfirmation(rowId: Int) = safeApiCall { raportichkaApi.updateConfirmation(rowId) }

    suspend fun deleteRow(rowId:Int) = safeApiCall { raportichkaApi.deleteRow(rowId) }
}