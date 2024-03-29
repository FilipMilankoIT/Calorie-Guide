package com.example.calorieguide.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.calorieguide.R

@Composable
fun FormPasswordField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    onValueChange: (String) -> Unit,
) {
    CoreFormField(
        value,
        label,
        modifier,
        KeyboardType.Password,
        R.drawable.show,
        error,
        onValueChange
    )
}

@Preview
@Composable
fun FormPasswordFieldPreview() {
    FormPasswordField(
        "12345",
        "Password"
    ) { }
}