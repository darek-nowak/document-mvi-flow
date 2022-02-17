package com.example.mviapp.tests

import android.os.AsyncTask
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mviapp.R
import com.example.mviapp.mockserver.MockResponses.DOCUMENTS_LIST_RESPONSE_SUCCESS
import com.example.mviapp.mockserver.MockResponses.DOCUMENT_DETAILS_RESPONSE_ERROR
import com.example.mviapp.mockserver.MockResponses.DOCUMENT_DETAILS_RESPONSE_SUCCESS
import com.example.mviapp.presentation.MainActivity
import com.example.mviapp.rules.MockWebServerRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DocumentDetailsTest {

    @JvmField
    @Rule
    val server = MockWebServerRule()

    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher())
    }

    @Before
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun documentDetailsSuccess() {
        server.setMockingDispatcherFor(
            DOCUMENTS_LIST_RESPONSE_SUCCESS,
            DOCUMENT_DETAILS_RESPONSE_SUCCESS
        )

        onView(withText("Mark Twain"))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText("Big Data Developer"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun documentDetailsError() {
        server.setMockingDispatcherFor(
            DOCUMENTS_LIST_RESPONSE_SUCCESS,
            DOCUMENT_DETAILS_RESPONSE_ERROR
        )

        onView(withText("Mark Twain"))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText("Error fetching data"))
            .check(matches(isDisplayed()))

        onView(ViewMatchers.withId(R.id.progressBar))
            .check(matches(not(isDisplayed())))
    }


    @Test
    fun repeatChoosingDetailsFromList() {
        server.setMockingDispatcherFor(
            DOCUMENTS_LIST_RESPONSE_SUCCESS,
            DOCUMENT_DETAILS_RESPONSE_ERROR,
            DOCUMENT_DETAILS_RESPONSE_SUCCESS
        )

        onView(withText("Marie Curie"))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withText("Error fetching data"))
            .check(matches(isDisplayed()))

        Espresso.pressBack()

        onView(withText("Mark Twain"))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withText("Big Data Developer"))
            .check(matches(isDisplayed()))
    }
}