package com.example.petdiary

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class HabbActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_habb)

        val openSheetBtn = findViewById<Button>(R.id.openSheetBtn)

        val cardsContainer =
            findViewById<LinearLayout>(R.id.cardsContainer)

        openSheetBtn.setOnClickListener {

            val bottomSheet = HabbBottomSheet {

                    name,
                    breed,
                    age,
                    imageUri ->

                val cardView = LayoutInflater.from(this)
                    .inflate(
                        R.layout.item_pet_card,
                        cardsContainer,
                        false
                    )

                val cardImage =
                    cardView.findViewById<ImageView>(R.id.cardImage)

                val cardName =
                    cardView.findViewById<TextView>(R.id.cardName)

                val cardBreed =
                    cardView.findViewById<TextView>(R.id.cardBreed)

                val cardAge =
                    cardView.findViewById<TextView>(R.id.cardAge)

                cardName.text =
                    if (name.isEmpty()) "Без имени" else name

                cardBreed.text =
                    if (breed.isEmpty()) "Порода не указана" else breed

                cardAge.text =
                    if (age.isEmpty()) "Возраст не указан" else age

                if (imageUri != null) {
                    cardImage.setImageURI(imageUri)
                }

                cardsContainer.addView(cardView)
                cardView.setOnClickListener {

                    val intent = Intent(this, DiaryActivity::class.java)

                    startActivity(intent)
                }
            }


            bottomSheet.show(
                supportFragmentManager,
                "BottomSheet"
            )
        }
    }
}