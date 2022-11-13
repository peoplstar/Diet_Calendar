package com.example.dietcalender

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dietcalender.databinding.FragmentMainViewBinding
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val TAG = "TAG"
/**
 * A simple [Fragment] subclass.
 * Use the [mainView.newInstance] factory method to
 * create an instance of this fragment.
 */
class mainView : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var storage: FirebaseStorage
    lateinit var bitmap :Bitmap
    lateinit var binding :FragmentMainViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainViewBinding.inflate(inflater, container,false)

        val storage: FirebaseStorage =
            FirebaseStorage.getInstance("gs://dietcalendar-9e182.appspot.com")
        val storageReference = storage.reference
        val pathReference = storageReference.child("images/2.jpg")
        Log.d(TAG, "onCreateView: $pathReference")

        pathReference.downloadUrl.addOnSuccessListener { uri ->
            Log.d(TAG, "onCreate: Success")
            Log.d(TAG, "onCreateView: $uri")
            Glide.with(binding.breakfast.context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(binding.breakfast)
        }.addOnFailureListener {
            Log.d(TAG, "onCreateView: Failes")
        }
//        BindingAdapter.loadImage(binding.breakfast, "2.jpg")

        // picture
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        binding.breakfast.setOnClickListener {
            activityResult.launch(intent)
        }
        return binding.root
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == RESULT_OK && it.data!! == null) {
            val extras = it.data!!.extras

            bitmap = extras?.get("data") as Bitmap
            binding.breakfast.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            mainView().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}