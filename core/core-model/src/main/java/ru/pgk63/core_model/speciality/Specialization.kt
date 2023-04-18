package ru.pgk63.core_common.api.speciality.model

data class SpecializationResponse(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val results: List<Specialization>
)

@kotlinx.serialization.Serializable
data class Specialization(
    val id:Int,
    val number:String,
    val name:String,
    val nameAbbreviation:String,
    val qualification:String,
    val department: ru.pgk63.core_model.department.Department
){
    override fun toString(): String {
        return nameAbbreviation
    }
}