package io.github.athorfeo.template.ui.search.result

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.github.athorfeo.template.model.state.DetailSearchState
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
    uiState: DetailSearchState,
    onGoDetail: (String) -> Unit
) {
    LazyColumn {
        if (uiState.isLoading) {
            item {
                Text("Loading data...")
            }
        } else {
            item {
                Text("Query: $query")
            }

            items(uiState.resultsQuery.size) { index ->
                val itemRow = uiState.resultsQuery[index]
                Text(
                    modifier = Modifier.clickable { onGoDetail(itemRow)  },
                    text = "Item: $itemRow"
                )
            }
        }
    }
}

@Preview
@Composable
fun ResultSearchScreenPreview() {
    ApplicationTheme {
        ResultSearchScreen("Query", DetailSearchState(), {})
    }
}
