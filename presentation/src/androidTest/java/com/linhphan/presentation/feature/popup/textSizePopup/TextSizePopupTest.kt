package com.linhphan.presentation.feature.popup.textSizePopup

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.linhphan.presentation.R
import com.linhphan.presentation.feature.extension.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import com.linhphan.presentation.util.ViewActionUtil


@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
class TextSizePopupTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun check_ApplyButton_state(){
        //given
        val progress = 90

        //when
        val bundle = Bundle()
        launchFragmentInHiltContainer<TextSizePopup>(bundle, R.style.Theme_WeatherForecast)

        //then
        val button = onView(withId(R.id.btn_apply))
            button.check(matches(not(isEnabled())))
        onView(withId(R.id.seek_bar)).perform(ViewActionUtil.setProgress(progress))
            button.check(matches(isEnabled()))
    }
}