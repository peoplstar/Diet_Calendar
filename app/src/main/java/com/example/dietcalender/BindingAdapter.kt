package com.example.dietcalender

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage

object BindingAdapter {
    @JvmStatic
//    @BindingAdapter("app:imageUrl")
    fun loadImage(imageView: ImageView, url: String) {

        val storage: FirebaseStorage =
            FirebaseStorage.getInstance("gs://dietcalendar-9e182.appspot.com")

        val storageReference = storage.reference
        val pathReference = storageReference.child("/images/$url")


        pathReference.downloadUrl.addOnSuccessListener { uri ->
            Log.d("TAG", "onCreate: Success")
            Glide.with(imageView.context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(imageView)

        }.addOnFailureListener {
            when (url[11]) {
                'b' -> {
                    Glide.with(imageView.context)
                        .load(R.drawable.morning)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(imageView)
                }

                'l' -> {
                    Glide.with(imageView.context)
                        .load(R.drawable.afternoon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(imageView)
                }
                'd' -> {
                    Glide.with(imageView.context)
                        .load(R.drawable.evening)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(imageView)
                }
            }
        }
    }
}