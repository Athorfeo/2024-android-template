package io.github.athorfeo.template.data.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import io.github.athorfeo.template.data.repository.SearchItemsRepository
import io.github.athorfeo.template.domain.GetItemInCacheUseCase
import io.github.athorfeo.template.domain.GetLastSearchItemsUseCase
import io.github.athorfeo.template.domain.OpenUrlBrowserUseCase
import io.github.athorfeo.template.model.Result
import io.github.athorfeo.template.network.response.ItemSearchItems
import io.github.athorfeo.template.network.response.SalePriceResultSearchItems
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.Before

class OpenUrlBrowserUseCaseTest {
    @MockK
    private lateinit var applicationContext: Context

    private lateinit var usecase: OpenUrlBrowserUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        usecase = OpenUrlBrowserUseCase(applicationContext)
    }

    @After
    fun after(){
        unmockkAll()
    }

    @Test
    fun open_url_test() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()

        mockkConstructor(Intent::class)
        val intent: Intent = mockk()
        every { anyConstructed<Intent>().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) } returns intent
        every { applicationContext.startActivity(any()) } returns Unit

        val url = "https://www.google.com/"
        usecase.openUrl(url)

        verify {
            anyConstructed<Intent>().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(any())
        }
    }
}
