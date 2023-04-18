package ru.pgk63.core_common.extension

fun String?.toIntArrayOrNull(): List<Int>? {
    return this
        ?.removeSurrounding("[","]")
        ?.replace(" ","")
        ?.split(",")
        ?.map {
            if(it == "null")
                return null
            else
                it.toInt()
        }
}

fun String?.isContentNull(): String? {
    return if(this == "null") null else this
}

fun String.isNumber(): Boolean {
    val regex = "-?\\d+(\\.\\d+)?".toRegex()
    return this.matches(regex)
}

fun String.addCharAtIndex(char: Char, index: Int) =
    StringBuilder(this).apply { insert(index, char) }.toString()