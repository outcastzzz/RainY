package com.example.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailsTextBlock(param: String, value: String) {

    Column {
        Text(
            param,
            style = MaterialTheme.typography.displaySmall,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.displayMedium,
        )
    }

}