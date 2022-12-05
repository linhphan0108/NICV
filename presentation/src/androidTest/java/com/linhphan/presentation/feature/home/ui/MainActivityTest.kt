package com.linhphan.presentation.feature.home.ui

import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.linhphan.presentation.R
import com.linhphan.presentation.util.DataBindingIdlingResource
import com.linhphan.presentation.util.EspressoIdlingResource
import com.linhphan.presentation.util.RecyclerViewAssertions.assertCount
import com.linhphan.presentation.util.RecyclerViewAssertions.isEmpty
import com.linhphan.presentation.util.RecyclerViewAssertions.isNotEmpty
import com.linhphan.presentation.util.RecyclerViewAssertions.withRowContaining
import com.linhphan.presentation.util.monitorActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.startsWith

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

//    @get:Rule(order = 1)
//    var mockitoRule: MockitoRule = MockitoJUnit.rule()


    // An idling resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setUp() {
        hiltRule.inject()
        registerIdlingResource()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    private fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }


    @After
    fun tearDown() {
        unregisterIdlingResource()
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    private fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }


    @Test
    fun check_getWeatherButton_state(){
        //start up the screen
        //we must set the initial state of the data before calling ActivityScenario.launch().
        //why don't use ActivityScenarioRule?
        //ActivityScenarioRule which calls launch and close for us. but calling additional setup code,
        //such as saving tasks to the repository, is not currently supported by ActivityScenarioRule.
        //Therefore, we choose not to use ActivityScenarioRule and instead manually call launch and close.
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //then
        onView(withId(R.id.btn_query)).check(matches(not(isEnabled())))
        onView(withId(R.id.edt_city)).perform(replaceText("sa"))
        onView(withId(R.id.btn_query)).check(matches(not(isEnabled())))
        onView(withId(R.id.edt_city)).perform(replaceText("saigon"))
        onView(withId(R.id.btn_query)).check(matches(isEnabled()))
        onView(withId(R.id.edt_city)).perform(replaceText("ha"))
        onView(withId(R.id.btn_query)).check(matches(not(isEnabled())))

        //If you are using a database, we must close the database at the end of the test.
        activityScenario.close()
    }

    @Test
    fun check_getWeatherButton_query_success(){
        //start up the screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //when
        onView(withId(R.id.edt_city)).perform(replaceText("saigon"))
        onView(withId(R.id.btn_query)).perform(click())

        SystemClock.sleep(1500)

        //then
        val recyclerView = onView(withId(R.id.recycler_view))
        recyclerView.check(isNotEmpty())
        recyclerView.check(assertCount(7))
        recyclerView.check(withRowContaining(withText(startsWith("Date"))))
        recyclerView.check(withRowContaining(withText(containsString("Description"))))

        onView(withId(R.id.layoutError)).check(matches(not(isDisplayed())))

        activityScenario.close()
    }

    @Test
    fun check_getWeatherButton_query_error(){
        //start up the screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //when
        onView(withId(R.id.edt_city)).perform(replaceText("saigonyyy"))
        onView(withId(R.id.btn_query)).perform(click())

        SystemClock.sleep(1500)

        //then
        val recyclerView = onView(withId(R.id.recycler_view))
        recyclerView.check(isEmpty())
        recyclerView.check(assertCount(0))

        onView(withId(R.id.layoutError)).check(matches(isDisplayed()))
        onView(withId(R.id.tvMessage)).check((matches(not(withText("")))))

        activityScenario.close()
    }
}