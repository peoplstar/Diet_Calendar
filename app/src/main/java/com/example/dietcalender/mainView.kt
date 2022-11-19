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
import com.example.dietcalender.databinding.FragmentMainViewBinding
import com.google.firebase.storage.FirebaseStorage
import kotlin.math.log

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
    lateinit var fileName: String
    lateinit var storage: FirebaseStorage
    lateinit var bitmap :Bitmap
    lateinit var binding :FragmentMainViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainViewBinding.inflate(inflater, container,false)
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val bundle = arguments
        fileName = bundle?.getString("name")!!
        binding.breakfast.setOnClickListener {
            activityResult.launch(intent)
        }
        BindingAdapter.loadImage(binding.breakfast, "${fileName}-breakfast.png")
        BindingAdapter.loadImage(binding.lunch, "${fileName}-lunch.png")
        BindingAdapter.loadImage(binding.dinner, "${fileName}-dinner.png")
        // picture

        return binding.root // Fragment View Show
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