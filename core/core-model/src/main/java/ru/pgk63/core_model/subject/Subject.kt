package ru.pgk63.core_model.subject

data class SubjectResponse(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val hasNext:Boolean,
    val results: List<Subject>
)

@kotlinx.serialization.Serializable
data class Subject(
    val id:Int,
    val subjectTitle:String
){
    override fun toString(): String {
        return subjectTitle
    }
}