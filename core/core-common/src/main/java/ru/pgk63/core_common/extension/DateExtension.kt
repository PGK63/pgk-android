package ru.pgk63.core_common.extension

import android.content.Context
import android.text.format.DateUtils
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.pgk63.core_common.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun Date.toLocalDate(): LocalDate {
    return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

fun Date.parseToBaseDateFormat(context: Context): String {

    return if(this.isToday()){
        context.getString(R.string.today)
    }else if(this.isYesterday()){
        context.getString(R.string.yesterday)
    }else if(this.isTomorrow()){
        context.getString(R.string.tomorrow)
    }else {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        dateFormat.format(this)
    }
}

@Composable
fun Date.parseToBaseDateFormat(): String {

    return if(this.isToday()){
        stringResource(id = R.string.today)
    }else if(this.isYesterday()){
        stringResource(id = R.string.yesterday)
    }else if(this.isTomorrow()){
        stringResource(id = R.string.tomorrow)
    }else {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        dateFormat.format(this)
    }
}

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this.time)
}

fun Date.isYesterday(): Boolean {
    return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.isTomorrow(): Boolean {
    return DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)
}

fun Date.parseToNetworkFormat(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return dateFormat.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

@Composable
fun getWelcomeTimesOfDay(): String {

    val date = getCurrentDateTime()
    val dateFormat = SimpleDateFormat("HH", Locale.getDefault())

    return when (dateFormat.format(date).toInt()) {
        in 0..3 -> stringResource(id = R.string.welcome_night)

        in 4..9 -> stringResource(id = R.string.welcome_morning)

        in 10..17 -> stringResource(id = R.string.welcome_day)

        else -> stringResource(id = R.string.welcome_evening)
    }

}
