package ru.pgk63.core_common.api.raportichka.pageSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.raportichka.RaportichkaApi
import ru.pgk63.core_common.api.raportichka.model.Raportichka

class RaportichkaPageSource(
    private val raportichkaApi: RaportichkaApi,
    private val confirmation:Boolean? = null ,
    private val onlyDate:String? = null,
    private val startDate:String? = null,
    private val endDate:String? = null,
    private val groupIds:List<Int>? = null,
    private val subjectIds:List<Int>? = null,
    private val classroomTeacherIds:List<Int>? = null,
    private val numberLessons:List<Int>? = null,
    private val teacherIds:List<Int>? = null,
    private val studentIds:List<Int>? = null
):PagingSource<Int, Raportichka>() {

    override fun getRefreshKey(state: PagingState<Int, Raportichka>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Raportichka> {
        return try {

            val page = params.key ?: 1

            val data = raportichkaApi.getRaportichkaAll(
                pageNumber = page,
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

            LoadResult.Page(
                data = data.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if(data.results.size < PAGE_SIZE) null else page + 1
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}