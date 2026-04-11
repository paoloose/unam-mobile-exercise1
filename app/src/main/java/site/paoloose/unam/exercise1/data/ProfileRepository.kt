package site.paoloose.unam.exercise1.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// We use the repository pattern to modify global user data
// Also this allows us to hide the implementation for persistent user data

class ProfileRepository(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_profile_prefs"
        private const val KEY_FIRST_NAME = "firstName"
        private const val KEY_LAST_NAME = "lastName"
        private const val KEY_BIRTH_DATE = "birthDate"
        private const val KEY_GENDER = "gender"
        private const val KEY_PHONE_NUMBER = "phoneNumber"
        private const val KEY_EMAIL = "email"
        private const val KEY_INTERESTS = "interests"
        private const val KEY_BIO = "bio"
        private const val KEY_PROFILE_IMAGE_PATH = "profileImagePath"
        private const val KEY_EXISTS = "exists"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveProfile(profile: UserProfile) {
        prefs.edit {
            putString(KEY_FIRST_NAME, profile.firstName)
            putString(KEY_LAST_NAME, profile.lastName)
            putString(KEY_BIRTH_DATE, profile.birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
            putString(KEY_GENDER, profile.gender.name)
            putString(KEY_PHONE_NUMBER, profile.phoneNumber)
            putString(KEY_EMAIL, profile.email)
            putStringSet(KEY_INTERESTS, profile.interests.map { it.name }.toSet())
            putString(KEY_BIO, profile.bio)
            putString(KEY_PROFILE_IMAGE_PATH, profile.profileImagePath)
            putBoolean(KEY_EXISTS, true)
        }
    }

    fun getProfile(): UserProfile? {
        if (!prefs.getBoolean(KEY_EXISTS, false)) return null
        
        return UserProfile(
            firstName = prefs.getString(KEY_FIRST_NAME, "") ?: "",
            lastName = prefs.getString(KEY_LAST_NAME, "") ?: "",
            birthDate = LocalDate.parse(prefs.getString(KEY_BIRTH_DATE, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)), DateTimeFormatter.ISO_LOCAL_DATE),
            gender = try { Gender.valueOf(prefs.getString(KEY_GENDER, "MALE") ?: "MALE") } catch (e: Exception) { Gender.MALE },
            phoneNumber = prefs.getString(KEY_PHONE_NUMBER, "") ?: "",
            email = prefs.getString(KEY_EMAIL, "") ?: "",
            interests = prefs.getStringSet(KEY_INTERESTS, emptySet())?.mapNotNull { 
                try { Interest.valueOf(it) } catch (e: Exception) { null } 
            } ?: emptyList(),
            bio = prefs.getString(KEY_BIO, "") ?: "",
            profileImagePath = prefs.getString(KEY_PROFILE_IMAGE_PATH, null)
        )
    }

    fun deleteProfile() {
        prefs.edit {
            clear()
        }
    }
}
