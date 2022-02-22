/**
 * sensor: Wrapper around AHT20 library by Adafruit
 * 
 * Author: Atharva Lele <atharva@pdx.edu>
 */

#include "sensor.h"
#include "mqtt.h"

/* Global variables */
bool sensorPresent = false;
bool tempHumidityMqttPending = false;

/* Static variables */
static Adafruit_AHTX0 aht;                      // AHT20 sensor object
static sensors_event_t hum_event, temp_event;   // Humidity, Temperature events
static float humidity, temperature;
static unsigned long lastReadTime = 0;
static uint8_t readInterval = 1;                // Default interval of 1s

/* Setup sensor and return false if sensor not found */
bool sensorSetup()
{
    if (aht.begin()) {
        sensorPresent = true;
        return true;
    } else {
        return false;
    }
}

/* Read the sensor values and store into variables */
void sensorRead()
{
    aht.getEvent(&hum_event, &temp_event);
    humidity = hum_event.relative_humidity;
    temperature = temp_event.temperature;
}

/* Set reading interval */
void sensorSetReadInterval(uint8_t sec)
{
    if (sec != 0) {
        readInterval = sec;
        Serial.printf("Interval updated to: %ds\n", readInterval);
    } else {
        Serial.println("ERROR: Cannot set interval to zero!");
    }
}

/* Get the read interval */
uint8_t sensorGetReadInterval()
{
    return readInterval;
}

/* Return values */
float sensorGetTemp()
{
    return temperature;
}

float sensorGetHumidity()
{
    return humidity;
}

/* Sensor task called from superloop */
void sensorTask()
{
    if (sensorPresent) {
        /* Is it time to read the sensor? */
        if ((millis() >= (lastReadTime + (readInterval * 1000))) || lastReadTime == 0) {
            sensorRead();
            lastReadTime = millis();
            tempHumidityMqttPending = true;
            Serial.printf("Reading sensor: %lu\n", lastReadTime);
            Serial.printf("Temp: %.2f C, Humidity: %.2f rH\n", temperature, humidity);
        }
    }
}