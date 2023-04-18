package ru.pgk63.core_common.api.director.model

data class DirectorResponse(
    val results: List<Director>
)

data class Director(
    val id:Int,
    val firstName:String,
    val lastName:String,
    val middleName:String?,
    val current:Boolean,
    val cabinet:String?,
    val information:String?,
    val photoUrl:String?
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