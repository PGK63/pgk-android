package ru.pgk63.core_common.validation

import ru.pgk63.core_common.R
import ru.pgk63.core_common.extension.isNumber

fun emailValidation(email:String): Pair<Boolean, Int?> {
    if(email.isEmpty())
        return false to R.string.field_required

    if(!email.any("."::contains) || !email.any("@"::contains))
        return false to R.string.incorrect_email

    return true to null
}

fun passwordValidation(password:String): Pair<Boolean, Int?> {
    if(password.isEmpty())
        return false to R.string.field_required

    if(password.length < 4)
        return false to R.string.incorrect_password

    return true to null
}

fun nameValidation(name:String): Pair<Boolean, Int?> {
    if(name.isEmpty())
        return false to R.string.field_required

    return true to null
}

fun numberValidation(number:String): Pair<Boolean, Int?> {
    if(number.isEmpty())
        return false to R.string.field_required

    if(!number.isNumber())
        return false to R.string.not_number

    return true to null
}

fun numberGroupValidation(number:String): Pair<Boolean, Int?> {
    if(number.isEmpty())
        return false to R.string.field_required

    if(!number.isNumber())
        return false to R.string.not_number

    if(number.length < 2)
        return false to R.string.minimum_three_characters

    return true to null
}