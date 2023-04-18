package ru.pgk63.core_common.api.language.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.api.language.LanguageApi
import ru.pgk63.core_common.api.language.model.Language
import ru.pgk63.core_common.api.language.paging.LanguagePageSource
import ru.pgk63.core_common.common.response.ApiResponse
import javax.inject.Inject

class LanguageRepository @Inject constructor(
    private val languageApi: LanguageApi
): ApiResponse() {

    fun getAll(search:String? = null): Flow<PagingData<Language>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)){
            LanguagePageSource(
                languageApi = languageApi,
                search = search
            )
        }.flow
    }
}