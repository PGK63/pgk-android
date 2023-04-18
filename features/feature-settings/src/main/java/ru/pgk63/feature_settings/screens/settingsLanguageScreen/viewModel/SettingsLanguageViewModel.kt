package ru.pgk63.feature_settings.screens.settingsLanguageScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.pgk63.core_common.api.language.model.Language
import ru.pgk63.core_common.api.language.repository.LanguageRepository
import ru.pgk63.core_common.api.user.repository.UserRepository
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import javax.inject.Inject

@HiltViewModel
internal class SettingsLanguageViewModel @Inject constructor(
    private val languageRepository: LanguageRepository,
    private val userRepository: UserRepository,
    userDataSource: UserDataSource
): ViewModel() {

    private val _responseLanguageList = MutableStateFlow<PagingData<Language>>(PagingData.empty())
    val responseLanguageList = _responseLanguageList.asStateFlow()

    val user = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    fun getLanguageList(search: String? = null){
        viewModelScope.launch {
            languageRepository.getAll(search = search).collect {
                _responseLanguageList.value = it
            }
        }
    }

    fun updateLanguage(languageId: Int, languageCode: String) {
        viewModelScope.launch {
            userRepository.updateLanguage(languageId,languageCode)
        }
    }
}