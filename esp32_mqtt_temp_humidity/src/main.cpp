#include <Arduino.h>
#include <WiFi.h>

#include "button.h"
#include "mqtt.h"
#include "rgb_led.h"
#include "sensor.h"

// #define WIFI_SSID "Y.A.A.S.2.4"
// #define WIFI_PASS "Storyline@809"

#define WIFI_SSID "BlueBlink"
#define WIFI_PASS "1604SWGHP426"

/* Connect to WiFi */
void wifiConnect()
{
    /* Station Mode */
    WiFi.mode(WIFI_STA);

    Serial.println("Connecting to WiFi...");
    WiFi.begin(WIFI_SSID, WIFI_PASS);
    while (WiFi.status() != WL_CONNECTED) {
        LEDSetState(LED_R, 255);
        LEDSetState(LED_B, 255);
        delay(250);
        LEDSetState(LED_R, 0);
        LEDSetState(LED_B, 0);
        delay(250);
    }

    /* Connected to Wifi, set LED to Cyan */
    LEDSetState(LED_R, 0);
    LEDSetState(LED_B, 255);
    LEDSetState(LED_G, 255);
}

void setup()
{
    /* Setup serial connection */
    Serial.begin(115200);

    /* Setup LEDs, buttons, sensors */
    buttonSetup();
    if (sensorSetup())
    {
        Serial.println("Sensor Found!");
    }
    else
    {
        Serial.println("Sensor error!");
        while (1); // Wait in error
    }

    /* RGB LED Setup */
    LEDSetup();
    /* On-board LED setup */
    pinMode(LED_BUILTIN, OUTPUT);

    /* Setup WiFi */
    wifiConnect();

    /* MQTT Setup */
    mqttSetup();
}

void loop()
{
    buttonTask();
    sensorTask();
    mqttTask();
}