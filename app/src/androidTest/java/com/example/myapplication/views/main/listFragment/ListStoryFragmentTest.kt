package com.example.myapplication.views.main.listFragment

import android.support.test.runner.AndroidJUnit4
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.example.myapplication.R
import com.example.myapplication.network.ApiConfig
import com.example.myapplication.utils.EspressoIdlingResource
import com.example.myapplication.utils.JsonConverter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class ListStoryFragmentTest{

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getStory_Success() {
        launchFragmentInContainer<Fragment>(null, R.style.Theme_MyApplication)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("response_success.json"))
        mockWebServer.enqueue(mockResponse)

        Espresso.onView(ViewMatchers.withId(R.id.rv_story))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText("tutup botol"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}