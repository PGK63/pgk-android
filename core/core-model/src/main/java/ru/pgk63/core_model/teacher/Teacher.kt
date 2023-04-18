package ru.pgk63.core_common.api.teacher.model

data class ResponseTeacher(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val hasNext:Boolean,
    val results: List<Teacher>
)

@kotlinx.serialization.Serializable
data class Teacher(
    val id:Int,
    val firstName:String,
    val lastName:String,
    val middleName:String? = null,
    val cabinet:String? = null,
    val information:String? = null,
    val photoUrl:String? = null
){
    override fun toString(): String {
        return fioAbbreviated()
    }

    fun fioAbbreviated():String{

        val middleName = if(middleName == null)
            ""
        else
            " ${middleName[0]}."

        return lastName + " ${firstName[0]}." + middleName
    }

    fun fio():String = lastName + " $firstName " + (middleName ?: "")

}