package com.example.myapplication.views.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.StoryRepository
import com.example.myapplication.getOrAwaitValue
import com.example.myapplication.response.ResponseLogin
import com.example.myapplication.utils.Results
import com.example.myapplication.utls.DataDummy
import com.example.myapplication.viewmodel.LoginViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    companion object {
        private const val EMAIL = "joko121@gmail.com"
        private const val PASSWORD = "joko_123"
    }
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLogin = DataDummy.generateDummyLoginResponse()

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when login should not null and return success`() {
        val expectUser = MutableLiveData<Results<ResponseLogin>>()
        expectUser.value = Results.Success(dummyLogin)
        `when`(loginViewModel.login(EMAIL, PASSWORD)).thenReturn(expectUser)

        val actualUser = loginViewModel.login(EMAIL, PASSWORD).getOrAwaitValue()
        Mockito.verify(storyRepository).login(EMAIL, PASSWORD)

        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Results.Success)
    }
}