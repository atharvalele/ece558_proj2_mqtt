#ifndef _SENSOR_H_
#define _SENSOR_H_

/**
 * sensor: Wrapper around AHT20 library by Adafruit
 * 
 * Author: Atharva Lele <atharva@pdx.edu>
 */

#include <Arduino.h>
#include <Adafruit_AHTX0.h>

/* Variables */
extern bool sensorPresent;
extern bool tempHumidityMqttPending;

/* Functions */
bool sensorSetup();
void sensorTask();
void sensorSetReadInterval(uint8_t sec);
uint8_t sensorGetReadInterval();
float sensorGetTemp();
float sensorGetHumidity();

#endif