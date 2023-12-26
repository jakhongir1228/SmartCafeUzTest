package sharedPrefHelper

interface PrefHelper {
    fun saveLogin(login: String?)

    fun getLogin(): String?

    fun savePassword(password: String?)

    fun getPassword(): String?
}