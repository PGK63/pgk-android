package ru.pgk63.core_model.raportichka

import androidx.annotation.StringRes
import ru.pgk63.core_model.R

enum class RaportichkaCause(@StringRes val text: Int) {
    STATEMENTS(R.string.STATEMENTS),
    SICKNESS(R.string.SICKNESS),
    ABSENTEEISM(R.string.ABSENTEEISM),
    PRIKAZ(R.string.PRIKAZ)
}