package ru.pgk63.core_common.security.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.pgk63.core_common.R

enum class SecurityTelegramState(@StringRes val nameId: Int, @DrawableRes val iconId: Int) {
    SECURITY(R.string.telegram_security, R.drawable.security_security),
    INSECURITY(R.string.telegram_insecurity, R.drawable.insecurity_security)
}

fun getSecurityTelegramState(
    telegramId: Int?
): SecurityTelegramState {
    return if(telegramId == null){
        SecurityTelegramState.INSECURITY
    }else{
        SecurityTelegramState.SECURITY
    }
}