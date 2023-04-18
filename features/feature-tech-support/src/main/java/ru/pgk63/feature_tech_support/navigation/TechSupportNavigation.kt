package ru.pgk63.feature_tech_support.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import ru.pgk63.core_navigation.NavigationDestination
import ru.pgk63.feature_tech_support.screen.chatListScreen.ChatListRoute
import ru.pgk63.feature_tech_support.screen.chatScreen.ChatRoute

object TechSupportChatListDestination : NavigationDestination {
    override val route = "tech_support_chat_list_screen"
}

object TechSupportChatDestination : NavigationDestination {
    override val route = "tech_support_chat_screen"
    const val chatId = "chatId"
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.techSupportNavigation(
    onBackScreen: () -> Unit,
    onChatScreen: (chatId: Int) -> Unit
) {
    composable(
        route = TechSupportChatListDestination.route
    ){
        ChatListRoute(
            onChatScreen = onChatScreen,
            onBackScreen = onBackScreen
        )
    }

    composable(
        route = "${TechSupportChatDestination.route}?${TechSupportChatDestination.chatId}" +
                "={${TechSupportChatDestination.chatId}}",
        arguments = listOf(
            navArgument(TechSupportChatDestination.chatId){
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ){
        ChatRoute(
            chatId = it.arguments?.getString(TechSupportChatDestination.chatId)?.toIntOrNull(),
            onBackScreen = onBackScreen
        )
    }
}