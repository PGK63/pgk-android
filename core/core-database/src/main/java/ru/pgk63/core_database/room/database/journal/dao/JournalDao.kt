package ru.pgk63.core_database.room.database.journal.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.pgk63.core_database.room.database.journal.model.JournalEntity
import ru.pgk63.core_database.room.database.journal.model.JournalEntityListItem

@Dao
interface JournalDao {

    @Upsert
    suspend fun add(journal: JournalEntity)

    @Query("SELECT id, semester, course, `group`, groupId FROM journal")
    fun getAll(): PagingSource<Int, JournalEntityListItem>

    @Query("SELECT * FROM journal WHERE id = :id")
    fun getById(id: Int): Flow<JournalEntity>

    @Query("SELECT COUNT(*) FROM journal")
    fun getCount(): Flow<Int>

    @Query(
        "SELECT CASE WHEN EXISTS (SELECT id FROM journal WHERE id=:id)" +
                " THEN 1 ELSE 0 END FROM journal")
    fun existsItemFlow(id: Int): Flow<Boolean>

    @Query(
        "SELECT CASE WHEN EXISTS (SELECT id FROM journal WHERE id=:id)" +
                " THEN 1 ELSE 0 END FROM journal")
    suspend fun existsItem(id: Int): Boolean

    @Query("UPDATE journal SET journalSubjectList = :journalSubjects WHERE (id = :journalId)")
    suspend fun updateJournalSubjects(journalId: Int, journalSubjects: String)

    @Query("UPDATE journal SET studentList = :students WHERE (id = :journalId)")
    suspend fun updateJournalStudents(journalId: Int, students: String)

    @Query("DELETE FROM journal")
    suspend fun clear()
}