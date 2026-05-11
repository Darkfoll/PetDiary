package com.example.petdiary

import android.content.Context
import android.content.SharedPreferences

class UserDataManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    fun saveUser(name: String, email: String, password: String) {
        prefs.edit().apply {
            putString("name", name)
            putString("email", email)
            putString("password", password)
            putBoolean("isRegistered", true)
            apply()
        }
    }

    fun isUserExists(email: String): Boolean {
        val savedEmail = prefs.getString("email", null)
        return savedEmail == email
    }

    fun getUserName(): String {
        return prefs.getString("name", "") ?: ""
    }

    fun getUserEmail(): String {
        return prefs.getString("email", "") ?: ""
    }

    // НОВЫЙ МЕТОД - получаем пароль
    fun getUserPassword(): String {
        return prefs.getString("password", "") ?: ""
    }

    fun isRegistered(): Boolean {
        return prefs.getBoolean("isRegistered", false)
    }
}