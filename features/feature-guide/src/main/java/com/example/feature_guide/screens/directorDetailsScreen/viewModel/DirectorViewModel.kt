package com.example.feature_guide.screens.directorDetailsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.director.model.Director
import ru.pgk63.core_common.api.director.repository.DirectorRepository
import ru.pgk63.core_common.common.response.Result
import javax.inject.Inject

@HiltViewModel
internal class DirectorViewModel @Inject constructor(
    private val directorRepository: DirectorRepository
): ViewModel() {

    private val _responseDirectorDetails = MutableStateFlow<Result<Director>>(Result.Loading())
    val responseDirectorDetails = _responseDirectorDetails.asStateFlow()

    fun getDirectorDetails(directorId: Int) {
        viewModelScope.launch {
            _responseDirectorDetails.value = directorRepository.getById(directorId)
        }
    }
}