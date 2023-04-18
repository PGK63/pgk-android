package ru.pgk63.core_database.room.database.journal.dataSource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.pgk63.core_common.Constants.PAGE_SIZE
import ru.pgk63.core_common.extension.fromJson
import ru.pgk63.core_database.room.database.journal.dao.JournalDao
import ru.pgk63.core_database.room.database.journal.model.JournalEntity
import ru.pgk63.core_model.journal.JournalSubject
import ru.pgk63.core_model.student.Student
import javax.inject.Inject

class JournalDataSource @Inject constructor(
    private val dao: JournalDao
) {
    private val gson = Gson()

    val journalList = Pager(PagingConfig(pageSize = PAGE_SIZE)){
        dao.getAll()
    }.flow

    val journalListCount = dao.getCount()

    suspend fun add(
        journalId: Int,
        course: Int,
        semester: Int,
        group: String,
        groupId: Int,
        journalSubject: List<JournalSubject>,
        studentList: List<Student>,
    ) {
        val entity = JournalEntity(
            id = journalId,
            course = course,
            semester = semester,
            group = group,
            journalSubjectList = gson.toJson(journalSubject),
            studentList = gson.toJson(studentList),
            groupId = groupId
        )

        dao.add(entity)
    }

    fun getSubjectList(id: Int): Flow<List<JournalSubject>> {
        return dao.getById(id).map { gson.fromJson(it.journalSubjectList) }
    }

    fun getStudentList(id: Int): Flow<List<Student>> {
        return dao.getById(id).map { gson.fromJson(it.studentList) }
    }

    fun existsItemFlow(id: Int): Flow<Boolean> {
        return dao.existsItemFlow(id)
    }

    suspend fun existsItem(id: Int): Boolean {
        return dao.existsItem(id)
    }

    suspend fun updateJournalSubjects(journalId: Int, journalSubjects: String) {
        dao.updateJournalSubjects(journalId, journalSubjects)
    }

    suspend fun updateJournalStudents(journalId: Int, students: String) {
        dao.updateJournalStudents(journalId, students)
    }

    suspend fun clear() {
        dao.clear()
    }
}