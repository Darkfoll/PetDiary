package com.example.petdiary

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_second)

        // Находим поле email
        val nameEditText = findViewById<EditText>(R.id.editTextText)
        val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {

            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Проверки

            if (name.isEmpty()) {
                nameEditText.error = "Введите имя"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Введите корректный email"
                return@setOnClickListener
            }

            if (password.length < 4) {
                passwordEditText.error = "Пароль слишком короткий"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Введите корректный email"
            } else {
                emailEditText.error = null
                val intent = Intent(this, HabbActivity::class.java)
                startActivity(intent)

            }



        }
    }
}