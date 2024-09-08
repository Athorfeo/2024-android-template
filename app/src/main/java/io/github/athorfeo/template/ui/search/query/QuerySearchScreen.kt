package io.github.athorfeo.template.ui.search.query

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.github.athorfeo.template.navigation.Screen
import io.github.athorfeo.template.ui.component.SearchTextField
import io.github.athorfeo.template.ui.theme.ApplicationTheme

@Composable
fun QuerySearchRoute(
    navController: NavController,
    viewModel: QuerySearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    QuerySearchScreen(
        query,
        viewModel::onQueryChange
    ) {
        val route = Screen.ResultSearch.buildNavigate(query)
        navController.navigate(route)
    }
}

@Composable
fun QuerySearchScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                verticalArrangement = Arrangement.Center
            ) {
                SearchTextField(query, onQueryChange, onSearch)
                Text(
                    modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 8.dp),
                    text = "Write what you want to search",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Athorfeo 2024",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    ApplicationTheme {
        QuerySearchScreen("Query", {}, {})
    }
}
