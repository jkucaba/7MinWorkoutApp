package com.example.a7minworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.a7minworkoutapp.databinding.ActivityExerciseBinding
import com.example.a7minworkoutapp.databinding.ActivityMainBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater) //inicjalizacja bindingu
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true) //activates back button
        }

        binding?.toolBarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setupRestView()

    }

    private fun setupRestView(){
        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        setRestProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(10000, 1000){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }
            override fun onFinish() {
                Toast.makeText(
                    this@ExerciseActivity,
                    "Here now we will start the exercise",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.start() //startujemy timer
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
            binding = null

    }

}