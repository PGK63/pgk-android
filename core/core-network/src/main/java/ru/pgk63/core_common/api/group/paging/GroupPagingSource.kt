package ru.pgk63.core_common.api.group.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.group.GroupApi

class GroupPagingSource (
    private val groupApi: GroupApi,
    private val search: String? = null,
    private val course: List<Int>? = null,
    private val number: List<Int>? = null,
    private val specialityIds: List<Int>? = null,
    private val departmentIds: List<Int>? = null,
    private val classroomTeacherIds: List<Int>? = null,
    private val deputyHeadmaIds: List<Int>? = null,
    private val headmanIds: List<Int>? = null,
) : PagingSource<Int, Group>() {

    override fun getRefreshKey(state: PagingState<Int, Group>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Group> {
        return try {
            val page = params.key ?: 1

            val groups = groupApi.getAll(
                search = search,
                course = course,
                number = number,
                specialityIds = specialityIds,
                departmentIds = departmentIds,
                classroomTeacherIds = classroomTeacherIds,
                deputyHeadmaIds = deputyHeadmaIds,
                headmanIds = headmanIds,
                pageNumber = page
            )

            LoadResult.Page(
                data = groups.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if(groups.results.size < Constants.PAGE_SIZE) null else page + 1
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}