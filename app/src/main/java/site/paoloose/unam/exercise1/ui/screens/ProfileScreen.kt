package site.paoloose.unam.exercise1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import site.paoloose.unam.exercise1.R
import site.paoloose.unam.exercise1.data.UserProfile

@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    onCreateProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onDeleteProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (userProfile == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.profile_not_created),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onCreateProfileClick) {
                Text(text = stringResource(R.string.btn_create_profile))
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (userProfile.profileImagePath != null) {
                AsyncImage(
                    model = userProfile.profileImagePath,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp).clip(androidx.compose.foundation.shape.CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp).clip(androidx.compose.foundation.shape.CircleShape),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "${userProfile.firstName} ${userProfile.lastName} (${stringResource(userProfile.gender.labelResId)})",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = userProfile.birthDate.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (userProfile.bio.isNotBlank()) {
                Text(
                    text = userProfile.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = userProfile.phoneNumber,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = userProfile.email,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            
            if (userProfile.interests.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            userProfile.interests.forEach { interest ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(stringResource(interest.labelResId)) },
                                    leadingIcon = { Icon(interest.icon, contentDescription = null, modifier = Modifier.size(AssistChipDefaults.IconSize)) }
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onDeleteProfileClick,
                    colors = ButtonDefaults.buttonColors(containerColor = site.paoloose.unam.exercise1.ui.theme.RedDelete, contentColor = androidx.compose.ui.graphics.Color.White)
                ) {
                    Text(text = stringResource(R.string.btn_delete_profile))
                }
                Button(onClick = onEditProfileClick) {
                    Text(text = stringResource(R.string.btn_edit_profile))
                }
            }
        }
    }
}
