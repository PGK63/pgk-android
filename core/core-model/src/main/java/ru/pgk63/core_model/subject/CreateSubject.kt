package ru.pgk63.core_model.subject

data class CreateSubjectBody(
    val subjectTitle:String
)

data class CreateSubjectResponse(
    val id: Int
)