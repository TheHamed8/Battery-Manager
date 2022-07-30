package com.example.batterymanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.batterymanager.R
import com.example.batterymanager.adapter.BatteryUsageAdapter
import com.example.batterymanager.databinding.ActivityMainBinding
import com.example.batterymanager.databinding.ActivityUsageBatteryBinding
import com.example.batterymanager.model.BatteryModel
import com.example.batterymanager.utils.BatteryUsage
import java.util.ArrayList
import kotlin.math.roundToInt

private lateinit var binding: ActivityUsageBatteryBinding

class usageBatteryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsageBatteryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val batteryUsage = BatteryUsage(this)

        val batteryPercentArray: MutableList<BatteryModel> = ArrayList()

        for (item in batteryUsage.getUsageStateList()) {
            if (item.totalTimeInForeground > 0) {
                val bm = BatteryModel()
                bm.packageName = item.packageName
                bm.percentUsage =
                    (item.totalTimeInForeground.toFloat() / batteryUsage.getTotalTime()
                        .toFloat() * 100).toInt()
                batteryPercentArray += bm
            }
        }

        val adapter = BatteryUsageAdapter(this,batteryPercentArray, batteryUsage.getTotalTime())
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter

    }
}