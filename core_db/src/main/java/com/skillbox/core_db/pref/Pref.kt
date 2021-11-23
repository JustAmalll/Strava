package com.skillbox.core_db.pref

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class Pref(context: Context, app: Application) {

    @RequiresApi(Build.VERSION_CODES.M)
    private var masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private var sharedPreferences = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        EncryptedSharedPreferences.create(
            FileName,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } else
        app.getSharedPreferences(FileName, Context.MODE_PRIVATE)

    var checkDay: Int
        get() = sharedPreferences.getInt(KeyDay, 1)
        set(value) {
            sharedPreferences.edit {
                putInt(KeyDay, value)
            }
        }

    var accessToken: String
        get() = sharedPreferences.getString(KeyToken, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KeyToken, value)
            }
        }

    var nameProfile: String
        get() = sharedPreferences.getString(KeyProfileName, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KeyProfileName, value)
            }
        }

    var photoprofile: String
        get() = sharedPreferences.getString(KeyProfilePhoto, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KeyProfilePhoto, value)
            }
        }

    var code: String?
        get() = sharedPreferences.getString(KeyCode, null)
        set(value) {
            sharedPreferences.edit {
                putString(KeyCode, value)
            }
        }

    var isBoarding: Boolean
        get() = sharedPreferences.getBoolean(KeyOnBoarding, false)
        set(value) {
            sharedPreferences.edit {
                putBoolean(KeyOnBoarding, value)
            }
        }

    fun clearProfile() {
        sharedPreferences.edit {
            remove(KeyToken)
            remove(KeyProfilePhoto)
            remove(KeyProfileName)
        }
    }

    companion object {
        const val FileName = "SkillboxPreference"
        const val KeyToken = "accessToken"
        const val KeyCode = "code"
        const val KeyOnBoarding = "onBoarding"
        const val KeyProfilePhoto = "photo"
        const val KeyProfileName = "name"
        const val KeyDay = "day"
    }
}