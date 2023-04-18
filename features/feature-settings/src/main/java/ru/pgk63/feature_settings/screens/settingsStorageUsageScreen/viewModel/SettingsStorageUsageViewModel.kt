package ru.pgk63.feature_settings.screens.settingsStorageUsageScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.pgk63.core_database.room.database.history.dataSource.HistoryDataSource
import ru.pgk63.core_database.room.database.journal.dataSource.JournalDataSource
import ru.pgk63.core_database.room.database.system.dataSource.SystemDataSource
import javax.inject.Inject

@HiltViewModel
internal class SettingsStorageUsageViewModel @Inject constructor(
    private val historyDataSource: HistoryDataSource,
    private val journalDataSource: JournalDataSource,
    private val systemDataSource: SystemDataSource
): ViewModel() {

    private val _databaseSizeKb = MutableStateFlow(0)
    val databaseSizeKb = _databaseSizeKb.asStateFlow()

    val historyCount = historyDataSource.count
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val journalCount = journalDataSource.journalListCount
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    companion object {
        private const val DELAY_REMOVE_UPDATE_SIZE = 100L
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyDataSource.clear()
            delay(DELAY_REMOVE_UPDATE_SIZE)
            getDatabaseSizeInfo()
        }
    }

    fun clearJournal() {
        viewModelScope.launch {
            journalDataSource.clear()
            delay(DELAY_REMOVE_UPDATE_SIZE)
            getDatabaseSizeInfo()
        }
    }

    fun getDatabaseSizeInfo() {
        viewModelScope.launch {
            val databaseSizeInfo = systemDataSource.getDatabaseSizeInfo()
            if (databaseSizeInfo.page_count != null && databaseSizeInfo.page_size != null){
                val dbSizeInBytes = databaseSizeInfo.page_size!! * databaseSizeInfo.page_count!!

                _databaseSizeKb.value = dbSizeInBytes / 1000
            }
        }
    }
}