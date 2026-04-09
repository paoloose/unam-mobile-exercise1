package site.paoloose.unam.exercise1.data

import androidx.annotation.StringRes
import site.paoloose.unam.exercise1.R

enum class Gender(@param:StringRes val labelResId: Int) {
    MALE(R.string.gender_male),
    FEMALE(R.string.gender_female),
    NON_BINARY(R.string.gender_nonbinary),
    PREFER_NOT_TO_SAY(R.string.gender_prefer_not_to_say)
}
