package ru.pgk63.core_ui.view

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*
import ru.pgk63.core_ui.R

enum class LottieAnimationErrorType(@RawRes val resId: Int) {
    ERROR_404_CUT(R.raw.error_404_cut),
    ERROR_404_PERSON(R.raw.error_404_person),
    ERROR_404_ROBOT(R.raw.error_404_robot),
}

enum class LottieAnimationType(@RawRes val resId: Int) {
    WELCOME(R.raw.welcome),
    LOADING(R.raw.loading),
    EMPTY(R.raw.empty),
    EMAIL(R.raw.email),
    TELEGRAM(R.raw.telegram),
    PASSWORD_SECURITY(R.raw.password_security),
    FORGOT_PASSWORD(R.raw.forgot_password),
    SWIPE_TO_SCREEN(R.raw.swipe_to_screen),
    REGISTRATION(R.raw.registration),
    UPDATE_APP(R.raw.update_app),
    DETAILS_APP(R.raw.details_app)
}

@Composable
fun BaseLottieAnimation(
    modifier: Modifier = Modifier,
    type:LottieAnimationType,
    iterations:Int = LottieConstants.IterateForever
){
    val compositionResult =
        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(type.resId))

    Animation(
        modifier = modifier,
        iterations = iterations,
        compositionResult = compositionResult
    )
}

@Composable
fun BaseLottieAnimation(
    modifier: Modifier = Modifier,
    type: LottieAnimationErrorType,
    iterations:Int = LottieConstants.IterateForever
){
    val compositionResult =
        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(type.resId))

    Animation(
        modifier = modifier,
        iterations = iterations,
        compositionResult = compositionResult
    )
}

@Composable
private fun Animation(
    modifier: Modifier = Modifier,
    iterations:Int = LottieConstants.IterateForever,
    compositionResult: LottieCompositionResult
){
    val progress = animateLottieCompositionAsState(
        composition = compositionResult.value,
        iterations = iterations,
    )

    LottieAnimation(
        composition = compositionResult.value,
        progress = progress.progress,
        modifier = modifier
    )
}