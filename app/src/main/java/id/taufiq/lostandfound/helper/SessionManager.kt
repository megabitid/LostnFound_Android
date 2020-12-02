package id.taufiq.lostandfound.helper

import android.content.Context
import android.content.SharedPreferences
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.helper.Constants.USER_ID
import id.taufiq.lostandfound.helper.Constants.USER_IMAGE
import id.taufiq.lostandfound.helper.Constants.USER_TOKEN

/**
 * Created By Gogxi on 17/11/2020.
 *
 */
class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name),
        Context.MODE_PRIVATE
    )

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch User Image
     */
    fun fetchUserImage(): String? {
        return prefs.getString(USER_IMAGE, null)
    }

    /**
     * Function to save User Image
     */
    fun saveUserImage(image: String) {
        val editor = prefs.edit()
        editor.putString(USER_IMAGE, image)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Function to save User Id
     */
    fun saveUserId(id: Int) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, id)
        editor.apply()
    }

    /**
     * Function to fetch User Id
     */
    fun fetchUserId(): Int {
        return prefs.getInt(USER_ID, 0)
    }

    /**
     * Function to clear logged in
     */
    fun clearLoggedInToken() {
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(USER_IMAGE)
        editor.remove(USER_ID)
        editor.apply()
    }
}