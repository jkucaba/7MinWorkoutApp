package com.example.a7minworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.example.a7minworkoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) //inny sposób do find view by id
        setContentView(binding?.root)

        //val flStartButton: FrameLayout = findViewById(R.id.flStart)
        binding?.flStart?.setOnClickListener{   //we can acces theirs id directely //we can use it in whole file
            Toast.makeText(
                this@MainActivity,
                "Here we will start the exercise",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null  //unassign binding
    }
}