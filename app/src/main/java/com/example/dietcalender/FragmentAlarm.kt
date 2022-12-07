package com.example.dietcalender

import android.R
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dietcalender.databinding.FragmentAlarmBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAlarm.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentAlarm : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private var times: String = ""

    private val binding: FragmentAlarmBinding by lazy {
        FragmentAlarmBinding.inflate(layoutInflater)
    }

    private fun timePick(Categroy: TextView) {

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            times = "%02d:%02d".format(hourOfDay, minute)
            Categroy.text = times
            myRef.setValue("$times")
        }

        val dialog = TimePickerDialog(
            requireContext(),
            R.style.Theme_Holo_Light_Dialog_NoActionBar,
            timeSetListener,
            9,
            0,
            true
        )

        dialog.setTitle("알람 설정")
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog.show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        binding.bSwitch.isChecked = App.prefs.bValue!!
        binding.lSwitch.isChecked = App.prefs.lValue!!
        binding.dSwitch.isChecked = App.prefs.dValue!!

        database =
            Firebase.database("https://dietcalendar-9e182-default-rtdb.asia-southeast1.firebasedatabase.app/")
        database.getReference("").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    when (data.key) {
                        "breakfast" -> binding.breakfastTime.text = data.value.toString()
                        "lunch" -> binding.lunchTime.text = data.value.toString()
                        "dinner" -> binding.dinnerTime.text = data.value.toString()
                    }
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding.bSwitch.setOnCheckedChangeListener { _, b ->
            App.prefs.bValue = b
        }

        binding.lSwitch.setOnCheckedChangeListener { _, b ->
            App.prefs.lValue = b
        }

        binding.dSwitch.setOnCheckedChangeListener { _, b ->
            App.prefs.dValue = b
        }
        binding.breakfastTime.setOnClickListener {
            timePick(binding.breakfastTime)
            myRef = database.getReference("breakfast")
        }

        binding.lunchTime.setOnClickListener {
            timePick(binding.lunchTime)
            myRef = database.getReference("lunch")
        }
        binding.dinnerTime.setOnClickListener {
            timePick(binding.dinnerTime)
            myRef = database.getReference("dinner")
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentAlarm.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentAlarm().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


