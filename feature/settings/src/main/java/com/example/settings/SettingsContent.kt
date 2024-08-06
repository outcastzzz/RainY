package com.example.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.common.R
import com.example.common.theme.TextColorAccent

@Composable
fun SettingsContent(
    modifier: Modifier,
    component: SettingsComponent
) {

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        topBar = { SettingsTopBar { component.onClickBack() } }
    ) { paddingValues ->

        Column (
            modifier = modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.primary)
        ) {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        start = 30.dp,
                        top = 50.dp,
                        end = 30.dp,
                    )
                    .background(MaterialTheme.colorScheme.primary)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(50.dp)
            ) {
                Theme()
                Feedback()
                About()
            }

        }

    }

}

@Composable
private fun About(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "About",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "About Weather",
                style = MaterialTheme.typography.displayMedium,
                color = TextColorAccent
            )
            Spacer(modifier.height(5.dp))
            Text(
                text = "Read a bit more about the app.",
                style = MaterialTheme.typography.displaySmall,
                color = TextColorAccent
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "The Team",
                style = MaterialTheme.typography.displayMedium,
                color = TextColorAccent
            )
            Spacer(modifier.height(5.dp))
            Text(
                text = "Get to know the team that made Weather a reality.",
                style = MaterialTheme.typography.displaySmall,
                color = TextColorAccent
            )
        }
    }
}

@Composable
private fun Feedback(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Feedback",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Report an Issue",
                style = MaterialTheme.typography.displayMedium,
                color = TextColorAccent
            )
            Spacer(modifier.height(5.dp))
            Text(
                text = "Facing an issue? Report and we’ll look into it.",
                style = MaterialTheme.typography.displaySmall,
                color = TextColorAccent
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "Rate App",
                style = MaterialTheme.typography.displayMedium,
                color = TextColorAccent
            )
            Spacer(modifier.height(5.dp))
            Text(
                text = "Enjoying the app? Leave a review on the App Store.",
                style = MaterialTheme.typography.displaySmall,
                color = TextColorAccent
            )
        }
    }
}

@Composable
private fun Theme(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Theme",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column{
                Text(
                    text = "System Theme",
                    style = MaterialTheme.typography.displayMedium,
                    color = TextColorAccent
                )
                Spacer(modifier.height(5.dp))
                Text(
                    text = "The same as in your system",
                    style = MaterialTheme.typography.displaySmall,
                    color = TextColorAccent
                )
            }
            Spacer(modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = "is true?"
            )

        }
    }
}

@Composable
private fun SettingsTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 30.dp, top = 70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBackClick() } ) {
                Image(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    modifier = modifier
                        .padding(
                            start = 7.88.dp,
                            end = 7.88.dp,
                            top = 5.25.dp,
                            bottom = 5.25.dp
                        )
                        .clickable {
                            onBackClick()
                        },
                    contentDescription = "go back"
                )
            }
            Spacer(modifier.width(5.dp))
            Text(
                text = "Settings",
                style = MaterialTheme.typography.displayMedium,
                color = TextColorAccent
            )
        }
    }

}