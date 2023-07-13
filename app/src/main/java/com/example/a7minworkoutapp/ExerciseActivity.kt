package com.example.a7minworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.a7minworkoutapp.databinding.ActivityExerciseBinding
import com.example.a7minworkoutapp.databinding.ActivityMainBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater) //inicjalizacja bindingu
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true) //activates back button
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.toolBarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        //binding?.flProgressBar?.visibility = View.GONE //INVISIBLE - ukrycie czegoś

        setupRestView()

    }

    private fun setupRestView(){
        binding?.flProgressBar?.visibility = View.VISIBLE //rest bar invisible
        binding?.tvTitle?.visibility = View.VISIBLE       //rest text invisible
        binding?.tvExercise?.visibility = View.INVISIBLE      //visible title
        binding?.flExerciseView?.visibility = View.INVISIBLE  //visible exercise
        binding?.ivImage?.visibility = View.INVISIBLE         //cisible image


        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flProgressBar?.visibility = View.INVISIBLE //rest bar invisible
        binding?.tvTitle?.visibility = View.INVISIBLE       //rest text invisible
        binding?.tvExercise?.visibility = View.VISIBLE      //visible title
        binding?.flExerciseView?.visibility = View.VISIBLE  //visible exercise
        binding?.ivImage?.visibility = View.VISIBLE         //cisible image

        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage()) //image from current exercise position
        binding?.tvExercise?.text = exerciseList!![currentExercisePosition].getName()            //set up the name opf the exercise
        setExerciseProgressBar()
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
                currentExercisePosition++
                setupExerciseView()
            }
        }.start() //startujemy timer
    }
    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000, 1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }
            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    setupRestView()
                } else {
                    Toast.makeText(
                        this@ExerciseActivity,
                        "Congrats! You have finished the 7 minutes workout.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start() //startujemy timer
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

            binding = null

    }

}