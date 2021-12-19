package com.openclassrooms.realestatemanager.presentation.realestatedetail

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.remote.RequestBuilder
import com.openclassrooms.realestatemanager.domain.models.Address
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateStatus
import com.openclassrooms.realestatemanager.presentation.realestatedetail.components.ExpandableCard
import com.openclassrooms.realestatemanager.presentation.realestatedetail.components.ImageListItem
import com.openclassrooms.realestatemanager.presentation.ui.theme.RealEstateManagerComposeTheme


@Composable
fun RealEstateDetailScreen(
    realEstate: RealEstate,
    onNavigate: (Int) -> Unit,
    viewModelDetail: RealEstateDetailViewModel
) {
    RealEstateManagerComposeTheme {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val state = viewModelDetail.state.value
            val isTablet = (LocalContext.current.resources.configuration.screenLayout
                    and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
            val boxWithConstraintsScope = this
            Surface(color = MaterialTheme.colors.background) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Box {
                        Column(
                            modifier = Modifier
                                .padding(
                                    start = dimensionResource(R.dimen.padding_fragment_start_end),
                                    top = dimensionResource(R.dimen.margin_padding_size_medium),
                                    end = dimensionResource(R.dimen.padding_fragment_start_end),
                                    bottom = dimensionResource(R.dimen.margin_padding_size_medium)
                                )
                        ) {
                            if (realEstate.photos.isNotEmpty()) {
                                Text(text = "Media", style = MaterialTheme.typography.h2)
                                Spacer(modifier = Modifier.height(12.dp))
                                ImageListItem(
                                    isTablet = isTablet,
                                    photos = realEstate.photos,
                                    onImageClick = {
                                    })
                                Spacer(modifier = Modifier.height(12.dp))
                                if (isTablet) SpacerTablet()
                            }
                            Text(text = "Information", style = MaterialTheme.typography.h2)
                            Spacer(modifier = Modifier.height(12.dp))
                            RealEstateInformation(
                                state.cardIsExpanded,
                                { viewModelDetail.onEvent(RealEstateDetailEvent.ExpandedCardTouch) },
                                maxWidth = boxWithConstraintsScope.maxWidth,
                                type = realEstate.type.toString(),
                                price = realEstate.price.toString(),
                                description = realEstate.description!!,
                                size = realEstate.size.toString(),
                                rooms = realEstate.room.toString(),
                                entryDate = realEstate.entryDate,
                                saleDate = realEstate.saleDate,
                                realEstateAgent = realEstate.realEstateAgent!!
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            if (isTablet) SpacerTablet()
                            Text(text = "Location", style = MaterialTheme.typography.h2)
                            Spacer(modifier = Modifier.height(12.dp))
                            RealEstateLocation(isTablet, realEstate.address!!)
                            Spacer(modifier = Modifier.height(12.dp))
                            if (isTablet) SpacerTablet()
                            Button(
                                onClick = {
                                    onNavigate(R.id.action_global_realEstateUpdateFragment)
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .width(200.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(R.color.blue400)
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                                    contentDescription = "icon edit button"
                                )
                                Text(text = " Edit real estate")
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        if (realEstate.status == RealEstateStatus.SOLD) {
                            Image(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(240.dp),
                                painter = painterResource(id = R.drawable.sold_banner),
                                contentDescription = "real estate sold"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RealEstateInformation(
    cardIsExpandedState: Boolean,
    expandedCardTouchEvent: () -> Unit,
    maxWidth: Dp,
    type: String,
    price: String,
    description: String,
    size: String,
    rooms: String,
    entryDate: String,
    saleDate: String?,
    realEstateAgent: String
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (maxWidth <= 610.dp) {
                Column {
                    Text(text = "Price $price€", style = MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Type $type", style = MaterialTheme.typography.body1)
                }
                Column(Modifier.padding(start = 16.dp)) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_square_foot_24),
                            contentDescription = "size"
                        )
                        Text(text = "Square meter : $size", Modifier.padding(start = 2.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_room_24),
                            contentDescription = "size"
                        )
                        Text(text = "Rooms : $rooms", Modifier.padding(start = 2.dp))
                    }
                }
            } else {
                Row {
                    Text(
                        text = "Price : $price€   Type : $type",
                        style = MaterialTheme.typography.body1
                    )
                    Icon(
                        modifier = Modifier.padding(start = 16.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_square_foot_24),
                        contentDescription = "size"
                    )
                    Text(text = "Square meter : $size", Modifier.padding(start = 2.dp))
                    Icon(
                        modifier = Modifier.padding(start = 16.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_room_24),
                        contentDescription = "size"
                    )
                    Text(text = "Rooms : $rooms", Modifier.padding(start = 2.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Entry on ${entryDate.dropLast(8)}", Modifier.padding(end = 16.dp))
        if (!saleDate.isNullOrBlank()) {
            Text(text = "Sold on $saleDate")
        } else {
            Text(text = "Status: Available")
        }

        Text(text = "Agent: $realEstateAgent")
        ExpandableCard(
            cardIsExpandedState = cardIsExpandedState,
            expandedCardTouchEvent = expandedCardTouchEvent,
            title = "Description",
            description = description,
            titleFontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun RealEstateLocation(isTablet: Boolean, address: Address) {

    Text(text = "${address.streetNumber} ${address.streetName}")
    Text(text = "${address.postalCode} ${address.city}")
    Text(text = address.country!!)
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val mapSize = if (isTablet) 400.dp else 200.dp
        Card(
            modifier = Modifier
                .size(mapSize),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(1.dp, Color.Gray),
            elevation = 2.dp
        ) {
            Image(
                modifier = Modifier
                    .size(mapSize),
                painter = rememberImagePainter(RequestBuilder.mapsStaticAPIUrl(address)),
                contentDescription = "map location",
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun SpacerTablet() {
    Spacer(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.LightGray,
                        Color.Transparent
                    )
                )
            )
    )
}