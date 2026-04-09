package site.paoloose.unam.exercise1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import site.paoloose.unam.exercise1.R
import site.paoloose.unam.exercise1.data.UserProfile
import site.paoloose.unam.exercise1.ui.components.LanguageSelector
import site.paoloose.unam.exercise1.ui.navigation.AppDestinations
import site.paoloose.unam.exercise1.ui.screens.CreateProfileScreen
import site.paoloose.unam.exercise1.ui.screens.HomeScreen
import site.paoloose.unam.exercise1.ui.screens.ProfileScreen
import site.paoloose.unam.exercise1.ui.theme.Exercise1Theme

import androidx.compose.ui.platform.LocalContext
import site.paoloose.unam.exercise1.data.ProfileRepository

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun Exercise1App() {

    val context = LocalContext.current
    val repository = remember { ProfileRepository(context) }
    
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var isCreatingProfile by rememberSaveable { mutableStateOf(false) }
    var userProfile by remember { mutableStateOf(repository.getProfile()) }

    Scaffold(
        bottomBar = {
            if (!isCreatingProfile) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline, androidx.compose.ui.graphics.RectangleShape)
                        .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                        .height(64.dp)
                ) {
                    AppDestinations.entries.forEach { destination ->
                        val selected = currentDestination == destination
                        val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { currentDestination = destination },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Icon(painterResource(destination.icon), contentDescription = null, tint = color)
                                Text(stringResource(destination.label), style = MaterialTheme.typography.labelMedium, color = color)
                            }
                        }
                    }
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(R.drawable.unam_logo),
                            contentDescription = "UNAM Logo",
                            modifier = Modifier.padding(end = 8.dp).size(32.dp)
                        )
                        Text(stringResource(R.string.app_name))
                    }
                },
                actions = {
                    LanguageSelector()
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (isCreatingProfile) {
                CreateProfileScreen(
                    initialProfile = userProfile,
                    onProfileSaved = { profile ->
                        repository.saveProfile(profile)
                        userProfile = profile
                        isCreatingProfile = false
                        currentDestination = AppDestinations.PROFILE
                    }
                )
            } else {
                when (currentDestination) {
                    AppDestinations.HOME -> {
                        HomeScreen(
                            onCreateProfileClick = { isCreatingProfile = true }
                        )
                    }
                    AppDestinations.PROFILE -> {
                        ProfileScreen(
                            userProfile = userProfile,
                            onCreateProfileClick = { isCreatingProfile = true },
                            onEditProfileClick = { isCreatingProfile = true },
                            onDeleteProfileClick = {
                                repository.deleteProfile()
                                userProfile = null
                                currentDestination = AppDestinations.HOME
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.greeting_text, name),
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Exercise1Theme {
        Greeting(stringResource(R.string.default_name))
    }
}
