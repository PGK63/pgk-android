package ru.pgk63.core_common.api.journal.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.journal.JournalApi

class JournalPagingSource(
    private val journalApi: JournalApi,
    private val course:List<Int>? = null,
    private val semesters:List<Int>? = null,
    private val groupIds:List<Int>? = null,
    private val specialityIds:List<Int>? = null,
    private val departmentIds:List<Int>? = null,
): PagingSource<Int, ru.pgk63.core_model.journal.Journal>() {

    override fun getRefreshKey(state: PagingState<Int, ru.pgk63.core_model.journal.Journal>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ru.pgk63.core_model.journal.Journal> {
        return try {

            val page = params.key ?: 1

            val data = journalApi.getAll(
                course = course,
                semesters = semesters,
                groupIds = groupIds,
                specialityIds = specialityIds,
                departmentIds = departmentIds,
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