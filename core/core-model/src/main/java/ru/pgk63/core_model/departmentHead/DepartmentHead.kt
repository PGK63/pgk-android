package ru.pgk63.core_model.departmentHead

data class DepartmentHeadResponse(
    val results: List<DepartmentHead>
)

@kotlinx.serialization.Serializable
data class DepartmentHead(
    val id:Int,
    val firstName:String,
    val lastName:String,
    val middleName:String?,
    val cabinet:String? = null,
    val information:String? = null,
    val photoUrl:String?
){
    override fun toString(): String {
        return fioAbbreviated()
    }

    fun fioAbbreviated():String{

        val middleName = if(middleName?.getOrNull(0) == null)
            ""
        else
            "${middleName[0]}."

        return lastName + " ${firstName[0]}." + " $middleName."
    }

    fun fio():String = lastName + " $firstName " + (middleName ?: "")

}