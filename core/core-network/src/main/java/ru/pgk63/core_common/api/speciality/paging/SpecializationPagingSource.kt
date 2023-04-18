package ru.pgk63.core_common.api.speciality.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.speciality.SpecializationApi
import ru.pgk63.core_common.api.speciality.model.Specialization

class SpecializationPagingSource(
    private val specializationApi: SpecializationApi,
    private val search:String? = null,
    private val departmentIds:List<Int>? = null,
):PagingSource<Int, Specialization>() {

    override fun getRefreshKey(state: PagingState<Int, Specialization>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Specialization> {
        return try {

            val page = params.key ?: 1

            val data = specializationApi.getAll(
                pageNumber = page,
                search = search,
                departmentIds = departmentIds
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