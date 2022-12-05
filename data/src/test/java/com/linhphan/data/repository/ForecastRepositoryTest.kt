package com.linhphan.data.repository

import android.accounts.NetworkErrorException
import com.google.gson.Gson
import com.linhphan.data.entity.ListForecastResponse
import com.linhphan.data.local.ForecastDB
import com.linhphan.data.local.ForecastDao
import com.linhphan.data.local.TblForecast
import com.linhphan.data.mapper.toTblForecast
import com.linhphan.data.remote.Services
import com.linhphan.data.testRule.MainCoroutineRule
import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.domain.repository.IForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception
import kotlin.test.assertEquals

private const val tag = "ForecastRepositoryTest"

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ForecastRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var database: ForecastDB

    @Mock
    private lateinit var service: Services

    private lateinit var repository: IForecastRepository

    private val appId = "appId"
    private val gson = Gson()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(ForecastRepositoryTest::class)
        repository = ForecastRepository(database, service, gson, appId, Dispatchers.Main)
        Mockito.`when`(database.getForecastDao())
            .thenReturn(Mockito.mock(ForecastDao::class.java))
    }

    @After
    fun tearDown() {
    }

    //#region success test
    @Test
    fun `getForecast success from local only`() {
        mainCoroutineRule.runBlockingTest {
            //given
            val cityName = "saigon"
            val dao = database.getForecastDao()
            val localResponse = listOf(fakeTodayForeCastFromDB())

            //when
            Mockito.`when`(dao.queryForecast(cityName))
                .thenReturn(localResponse)

            //then
            repository.getForecast(cityName).collect { result ->
                assert(result is ResultWrapper.Success)
                assert((result as ResultWrapper.Success).data.isNullOrEmpty().not())
            }
        }
    }

    @Test
    fun `getForecast success from remote only`() {
        mainCoroutineRule.runBlockingTest {
            //given
            val cityName = "saigon"
            val dao = database.getForecastDao()
            val remoteResponse = fakeForecastFromRemote()
            val tblForecasts = remoteResponse.forecasts.map { it.toTblForecast(cityName, gson) }

            //when
            Mockito.`when`(dao.storeForecasts(tblForecasts))
                .thenReturn(longArrayOf())
            Mockito.`when`(dao.queryForecast(cityName))
                .thenReturn(null)
            Mockito.`when`(service.getForecast(cityName, 7, appId))
                .thenReturn(remoteResponse)

            //then
            val listResults = repository.getForecast(cityName).toList()
            assertEquals(2, listResults.size)
            val progress = listResults[0]
            val remoteResult = listResults[1]
            assert(progress is ResultWrapper.InProgress)
            assert(remoteResult is ResultWrapper.Success)
            assert((remoteResult as ResultWrapper.Success).data.isNullOrEmpty().not())
        }
    }

    @Test
    fun `getForecast success from both local and remote`() {
        mainCoroutineRule.runBlockingTest {
            //given
            val cityName = "saigon"
            val dao = database.getForecastDao()
            val localResponse = listOf(fakeOldForeCastFromDB())
            val remoteResponse = fakeForecastFromRemote()
            val tblForecasts = remoteResponse.forecasts.map { it.toTblForecast(cityName, gson) }

            //when
            Mockito.`when`(dao.storeForecasts(tblForecasts))
                .thenReturn(longArrayOf())
            Mockito.`when`(dao.queryForecast(cityName))
                .thenReturn(localResponse)
            Mockito.`when`(service.getForecast(cityName, 7, appId))
                .thenReturn(remoteResponse)

            //then
            val listResults = repository.getForecast(cityName).toList()
            assertEquals(3, listResults.size)
            val localResult = listResults[0]
            val progress = listResults[1]
            val remoteResult = listResults[2]
            assert(localResult is ResultWrapper.Success)
            assert((localResult as ResultWrapper.Success).data.isNullOrEmpty().not())
            assert(progress is ResultWrapper.InProgress)
            assert(remoteResult is ResultWrapper.Success)
            assert((remoteResult as ResultWrapper.Success).data.isNullOrEmpty().not())
        }
    }
    //#endregion success test

    //#region error test
    @Test
    fun `getForecast local error but remote success`() {
        mainCoroutineRule.runBlockingTest {
            //given
            val cityName = "saigon"
            val dao = database.getForecastDao()
            val remoteResponse = fakeForecastFromRemote()
            val tblForecasts = remoteResponse.forecasts.map { it.toTblForecast(cityName, gson) }

            //when
            Mockito.`when`(dao.storeForecasts(tblForecasts))
                .thenReturn(longArrayOf())
            Mockito.`when`(dao.queryForecast(cityName))
                .thenAnswer { throw Exception("local error") }
            Mockito.`when`(service.getForecast(cityName, 7, appId))
                .thenReturn(remoteResponse)

            //then
            val listResults = repository.getForecast(cityName).toList()
            assertEquals(2, listResults.size)
            val progress = listResults[0]
            val remoteResult = listResults[1]
            assert(progress is ResultWrapper.InProgress)
            assert(remoteResult is ResultWrapper.Success)
            assert((remoteResult as ResultWrapper.Success).data.isNullOrEmpty().not())
        }
    }

    @Test
    fun `getForecast local success but remote error`() {
        mainCoroutineRule.runBlockingTest {
            //given
            val cityName = "saigon"
            val dao = database.getForecastDao()
            val remoteResponse = fakeForecastFromRemote()
            val tblForecasts = remoteResponse.forecasts.map { it.toTblForecast(cityName, gson) }

            //when
            Mockito.`when`(dao.queryForecast(cityName))
                .thenReturn(null)
            Mockito.`when`(service.getForecast(cityName, 7, appId))
                .thenAnswer { throw NetworkErrorException() }

            //then
            val listResults = repository.getForecast(cityName).toList()
            assertEquals(2, listResults.size)
            val progress = listResults[0]
            val remoteResult = listResults[1]
            assert(progress is ResultWrapper.InProgress)
            assert(remoteResult is ResultWrapper.GenericError)
            assert((remoteResult as ResultWrapper.GenericError).message.isBlank().not())
        }
    }

    @Test
    fun `getForecast error both local and remote`() {
        mainCoroutineRule.runBlockingTest {
            //given
            val cityName = "saigon"
            val dao = database.getForecastDao()
            val remoteResponse = fakeForecastFromRemote()
            val tblForecasts = remoteResponse.forecasts.map { it.toTblForecast(cityName, gson) }

            //when
            Mockito.`when`(dao.queryForecast(cityName))
                .thenAnswer { throw Exception("local error") }
            Mockito.`when`(service.getForecast(cityName, 7, appId))
                .thenAnswer { throw NetworkErrorException() }

            //then
            val listResults = repository.getForecast(cityName).toList()
            assertEquals(2, listResults.size)
            val progress = listResults[0]
            val remoteResult = listResults[1]
            assert(progress is ResultWrapper.InProgress)
            assert(remoteResult is ResultWrapper.GenericError)
            assert((remoteResult as ResultWrapper.GenericError).message.isBlank().not())
        }
    }
    //#endregion error test


    //dummy data
    private fun fakeTodayForeCastFromDB() = TblForecast(
        cityName = "saigon",
        date = System.currentTimeMillis(),
        temp = "{}",
        pressure = 1010,
        humidity = 58,
        weathers = "[]"
    )

    private fun fakeOldForeCastFromDB() = TblForecast(
        cityName = "saigon",
        date = 1637208000000,
        temp = "{}",
        pressure = 1010,
        humidity = 58,
        weathers = "[]"
    )

    private fun fakeForecastFromRemote(): ListForecastResponse{
        return gson.fromJson(fakeForecastFromRemoteJson(), ListForecastResponse::class.java)
    }

    private fun fakeForecastFromRemoteJson(): String{
        return "{\n" +
                "    \"city\":\n" +
                "    {\n" +
                "        \"id\": 1580578,\n" +
                "        \"name\": \"Ho Chi Minh City\",\n" +
                "        \"coord\":\n" +
                "        {\n" +
                "            \"lon\": 106.6667,\n" +
                "            \"lat\": 10.8333\n" +
                "        },\n" +
                "        \"country\": \"VN\",\n" +
                "        \"population\": 0,\n" +
                "        \"timezone\": 25200\n" +
                "    },\n" +
                "    \"cod\": \"200\",\n" +
                "    \"message\": 5.315425,\n" +
                "    \"cnt\": 7,\n" +
                "    \"list\":\n" +
                "    [\n" +
                "        {\n" +
                "            \"dt\": 1637121600,\n" +
                "            \"sunrise\": 1637102993,\n" +
                "            \"sunset\": 1637144814,\n" +
                "            \"temp\":\n" +
                "            {\n" +
                "                \"day\": 32.44,\n" +
                "                \"min\": 23.61,\n" +
                "                \"max\": 32.97,\n" +
                "                \"night\": 24.84,\n" +
                "                \"eve\": 27.57,\n" +
                "                \"morn\": 23.69\n" +
                "            },\n" +
                "            \"feels_like\":\n" +
                "            {\n" +
                "                \"day\": 37.48,\n" +
                "                \"night\": 25.76,\n" +
                "                \"eve\": 31.05,\n" +
                "                \"morn\": 24.65\n" +
                "            },\n" +
                "            \"pressure\": 1010,\n" +
                "            \"humidity\": 58,\n" +
                "            \"weather\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"id\": 501,\n" +
                "                    \"main\": \"Rain\",\n" +
                "                    \"description\": \"moderate rain\",\n" +
                "                    \"icon\": \"10d\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"speed\": 1.89,\n" +
                "            \"deg\": 358,\n" +
                "            \"gust\": 2.6,\n" +
                "            \"clouds\": 78,\n" +
                "            \"pop\": 0.97,\n" +
                "            \"rain\": 6.45\n" +
                "        },\n" +
                "        {\n" +
                "            \"dt\": 1637208000,\n" +
                "            \"sunrise\": 1637189418,\n" +
                "            \"sunset\": 1637231215,\n" +
                "            \"temp\":\n" +
                "            {\n" +
                "                \"day\": 30.66,\n" +
                "                \"min\": 23.53,\n" +
                "                \"max\": 30.87,\n" +
                "                \"night\": 24.64,\n" +
                "                \"eve\": 27.71,\n" +
                "                \"morn\": 23.62\n" +
                "            },\n" +
                "            \"feels_like\":\n" +
                "            {\n" +
                "                \"day\": 35.88,\n" +
                "                \"night\": 25.54,\n" +
                "                \"eve\": 31.52,\n" +
                "                \"morn\": 24.54\n" +
                "            },\n" +
                "            \"pressure\": 1009,\n" +
                "            \"humidity\": 67,\n" +
                "            \"weather\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"id\": 500,\n" +
                "                    \"main\": \"Rain\",\n" +
                "                    \"description\": \"light rain\",\n" +
                "                    \"icon\": \"10d\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"speed\": 2.2,\n" +
                "            \"deg\": 133,\n" +
                "            \"gust\": 4.01,\n" +
                "            \"clouds\": 96,\n" +
                "            \"pop\": 0.66,\n" +
                "            \"rain\": 2.53\n" +
                "        },\n" +
                "        {\n" +
                "            \"dt\": 1637294400,\n" +
                "            \"sunrise\": 1637275844,\n" +
                "            \"sunset\": 1637317616,\n" +
                "            \"temp\":\n" +
                "            {\n" +
                "                \"day\": 29.04,\n" +
                "                \"min\": 23.7,\n" +
                "                \"max\": 31.04,\n" +
                "                \"night\": 24.68,\n" +
                "                \"eve\": 28.62,\n" +
                "                \"morn\": 23.7\n" +
                "            },\n" +
                "            \"feels_like\":\n" +
                "            {\n" +
                "                \"day\": 33.59,\n" +
                "                \"night\": 25.58,\n" +
                "                \"eve\": 33.14,\n" +
                "                \"morn\": 24.61\n" +
                "            },\n" +
                "            \"pressure\": 1010,\n" +
                "            \"humidity\": 74,\n" +
                "            \"weather\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"id\": 500,\n" +
                "                    \"main\": \"Rain\",\n" +
                "                    \"description\": \"light rain\",\n" +
                "                    \"icon\": \"10d\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"speed\": 2.75,\n" +
                "            \"deg\": 168,\n" +
                "            \"gust\": 5.05,\n" +
                "            \"clouds\": 77,\n" +
                "            \"pop\": 0.9,\n" +
                "            \"rain\": 3.21\n" +
                "        },\n" +
                "        {\n" +
                "            \"dt\": 1637380800,\n" +
                "            \"sunrise\": 1637362270,\n" +
                "            \"sunset\": 1637404019,\n" +
                "            \"temp\":\n" +
                "            {\n" +
                "                \"day\": 28.95,\n" +
                "                \"min\": 23.65,\n" +
                "                \"max\": 30.9,\n" +
                "                \"night\": 24.76,\n" +
                "                \"eve\": 30.01,\n" +
                "                \"morn\": 23.65\n" +
                "            },\n" +
                "            \"feels_like\":\n" +
                "            {\n" +
                "                \"day\": 33.18,\n" +
                "                \"night\": 25.67,\n" +
                "                \"eve\": 35.06,\n" +
                "                \"morn\": 24.5\n" +
                "            },\n" +
                "            \"pressure\": 1009,\n" +
                "            \"humidity\": 73,\n" +
                "            \"weather\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"id\": 500,\n" +
                "                    \"main\": \"Rain\",\n" +
                "                    \"description\": \"light rain\",\n" +
                "                    \"icon\": \"10d\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"speed\": 3.08,\n" +
                "            \"deg\": 157,\n" +
                "            \"gust\": 6.94,\n" +
                "            \"clouds\": 81,\n" +
                "            \"pop\": 0.9,\n" +
                "            \"rain\": 3.72\n" +
                "        },\n" +
                "        {\n" +
                "            \"dt\": 1637467200,\n" +
                "            \"sunrise\": 1637448696,\n" +
                "            \"sunset\": 1637490423,\n" +
                "            \"temp\":\n" +
                "            {\n" +
                "                \"day\": 29.55,\n" +
                "                \"min\": 23.76,\n" +
                "                \"max\": 32.38,\n" +
                "                \"night\": 24.71,\n" +
                "                \"eve\": 29.93,\n" +
                "                \"morn\": 23.76\n" +
                "            },\n" +
                "            \"feels_like\":\n" +
                "            {\n" +
                "                \"day\": 33.97,\n" +
                "                \"night\": 25.56,\n" +
                "                \"eve\": 33.95,\n" +
                "                \"morn\": 24.67\n" +
                "            },\n" +
                "            \"pressure\": 1011,\n" +
                "            \"humidity\": 70,\n" +
                "            \"weather\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"id\": 500,\n" +
                "                    \"main\": \"Rain\",\n" +
                "                    \"description\": \"light rain\",\n" +
                "                    \"icon\": \"10d\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"speed\": 3.26,\n" +
                "            \"deg\": 142,\n" +
                "            \"gust\": 8.21,\n" +
                "            \"clouds\": 91,\n" +
                "            \"pop\": 0.36,\n" +
                "            \"rain\": 0.27\n" +
                "        },\n" +
                "        {\n" +
                "            \"dt\": 1637553600,\n" +
                "            \"sunrise\": 1637535123,\n" +
                "            \"sunset\": 1637576828,\n" +
                "            \"temp\":\n" +
                "            {\n" +
                "                \"day\": 29.52,\n" +
                "                \"min\": 23.89,\n" +
                "                \"max\": 30.61,\n" +
                "                \"night\": 24.6,\n" +
                "                \"eve\": 29.27,\n" +
                "                \"morn\": 23.89\n" +
                "            },\n" +
                "            \"feels_like\":\n" +
                "            {\n" +
                "                \"day\": 33.48,\n" +
                "                \"night\": 25.49,\n" +
                "                \"eve\": 33.73,\n" +
                "                \"morn\": 24.76\n" +
                "            },\n" +
                "            \"pressure\": 1012,\n" +
                "            \"humidity\": 68,\n" +
                "            \"weather\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"id\": 500,\n" +
                "                    \"main\": \"Rain\",\n" +
                "                    \"description\": \"light rain\",\n" +
                "                    \"icon\": \"10d\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"speed\": 2.48,\n" +
                "            \"deg\": 128,\n" +
                "            \"gust\": 7.29,\n" +
                "            \"clouds\": 17,\n" +
                "            \"pop\": 0.95,\n" +
                "            \"rain\": 5.83\n" +
                "        },\n" +
                "        {\n" +
                "            \"dt\": 1637640000,\n" +
                "            \"sunrise\": 1637621550,\n" +
                "            \"sunset\": 1637663233,\n" +
                "            \"temp\":\n" +
                "            {\n" +
                "                \"day\": 27.8,\n" +
                "                \"min\": 23.58,\n" +
                "                \"max\": 31.52,\n" +
                "                \"night\": 24.73,\n" +
                "                \"eve\": 30.61,\n" +
                "                \"morn\": 23.58\n" +
                "            },\n" +
                "            \"feels_like\":\n" +
                "            {\n" +
                "                \"day\": 30.79,\n" +
                "                \"night\": 25.48,\n" +
                "                \"eve\": 34.74,\n" +
                "                \"morn\": 24.47\n" +
                "            },\n" +
                "            \"pressure\": 1012,\n" +
                "            \"humidity\": 74,\n" +
                "            \"weather\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"id\": 500,\n" +
                "                    \"main\": \"Rain\",\n" +
                "                    \"description\": \"light rain\",\n" +
                "                    \"icon\": \"10d\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"speed\": 2.79,\n" +
                "            \"deg\": 310,\n" +
                "            \"gust\": 3.49,\n" +
                "            \"clouds\": 79,\n" +
                "            \"pop\": 0.55,\n" +
                "            \"rain\": 0.84\n" +
                "        }\n" +
                "    ]\n" +
                "}"
    }
}