package com.example.calorieguide.ui.activities.auth.fragments.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calorieguide.R
import com.example.core.model.ErrorCode
import com.example.core.model.RepositoryResult
import com.example.core.model.UserRole
import com.example.core.model.requests.LoginRequest
import com.example.core.model.responses.LoginResponse
import com.example.core.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoginViewModel(repository)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialValues() {
        assertEquals(viewModel.usernameError.value, null)
        assertEquals(viewModel.passwordError.value, null)
        assertEquals(viewModel.isUserLoggedIn.value, false)
    }

    @Test
    fun testSubmitWithEmptyUsername() {
        val usernameError = R.string.empty_username
        viewModel.submit("", "Pass123!")
        assertEquals(viewModel.usernameError.value, usernameError)
    }

    @Test
    fun testSubmitWithEmptyPassword() {
        val passwordError = R.string.empty_password
        viewModel.submit("FilipMilanko", "")
        assertEquals(viewModel.passwordError.value, passwordError)
    }

    @Test
    fun testSubmitSuccessful() = runBlocking  {
        val username = "FilipMilanko"
        val password = "Pass123!"
        val response = LoginResponse(
            "",
            username,
            UserRole.USER,
            2000,
            null,
            null,
            null,
            null
        )
        `when`(
            repository.login(LoginRequest(username, password))
        ).thenReturn(RepositoryResult.Success(response))
        viewModel.submit(username, password)
        assertEquals(viewModel.isUserLoggedIn.value, true)
    }

    @Test
    fun testSubmitSomeError() = runBlocking  {
        val username = "FilipMilanko"
        val password = "Pass123!"
        `when`(
            repository.login(LoginRequest(username, password))
        ).thenReturn(RepositoryResult.Error(ErrorCode.UNKNOWN.code, ""))
        viewModel.submit(username, password)
        assertEquals(viewModel.isUserLoggedIn.value, false)
        assertEquals(viewModel.error.value, R.string.error_unknown)
    }

    @Test
    fun testSubmitUnauthorizedError() = runBlocking  {
        val username = "FilipMilanko"
        val password = "Pass123!"
        `when`(
            repository.login(LoginRequest(username, password))
        ).thenReturn(RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, ""))
        viewModel.submit(username, password)
        assertEquals(viewModel.isUserLoggedIn.value, false)
        assertEquals(viewModel.error.value, R.string.error_login)
    }

    @Test
    fun testSubmitUsernameNotFoundError() = runBlocking  {
        val username = "FilipMilanko"
        val password = "Pass123!"
        `when`(
            repository.login(LoginRequest(username, password))
        ).thenReturn(RepositoryResult.Error(ErrorCode.USERNAME_NOT_FOUND.code, ""))
        viewModel.submit(username, password)
        assertEquals(viewModel.isUserLoggedIn.value, false)
        assertEquals(viewModel.error.value, R.string.error_login)
    }

    @Test
    fun testSubmitNetworkError() = runBlocking  {
        val username = "FilipMilanko"
        val password = "Pass123!"
        `when`(
            repository.login(LoginRequest(username, password))
        ).thenReturn(RepositoryResult.NetworkError(""))
        viewModel.submit(username, password)
        assertEquals(viewModel.isUserLoggedIn.value, false)
        assertEquals(viewModel.error.value, R.string.error_no_network)
    }

    @Test
    fun testSubmitUnknownError() = runBlocking  {
        val username = "FilipMilanko"
        val password = "Pass123!"
        `when`(
            repository.login(LoginRequest(username, password))
        ).thenReturn(RepositoryResult.UnknownError(""))
        viewModel.submit(username, password)
        assertEquals(viewModel.isUserLoggedIn.value, false)
        assertEquals(viewModel.error.value, R.string.error_unknown)
    }
}