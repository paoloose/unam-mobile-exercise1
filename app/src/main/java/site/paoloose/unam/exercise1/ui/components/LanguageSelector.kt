package site.paoloose.unam.exercise1.ui.components

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import site.paoloose.unam.exercise1.R
import site.paoloose.unam.exercise1.ui.theme.Exercise1Theme

@Composable
fun LanguageSelector() {
    var expanded by remember { mutableStateOf(false) }
    // Using LocalConfiguration.current makes this reactive to locale changes
    val currentLocale = LocalConfiguration.current.locales[0].language

    val languages = remember {
        listOf(
            Language("fr", R.string.lang_fr),
            Language("en", R.string.lang_en),
            Language("es", R.string.lang_es)
        )
    }

    val currentLanguage = languages.firstOrNull { it.code == currentLocale } ?: languages[0]

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        TextButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = stringResource(R.string.language_select),
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(currentLanguage.labelRes),
                style = MaterialTheme.typography.labelLarge
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(language.labelRes),
                            style = if (currentLocale == language.code) {
                                MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                            } else {
                                MaterialTheme.typography.bodyLarge
                            }
                        )
                    },
                    onClick = {
                        expanded = false
                        val appLocale = LocaleListCompat.forLanguageTags(language.code)
                        AppCompatDelegate.setApplicationLocales(appLocale)
                    }
                )
            }
        }
    }
}

data class Language(val code: String, @param:StringRes val labelRes: Int)

@Preview(showBackground = true)
@Composable
fun LanguageSelectorPreview() {
    Exercise1Theme {
        LanguageSelector()
    }
}
