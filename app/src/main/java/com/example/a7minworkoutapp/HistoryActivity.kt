package com.example.a7minworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a7minworkoutapp.databinding.ActivityBmiBinding
import com.example.a7minworkoutapp.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private var binding : ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistoryActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true) //activates back button
            supportActionBar?.title = "HISTORY"
        }
        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}