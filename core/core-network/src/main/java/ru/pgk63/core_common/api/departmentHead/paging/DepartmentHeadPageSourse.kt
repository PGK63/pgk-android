package ru.pgk63.core_common.api.departmentHead.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.departmentHead.DepartmentHeadApi
import ru.pgk63.core_model.departmentHead.DepartmentHead

internal class DepartmentHeadPageSourse(
    private val departmentHeadApi: DepartmentHeadApi,
    private val search: String? = null
): PagingSource<Int, DepartmentHead>() {

    override fun getRefreshKey(state: PagingState<Int, DepartmentHead>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DepartmentHead> {
        return try {

            val page = params.key ?: 1

            val data = departmentHeadApi.getAll(
                search = search,
                pageNumber = page
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