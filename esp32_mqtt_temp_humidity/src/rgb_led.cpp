/**
 * rgb_led: LED driver around ledc() PWM functions
 * provided by the ESP32 HAL. Catered for the ECE558
 * project requirements.
 * 
 * Author: Atharva Lele <atharva@pdx.edu>
 */

#include "rgb_led.h"

/* Individual LED states -- ON/OFF */
bool ledON_R = false;
bool ledON_G = false;
bool ledON_B = false;
uint8_t ledR_brightness = 0;
uint8_t ledG_brightness = 0;
uint8_t ledB_brightness = 0;

/* Dutycycle */
bool dutyCycleIncrease = true;
uint16_t LEDDutyCycle = 0;

/* Setup pins for RGB LED */
void LEDSetup()
{
    /* Attach to ledc PWM controller */
    ledcSetup(ledcLEDChannel_R, ledcFreq, ledcResolution);
    ledcSetup(ledcLEDChannel_G, ledcFreq, ledcResolution);
    ledcSetup(ledcLEDChannel_B, ledcFreq, ledcResolution);

    /* Attach R, G, B pins to the controller */
    ledcAttachPin(LED_R_PIN, ledcLEDChannel_R);
    ledcAttachPin(LED_G_PIN, ledcLEDChannel_G);
    ledcAttachPin(LED_B_PIN, ledcLEDChannel_B);

    /* Set initial colour to red */
    LEDSetState(LED_R, 255);
    LEDSetState(LED_G, 0);
    LEDSetState(LED_B, 0);
}

/* Wrapper to set LED brightness */
void LEDSetState(uint8_t led, uint8_t brightness)
{
    ledcWrite(led, brightness);
}
