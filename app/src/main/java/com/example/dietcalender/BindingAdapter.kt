//package com.example.dietcalender
//
//import android.util.Log
//import android.widget.ImageView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.google.firebase.storage.FirebaseStorage
//
//object BindingAdapter {
//    @JvmStatic
//    @BindingAdapter("app:imageUrl")
//    fun loadImage(imageView: ImageView, url: String) {
//
//        val storage: FirebaseStorage =
//            FirebaseStorage.getInstance("gs://dietcalendar-9e182.appspot.com")
//        val storageReference = storage.reference
//        val pathReference = storageReference.child("images/$url")
//        Log.d(TAG, "onCreateView: $pathReference")
//
//        pathReference.downloadUrl.addOnSuccessListener { uri ->
//            Log.d(TAG, "onCreate: Success")
//            Glide.with(imageView.context)
//                .load(uri)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .centerCrop()
//                .into(imageView)
//        }
//    }
//}