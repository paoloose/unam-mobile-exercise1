package site.paoloose.unam.exercise1.data

import java.time.LocalDate

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val gender: Gender,
    val phoneNumber: String,
    val email: String,
    val interests: List<Interest>,
    val bio: String,
    val profileImagePath: String? = null
)
