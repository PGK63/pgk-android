package ru.pgk63.core_common.link.vedomostAttendance

import ru.pgk63.core_model.Month

fun getVedomostAttendanceLink(groupId: Int, month: Month, year: Int): String {
    return "https://api.cfif31.ru/pgk63/api/Group/${groupId}/Vedomost/Attendance?year=${year}&month=${month}"
}