package ru.pgk63.core_common.api.language

import retrofit2.http.GET
import retrofit2.http.Query
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.language.model.LanguageResponse

interface LanguageApi {

    @GET("/pgk63/api/Language")
    suspend fun getAll(
        @Query("search") search:String?,
        @Query("pageNumber") pageNumber:Int,
        @Query("pageSize") pageSize:Int = PAGE_SIZE,
    ): LanguageResponse
}