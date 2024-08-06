package com.example.searchcity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.R
import com.example.domain.entity.City

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(modifier: Modifier, component: SearchCityComponent) {

    val state by component.model.collectAsStateWithLifecycle()

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = modifier
                .align(Alignment.TopCenter)
                .semantics {
                    traversalIndex = 0f
                }
                .focusRequester(focusRequester)
                .background(MaterialTheme.colorScheme.primary),
            placeholder = {Text(text = "")},
            query = state.searchQuery,
            onQueryChange = { component.changeSearchQuery(it) },
            onSearch = { component.onClickSearch() },
            active = true,
            onActiveChange = {},
            leadingIcon = {
                IconButton(onClick = { component.onClickBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "search icon"
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = { component.onClickSearch() }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            },
            shape = MaterialTheme.shapes.large,
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primary,
                dividerColor = com.example.common.theme.TextColorAccent,
                inputFieldColors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.White
                )
            ),

        ) {
            when(val searchState = state.searchState) {
                SearchCityStore.State.SearchState.EmptyResult -> {
                    Column(
                        modifier = modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.found_nothing),
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                SearchCityStore.State.SearchState.Error -> {
                    Column(
                        modifier = modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.something_went_wrong),
                            modifier = modifier.padding(8.dp)
                        )
                    }
                }
                SearchCityStore.State.SearchState.Initial -> {}
                is SearchCityStore.State.SearchState.Loaded -> {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(searchState.cities) { city ->
                            CityCard(
                                city = city,
                                onCityClick = { clickCity ->
                                    component.onClickCity(clickCity)
                                }
                            )
                        }
                    }
                }
                SearchCityStore.State.SearchState.Loading -> SearchCityLoading()
            }
        }
    }

}

@Composable
private fun CityCard(
    modifier: Modifier = Modifier,
    city: City,
    onCityClick: (City) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .clickable { onCityClick(city) }
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
                .background(Color.Transparent),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = city.name.split(',')[0],
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun SearchCityLoading(modifier: Modifier = Modifier,) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}