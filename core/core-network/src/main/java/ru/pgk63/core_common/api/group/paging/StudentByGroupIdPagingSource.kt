package ru.pgk63.core_common.api.group.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_common.api.group.repository.GroupRepository

class StudentByGroupIdPagingSource(
    private val groupRepository: GroupRepository,
    private val groupId:Int
): PagingSource<Int, ru.pgk63.core_model.student.Student>() {

    override fun getRefreshKey(state: PagingState<Int, ru.pgk63.core_model.student.Student>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ru.pgk63.core_model.student.Student> {
        return try {
            val nextPage = params.key ?: 1

            val students = groupRepository.getStudentByGroupId(
                id = groupId,
                pageNumber = nextPage
            ).results

            LoadResult.Page(
                data = students,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage.plus(1)
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}