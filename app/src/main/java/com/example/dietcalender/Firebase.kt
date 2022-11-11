package com.example.dietcalender

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Firebase {

    private var storage = FirebaseStorage.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    lateinit var fileName: String

    // private var day = now.format(DateTimeFormatter.ofPattern("yyyy-mm--dd"))

    private fun Upload(category: String) {

        when (category) {
            "breakfast" -> {
                fileName
            }
            "lunch" -> {
                fileName
            }
            "dinner" -> {
                fileName
            }
        }

        uploadButton.setOnClickListener {

            val mountainsRef = FirebaseStorage.getInstance().getReference().child("aaa")

            // Get the data from an ImageView as bytes
            imageView.isDrawingCacheEnabled = true
            imageView.buildDrawingCache()
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = mountainsRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }

        }
    }

    private fun Download(category: String) {
        // 다운로드
        button.setOnClickListener {

            val ref = FirebaseStorage.getInstance().getReference("bokchi.jpg")

            ref.downloadUrl
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Glide.with(this)
                            .load(task.result)
                            .into(imageView)

                    } else {
                        Log.e(TAG, "error")
                    }

                })

        }
    }

}