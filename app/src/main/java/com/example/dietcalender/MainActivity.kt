package com.example.dietcalender

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dietcalender.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
        .add(R.id.frameLayout, mainView())
        transaction.commit()

        binding.mainBtn.setOnClickListener {
            replaceFragment()
        }
    }
    /*
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(fragment)
        ...
        그리고 사용할 프래그먼트만 show하고 나머지는 hide한다.

        transaction.show(fragmentToUse)
        transaction.hide(fragmentNotForUse)
        ...
        transaction.commit()
     */
    private fun replaceFragment() {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, mainView())
        transaction.commit()
    }
}
