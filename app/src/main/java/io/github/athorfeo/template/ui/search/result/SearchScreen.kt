package io.github.athorfeo.template.ui.search.result

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import io.github.athorfeo.template.R
import io.github.athorfeo.template.model.Item
import io.github.athorfeo.template.model.SalePriceItem
import io.github.athorfeo.template.model.state.SearchItemsState
import io.github.athorfeo.template.navigation.Screen
import io.github.athorfeo.template.ui.theme.ApplicationTheme

@Composable
fun ResultSearchRoute(
    navController: NavController,
    viewModel: ResultSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ResultSearchScreen(
        viewModel.query,
        uiState
    ) { itemId ->
        val route = Screen.DetailItem.buildNavigate(itemId)
        navController.navigate(route)
    }
}

@Composable
fun ResultSearchScreen(
    query: String,
    uiState: SearchItemsState,
    onGoDetail: (String) -> Unit
) {
    LazyColumn {
        if (uiState.isLoading) {
            item {
                Text("Loading data...")
            }
        } else {
            item {
                CaptionResultSearchScreen(query)
            }

            items(uiState.results.size) { index ->
                val itemRow = uiState.results[index]
                ItemResultSearchScreen(itemRow, onGoDetail)
            }
        }
    }
}

@Composable
fun CaptionResultSearchScreen(query: String) {
    Column(
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
            .shadow(elevation = 1.dp, RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "${stringResource(R.string.result_search)} $query",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ItemResultSearchScreen(
    item: Item,
    onGoDetail: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp, 0.dp, 0.dp)
            .clickable { onGoDetail(item.id) },
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primaryContainer,
            thickness = 1.dp,
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 0.dp)
                .fillMaxWidth()
        ) {
            ImageItemResultSearchScreen(item)
            ContentItemResultSearchScreen(item)
        }
    }
}

@Composable
fun ImageItemResultSearchScreen(
    item: Item
) {
    Box(
        modifier = Modifier
            .shadow(elevation = 1.dp, RoundedCornerShape(8.dp))
            .background(color = Color.White, RoundedCornerShape(8.dp))
            .padding(0.dp, 8.dp, 0.dp, 8.dp)
    ) {
        AsyncImage(
            modifier = Modifier.width(80.dp).height(80.dp),
            model = item.thumbnail,
            contentDescription = null
        )
    }
}

@Composable
fun RowScope.ContentItemResultSearchScreen(
    item: Item
) {
    Column(modifier = Modifier.weight(0.6f)) {
        Text(
            modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp),
            text = item.title,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            modifier = Modifier.padding(4.dp, 16.dp, 0.dp, 0.dp),
            text = "$ ${item.price}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.padding(4.dp, 16.dp, 0.dp, 0.dp),
            text = "${item.availableQuantity} ${stringResource(R.string.item_available)}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
fun ResultSearchScreenPreview() {
    ApplicationTheme {
        val item = Item(
            "",
            "Title",
            "",
            "http://http2.mlstatic.com/D_877779-MLA73646533395_122023-O.jpg",
            "",
            0.0,
            SalePriceItem("", 0.0),
            1,
        )
        val results = listOf(item)
        val state = SearchItemsState(results = results)
        ResultSearchScreen("Query", state, {})
    }
}
