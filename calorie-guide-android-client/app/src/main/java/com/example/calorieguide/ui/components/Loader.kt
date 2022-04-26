package com.example.calorieguide.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Loader(
    show: Boolean?,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    if (show == true) {
        Box(
            modifier,
            Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
fun LoaderPreview() {
    Loader(true)
}