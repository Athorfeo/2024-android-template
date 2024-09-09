package io.github.athorfeo.template.ui.search.result

import androidx.lifecycle.SavedStateHandle
import io.github.athorfeo.template.domain.GetItemInCacheUseCase
import io.github.athorfeo.template.domain.GetLastSearchItemsUseCase
import io.github.athorfeo.template.domain.OpenUrlBrowserUseCase
import io.github.athorfeo.template.model.Item
import io.github.athorfeo.template.model.state.ItemState
import io.github.athorfeo.template.model.state.QuerySearchState
import io.github.athorfeo.template.navigation.Screen
import io.github.athorfeo.template.util.AppException
import io.github.athorfeo.template.util.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ResultSearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var fetchSearchItems: GetLastSearchItemsUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun init_ui_state_test()= runTest {
        val query = "query"
        every { savedStateHandle.get<String>(Screen.QUERY_ARG) } returns query
        every { fetchSearchItems.get() } returns flow {  }

        val viewModel = ResultSearchViewModel(savedStateHandle, fetchSearchItems)
        val job = launch(UnconfinedTestDispatcher()){ viewModel.items.collect() }

        Assert.assertEquals(0, viewModel.items.value.size)

        verify {
            savedStateHandle.get<String>(Screen.QUERY_ARG)
        }

        job.cancel()
    }

    @Test
    fun collect_ui_state_test() = runTest {
        val query = "query"
        every { savedStateHandle.get<String>(Screen.QUERY_ARG) } returns query

        val items = listOf<Item>(mockk())
        every { fetchSearchItems.get() } returns flow { emit(items) }

        val viewModel = ResultSearchViewModel(savedStateHandle, fetchSearchItems)
        val job = launch(UnconfinedTestDispatcher()){ viewModel.items.collect() }

        Assert.assertEquals(items, viewModel.items.value)

        job.cancel()

        verify {
            savedStateHandle.get<String>(Screen.QUERY_ARG)
            fetchSearchItems.get()
        }
    }
}
