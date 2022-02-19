#ifndef _BUTTON_H_
#define _BUTTON_H_

/**
 * button: implementation of button debouncing 
 * and wrapper around Arduino's digitalRead()
 * 
 * Author: Atharva Lele <atharva@pdx.edu>
 */

#include <Arduino.h>

extern bool buttonMqttPending;

/* Functions */
void buttonSetup();
bool buttonGetStatus();
void buttonTask();

#endif