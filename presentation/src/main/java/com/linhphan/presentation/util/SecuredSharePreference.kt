package com.linhphan.presentation.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.linhphan.presentation.common.Constants.DEFAULT_SCALE_FACTOR_PERCENT
import javax.inject.Inject

private const val FILE_SECRET_SHARED_PREFERENCES = "FORECAST_SHARED_PREFERENCES"
private const val KEY_TEXT_SCALE_FACTOR = "TIME_REQUEST"
class SecuredSharePreference @Inject constructor(private val context: Context) {

    private fun getSharedPreferences(): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            FILE_SECRET_SHARED_PREFERENCES,
            createOrGetMasterKeys(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun createOrGetMasterKeys(): MasterKey {
        return MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }



    //#region saving base
    private fun putInt(key: String, value: Int) {
        getSharedPreferences().edit().putInt(key, value).apply()
    }

    private fun putFloat(key: String, value: Float) {
        getSharedPreferences().edit().putFloat(key, value).apply()
    }

    //#endregion saving

    //#region fetching base
    private fun getInt(key: String, default: Int): Int {
        return getSharedPreferences().getInt(key, default)
    }

    private fun getFloat(key: String, default: Float): Float {
        return getSharedPreferences().getFloat(key, default)
    }
    //#endregion fetching

    //
    fun getTextScaleFactor(): Int{
        return getInt(KEY_TEXT_SCALE_FACTOR, DEFAULT_SCALE_FACTOR_PERCENT)// ~ 100%
    }

    fun saveTextScaleFactor(value: Int){
        putInt(KEY_TEXT_SCALE_FACTOR, value)
    }
}