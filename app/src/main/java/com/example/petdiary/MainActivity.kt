package com.example.petdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    // Счётчик нажатий
    private var clickCount = 0

    // Массив картинок
    private val images = listOf(
        R.drawable.bg1,
        R.drawable.bg2,
        R.drawable.bg3
    )

    // Какая картинка сейчас
    private var currentImage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val backgroundImage = findViewById<ImageView>(R.id.backgroundImage)
        val changeButton = findViewById<Button>(R.id.changeButton)

        changeButton.setOnClickListener {

            // Увеличиваем счётчик
            clickCount++

            // Меняем картинку
            currentImage++

            // Если дошли до конца списка — начинаем заново
            if (currentImage >= images.size) {
                currentImage = 0
            }

            // Устанавливаем новую картинку
            backgroundImage.setImageResource(images[currentImage])

            // После 3 нажатий открываем новое окно
            if (clickCount == 3) {

                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)

            }
        }
    }
}