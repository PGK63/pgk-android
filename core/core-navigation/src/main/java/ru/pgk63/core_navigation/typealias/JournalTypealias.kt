package ru.pgk63.core_navigation.`typealias`

typealias onJournalDetailsScreen = (
    journalId: Int,
    course: Int,
    semester: Int,
    group: String,
    groupId: Int,
    journalSubjectId: Int,
    subjectTitle: String,
    subjectTeacher: String,
    subjectHorse: Int,
    subjectTeacherId: Int,
) -> Unit

typealias onJournalSubjectListScreen = (
    journalId: Int,
    course: Int,
    semester: Int,
    group: String,
    groupId: Int,
) -> Unit