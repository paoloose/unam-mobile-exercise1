package site.paoloose.unam.exercise1.ui.navigation

import androidx.annotation.StringRes
import site.paoloose.unam.exercise1.R

enum class AppDestinations(
    @param:StringRes val label: Int,
    val icon: Int,
) {
    HOME(R.string.label_home, R.drawable.ic_home),
    PROFILE(R.string.label_profile, R.drawable.ic_account_box),
}
