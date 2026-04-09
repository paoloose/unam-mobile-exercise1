package site.paoloose.unam.exercise1.data

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.ui.graphics.vector.ImageVector
import site.paoloose.unam.exercise1.R

enum class Interest(
    @param:StringRes val labelResId: Int,
    val icon: ImageVector
) {
    SPORTS(R.string.interest_sports, Icons.Default.SportsSoccer),
    MUSIC(R.string.interest_music, Icons.Default.MusicNote),
    TECH(R.string.interest_tech, Icons.Default.Computer),
    ART(R.string.interest_art, Icons.Default.Brush),
    TRAVEL(R.string.interest_travel, Icons.Default.Flight),
    GAMING(R.string.interest_gaming, Icons.Default.VideogameAsset);
}
