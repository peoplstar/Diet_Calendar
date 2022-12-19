package com.example.dietcalender

import android.R
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
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

    private lateinit var af: AlarmFunctions

    private val binding: FragmentAlarmBinding by lazy {
        FragmentAlarmBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        af = AlarmFunctions(requireContext())

        timeInit()
        binding.bSwitch.isChecked = App.prefs.bValue!!
        binding.lSwitch.isChecked = App.prefs.lValue!!
        binding.dSwitch.isChecked = App.prefs.dValue!!

    }

    private fun timeInit() {
        binding.breakfastTime.text =
            if (App.prefs.bTime.isNullOrBlank()) "00:00" else App.prefs.bTime!!

        binding.lunchTime.text =
            if (App.prefs.lTime.isNullOrBlank()) "00:00" else App.prefs.lTime!!

        binding.dinnerTime.text =
            if (App.prefs.dTime.isNullOrBlank()) "00:00" else App.prefs.dTime!!

    }

    private fun timePick(Category: TextView){
        var times = ""
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            times = "%02d:%02d".format(hourOfDay, minute)
            Category.text = times

            when(Category.tag.toString()){
                "b" -> {
                    App.prefs.bTime = times
                    if (binding.bSwitch.isChecked) {
                        af.cancelAlarm(1)
                        val time = "$times"
                        setAlarm(time, 1, "BREAKFAST")
                    }
                }
                "l" -> {
                    App.prefs.lTime = times
                    if (binding.lSwitch.isChecked) {
                        af.cancelAlarm(2)
                        val time = "$times"
                        setAlarm(time, 2, "LUNCH")
                    }
                }
                "d" -> {
                    App.prefs.dTime = times
                    if (binding.dSwitch.isChecked) {
                        af.cancelAlarm(3)
                        val time = "$times"
                        setAlarm(time, 3, "DINNER")
                    }
                }
            }
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

    private fun setAlarm(time : String, alarmCode : Int, content : String){
        af.callAlarm(time, alarmCode, content)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding.bSwitch.setOnCheckedChangeListener { _, b ->
            App.prefs.bValue = b
            val time = "${binding.breakfastTime.text}:00"
            if(b){
                setAlarm(time, 1, "BREAKFAST")
            }
            else {
                af.cancelAlarm(1)
            }
        }

        binding.lSwitch.setOnCheckedChangeListener { _, b ->
            App.prefs.lValue = b
            val time = "${binding.lunchTime.text}:00"
            if(b){
                setAlarm(time, 2, "LUNCH")
            }
            else {
                af.cancelAlarm(2)
            }
        }

        binding.dSwitch.setOnCheckedChangeListener { _, b ->
            App.prefs.dValue = b
            val time = "${binding.dinnerTime.text}:00"
            if(b){
                setAlarm(time, 3, "DINNER")
            }
            else {
                af.cancelAlarm(3)
            }
        }

        binding.breakfastTime.setOnClickListener {
            timePick(binding.breakfastTime)
        }

        binding.lunchTime.setOnClickListener {
            timePick(binding.lunchTime)

        }
        binding.dinnerTime.setOnClickListener {
            timePick(binding.dinnerTime)
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


