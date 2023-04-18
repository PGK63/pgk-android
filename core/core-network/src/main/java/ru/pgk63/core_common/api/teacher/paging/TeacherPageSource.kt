package ru.pgk63.core_common.api.teacher.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.teacher.TeacherApi
import ru.pgk63.core_common.api.teacher.model.Teacher

class TeacherPageSource(
    private val teacherApi: TeacherApi,
    private val search:String? = null,
    private val subjectIds:List<Int>? = null
): PagingSource<Int, Teacher>() {

    override fun getRefreshKey(state: PagingState<Int, Teacher>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Teacher> {
        return try {

            val page = params.key ?: 1

            val data = teacherApi.getAll(
                pageNumber = page,
                search = search,
                subjectIds = subjectIds
            )

            LoadResult.Page(
                data = data.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if(data.results.size < Constants.PAGE_SIZE) null else page + 1
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}