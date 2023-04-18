package ru.pgk63.core_common.api.search

import retrofit2.http.GET
import retrofit2.http.Query
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.search.model.SearchResponse
import ru.pgk63.core_common.api.search.model.SearchType

interface SearchApi {

    @GET("/pgk63/api/Search")
    suspend fun search(
        @Query("search") searchText:String,
        @Query("type") type: SearchType? = null,
        @Query("pageNumber") pageNumber:Int,
        @Query("pageSize") pageSize:Int = PAGE_SIZE
    ): SearchResponse
}