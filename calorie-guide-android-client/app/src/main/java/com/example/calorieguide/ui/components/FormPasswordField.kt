package com.example.calorieguide.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calorieguide.R
import com.example.calorieguide.ui.theme.*

@Composable
fun FormPasswordField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    onValueChange: (String) -> Unit,
) {
    var isShown by rememberSaveable { mutableStateOf(false) }

    Column(modifier) {
        Text(label, style = MaterialTheme.typography.h6)

        Row(
            Modifier
                .fillMaxWidth()
                .border(
                    2.dp,
                    if (error == null) MaterialTheme.colors.onBackground
                    else MaterialTheme.colors.error,
                    RoundedCornerShape(2.dp)
                )
                .padding(16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                modifier = Modifier.weight(1f),
                value = value,
                visualTransformation =
                if (isShown)  VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = onValueChange,
                textStyle = textField,
                cursorBrush = SolidColor(orange100)
            )

            if (error != null) {
                Spacer(Modifier.width(4.dp))
                Icon(painterResource(
                    R.drawable.error),
                    null,
                    Modifier.size(24.dp),
                    MaterialTheme.colors.error
                )
            }

            Spacer(Modifier.width(4.dp))

            val color = if (isShown) MaterialTheme.colors.formIconDefault
                        else MaterialTheme.colors.formIconDisabled

            IconButton({ isShown = !isShown }, Modifier.size(24.dp)) {
                Icon(painterResource(R.drawable.show), null, tint = color)
            }
        }

        Text(
            error ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body2
        )
    }
}

@Preview
@Composable
fun FormPasswordFieldPreview() {
    FormPasswordField(
        "12345",
        "Password"
    ) { }
}