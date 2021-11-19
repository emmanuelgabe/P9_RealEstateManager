package com.openclassrooms.realestatemanager.presentation.realestatedetail.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit

@Composable
fun ExpandableCard(
    cardIsExpandedState: Boolean,
    expandedCardTouchEvent: () -> Unit,
    title: String,
    titleFontSize: TextUnit = MaterialTheme.typography.h6.fontSize,
    titleFontWeight: FontWeight = FontWeight.Black,
    description: String,
    descriptionFontSize: TextUnit = MaterialTheme.typography.subtitle1.fontSize,
    descriptionMaxLines: Int = 10
) {
    val rotationState by animateFloatAsState(
        targetValue = if (cardIsExpandedState) 180f else 0f
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable {
                expandedCardTouchEvent()
            },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = title,
                    style = MaterialTheme.typography.body1,
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                    onClick = { expandedCardTouchEvent() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (cardIsExpandedState) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.body1,
                    fontSize = descriptionFontSize,
                    maxLines = descriptionMaxLines,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun ExpandableCardPreview() {
    ExpandableCard(
        cardIsExpandedState = true,
        expandedCardTouchEvent = {},
        title = "My Title",
        description = "Apartment on the first floor of an elegant period building in Earl's Court.The property consists of a bright room with a kitchenette and bathroom. The location is excellent, with the numerous restaurants and shops of Earl's Court Road and the underground stations of Earl's Court, West Brompton and West Kensington within walking distance from the property."
    )
}