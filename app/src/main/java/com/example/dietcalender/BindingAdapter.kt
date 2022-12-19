package com.example.dietcalender

import android.app.PendingIntent.getActivity
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage

object BindingAdapter {
    @JvmStatic
//    @BindingAdapter("app:imageUrl")
    fun uploadImageTOFirebase(uri: Uri, fileName: String) {
        var storage: FirebaseStorage? = FirebaseStorage.getInstance()   //FirebaseStorage 인스턴스 생성
        //파일 이름 생성.


        //파일 업로드, 다운로드, 삭제, 메타데이터 가져오기 또는 업데이트를 하기 위해 참조를 생성.
        //참조는 클라우드 파일을 가리키는 포인터라고 할 수 있음.
        var imagesRef = storage!!.reference.child("images/").child(fileName)    //기본 참조 위치/images/${fileName}
        //이미지 파일 업로드
        imagesRef.putFile(uri!!).addOnSuccessListener {
            Log.d(TAG, "uploadImageTOFirebase: Success")
        }.addOnFailureListener {
            Log.d(TAG, "uploadImageTOFirebase: Fail")
        }
    }

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