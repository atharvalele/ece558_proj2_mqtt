#ifndef _RGB_LED_H_
#define _RGB_LED_H_

/**
 * rgb_led: LED driver around ledc() PWM functions
 * provided by the ESP32 HAL. Catered for the ECE558
 * project requirements.
 * 
 * Author: Atharva Lele <atharva@pdx.edu>
 */

#include <Arduino.h>

#define LED_R   0
#define LED_G   1
#define LED_B   2

/* LED Pins */
const uint8_t LED_R_PIN = 14;
const uint8_t LED_G_PIN = 32;
const uint8_t LED_B_PIN = 15;

/* LED PWM Properties */
const uint16_t ledcFreq = 5000;
const uint8_t ledcLEDChannel_R = 0;
const uint8_t ledcLEDChannel_G = 1;
const uint8_t ledcLEDChannel_B = 2;
const uint8_t ledcResolution = 10;

/* Functions */
void LEDSetup();
void LEDSetState(uint8_t led, uint8_t brightness);

#endif