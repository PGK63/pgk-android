package ru.pgk63.core_common.compose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

fun annotatedLinkString(
    text: String,
    linkText: String = text,
    url: String,
    fontSize: TextUnit = 18.sp,
    fontFamily: FontFamily? = null
): AnnotatedString {

    return buildAnnotatedString {

        val startIndex = text.indexOf(linkText)
        val endIndex = startIndex + 4

        append(text)
        addStyle(
            style = SpanStyle(
                color = Color(0xff64B5F6),
                fontSize = fontSize,
                textDecoration = TextDecoration.Underline,
                fontFamily = fontFamily
            ), start = startIndex, end = endIndex
        )

        // attach a string annotation that stores a URL to the text "link"
        addStringAnnotation(
            tag = "URL",
            annotation = url,
            start = startIndex,
            end = endIndex
        )
    }
}