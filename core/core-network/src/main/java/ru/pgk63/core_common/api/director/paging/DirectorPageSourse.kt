package ru.pgk63.core_common.api.director.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.director.DirectorApi
import ru.pgk63.core_common.api.director.model.Director

class DirectorPageSourse(
    private val directorApi: DirectorApi,
    private val search:String? = null,
    private val current:Boolean? = null
): PagingSource<Int, Director>() {

    override fun getRefreshKey(state: PagingState<Int, Director>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Director> {
        return try {

            val page = params.key ?: 1

            val data = directorApi.getAll(
                search = search,
                current = current,
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