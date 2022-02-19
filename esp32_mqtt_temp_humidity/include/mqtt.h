#ifndef _MQTT_H_
#define _MQTT_H_

/**
 * mqtt: Implement communication with a broker and coordinate
 * LED / Push button / Sensor value
 * 
 * Author: Atharva Lele <atharva@pdx.edu>
 */

#include <Arduino.h>
#include <EspMQTTClient.h>

#define mqttServer          "broker.hivemq.com"
#define mqttPort            1883
#define mqttClientName      "AtharvaLele"

#define deviceStatusTopic   "leleAtharva/deviceStatus"
#define tempTopic           "leleAtharva/tempHumidity/temperature"
#define humTopic            "leleAtharva/tempHumidity/humidity"
#define buttonTopic         "leleAtharva/button/state"

#define ledRTopic           "leleAtharva/rgbLED/R"
#define ledGTopic           "leleAtharva/rgbLED/G"
#define ledBTopic           "leleAtharva/rgbLED/B"
#define intervalTopic       "leleAtharva/tempHumidity/interval"

/* Functions */
void mqttSetup();
void mqttTask();
void mqttPublishTemp(float temp);
void mqttPublishHumidity(float hum);
void mqttPublishButton(bool state);

#endif