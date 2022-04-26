package com.example.calorieguide.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FormField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null,
    onValueChange: (String) -> Unit,
) {
    CoreFormField(value, label, modifier,keyboardType, null, error, onValueChange)
}

@Preview
@Composable
fun FormTextFieldPreview() {
    FormField("my@mail.com", "Email") { }
}