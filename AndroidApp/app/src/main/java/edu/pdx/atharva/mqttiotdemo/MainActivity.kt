package edu.pdx.atharva.mqttiotdemo

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.slider.Slider
import edu.pdx.atharva.mqttiotdemo.databinding.ActivityMainBinding
import org.eclipse.paho.client.mqttv3.*

class MainActivity : AppCompatActivity() {
    // Binding for the Main Activity
    lateinit var binding: ActivityMainBinding

    // MQTT Client
    private lateinit var mqttClient: MQTTClient
    private lateinit var mqttClientID: String

    // Temperature-Humidity Sensor values
    var temperature = 0.0f
    var humidity = 0.0f
    var senseInterval = 5   // default of 5s

    // LED Brightness values
    var ledRedBrightness = 0
    var ledGreenBrightness = 0
    var ledBlueBrightness = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if connected to internet, quit if not
        if (!isInternetConnected()) {
            Toast.makeText(this, "Internet NOT AVAILABLE, quitting.", Toast.LENGTH_LONG).show()
            finish()
        }

        // Setup MQTT Stuff
        mqttClientID = MqttClient.generateClientId()
        mqttClient = MQTTClient(this, MQTT_SERVER_URI, mqttClientID)

        // Connect to the server
        mqttClient.connect(
            MQTT_USERNAME,
            MQTT_PWD,
            object: IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    val msg = "Connected to server!"
                    Log.d("MQTT", msg)
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()

                    // Subscribe to topics here
                    subscribeMqttTopics()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    val msg = "Could not connect to server!"
                    Log.d("MQTT", msg)
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    exception?.printStackTrace()
                }
            },

            object: MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val msg = "$topic: $message"
                    Log.d("MQTT", msg)
                    handleMqttMessage(topic.toString(), message.toString())
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.d("MQTT", "Lost connection")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.d("MQTT", "Delivery complete")
                }
            }
        )

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
                setSensorInterval(interval)
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
                setLedBrightness("R", slider.value.toInt())
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
                setLedBrightness("G", slider.value.toInt())
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
                setLedBrightness("B", slider.value.toInt())
                Log.d("B_Slider", slider.value.toInt().toString())
            }
        })
    }

    // Helper functions
    @RequiresApi(Build.VERSION_CODES.M)
    private fun isInternetConnected(): Boolean {
        var connected = false;
        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilites = connManager.getNetworkCapabilities(connManager.activeNetwork)
        if (capabilites != null) {
            if (capabilites.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilites.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                connected = true;
                Log.d("NW", "NW Available")
            }
        } else {
            Log.d("NW", "NW Unavailable")
        }

        return connected
    }

    // Subscribe to MQTT Topics
    private fun subscribeMqttTopics() {
        Log.d("MQTT", "Subscribing to topics")

        if (mqttClient.isConnected()) {
            // Temperature
            mqttClient.subscribe(
                topic = TEMPERATURE_TOPIC,
                qos = 1,
                object: IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d("MQTT", "Subscribed to temperature")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d("MQTT", "Could not subscribe to temperature")
                    }
                }
            )

            // Humidity
            mqttClient.subscribe(
                topic = HUMIDITY_TOPIC,
                qos = 1,
                object: IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d("MQTT", "Subscribed to humidity")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d("MQTT", "Could not subscribe to humidity")
                    }
                }
            )

            // Button
            mqttClient.subscribe(
                topic = BUTTON_TOPIC,
                qos = 1,
                object: IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d("MQTT", "Subscribed to button")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d("MQTT", "Could not subscribe to button")
                    }
                }
            )
        }
    }

    // Handle MQTT messages
    private fun handleMqttMessage(topic: String, message: String) {
        when (topic) {
            TEMPERATURE_TOPIC -> {
                if (binding.celsiusBtn.isChecked) {
                    binding.tempText.text = "$message °C"
                } else {
                    binding.tempText.text = "${cToF(message.toFloat())} °F"
                }
            }

            HUMIDITY_TOPIC -> {
                binding.humText.text = "$message % rH"
            }

            BUTTON_TOPIC -> {
                binding.toggleButton2.isChecked = message == "1"
            }
        }
    }

    // C to F converter
    private fun cToF(celsius: Float) : Float {
        var fahrenheit = celsius
        fahrenheit = (fahrenheit * 9 / 5) + 32
        return fahrenheit
    }

    // Publish sensor interval
    private fun setSensorInterval(interval: Int) {
        if (mqttClient.isConnected()) {
            mqttClient.publish(
                INTERVAL_TOPIC,
                interval.toString(),
                1,
                false,
                object: IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d("Interval", "Published")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d("Interval", "Failed to publish")
                    }
                }
            )
        }
    }

    // LED Brightness MQTT message publishers
    private fun setLedBrightness(led: String, brightness: Int) {
        lateinit var topic: String
        when (led) {
            "R" -> topic = LED_R_TOPIC
            "G" -> topic = LED_G_TOPIC
            "B" -> topic = LED_B_TOPIC
        }

        if (mqttClient.isConnected()) {
            mqttClient.publish(
                topic,
                brightness.toString(),
                1,
                false,
                object: IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d("LED", "Published")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d("LED", "Failed to publish")
                    }
                }
            )
        }
    }
}