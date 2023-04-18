package ru.pgk63.core_common.api.techSupport.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.techSupport.model.Chat
import ru.pgk63.core_common.api.techSupport.repository.TechSupportRepository

class ChatPagingSource(
    private val techSupportRepository: TechSupportRepository
): PagingSource<Int, Chat>() {
    override fun getRefreshKey(state: PagingState<Int, Chat>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Chat> {
        return try {

            val page = params.key ?: 1

            val data = techSupportRepository.getChatAll(pageNumber = page).results

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if(data.size < Constants.PAGE_SIZE) null else page + 1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}