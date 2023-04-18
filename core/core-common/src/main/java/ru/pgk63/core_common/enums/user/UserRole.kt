package ru.pgk63.core_common.enums.user

import androidx.annotation.StringRes
import ru.pgk63.core_common.R

enum class UserRole(@StringRes val nameId:Int) {
    STUDENT(R.string.student),
    HEADMAN(R.string.headman),
    DEPUTY_HEADMAN(R.string.deputy_headman),
    TEACHER(R.string.teacher),
    EDUCATIONAL_SECTOR(R.string.educational_sector),
    DEPARTMENT_HEAD(R.string.department_head),
    DIRECTOR(R.string.director),
    ADMIN(R.string.admin);
}