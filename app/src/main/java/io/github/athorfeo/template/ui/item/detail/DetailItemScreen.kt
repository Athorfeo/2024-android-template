package io.github.athorfeo.template.ui.item.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.athorfeo.template.model.state.ItemState
import io.github.athorfeo.template.ui.theme.ApplicationTheme

@Composable
fun DetailItemRoute(
    viewModel: DetailItemViewModel = hiltViewModel()
) {
    val uiState by viewModel.item.collectAsStateWithLifecycle()
    DetailItemScreen(uiState)
}

@Composable
fun DetailItemScreen(
    uiState: ItemState
) {
    uiState.item?.let { Text("Item: $it") }
}

@Preview
@Composable
fun ItemScreenPreview() {
    ApplicationTheme {
        DetailItemScreen(ItemState())
    }
}
