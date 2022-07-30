package com.example.batterymanager.activity

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Color
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.batterymanager.R
import com.example.batterymanager.model.BatteryModel
import com.example.batterymanager.utils.BatteryUsage
import com.example.batterymanager.databinding.ActivityMainBinding
import com.example.batterymanager.helper.SpManager
import com.example.batterymanager.services.BatteryAlarmService
import java.util.ArrayList
import kotlin.math.roundToInt

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initDrawer()
        serviceConfig()
        registerReceiver(batteryInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private fun initDrawer() {
        binding.imgMenu.setOnClickListener {
            binding.drawer.openDrawer(Gravity.RIGHT)
        }

        binding.incDrawer.txtAppUsage.setOnClickListener {
            startActivity(Intent(this@MainActivity, usageBatteryActivity::class.java))
            binding.drawer.closeDrawer(Gravity.RIGHT)
        }
    }

    private fun serviceConfig() {
        if (SpManager.isServiceOn(this@MainActivity) == true) {
            binding.incDrawer.serviceSwitchText.text = "Notification State"
            binding.incDrawer.switchDrawer.isChecked = true
        } else {
            binding.incDrawer.serviceSwitchText.text = "Notification State"
            binding.incDrawer.switchDrawer.isChecked = false
            stopService()
        }

        binding.incDrawer.switchDrawer.setOnCheckedChangeListener { Switch, isCheck ->
            SpManager.setServiceState(this@MainActivity, isCheck)

            if (isCheck) {
                startService()
                Toast.makeText(applicationContext, "service is turned on", Toast.LENGTH_SHORT)
                    .show()
            } else {
                stopService()
                Toast.makeText(applicationContext, "service is turned off", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun startService() {
        val serviceIntent = Intent(this, BatteryAlarmService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun stopService() {
        val serviceIntent = Intent(this, BatteryAlarmService::class.java)
        stopService(serviceIntent)
    }

    private var batteryInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            var batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

            if ((intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0)) {
                binding.txtPlug.text = "plug-out"
            } else {
                binding.txtPlug.text = "plug-in"
            }

            binding.txtTemp.text =
                (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10).toString() + " °C"
            binding.txtVoltage.text =
                (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000).toString() + " volt"
            binding.txtTech.text = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)


            binding.circularProgressBar.progressMax = 100f
            binding.circularProgressBar.setProgressWithAnimation(batteryLevel.toFloat())
            binding.txtCharge.text = batteryLevel.toString() + "%"

            val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
            when (health) {
                BatteryManager.BATTERY_HEALTH_COLD -> {
                    binding.txtHealth.text = "your battery is cold"
                    binding.txtHealth.setTextColor(Color.BLUE)
                    binding.imgHealth.setImageResource(R.drawable.cold)
                }
                BatteryManager.BATTERY_HEALTH_DEAD -> {
                    binding.txtHealth.text = "your battery is dead, please change it"
                    binding.txtHealth.setTextColor(Color.BLACK)
                    binding.imgHealth.setImageResource(R.drawable.dead)
                }
                BatteryManager.BATTERY_HEALTH_GOOD -> {
                    binding.txtHealth.text = "your battery health is good"
                    binding.txtHealth.setTextColor(Color.GREEN)
                    binding.imgHealth.setImageResource(R.drawable.good)
                }
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> {
                    binding.txtHealth.text = "your battery is over voltage"
                    binding.txtHealth.setTextColor(Color.RED)
                    binding.imgHealth.setImageResource(R.drawable.overvol)
                }
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> {
                    binding.txtHealth.text = "your battery is overheat"
                    binding.txtHealth.setTextColor(Color.YELLOW)
                    binding.imgHealth.setImageResource(R.drawable.overheat)
                }
                else -> {
                    binding.txtHealth.text = "your battery is dead, please change it"
                    binding.txtHealth.setTextColor(Color.BLACK)
                    binding.imgHealth.setImageResource(R.drawable.dead)

                }


            }
        }

    }

    override fun onBackPressed() {
        val dialogBuilder = AlertDialog.Builder(this)
            .setMessage("Do you want to close this app?")
            .setCancelable(true)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                finish()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Exit App")
        alert.show()

    }
}