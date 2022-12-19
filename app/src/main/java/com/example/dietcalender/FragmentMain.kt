package com.example.dietcalender

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dietcalender.databinding.FragmentMainViewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.io.FileOutputStream
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

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
    var before: TextView? = null
    private var now: LocalDate = LocalDate.now()
    private lateinit var today: String
    private lateinit var day: String
    private lateinit var clickDay: String
    private lateinit var fileName: String
    private val png = ".png"

    private val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    private fun checkPermission() {

        val cameraPermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPermission()
        }
    }

    // 2. 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.CAMERA), 99)
    }

    private val binding: FragmentMainViewBinding by lazy {
        FragmentMainViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val daysOfWeek = arrayOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )

        binding.calendarView.setup(firstMonth, lastMonth, daysOfWeek.first())
        binding.calendarView.scrollToDate(LocalDate.now())


        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                container.bind(day)

            }
        }
    }



    inner class DayViewContainer(view: View) : ViewContainer(view) {
        var year: String = now.format(DateTimeFormatter.ofPattern("yyyy"))
        private val monthText = view.findViewById<TextView>(R.id.tv_month)
        private val dateText = view.findViewById<TextView>(R.id.tv_date)
        private val dayText = view.findViewById<TextView>(R.id.tv_day)

        lateinit var day: CalendarDay

        init {
            view.setOnClickListener {
                before?.setTextColor(Color.parseColor("#ffffff"))
                it.findViewById<TextView>(R.id.tv_day).apply {
                    today = this.text.toString()
                    this.setTextColor(Color.parseColor("#f3b369"))
                    before = this
                }

                val changeDayText: String = when (dayText.text.toString().toInt()) {
                    in 1..9 -> "0" + dayText.text
                    else -> dayText.text as String
                } // 1 .. 9 날짜 -> 01 .. 09 변환

                clickDay = "$year-${monthText.text.dropLast(1)}-$changeDayText"

                BindingAdapter.loadImage(binding.breakfast, "${clickDay}-breakfast.png")
                BindingAdapter.loadImage(binding.lunch, "${clickDay}-lunch.png")
                BindingAdapter.loadImage(binding.dinner, "${clickDay}-dinner.png")
                Thread.sleep(200)
            }
        }

        // 날짜에 따라 textview에 값을 bind해줌
        fun bind(day: CalendarDay) {
            this.day = day
            monthText.text = day.date.monthValue.toString() + '월'
            dateText.text = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            dayText.text = day.date.dayOfMonth.toString()

            if (now == day.date) {
                dayText.setTextColor(Color.parseColor("#f3b369")) // 시작 시 색 변경
                before = dayText
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.apply { registerForContextMenu(this.breakfast) }
        binding.apply { registerForContextMenu(this.lunch) }
        binding.apply { registerForContextMenu(this.dinner) }


        today = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        clickDay = today

        BindingAdapter.loadImage(binding.breakfast, "${today}-breakfast.png")
        BindingAdapter.loadImage(binding.lunch, "${today}-lunch.png")
        BindingAdapter.loadImage(binding.dinner, "${today}-dinner.png")

        return binding.root // Fragment View Show
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        when (v?.id) {
            R.id.breakfast -> {
                requireActivity().menuInflater.inflate(R.menu.bmenu, menu)
            }
            R.id.lunch -> {
                requireActivity().menuInflater.inflate(R.menu.lmenu, menu)
            }
            R.id.dinner -> {
                requireActivity().menuInflater.inflate(R.menu.dmenu, menu)
            }
        }
    }

    private fun takePicture(Category: String){
        checkPermission()
        fileName = "$clickDay-$Category$png" // 현재 클릭한 날짜 전역 변수로 선언하여 받아올 것
        activityResult.launch(intent)
    }

    private fun contentText(Category: String){
        val dialog = CustomDialog(requireContext())
        dialog.showDia("$clickDay", Category)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.bTakePicture -> {
                takePicture("breakfast")
            }

            R.id.bContent -> {
                contentText("breakfast")
            }

            R.id.lTakePicture -> {
                takePicture("lunch")
            }

            R.id.lContent -> {
                contentText("lunch")
            }

            R.id.dTakePicture -> {
                takePicture("dinner")
            }

            R.id.dContent -> {
                contentText("dinner")
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun saveFile(fileName:String, mimeType:String, bitmap: Bitmap): Uri?{

        var CV = ContentValues()

        // MediaStore 에 파일명, mimeType 을 지정
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        // 안정성 검사
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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

    private fun getOrientationOfImage(filepath : String): Int? {
        var exif :ExifInterface? = null
        var result: Int? = null

        try {
            exif = ExifInterface(filepath)
        } catch (e: Exception){
            e.printStackTrace()
            return -1
        }

        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        if(orientation != -1){
            result = when(orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        }
        return result
    }

    private fun rotatedBitmap(bitmap: Bitmap?, filepath: String): Bitmap? {
        val matrix = Matrix()
        var resultBitmap : Bitmap? = null

        when(getOrientationOfImage(filepath)){
            0 -> matrix.setRotate(0F)
            90 -> matrix.setRotate(90F)
            180 -> matrix.setRotate(180F)
            270 -> matrix.setRotate(270F)
        }

        resultBitmap = try{
            bitmap?.let { Bitmap.createBitmap(it, 0, 0, bitmap.width, bitmap.height, matrix, true) }
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
        return resultBitmap
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == RESULT_OK) {
            val extras = it.data!!.extras
            val img = extras?.get("data") as Bitmap

            val uri = rotatedBitmap(img, fileName)?.let { it1 ->
                saveFile(fileName, "image/png",
                    it1
                )
            }

            when (fileName[11]) {
                'b' -> {
                    Glide.with(this)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(binding.breakfast)
                }
                'l' -> {
                    Glide.with(this)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(binding.lunch)
                }
                'd' -> {
                    Glide.with(this)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(binding.dinner)
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