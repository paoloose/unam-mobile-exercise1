package site.paoloose.unam.exercise1.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import site.paoloose.unam.exercise1.R
import site.paoloose.unam.exercise1.data.UserProfile
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import site.paoloose.unam.exercise1.data.Gender
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
    initialProfile: UserProfile?,
    onProfileSaved: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var firstName by rememberSaveable { mutableStateOf(initialProfile?.firstName ?: "") }
    var lastName by rememberSaveable { mutableStateOf(initialProfile?.lastName ?: "") }
    
    var profileImagePath by rememberSaveable { mutableStateOf(initialProfile?.profileImagePath) }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        if (uri != null) {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "profile_image_${System.currentTimeMillis()}.jpg")
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            profileImagePath = file.absolutePath
        }
    }
    
    var birthDateMillis by rememberSaveable { 
        mutableStateOf(initialProfile?.birthDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()) 
    }
    var showDatePicker by remember { mutableStateOf(false) }

    val genders = Gender.entries
    var gender by rememberSaveable { mutableStateOf(initialProfile?.gender) }
    var genderExpanded by remember { mutableStateOf(false) }

    var phoneNumber by rememberSaveable { mutableStateOf(initialProfile?.phoneNumber?.takeLast(10) ?: "") }
    val countryCodePickerState = rememberKomposeCountryCodePickerState(
        defaultCountryCode = "MX"
    )

    var email by rememberSaveable { mutableStateOf(initialProfile?.email ?: "") }
    
    val allInterests = site.paoloose.unam.exercise1.data.Interest.entries
    var selectedInterests by rememberSaveable { mutableStateOf(initialProfile?.interests?.toSet() ?: emptySet()) }
    
    var bio by rememberSaveable { mutableStateOf(initialProfile?.bio ?: "") }

    // Validations
    var attemptedSubmit by rememberSaveable { mutableStateOf(false) }
    
    val firstNameError = if (firstName.isBlank()) stringResource(R.string.form_error_required) else null
    val lastNameError = if (lastName.isBlank()) stringResource(R.string.form_error_required) else null
    
    val age = birthDateMillis?.let {
        val selectedDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        java.time.Period.between(selectedDate, LocalDate.now()).years
    } ?: 0
    val birthDateError = when {
        birthDateMillis == null -> stringResource(R.string.form_error_required)
        age < 13 -> stringResource(R.string.form_error_required) // Use form_error_age actually
        else -> null
    }
    
    val genderError = if (gender == null) stringResource(R.string.form_error_required) else null
    
    val phoneError = when {
        phoneNumber.isBlank() -> stringResource(R.string.form_error_required)
        !countryCodePickerState.isPhoneNumberValid(countryCodePickerState.getCountryPhoneCode() + phoneNumber) -> stringResource(R.string.form_error_phone)
        else -> null
    }

    val emailError = when {
        email.isBlank() -> stringResource(R.string.form_error_required)
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> stringResource(R.string.form_error_email)
        else -> null
    }

    val isFormValid = firstNameError == null && lastNameError == null && birthDateError == null &&
            genderError == null && phoneError == null && emailError == null && age >= 13

    val placeholderColors = OutlinedTextFieldDefaults.colors(
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (profileImagePath != null) {
            AsyncImage(
                model = profileImagePath,
                contentDescription = stringResource(R.string.cd_profile_image),
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
        Button(onClick = { launcher.launch(androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
            Text(stringResource(R.string.btn_select_image))
        }

        Text(stringResource(R.string.form_first_name), style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            placeholder = { Text(stringResource(R.string.placeholder_first_name)) },
            isError = attemptedSubmit && firstNameError != null,
            supportingText = { if (attemptedSubmit && firstNameError != null) Text(firstNameError) },
            modifier = Modifier.fillMaxWidth(),
            colors = placeholderColors
        )

        Text(stringResource(R.string.form_last_name), style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            placeholder = { Text(stringResource(R.string.placeholder_last_name)) },
            isError = attemptedSubmit && lastNameError != null,
            supportingText = { if (attemptedSubmit && lastNameError != null) Text(lastNameError) },
            modifier = Modifier.fillMaxWidth(),
            colors = placeholderColors
        )

        // BirthDate
        Text(stringResource(R.string.form_birthdate), style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = birthDateMillis?.let {
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
                } ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(stringResource(R.string.form_birthdate)) },
                isError = attemptedSubmit && birthDateError != null,
                supportingText = { 
                    if (attemptedSubmit) {
                        if (birthDateMillis == null) Text(stringResource(R.string.form_error_required))
                        else if (age < 13) Text(stringResource(R.string.form_error_age))
                    } 
                },
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = placeholderColors
            )
            // Invisible button to catch clicks
            Surface(
                modifier = Modifier.matchParentSize(),
                color = androidx.compose.ui.graphics.Color.Transparent,
                onClick = { showDatePicker = true }
            ) {}
        }
        
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = birthDateMillis)
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        birthDateMillis = datePickerState.selectedDateMillis
                        showDatePicker = false
                    }) {
                        Text(stringResource(android.R.string.ok))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Gender Dropdown
        Text(stringResource(R.string.form_gender), style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = !genderExpanded }
        ) {
            OutlinedTextField(
                value = gender?.let { stringResource(it.labelResId) } ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(stringResource(R.string.form_gender)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                isError = attemptedSubmit && genderError != null,
                supportingText = { if (attemptedSubmit && genderError != null) Text(genderError) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                genders.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(stringResource(selectionOption.labelResId)) },
                        onClick = {
                            gender = selectionOption
                            genderExpanded = false
                        }
                    )
                }
            }
        }

        // Phone Number
        Text(stringResource(R.string.form_phone), style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
        KomposeCountryCodePicker(
            modifier = Modifier.fillMaxWidth(),
            text = phoneNumber,
            onValueChange = { phoneNumber = it },
            state = countryCodePickerState,
        )
        if (attemptedSubmit && phoneError != null) {
            Text(
                text = phoneError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp).align(Alignment.Start)
            )
        }

        // Email
        Text(stringResource(R.string.form_email), style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(stringResource(R.string.placeholder_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = attemptedSubmit && emailError != null,
            supportingText = { if (attemptedSubmit && emailError != null) Text(emailError) },
            modifier = Modifier.fillMaxWidth(),
            colors = placeholderColors
        )

        // Interests (Optional)
        Text(stringResource(R.string.form_interests), style = MaterialTheme.typography.titleMedium)
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allInterests.forEach { interest ->
                FilterChip(
                    selected = selectedInterests.contains(interest),
                    onClick = {
                        selectedInterests = if (selectedInterests.contains(interest)) {
                            selectedInterests - interest
                        } else {
                            selectedInterests + interest
                        }
                    },
                    label = { Text(stringResource(interest.labelResId)) },
                    leadingIcon = { Icon(interest.icon, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                )
            }
        }

        // Bio (Optional)
        Text(stringResource(R.string.form_bio), style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            placeholder = { Text(stringResource(R.string.placeholder_bio)) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
            colors = placeholderColors
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                attemptedSubmit = true
                if (isFormValid) {
                    val finalPhone = countryCodePickerState.getCountryPhoneCode() + phoneNumber
                    val profile = UserProfile(
                        firstName = firstName,
                        lastName = lastName,
                        birthDate = Instant.ofEpochMilli(birthDateMillis!!).atZone(ZoneId.systemDefault()).toLocalDate(),
                        gender = gender!!,
                        phoneNumber = finalPhone,
                        email = email,
                        interests = selectedInterests.toList(),
                        bio = bio,
                        profileImagePath = profileImagePath
                    )
                    onProfileSaved(profile)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.btn_save))
        }
    }
}
