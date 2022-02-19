/**
 * button: implementation of button debouncing 
 * and wrapper around Arduino's digitalRead()
 * 
 * Author: Atharva Lele <atharva@pdx.edu>
 */

#include "button.h"

static bool buttonPressed = false;
static bool lastButtonState = false;
static uint8_t buttonPin = 21;
static unsigned long pressDetectTime = 0;
static uint8_t debounceTime = 25;

bool buttonMqttPending = false;

/* Setup button pin mode */
void buttonSetup()
{
    // Using INPUT_PULLDOWN since I don't have a
    // pulldown resistor on the board
    pinMode(buttonPin, INPUT_PULLDOWN);
}

/* Return button status */
bool buttonGetStatus()
{
    return buttonPressed;
}

/* Button Task */
void buttonTask()
{
    /* Read pin status */
    bool pinVal = digitalRead(buttonPin);

    /* Reset timer if button status changed */
    if (pinVal != lastButtonState) {
        pressDetectTime = millis();
    }
    
    /* Button has stabilized, update status */
    if ((millis() - pressDetectTime) > debounceTime) {
        if (pinVal != buttonPressed) {
            buttonPressed = pinVal;
            buttonMqttPending = true;
            Serial.printf("Button Press: %d\n", buttonPressed);
        }
    }

    /* Update last state */
    lastButtonState = pinVal;
}