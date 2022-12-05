package com.linhphan.data.loca

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.*
import com.linhphan.data.local.ForecastDB
import com.linhphan.data.local.TblForecast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ForecastDaoTest {
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ForecastDB

    @Before
    fun setUp() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ForecastDB::class.java
        ).build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun storeForecasts_and_queryAll() = runBlockingTest{
        //given
        val cityName = "saigon"
        val count = 7
        val forecasts = fakeListForecast(cityName, count)
        val dao = database.getForecastDao()
        dao.storeForecasts(forecasts)

        //when
        val loaded = dao.queryForecast(cityName)

        //then
        assertThat(loaded).isNotEmpty()
        assertThat(loaded.count()).isEqualTo(count)
        assertThat(assertAllItemContainCityName(loaded, cityName)).isTrue()
    }

    @Test
    fun storeForecasts_and_deleteOneItem_queryNotEmpty() = runBlockingTest{
        //given
        val cityName = "saigon"
        val count = 7
        val forecasts = fakeListForecast(cityName, count)
        val deletedIndex = 3
        val dao = database.getForecastDao()
        dao.storeForecasts(forecasts)
        val deleted = dao.deleteForecasts(forecasts[deletedIndex].date)

        //when
        val loaded = dao.queryForecast(cityName)

        //then
        assertThat(deleted).isEqualTo(3)
        assertThat(loaded).isNotEmpty()
        assertThat(loaded.count()).isEqualTo(4)
        assertThat(assertAllItemContainCityName(loaded, cityName)).isTrue()
    }

    @Test
    fun storeForecasts_and_deleteAll_queryEmpty() = runBlockingTest{
        //given
        val cityName = "saigon"
        val count = 7
        val forecasts = fakeListForecast(cityName, count)
        val dao = database.getForecastDao()
        dao.storeForecasts(forecasts)
        dao.deleteForecasts()

        //when
        val loaded = dao.queryForecast(cityName)

        //then
        assertThat(loaded).isEmpty()
    }

    private fun fakeListForecast(city: String, count: Int = 7): List<TblForecast> {
        val result = mutableListOf<TblForecast>()
        for (i in 0 until count){
            result.add(
                TblForecast(
                    cityName = city,
                    date = 1637208000000 - i,
                    temp = "{}",
                    pressure = 1010,
                    humidity = 58,
                    weathers = "[]"
            ))
        }
        return result
    }

    private fun assertAllItemContainCityName(list: List<TblForecast>, city: String): Boolean {
        if (list.isEmpty()) return false
        for (forecast in list){
            if (forecast.cityName != city){
                return false
            }
        }
        return true
    }
}