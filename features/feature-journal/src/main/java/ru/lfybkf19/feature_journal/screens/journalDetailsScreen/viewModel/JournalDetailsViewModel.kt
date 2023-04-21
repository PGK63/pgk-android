package ru.lfybkf19.feature_journal.screens.journalDetailsScreen.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.pgk63.core_common.Constants
import ru.pgk63.core_common.api.journal.repository.JournalRepository
import ru.pgk63.core_common.api.student.repository.StudentRepository
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_database.room.database.journal.dataSource.JournalDataSource
import ru.pgk63.core_database.user.UserDataSource
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_model.journal.*
import ru.pgk63.core_model.student.Student
import javax.inject.Inject

@HiltViewModel
internal class JournalDetailsViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val studentRepository: StudentRepository,
    private val journalDataSource: JournalDataSource,
    userDataSource: UserDataSource
): ViewModel() {

    val userLocal = userDataSource.get()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserLocalDatabase())

    private val _responseJournalRowList = MutableStateFlow<PagingData<JournalRow>>(PagingData.empty())
    val responseJournalRowList = _responseJournalRowList.asStateFlow()

    private val _responseStudentList = MutableStateFlow<PagingData<Student>>(PagingData.empty())
    val responseStudentList = _responseStudentList.asStateFlow()

    private val _responseJournalResult = MutableStateFlow<Result<Unit?>?>(null)
    val responseJournalResult = _responseJournalResult.asStateFlow()

    private val _responseJournalExistsDatabase = MutableStateFlow(false)
    val responseJournalExistsDatabase = _responseJournalExistsDatabase.asStateFlow()

    private val _responseJournalTopics = MutableStateFlow<PagingData<JournalTopic>>(PagingData.empty())
    val responseJournalTopics = _responseJournalTopics.asStateFlow()

    fun updateLocalJournal(
        journalId: Int,
        course: Int,
        semester: Int,
        group: String,
        groupId: Int,
        journalSubject: List<JournalSubject>,
        studentList: List<Student>
    ) {
        viewModelScope.launch {
            try {
                if(journalDataSource.existsItem(journalId)) {
                    saveLocalJournal(
                        message = false,
                        journalId = journalId,
                        course = course,
                        semester = semester,
                        group = group,
                        groupId = groupId,
                        journalSubject = journalSubject,
                        studentList = studentList
                    )
                }
            }catch (e:Exception) {
                Log.e("updateLocalJournal", e.message.toString())
            }
        }
    }

    fun saveLocalJournal(
        message: Boolean = true,
        journalId: Int,
        course: Int,
        semester: Int,
        group: String,
        groupId: Int,
        journalSubject: List<JournalSubject>,
        studentList: List<Student>
    ) {
        viewModelScope.launch {
            try {
                if(message){
                    _responseJournalResult.value = Result.Loading()
                }

                journalDataSource.add(
                    journalId = journalId,
                    course = course,
                    semester = semester,
                    group = group,
                    groupId = groupId,
                    journalSubject = journalSubject,
                    studentList = studentList
                )

                if(message){
                    _responseJournalResult.value = Result.Success(null)
                }
            }catch (e:Exception){
                if(message){
                    _responseJournalResult.value = Result.Error(e.message.toString())
                }
            }
        }
    }

//    fun getJournalSubjects(
//        journalId:Int? = null,
//        network: Boolean
//    ) {
//        viewModelScope.launch {
//            if(network){
//                journalRepository.getJournalSubjects(
//                    journalId = journalId
//                ).cachedIn(viewModelScope).collect {
//                    _responseJournalRowList.value = it
//                }
//            }else if(journalId != null){
//                journalDataSource.getSubjectList(journalId).collect {
//                    _responseJournalSubjectsList.value = PagingData.from(it)
//                }
//            }
//        }
//    }

    fun getJournalRowAll(journalSubjectId: Int, network: Boolean) {
        viewModelScope.launch {
            journalRepository.getJournalRow(journalSubjectId)
                .cachedIn(viewModelScope).collect {
                    _responseJournalRowList.value = it
                }
        }
    }

    fun getStudents(journalId: Int?,groupIds: List<Int>, network: Boolean) {
        viewModelScope.launch {
            if(network) {
                studentRepository.getAll(
                    groupIds = groupIds,
                    pageSize = Constants.JOURNAL_STUDENTS_PAGE_SIZE
                ).cachedIn(viewModelScope).collect {
                    _responseStudentList.value = it
                }
            }else if(journalId != null) {
                journalDataSource.getStudentList(journalId).collect {
                    _responseStudentList.value = PagingData.from(it)
                }
            }
        }
    }

    fun journalExistsDatabase(id: Int) {
        viewModelScope.launch {
            try {
                journalDataSource.existsItemFlow(id).collect {
                    _responseJournalExistsDatabase.value = it
                }
            }catch (e:Exception) {
                Log.e("journalExistsDatabase", e.message.toString())
            }
        }
    }

    fun createColumn(body: CreateJournalColumnBody) {
        viewModelScope.launch {
            _responseJournalResult.value = Result.Loading()
            _responseJournalResult.value = journalRepository.createColumn(body)
        }
    }

    fun updateEvaluation(columnId: Int, evaluation: JournalEvaluation) {
        viewModelScope.launch {
            _responseJournalResult.value = Result.Loading()
            _responseJournalResult.value = journalRepository.updateEvaluation(columnId, evaluation)
        }
    }

    fun deleteColumn(id: Int) {
        viewModelScope.launch {
            _responseJournalResult.value = Result.Loading()
            _responseJournalResult.value = journalRepository.deleteColumn(id)
        }
    }

    fun getTopics(journalSubjectId: Int) {
        viewModelScope.launch {
            journalRepository.getJournalTopics(journalSubjectId)
                .cachedIn(viewModelScope).collect {
                    _responseJournalTopics.value = it
                }
        }
    }
}