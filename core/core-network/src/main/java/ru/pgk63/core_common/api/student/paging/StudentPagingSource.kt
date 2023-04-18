package ru.pgk63.core_common.api.student.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.student.StudentApi

class StudentPagingSource(
    private val studentApi: StudentApi,
    private val search:String? = null,
    private val pageSize: Int = Constants.PAGE_SIZE,
    private val groupIds:List<Int>? = null
): PagingSource<Int, ru.pgk63.core_model.student.Student>() {
    override fun getRefreshKey(state: PagingState<Int, ru.pgk63.core_model.student.Student>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ru.pgk63.core_model.student.Student> {
        return try {

            val page = params.key ?: 1

            val data = studentApi.getAll(
                pageNumber = page,
                search = search,
                groupIds = groupIds,
                pageSize = pageSize
            )

            LoadResult.Page(
                data = data.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if(data.results.size < pageSize) null else page + 1
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}