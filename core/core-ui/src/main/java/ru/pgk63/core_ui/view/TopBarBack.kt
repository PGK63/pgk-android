package ru.pgk63.core_ui.view

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.collapsingToolbar.CollapsingTitle
import ru.pgk63.core_ui.view.collapsingToolbar.CollapsingToolbar
import ru.pgk63.core_ui.view.collapsingToolbar.CollapsingToolbarScrollBehavior

@Composable
fun TopBarBack(
    modifier:Modifier = Modifier,
    title: String? = null,
    collapsingTitle: CollapsingTitle? = if(title == null) null else CollapsingTitle.large(titleText = title),
    scrollBehavior: CollapsingToolbarScrollBehavior? = null,
    backgroundColor: Color = PgkTheme.colors.primaryBackground,
    onBackClick: () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    additionalContent: (@Composable () -> Unit)? = null,
    centralContent: (@Composable () -> Unit)? = null
) {
    CollapsingToolbar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        collapsingTitle = collapsingTitle,
        scrollBehavior = scrollBehavior,
        additionalContent = additionalContent,
        centralContent = centralContent,
        navigationIcon = {

            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = PgkTheme.colors.primaryText
                )
            }

            navigationIcon()
        },
        actions = actions
    )
}

@Composable
fun TopBarBack(
    modifier:Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = PgkTheme.colors.primaryBackground,
        title = {
            Text(
                text = title,
                color = PgkTheme.colors.primaryText,
                style = PgkTheme.typography.toolbar,
                fontFamily = PgkTheme.fontFamily.fontFamily
            )
        },
        navigationIcon = {

            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = PgkTheme.colors.primaryText
                )
            }

            navigationIcon()
        },
        actions = actions
    )
}