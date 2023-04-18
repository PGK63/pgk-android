package ru.pgk63.core_common.api.raportichka.model

import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.teacher.model.Teacher
import java.util.Date

data class RaportichkaResponse(
    val currentPage:Int,
    val totalPages:Int,
    val pageSize:Int,
    val totalCount:Int,
    val hasPrevious:Boolean,
    val hasNext:Boolean,
    val results:List<Raportichka>
)

data class Raportichka(
    val id:Int,
    val date:Date,
    val rowsCount:Int,
    val group: Group,
    val rows: List<ru.pgk63.core_model.deputyHeadma.RaportichkaRow>
)