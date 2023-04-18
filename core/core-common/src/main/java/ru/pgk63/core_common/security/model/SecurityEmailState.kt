package ru.pgk63.core_common.security.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.pgk63.core_common.R

enum class SecurityEmailState(@StringRes val nameId: Int, @DrawableRes val iconId: Int) {
    SECURITY(R.string.email_security, R.drawable.security_security),
    EMAIL_VERIFICATION(R.string.email_verification, R.drawable.email_blocker),
    INSECURITY(R.string.email_insecurity, R.drawable.insecurity_security)
}

fun getSecurityEmailState(
    email: String?,
    emailVerification: Boolean,
): SecurityEmailState {
    return if(email == null){
       SecurityEmailState.INSECURITY
    } else if (!emailVerification){
        SecurityEmailState.EMAIL_VERIFICATION
    }else {
        SecurityEmailState.SECURITY
    }
}