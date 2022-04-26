package com.example.calorieguide.ui.activities.auth.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calorieguide.R
import com.example.calorieguide.ui.activities.auth.fragments.login.LoginViewModel
import com.example.calorieguide.ui.components.FormPasswordField
import com.example.calorieguide.ui.components.FormField
import com.example.calorieguide.ui.components.Loader
import com.example.calorieguide.ui.components.PrimaryButton
import com.example.calorieguide.ui.theme.MyTheme
import com.example.calorieguide.ui.theme.blue
import com.example.calorieguide.ui.utils.WindowSize

@Preview
@Composable
fun LoginScreen(
    windowsSize: WindowSize = WindowSize(
        WindowSize.WindowType.COMPACT,
        WindowSize.WindowType.COMPACT,
        550.dp,
        450.dp
    ),
    onCreateAccount: () -> Unit = {}
) {

    val viewModel : LoginViewModel = viewModel()
    val usernameError by viewModel.usernameError.observeAsState()
    val passwordError by viewModel.passwordError.observeAsState()
    val isUserLoggedIn by viewModel.isUserLoggedIn.observeAsState()
    val error by viewModel.error.observeAsState(null)
    val waiting by viewModel.waiting.observeAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    MyTheme {
        Surface {
            Box(
                Modifier.fillMaxSize(),
                Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(
                            if (windowsSize.widthWindowSize == WindowSize.WindowType.COMPACT)
                                windowsSize.widthWindowDpSize
                            else 425.dp
                        )
                        .padding(
                            dimensionResource(R.dimen.page_horizontal_margin),
                            0.dp
                        )
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.login_page_top_margin)))

                    var usernameErrorMessage: String? = null
                    usernameError?.let { usernameErrorMessage = stringResource(it) }

                    FormField(
                        username,
                        stringResource(R.string.username_label),
                        Modifier.fillMaxWidth(),
                        KeyboardType.Email,
                        usernameErrorMessage
                    ) { username = it }

                    Spacer(Modifier.height(dimensionResource(R.dimen.form_field_vertical_margin)))

                    var passwordErrorMessage: String? = null
                    passwordError?.let { passwordErrorMessage = stringResource(it) }

                    FormPasswordField(
                        password,
                        stringResource(R.string.password_label),
                        Modifier.fillMaxWidth(),
                        error = passwordErrorMessage
                    ) { password = it }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row {
                        Text(stringResource(R.string.not_registered), style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            stringResource(R.string.create_an_account),
                            Modifier.clickable { onCreateAccount() },
                            color = blue,
                            style = MaterialTheme.typography.h6
                        )
                    }

                    Spacer(Modifier.weight(0.3f))

                    PrimaryButton(
                        stringResource(R.string.sign_in_label),
                        Modifier.padding(0.dp, 16.dp, 0.dp, 64.dp)
                    ) {
                        viewModel.submit(username, password)
                    }

                    Spacer(Modifier.weight(0.7f))
                }
            }

            Loader(waiting)

            SnackbarHost(
                snackbarHostState,
                Modifier
                    .fillMaxSize()
                    .wrapContentHeight(Alignment.Bottom)
            )
        }

        error?.let {
            val errorMessage = stringResource(it)
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(errorMessage)
            }
        }
    }
}