package com.example.dietcalender

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CustomDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var database: FirebaseDatabase

    fun showDia(Day: String, Category: String) {
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dataLoad(Day, Category) // Data Load
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val foodContent = dialog.findViewById<EditText>(R.id.foodContent)
        val calorie = dialog.findViewById<EditText>(R.id.calorieContent)
        val addBtn = dialog.findViewById<Button>(R.id.add)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancel)

        addBtn.setOnClickListener {
            database =
                Firebase.database("https://dietcalendar-9e182-default-rtdb.asia-southeast1.firebasedatabase.app/")
            val myRef = database.getReference("")
            myRef.child("$Day-$Category").child("food").setValue(foodContent.text.toString())
            myRef.child("$Day-$Category").child("calorie").setValue(calorie.text.toString())
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun dataLoad(Day:String, Category: String) {
        val food = dialog.findViewById<TextView>(R.id.food)
        val calorieCnt = dialog.findViewById<TextView>(R.id.kcalCnt)
        database =
            Firebase.database("https://dietcalendar-9e182-default-rtdb.asia-southeast1.firebasedatabase.app/")
        database.getReference("$Day-$Category").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    when (data.key) {
                        "food" -> food.text = data.value.toString()
                        "calorie" -> calorieCnt.text = data.value.toString()
                    }
                }
            }
        })
    }
}
