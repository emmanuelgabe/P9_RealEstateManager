package com.openclassrooms.realestatemanager.presentation.realestatedetail.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.ui.theme.TextWhite

@Composable
fun ImageListItem(
    modifier: Modifier = Modifier,
    isTablet: Boolean? = false,
    imagesUri: List<Uri>?,
    description: List<String>?,
    onImageClick: () -> Unit
) {
    val itemSize = if (isTablet!!) 240.dp else 160.dp
    //TODO replace by uri
    val fakeUri = listOf<Painter>(
        painterResource(id = R.drawable.ic_launcher_background),
        painterResource(id = R.drawable.ic_launcher_background),
        painterResource(id = R.drawable.ic_launcher_foreground),
        painterResource(id = R.drawable.ic_launcher_background),
        painterResource(id = R.drawable.ic_launcher_background)
    )
    val fakeDescription = listOf(
        "photo 1",
        "photo 2",
        "photo 3",
        "photo 1",
        "photo 2",
    )
    LazyRow {
        itemsIndexed(items = fakeUri) { index, imagesUri -> //   items(imagesUri) { imagesUri ->
            Card(
                modifier = modifier
                    .size(itemSize)
                    .padding(3.dp)
                    .clickable(onClick = onImageClick),
                shape = MaterialTheme.shapes.large,
                elevation = 2.dp
            ) {
                Image(
                    painter = imagesUri, contentDescription = fakeDescription?.get(index),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent, Color.Black
                                ), startY = 300f
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp), contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = fakeDescription!![index],
                        style = TextStyle(color = TextWhite, fontSize = 16.sp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ImageListItemPreview() {
    ImageListItem(
        isTablet = false,
        imagesUri = null,
        description = null,
        onImageClick = { })
}
