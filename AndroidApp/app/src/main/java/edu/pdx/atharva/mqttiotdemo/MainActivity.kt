package edu.pdx.atharva.mqttiotdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.slider.Slider
import edu.pdx.atharva.mqttiotdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Binding for the Main Activity
    lateinit var binding: ActivityMainBinding

    // Temperature-Humidity Sensor values
    var temperature = 0.0f
    var humidity = 0.0f
    var senseInterval = 5   // default of 5s

    // LED Brightness values
    var ledRedBrightness = 0
    var ledGreenBrightness = 0
    var ledBlueBrightness = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup interval slider
        binding.intervalSlider.setLabelFormatter { value: Float ->
            val format = value.toInt().toString() + "s"
            format.toString()
        }

        binding.intervalSlider.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
                Log.d("IntervalSlider", "Starting tracking")
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                val interval = slider.value.toInt()
                Log.d("IntervalSlider", interval.toString())
                binding.intervalVal.text = interval.toString() + "s"
            }
        })

        // Setup RGB Sliders
        binding.redLedSlider.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
                Log.d("R_Slider", "Starting tracking")
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                Log.d("R_Slider", slider.value.toInt().toString())
            }
        })

        binding.greenLedSlider.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
                Log.d("G_Slider", "Starting tracking")
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                Log.d("G_Slider", slider.value.toInt().toString())
            }
        })

        binding.blueLedSlider.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
                Log.d("B_Slider", "Starting tracking")
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                Log.d("B_Slider", slider.value.toInt().toString())
            }
        })
    }

}