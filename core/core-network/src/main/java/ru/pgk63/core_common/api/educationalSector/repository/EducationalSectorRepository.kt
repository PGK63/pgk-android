package ru.pgk63.core_common.api.educationalSector.repository

import ru.pgk63.core_common.api.educationalSector.EducationalSectorApi
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.common.response.ApiResponse
import javax.inject.Inject

class EducationalSectorRepository @Inject constructor(
    private val educationalSectorApi: EducationalSectorApi
): ApiResponse() {

    suspend fun registration(body: UserRegistrationBody) = safeApiCall {
        educationalSectorApi.registration(body)
    }
}