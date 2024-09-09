package io.github.athorfeo.template.ui.search.query

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.athorfeo.template.model.state.QuerySearchState
import io.github.athorfeo.template.ui.BUTTON_DIALOG_UI_TAG
import io.github.athorfeo.template.ui.SEARCH_BUTTON_UI_TAG
import io.github.athorfeo.template.ui.SEARCH_INPUT_UI_TAG
import io.github.athorfeo.template.ui.TEXT_DIALOG_UI_TAG
import io.github.athorfeo.template.ui.TITLE_DIALOG_UI_TAG
import io.github.athorfeo.template.ui.theme.ApplicationTheme
import io.github.athorfeo.template.util.AppException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class QuerySearchScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun default_state_query_search_screen_test() {
        composeTestRule.setContent {
            ApplicationTheme {
                QuerySearchScreen(QuerySearchState(), {}, {}, {})
            }
        }
        composeTestRule.onNodeWithTag(SEARCH_BUTTON_UI_TAG).assertIsNotEnabled()
    }

    @Test
    fun type_and_search_query_search_screen_test() {
        val state = MutableStateFlow(QuerySearchState())
        val onQueryChange: (String) -> Unit = { query ->
            state.update { state -> state.copy(query = query) }
        }
        val onSearch: () -> Unit = mockk()
        every { onSearch.invoke() } returns Unit

        composeTestRule.setContent {
            val uiState by state.collectAsStateWithLifecycle()
            ApplicationTheme {
                QuerySearchScreen(uiState, {}, onQueryChange, onSearch)
            }
        }

        composeTestRule.onNodeWithTag(SEARCH_BUTTON_UI_TAG).assertIsNotEnabled()
        composeTestRule.onNodeWithTag(SEARCH_INPUT_UI_TAG).performTextInput("Motorola")
        composeTestRule.onNodeWithTag(SEARCH_BUTTON_UI_TAG).assertIsEnabled()
        composeTestRule.onNodeWithTag(SEARCH_BUTTON_UI_TAG).performClick()

        verify { onSearch.invoke() }
    }

    @Test
    fun error_state_query_search_screen_test() {
        val exception = AppException()
        val state = QuerySearchState(exception = exception)
        val onDismissErrorDialog: () -> Unit = mockk()
        every { onDismissErrorDialog.invoke() } returns Unit

        composeTestRule.setContent {
            ApplicationTheme {
                QuerySearchScreen(state, onDismissErrorDialog, {}, {})
            }
        }

        composeTestRule.onNodeWithTag(TITLE_DIALOG_UI_TAG).assertExists()
        composeTestRule.onNodeWithTag(TEXT_DIALOG_UI_TAG).assertExists()
        composeTestRule.onNodeWithTag(BUTTON_DIALOG_UI_TAG).performClick()

        verify { onDismissErrorDialog.invoke() }
    }
}
