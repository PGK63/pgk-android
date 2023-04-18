package ru.pgk63.core_ui.view.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.theme.MainTheme
import ru.pgk63.core_ui.theme.PgkTheme

@Composable
fun VerticalListItemShimmer(modifier: Modifier = Modifier) {

    val brush = rememberAnimationShimmer()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 15.dp, vertical = 10.dp),
        shape = PgkTheme.shapes.cornersStyle,
        backgroundColor = PgkTheme.colors.secondaryBackground,
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Spacer(modifier = Modifier.padding(5.dp))
            //Text
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.7f)
                    .background(brush)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            //Text
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.7f)
                    .background(brush)
            )

            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
}

@Preview
@Composable
private fun VerticalListItemShimmerPreview() {
    MainTheme {
        LazyColumn {
            items(30){
                VerticalListItemShimmer()
            }
        }
    }
}