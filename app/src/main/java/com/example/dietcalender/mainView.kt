package com.example.dietcalender

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dietcalender.databinding.FragmentMainViewBinding
import java.io.FileOutputStream

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
    private lateinit var day: String
    private lateinit var fileName: String
    private val png = ".png"
    private lateinit var binding :FragmentMainViewBinding
    private val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    private fun checkPermission() {

        // 1. 위험권한(Camera) 권한 승인상태 가져오기
        val cameraPermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            // 카메라 권한이 승인된 상태일 경우

        } else {
            // 카메라 권한이 승인되지 않았을 경우
            requestPermission()
        }
    }

    // 2. 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.CAMERA), 99)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainViewBinding.inflate(inflater, container,false)

        val bundle = arguments
        day = bundle?.getString("name")!!


        binding.breakfast.setOnClickListener {
            checkPermission()
            fileName = "$day-breakfast$png"
            activityResult.launch(intent)
        }
        binding.lunch.setOnClickListener {
            checkPermission()
            fileName = "$day-lunch$png"
            activityResult.launch(intent)
        }
        binding.dinner.setOnClickListener {
            checkPermission()
            fileName += "$day-dinner$png"
            activityResult.launch(intent)
        }

        BindingAdapter.loadImage(binding.breakfast, "${day}-breakfast.png")
        BindingAdapter.loadImage(binding.lunch, "${day}-lunch.png")
        BindingAdapter.loadImage(binding.dinner, "${day}-dinner.png")
        // picture

        return binding.root // Fragment View Show
    }

    fun saveFile(fileName:String, mimeType:String, bitmap: Bitmap): Uri?{

        var CV = ContentValues()

        // MediaStore 에 파일명, mimeType 을 지정
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        // 안정성 검사
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            CV.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        // MediaStore 에 파일을 저장
        val uri = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)
        if(uri != null){
            var scriptor = context?.contentResolver?.openFileDescriptor(uri, "w")

            val fos = FileOutputStream(scriptor?.fileDescriptor)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                CV.clear()
                // IS_PENDING 을 초기화
                CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                context?.contentResolver?.update(uri, CV, null, null)
            }
        }
        return uri
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == RESULT_OK) {
            val extras = it.data!!.extras
            val img = extras?.get("data") as Bitmap
            val uri = saveFile(fileName, "image/png", img)

            when (fileName[11]) {
                'b' -> {
                    binding.breakfast.setImageURI(uri)
                }
                'l' -> {
                    binding.lunch.setImageURI(uri)
                }
                'd' -> {
                    binding.dinner.setImageURI(uri)
                }
            }
            BindingAdapter.uploadImageTOFirebase(uri!!, fileName)
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