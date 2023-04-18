package ru.pgk63.core_common.api.department.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.department.DepartmentApi

class DepartmentPagingSource(
    private val departmentApi: DepartmentApi,
    private val search: String? = null
): PagingSource<Int, ru.pgk63.core_model.department.Department>() {

    override fun getRefreshKey(state: PagingState<Int, ru.pgk63.core_model.department.Department>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ru.pgk63.core_model.department.Department> {
        return try {

            val page = params.key ?: 1

            val data = departmentApi.getAll(
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