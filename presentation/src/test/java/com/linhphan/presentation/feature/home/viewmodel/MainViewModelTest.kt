package com.linhphan.presentation.feature.home.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.linhphan.domain.entity.ForecastEntity
import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.domain.usecase.IForecastUseCase
import com.linhphan.presentation.extensions.getOrAwaitValue
import com.linhphan.presentation.model.ForecastModel
import com.linhphan.presentation.util.SecuredSharePreference
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var forecastUseCase: IForecastUseCase
    @Mock
    private lateinit var preference: SecuredSharePreference

    private lateinit var context: Context
    //@Mock
    //private lateinit var forecastUsersObserver: Observer<ResultWrapper<List<ForecastModel>>>

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        // Context of the presentation under test.
        context = InstrumentationRegistry.getInstrumentation().targetContext
        viewModel = MainViewModel(forecastUseCase, preference)
    }

    @After
    fun tearDown() {
    }


    @Test
    fun fetchForecasts_success() = runBlockingTest{
        //given
        val cityName = "saigon"
        val count = 7
        val forecastEntity = fakeForecastEntity()
        val useCaseResult = ResultWrapper.Success(listOf(forecastEntity))
        Mockito.clearInvocations(forecastUseCase)

        //when
        Mockito.`when`(forecastUseCase.getForecast(cityName, count))
            .thenReturn(flowOf(useCaseResult))
        viewModel.fetchForecasts(context, cityName)

        //then
//        Mockito.verify(forecastUsersObserver).onChanged()
        Mockito.verify(forecastUseCase).getForecast(cityName, count)
        val result = viewModel.forecastsObservable.getOrAwaitValue()
        assertThat(result).isNotNull()
        assert(result is ResultWrapper.Success)
        assertThat((result as ResultWrapper.Success).data).isNotNull()
        assertThat(result.data).isNotEmpty()
    }

    @Test
    fun fetchForecasts_error() = runBlockingTest{
        //given
        val cityName = "saigon"
        val count = 7
        Mockito.clearInvocations(forecastUseCase)

        //when
        Mockito.`when`(forecastUseCase.getForecast(cityName, count))
            .thenAnswer{ flowOf(ResultWrapper.GenericError(-1, "error")) }
        viewModel.fetchForecasts(context, cityName)

        //then
        Mockito.verify(forecastUseCase).getForecast(cityName, count)
        val result = viewModel.forecastsObservable.getOrAwaitValue()
        assertThat(result).isNotNull()
        assert(result is ResultWrapper.GenericError)
        assertThat((result as ResultWrapper.GenericError).message).isNotNull()
    }

    //dummy data
    private fun fakeForecastEntity(): ForecastEntity {
        return ForecastEntity(
            date = "CN, 20 Th11 2021",
            avgTemp = "26,5 C",
            humidity = "76%",
            pressure = "1009",
            desc = "light rain"
        )
    }

    private fun fakeForecastModel() = ForecastModel(
        date = "CN, 20 Th11 2021",
        avgTemp = "26,5 C",
        humidity = "76%",
        pressure = "1009",
        desc = "light rain"
    )

}