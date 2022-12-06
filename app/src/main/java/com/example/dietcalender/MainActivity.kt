package com.example.dietcalender

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.example.dietcalender.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var alarmTransaction : FragmentTransaction
    private lateinit var mainTransaction : FragmentTransaction

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private fun checkPermission() {

        // 1. 위험권한(Camera) 권한 승인상태 가져오기
        val cameraPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            // 카메라 권한이 승인된 상태일 경우

        } else {
            // 카메라 권한이 승인되지 않았을 경우
            requestPermission()
        }
    }

    // 2. 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 99)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkPermission()
        replaceFragment()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

        binding.mainBtn.setOnClickListener {
            replaceFragment()
        }

        binding.alarmBtn.setOnClickListener {
            alarmReplaceFragment()
        }

    }

    private fun replaceFragment() {
        mainTransaction = supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, mainView().apply {
            })
        mainTransaction.commit()

    }

    private fun alarmReplaceFragment() {
        alarmTransaction = supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, FragmentAlarm())
        alarmTransaction.commit()
    }
}