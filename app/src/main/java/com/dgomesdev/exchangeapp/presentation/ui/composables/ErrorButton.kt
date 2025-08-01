package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dgomesdev.exchangeapp.R

@Composable
fun ErrorButton(
    modifier: Modifier = Modifier,
    retryAction: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { retryAction }) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Preview
@Composable
private fun ErrorButtonPreview() {
    ErrorButton {  }
}