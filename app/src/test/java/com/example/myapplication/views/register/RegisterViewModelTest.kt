package com.example.myapplication.views.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.StoryRepository
import com.example.myapplication.getOrAwaitValue
import com.example.myapplication.response.ResponseRegister
import com.example.myapplication.utils.Results
import com.example.myapplication.utls.DataDummy
import com.example.myapplication.viewmodel.RegisterViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    companion object {
        private const val NAME = "joko"
        private const val EMAIL = "joko121@gmail.com"
        private const val PASSWORD = "joko_123"
    }
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegister = DataDummy.generateDummyRegisterResponse()

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @Test
    fun `when register should not null return success`() {
        val expectUser = MutableLiveData<Results<ResponseRegister>>()
        expectUser.value = Results.Success(dummyRegister)
        Mockito.`when`(registerViewModel.register(EMAIL, NAME, PASSWORD)).thenReturn(expectUser)

        val actualUser = registerViewModel.register(EMAIL, NAME, PASSWORD).getOrAwaitValue()
        Mockito.verify(storyRepository).register(EMAIL, NAME, PASSWORD)
        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Results.Success)
    }
}