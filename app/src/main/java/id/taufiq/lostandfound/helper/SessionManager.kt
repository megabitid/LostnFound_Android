package id.taufiq.lostandfound.helper

import android.content.Context
import android.content.SharedPreferences
import id.taufiq.lostandfound.R

/**
 * Created By Gogxi on 17/11/2020.
 *
 */
class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name),
        Context.MODE_PRIVATE
    )

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Function to clear logged in token
     */
    fun clearLoggedInToken() {
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }
}