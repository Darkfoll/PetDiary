package com.example.petdiary

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HabbBottomSheet(
    private val onSave: (
        name: String,
        breed: String,
        age: String,
        imageUri: Uri?
    ) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var imageView: ImageView

    private var selectedImageUri: Uri? = null

    private val PICK_IMAGE = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.bottom_sheet_habb,
            container,
            false
        )

        imageView = view.findViewById(R.id.imageView)

        val editName = view.findViewById<EditText>(R.id.editTextText)
        val editBreed = view.findViewById<EditText>(R.id.editTextText2)
        val editAge = view.findViewById<EditText>(R.id.editTextNumber)

        val saveButton = view.findViewById<Button>(R.id.button)

        // Выбор картинки
        imageView.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"

            startActivityForResult(intent, PICK_IMAGE)
        }

        // Сохранение
        saveButton.setOnClickListener {

            onSave(
                editName.text.toString(),
                editBreed.text.toString(),
                editAge.text.toString(),
                selectedImageUri
            )

            dismiss()
        }

        return view
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE &&
            resultCode == Activity.RESULT_OK &&
            data != null
        ) {

            selectedImageUri = data.data

            imageView.setImageURI(selectedImageUri)
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.let { dialog ->

            val bottomSheet = dialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )

            bottomSheet?.layoutParams?.height =
                (resources.displayMetrics.heightPixels * 0.9).toInt()
        }
    }
}