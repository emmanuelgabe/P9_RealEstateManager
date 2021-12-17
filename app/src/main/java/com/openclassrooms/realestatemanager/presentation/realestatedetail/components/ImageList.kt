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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.presentation.ui.theme.TextWhite

@Composable
fun ImageListItem(
    modifier: Modifier = Modifier,
    isTablet: Boolean? = false,
    photos: List<Photo>,
    onImageClick: () -> Unit
) {
    val itemSize = if (isTablet!!) 240.dp else 160.dp
    LazyRow {
        items(photos) { photo -> //   items(imagesUri) { imagesUri ->
            Card(
                modifier = modifier
                    .size(itemSize)
                    .padding(3.dp)
                    .clickable(onClick = onImageClick),
                shape = MaterialTheme.shapes.large,
                elevation = 2.dp
            ) {
                Image(
                    painter = rememberImagePainter(photo.uri),
                    contentDescription = photo.description,
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
                        text = photo.description,
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
    val fakePhotoList = listOf<Photo>(
        Photo(
            Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/ic_launcher_background"),
            "photo 1"  ,false),
        Photo(
            Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/ic_launcher_background"),
            "photo 2"  ,false),
        Photo(
            Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/ic_launcher_background"),
            "photo 3"  ,false)

    )

    ImageListItem(
        isTablet = false,
        photos = fakePhotoList,
        onImageClick = { })
}
