package ru.pgk63.core_ui.view.dialog.model

import com.maxkeppeker.sheets.core.utils.BaseConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Base behaviors for the use-case views.
 */
object DialogBaseBehaviors {

    /**
     * A behavior construct for views to handle the automatic selection process when the button view is disabled.
     *
     * @param coroutine The coroutine scope that is used to execute the behavior on.
     * @param selection The base selection that is used to identify if the behavior should be enabled or not.
     * @param condition A custom additional condition when the button view is not enabled.
     * @param onDisableInput Listener that is invoked when the input is disabled as it should be blocked based on the behavior.
     * @param onSelection Listener that is invoked the automatic selection is executed.
     * @param onFinished Listener that is invoked when the behavior has ended.
     */
    fun autoFinish(
        coroutine: CoroutineScope,
        selection: DialogBaseSelection,
        condition: Boolean = true,
        onDisableInput: () -> Unit = {},
        onSelection: () -> Unit,
        onFinished: () -> Unit,
    ) {
        if (!selection.withButtonView && condition) {
            coroutine.launch {
                onDisableInput()
                delay(BaseConstants.SUCCESS_DISMISS_DELAY)
                onSelection()
                onFinished()
            }
        }
    }

}