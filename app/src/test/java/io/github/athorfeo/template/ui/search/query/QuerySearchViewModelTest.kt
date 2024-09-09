package io.github.athorfeo.template.ui.search.query

import io.github.athorfeo.template.data.repository.SearchItemsRepository
import io.github.athorfeo.template.model.Result
import io.github.athorfeo.template.network.response.ItemSearchItems
import io.github.athorfeo.template.util.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class QuerySearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var searchItemsRepository: SearchItemsRepository

    private lateinit var viewModel: QuerySearchViewModel

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = QuerySearchViewModel(searchItemsRepository)
    }

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun init_query_search_state_test() = runTest {
        val job = launch(UnconfinedTestDispatcher()){ viewModel.uiState.collect() }

        Assert.assertFalse(viewModel.uiState.value.isLoading)
        Assert.assertNull(viewModel.uiState.value.exception)
        Assert.assertEquals("", viewModel.uiState.value.query)

        job.cancel()
    }

    @Test
    fun on_dismiss_error_test() = runTest {
        val exception = Exception()
        every { searchItemsRepository.searchItems(any()) } returns flow { emit(Result.Error(exception)) }
        val job = launch(UnconfinedTestDispatcher()){ viewModel.uiState.collect() }

        viewModel.onSearch(mockk())

        Assert.assertFalse(viewModel.uiState.value.isLoading)
        Assert.assertEquals(exception, viewModel.uiState.value.exception?.cause)
        Assert.assertEquals("", viewModel.uiState.value.query)

        viewModel.onDismissError()

        Assert.assertFalse(viewModel.uiState.value.isLoading)
        Assert.assertNull(viewModel.uiState.value.exception)
        Assert.assertEquals("", viewModel.uiState.value.query)

        verify {
            searchItemsRepository.searchItems(any())
        }

        job.cancel()
    }

    @Test
    fun on_query_change_test() = runTest {
        val job = launch(UnconfinedTestDispatcher()){ viewModel.uiState.collect() }

        val query = "query"
        viewModel.onQueryChange(query)

        Assert.assertFalse(viewModel.uiState.value.isLoading)
        Assert.assertNull(viewModel.uiState.value.exception)
        Assert.assertEquals(query, viewModel.uiState.value.query)

        job.cancel()
    }

    @Test
    fun loading_on_search_test() = runTest {
        every { searchItemsRepository.searchItems(any()) } returns flow { emit(Result.Loading) }
        val job = launch(UnconfinedTestDispatcher()){ viewModel.uiState.collect() }

        viewModel.onSearch(mockk())

        Assert.assertTrue(viewModel.uiState.value.isLoading)
        Assert.assertNull(viewModel.uiState.value.exception)
        Assert.assertEquals("", viewModel.uiState.value.query)

        verify {
            searchItemsRepository.searchItems(any())
        }

        job.cancel()
    }

    @Test
    fun error_on_search_test() = runTest {
        val exception = Exception()
        every { searchItemsRepository.searchItems(any()) } returns flow { emit(Result.Error(exception)) }
        val job = launch(UnconfinedTestDispatcher()){ viewModel.uiState.collect() }

        viewModel.onSearch(mockk())

        Assert.assertFalse(viewModel.uiState.value.isLoading)
        Assert.assertEquals(exception, viewModel.uiState.value.exception?.cause)
        Assert.assertEquals("", viewModel.uiState.value.query)

        verify {
            searchItemsRepository.searchItems(any())
        }

        job.cancel()
    }

    @Test
    fun success_on_search_test() = runTest {
        val list = listOf<ItemSearchItems>(mockk())
        every { searchItemsRepository.searchItems(any()) } returns flow { emit(Result.Success(list)) }
        val job = launch(UnconfinedTestDispatcher()){ viewModel.uiState.collect() }

        val onSuccess: (String) -> Unit = mockk()
        every { onSuccess.invoke(any()) } returns Unit

        viewModel.onSearch(onSuccess)

        verify {
            searchItemsRepository.searchItems(any())
            onSuccess.invoke(any())
        }

        job.cancel()
    }

    @Test
    fun empty_list_success_on_search_test() = runTest {
        val list = listOf<ItemSearchItems>()
        every { searchItemsRepository.searchItems(any()) } returns flow { emit(Result.Success(list)) }
        val job = launch(UnconfinedTestDispatcher()){ viewModel.uiState.collect() }

        viewModel.onSearch(mockk())

        Assert.assertFalse(viewModel.uiState.value.isLoading)
        Assert.assertNotNull(viewModel.uiState.value.exception)
        Assert.assertEquals("", viewModel.uiState.value.query)

        verify {
            searchItemsRepository.searchItems(any())
        }

        job.cancel()
    }
}
