package com.example.a7minworkoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minworkoutapp.databinding.ActivityExerciseBinding
import com.example.a7minworkoutapp.databinding.ActivityMainBinding
import com.example.a7minworkoutapp.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration: Long = 30

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null // Variable for TextToSpeech

    private var player : MediaPlayer? = null //media player

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater) //inicjalizacja bindingu
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true) //activates back button
        }
        binding?.toolBarExercise?.setNavigationOnClickListener {
            customDialogFOrBackButton()     //we customize what the back button should do
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)


        //binding?.flProgressBar?.visibility = View.GONE //INVISIBLE - ukrycie czegoś

        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    override fun onBackPressed() {
        customDialogFOrBackButton()
        // super.onBackPressed()
    }
    private fun customDialogFOrBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)  //inflate xml item and use its items // custom binding for xml file
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)       //user cannot cancel this dialog
        dialogBinding.tvYes.setOnClickListener { //button YES
            this@ExerciseActivity.finish()
            customDialog.dismiss()      //dismiss the custom dialog
        }
        dialogBinding.tvNo.setOnClickListener { //button NO
            customDialog.dismiss()
        }
        customDialog.show()

    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupRestView(){

        try{
            val soubdURI = Uri.parse("android.resource://com.example.a7minworkoutapp/" + R.raw.press_start) //passing the location of sound
            player = MediaPlayer.create(applicationContext, soubdURI)
            player?.isLooping = false
            player?.start()
        } catch(e: Exception){
            e.printStackTrace()
        }

        binding?.flProgressBar?.visibility = View.VISIBLE //rest bar invisible
        binding?.tvTitle?.visibility = View.VISIBLE       //rest text invisible
        binding?.tvExercise?.visibility = View.INVISIBLE      //visible title
        binding?.flExerciseView?.visibility = View.INVISIBLE  //visible exercise
        binding?.ivImage?.visibility = View.INVISIBLE         //visible image
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE


        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }



        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition+1].getName()

        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flProgressBar?.visibility = View.INVISIBLE //rest bar invisible
        binding?.tvTitle?.visibility = View.INVISIBLE       //rest text invisible
        binding?.tvExercise?.visibility = View.VISIBLE      //visible title
        binding?.flExerciseView?.visibility = View.VISIBLE  //visible exercise
        binding?.ivImage?.visibility = View.VISIBLE         //cisible image
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage()) //image from current exercise position
        binding?.tvExercise?.text = exerciseList!![currentExercisePosition].getName()            //set up the name opf the exercise
        setExerciseProgressBar()
    }
    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(restTimerDuration*1000, 1000){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }
            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start() //startujemy timer
    }
    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000, 1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }
            override fun onFinish() {

                if(currentExercisePosition < exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)

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

        //Shutting down the text to speech
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }

        if(player != null){
            player!!.stop()
        }

        binding = null

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}