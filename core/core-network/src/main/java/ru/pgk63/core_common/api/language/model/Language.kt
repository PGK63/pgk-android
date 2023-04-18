package ru.pgk63.core_common.api.language.model

data class LanguageResponse(
    val results: List<Language>
)

data class Language(
    val id:Int,
    val name:String,
    val nameEn:String,
    val code:String
)