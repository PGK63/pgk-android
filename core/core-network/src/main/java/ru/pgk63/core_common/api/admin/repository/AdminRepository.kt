package ru.pgk63.core_common.api.admin.repository

import ru.pgk63.core_common.api.admin.AdminApi
import ru.pgk63.core_common.api.user.model.UserRegistrationBody
import ru.pgk63.core_common.common.response.ApiResponse
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val adminApi: AdminApi
): ApiResponse() {

    suspend fun registration(body: UserRegistrationBody) = safeApiCall {
        adminApi.registration(body)
    }
}