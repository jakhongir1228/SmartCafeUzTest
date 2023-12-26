package prefHelper

import android.content.Context
import android.content.SharedPreferences
import sharedPrefHelper.PrefHelper

class SharedPref: PrefHelper {
    var preferences: SharedPreferences? = null

    constructor(context: Context) {
        preferences = context.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
    }

    override fun saveLogin(login: String?) {
        preferences!!.edit().putString("login", login).apply()
    }

    override fun getLogin(): String? {
        return preferences!!.getString("login", null)
    }

    override fun savePassword(password: String?) {
        preferences!!.edit().putString("password", password).apply()
    }

    override fun getPassword(): String? {
        return preferences!!.getString("password", null)
    }
}