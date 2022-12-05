package com.linhphan.presentation.feature.popup.textSizePopup

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.linhphan.presentation.extensions.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TextSizePopupViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var context: Context

    private lateinit var viewModel: TextSizePopupViewModel

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        viewModel = TextSizePopupViewModel()
        viewModel.setDefaultTextSize(context)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `onApplyNewTextScale success`(){
        //given

        //when
        viewModel.onApplyNewTextScale()

        //then
        val result = viewModel.onApplyTextSizeObservable.getOrAwaitValue()
        assertThat(result).isNotNull()
        assertThat(result).isAtLeast(50)
        assertThat(result).isAtMost(200)
    }
}