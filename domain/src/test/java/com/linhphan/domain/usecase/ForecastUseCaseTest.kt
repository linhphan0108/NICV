package com.linhphan.domain.usecase

import com.linhphan.domain.entity.ForecastEntity
import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.domain.repository.IForecastRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ForecastUseCaseTest {

    @Mock
    private lateinit var repository: IForecastRepository

    private lateinit var useCase: IForecastUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(ForecastUseCaseTest::class)
        useCase = ForecastUseCase(repository)
    }

    @After
    fun tearDown() {
    }

    //#region success test
    @Test
    fun `getForecast success`() = runBlockingTest {
        //given
        val cityName = "saigon"
        val count = 7

        //when
        Mockito.`when`(repository.getForecast(cityName, count))
            .thenReturn(
                flowOf(ResultWrapper.InProgress, ResultWrapper.Success(fakeForecast()))
            )

        //then
        val listResults = useCase.getForecast(cityName, count).toList()
        assertEquals(2, listResults.size)
        val progress = listResults[0]
        val result = listResults[1]
        assert(progress is ResultWrapper.InProgress)
        assert(result is ResultWrapper.Success)
        assert((result as ResultWrapper.Success).data.isNullOrEmpty().not())
    }

    @Test
    fun `getForecast error`() = runBlockingTest {
        //given
        val cityName = "saigon"
        val count = 7

        //when
        Mockito.`when`(repository.getForecast(cityName, count))
            .thenReturn(
                flowOf(ResultWrapper.InProgress, ResultWrapper.GenericError(-1, "error"))
            )

        //then
        val listResults = useCase.getForecast(cityName, count).toList()
        assertEquals(2, listResults.size)
        val progress = listResults[0]
        val result = listResults[1]
        assert(progress is ResultWrapper.InProgress)
        assert(result is ResultWrapper.GenericError)
        assert((result as ResultWrapper.GenericError).message.isNotEmpty())
    }
    //#endregion success test

    //#region error test

    //#endregion error test

    //dummy data
    private fun fakeForecast() = listOf(ForecastEntity(
            date = "CN, 20 Th11 2021",
            avgTemp = "26,5 C",
            humidity = "76%",
            pressure = "1009",
            desc = "light rain"
        )
    )
}