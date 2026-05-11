package com.example.petdiary

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    private lateinit var userDataManager: UserDataManager

    private lateinit var titleTextView: TextView
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var actionButton: Button
    private lateinit var switchModeTextView: TextView

    private var isLoginMode = false  // false = регистрация, true = вход

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        userDataManager = UserDataManager(this)

        // Находим все элементы
        titleTextView = findViewById(R.id.titleTextView)
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        actionButton = findViewById(R.id.actionButton)
        switchModeTextView = findViewById(R.id.switchModeTextView)

        // Обработчик для кнопки действия
        actionButton.setOnClickListener {
            if (isLoginMode) {
                loginUser()
            } else {
                registerUser()
            }
        }

        // Обработчик для переключения режима
        switchModeTextView.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUIMode()
        }
    }

    private fun updateUIMode() {
        if (isLoginMode) {
            // Режим ВХОДА
            titleTextView.text = "Вход в аккаунт"
            editTextName.visibility = TextView.GONE  // Скрываем поле Имя
            actionButton.text = "Войти"
            switchModeTextView.text = "Нет аккаунта? Зарегистрироваться"
        } else {
            // Режим РЕГИСТРАЦИИ
            titleTextView.text = "Создание аккаунта"
            editTextName.visibility = TextView.VISIBLE  // Показываем поле Имя
            actionButton.text = "Зарегистрироваться"
            switchModeTextView.text = "Уже есть аккаунт? Войти"
        }
    }

    private fun registerUser() {
        val name = editTextName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        // Проверки для регистрации
        if (name.isEmpty()) {
            editTextName.error = "Введите имя"
            return
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Введите корректный email"
            return
        }

        if (password.length < 4) {
            editTextPassword.error = "Пароль слишком короткий (мин. 4 символа)"
            return
        }

        // Проверяем, не существует ли уже такой email
        if (userDataManager.isUserExists(email)) {
            Toast.makeText(this, "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show()
            return
        }

        // Сохраняем пользователя
        userDataManager.saveUser(name, email, password)
        Toast.makeText(this, "✅ Регистрация успешна!", Toast.LENGTH_SHORT).show()

        // Очищаем поля
        clearFields()

        // Переключаемся на режим входа
        isLoginMode = true
        updateUIMode()
    }

    private fun loginUser() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        // Проверки для входа
        if (email.isEmpty()) {
            editTextEmail.error = "Введите email"
            return
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Введите пароль"
            return
        }

        // Проверяем данные
        val savedEmail = userDataManager.getUserEmail()
        val savedPassword = userDataManager.getUserPassword()
        val savedName = userDataManager.getUserName()

        if (savedEmail == email && savedPassword == password) {
            // Успешный вход
            Toast.makeText(this, "✅ Добро пожаловать, $savedName!", Toast.LENGTH_LONG).show()

            // Переходим на главный экран
            val intent = Intent(this, HabbActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "❌ Неверный email или пароль", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        editTextName.text.clear()
        editTextEmail.text.clear()
        editTextPassword.text.clear()
    }
}