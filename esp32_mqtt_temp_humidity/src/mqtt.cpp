/**
 * mqtt: Implement communication with a broker and coordinate
 * LED / Push button / Sensor value
 * 
 * Author: Atharva Lele <atharva@pdx.edu>
 */

#include "mqtt.h"
#include "button.h"
#include "rgb_led.h"
#include "sensor.h"

static EspMQTTClient mqttClient(
    mqttServer,
    mqttPort,
    mqttClientName
);

/* LED callbacks */
static void ledRedCallback(const String& msg)
{
    uint8_t brightness = msg.toInt();
    Serial.printf("Brightness R: %d\n", brightness);
    LEDSetState(LED_R, brightness);
}

static void ledGreenCallback(const String& msg)
{
    uint8_t brightness = msg.toInt();
    LEDSetState(LED_G, brightness);
}

static void ledBlueCallback(const String& msg)
{
    uint8_t brightness = msg.toInt();
    LEDSetState(LED_B, brightness);
}

/* Sensor Callback */
static void sensorIntervalCallback(const String& msg)
{
    uint8_t interval = msg.toInt();
    sensorSetReadInterval(interval);
}


/* Connection established callback */
void onConnectionEstablished()
{
    /* Turn LED Green once connection is established */
    LEDSetState(LED_R, 0);
    LEDSetState(LED_G, 255);
    LEDSetState(LED_B, 0);

    /* Subscribe to topics */
    mqttClient.subscribe(ledRTopic, ledRedCallback);
    mqttClient.subscribe(ledGTopic, ledGreenCallback);
    mqttClient.subscribe(ledBTopic, ledBlueCallback);
    mqttClient.subscribe(intervalTopic, sensorIntervalCallback);

    /* Publish device connected message */
    mqttClient.publish(deviceStatusTopic, "1");
}

void mqttPublishTemp(float temp)
{
    const String tempStr = String(temp);
    mqttClient.publish(tempTopic, tempStr);
}

void mqttPublishHumidity(float hum)
{
    const String humStr = String(hum);
    mqttClient.publish(humTopic, humStr);
}

void mqttPublishButton(bool state)
{
    const String buttonStr = String(state);
    mqttClient.publish(buttonTopic, buttonStr);
}

void mqttSetup()
{
    mqttClient.enableDebuggingMessages();
}

void mqttTask()
{
    /* Publish pending MQTT messages, blink LED when doing so */
    if (buttonMqttPending) {
        digitalWrite(LED_BUILTIN, HIGH);
        mqttPublishButton(buttonGetStatus());
        buttonMqttPending = false;
        digitalWrite(LED_BUILTIN, LOW);
    }

    if (tempHumidityMqttPending) {
        digitalWrite(LED_BUILTIN, HIGH);
        mqttPublishTemp(sensorGetTemp());
        mqttPublishHumidity(sensorGetHumidity());
        tempHumidityMqttPending = false;
        digitalWrite(LED_BUILTIN, LOW);
    }

    /* MQTT Loop */
    mqttClient.loop();
}