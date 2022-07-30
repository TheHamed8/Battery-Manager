package com.example.batterymanager.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.batterymanager.databinding.ActivitySplashBinding
import com.example.batterymanager.helper.SpManager
import java.util.*
import kotlin.concurrent.timerTask

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Timer().schedule(timerTask {
            runOnUiThread(timerTask {
                binding.helpTxt.text = "I will take care of your battery"
            })
        }, 1000)

        Timer().schedule(timerTask {
            runOnUiThread(timerTask {
                binding.helpTxt.text = "You should take care of your battery too"
            })
        }, 4000)


        Timer().schedule(timerTask {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 7000)

    }

}