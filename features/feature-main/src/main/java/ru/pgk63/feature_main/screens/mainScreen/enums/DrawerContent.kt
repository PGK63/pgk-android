package ru.pgk63.feature_main.screens.mainScreen.enums

import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.icon.ResIcons

internal enum class DrawerContent(val nameId:Int, val iconId:Int) {
    PROFILE(nameId = R.string.profile, iconId = ResIcons.profile),
    JOURNAL(nameId = R.string.journal, iconId = ResIcons.journal),
    RAPORTICHKA(nameId = R.string.raportichka, iconId = ResIcons.raportichka),
//    SCHEDULE(nameId = R.string.schedule, iconId = ResIcons.schedule),
    GROUPS(nameId = R.string.groups, iconId = ResIcons.groups),
    STUDENTS(nameId = R.string.students, iconId = ResIcons.student),
    GUIDE(nameId = R.string.guide, iconId = ResIcons.guide),
    DEPARTMENS(nameId = R.string.departmens, iconId = ResIcons.departmen),
    SPECIALTIES(nameId = R.string.specialties, iconId = ResIcons.specialization),
    SUBJECTS(nameId = R.string.subjects, iconId = ResIcons.subject),
    SETTINGS(nameId = R.string.settings, iconId = ResIcons.settings),
    HELP(nameId = R.string.help, iconId = ResIcons.help)
}