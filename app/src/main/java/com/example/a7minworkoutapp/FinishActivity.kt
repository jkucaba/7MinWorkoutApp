package com.example.a7minworkoutapp

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.a7minworkoutapp.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.util.*


class FinishActivity : AppCompatActivity() {

    private var binding : ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true) //activates back button
        }
        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener {
            finish()

        }

        val historyDao = (application as WorkoutApp).db.historyDao()
        addDateToDatabase(historyDao)

    }

    private fun addDateToDatabase(historyDao: HistoryDao){

        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.e("Date: ", "" +dateTime)

        val sdf = SimpleDateFormat("dd MMM yyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)
        Log.e("Formatted Date: ", "" +date)

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))      //  insert into database
            Log.e(
                "Date :",
                "Added..."
            )
        }

    }
}