package com.openclassrooms.realestatemanager.presentation.realestatedetail

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.*
import com.openclassrooms.realestatemanager.domain.utils.DateUtil
import com.openclassrooms.realestatemanager.presentation.realestatedetail.components.ExpandableCard
import com.openclassrooms.realestatemanager.presentation.realestatedetail.components.ImageListItem
import com.openclassrooms.realestatemanager.presentation.ui.theme.RealEstateManagerComposeTheme
import java.text.SimpleDateFormat

@Preview
@Composable
fun RealEstateDetailScreen(
    @PreviewParameter(RealEstateProvider::class) realEstate: RealEstate,
    detailViewModel: RealEstateDetailViewModel = hiltViewModel()
) {
    RealEstateManagerComposeTheme {
        BoxWithConstraints {
            val state = detailViewModel.state.value
            val boxWithConstraintsScope = this
            val isTablet = (LocalContext.current.resources.configuration.screenLayout
                    and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
            Surface(color = MaterialTheme.colors.background) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(R.dimen.padding_fragment_start_end),
                            top = dimensionResource(R.dimen.margin_padding_size_medium),
                            end = dimensionResource(R.dimen.padding_fragment_start_end),
                            bottom = dimensionResource(R.dimen.margin_padding_size_medium)
                        )
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = "Media", style = MaterialTheme.typography.h2)
                    Spacer(modifier = Modifier.height(12.dp))
                    ImageListItem(
                        isTablet = isTablet,
                        imagesUri = realEstate.photoUri,
                        description = realEstate.photoDescription,
                        onImageClick = {  // TODO add full screen image display
                        })
                    Spacer(modifier = Modifier.height(12.dp))
                    if (isTablet) SpacerTablet()
                    Text(text = "Information", style = MaterialTheme.typography.h2)
                    Spacer(modifier = Modifier.height(12.dp))
                    RealEstateInformation(
                        state.cardIsExpanded,
                        { detailViewModel.onEvent(RealEstateDetailEvent.ExpandedCardTouch) },
                        maxWidth = boxWithConstraintsScope.maxWidth,
                        type = realEstate.type.toString(),
                        price = realEstate.price.toString(),
                        description = realEstate.description,
                        size = realEstate.size.toString(),
                        rooms = realEstate.room.toString()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (isTablet) SpacerTablet()
                    Text(text = "Location", style = MaterialTheme.typography.h2)
                    Spacer(modifier = Modifier.height(12.dp))
                    RealEstateLocation(isTablet, realEstate.address)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Real Estate entry at ${realEstate.entryDate}",
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.height(12.dp))
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
    rooms: String
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
        Text(text = "Map")
        val mapSize = if (isTablet) 400.dp else 200.dp
        Image(
            modifier = Modifier
                .border(3.dp, Color.Black)
                .size(mapSize),
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "map location",
            contentScale = ContentScale.Crop
        )
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

class RealEstateProvider : PreviewParameterProvider<RealEstate> {
    private val fakeRealEstate =
        RealEstateFactory(DateUtil(SimpleDateFormat("yyyy.MM.dd HH:mm:ss"))).createRealEstate(
            id = null,
            type = RealEstateType.APARTMENT,
            price = 300000,
            size = 40,
            room = 1,
            description = "Apartment on the first floor of an elegant period building in Earl's Court.The property consists of a bright room with a kitchenette and bathroom. The location is excellent, with the numerous restaurants and shops of Earl's Court Road and the underground stations of Earl's Court, West Brompton and West Kensington within walking distance from the property.",
            streetNumber = 3,
            streetName = "Nevern Square",
            postalCode = 533420,
            city = "London",
            country = "England",
            status = RealEstateStatus.AVAILABLE,
            lat = 0.0,
            lng = 1.1,
        )
    override val values: Sequence<RealEstate> = sequenceOf(fakeRealEstate)
}