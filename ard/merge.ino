#include <Wire.h>
#include <Adafruit_PN532.h>
#include "HX711.h"
#include <SoftwareSerial.h>
#include <RtcDS1302.h>

SoftwareSerial bluetooth(2, 3); // RX, TX

#define SDA_PIN A4
#define SCL_PIN A5

#define calibration_factor -7050.0

const int DOUT = 4;
const int CLK = 5;

HX711 scale;
ThreeWire myWire(8, 9, 7); // IO(DAT), SCLK, CE
RtcDS1302<ThreeWire> Rtc(myWire);

float sum_kg = 0.00; // Variable to store cumulative weight

Adafruit_PN532 nfc(SDA_PIN, SCL_PIN);

unsigned long previousResetMillis = 0; // Variable to store the previous reset time in milliseconds
const unsigned long resetInterval = 2 * 60 * 1000; // 2 minutes in milliseconds

void setup(void) {
  // Initialize the scale
  scale.begin(DOUT, CLK);
  scale.set_scale(calibration_factor);
  scale.tare();

  // Initialize the NFC module
  nfc.begin();

  // Initialize Bluetooth communication
  bluetooth.begin(9600);

  // RTC module
  Rtc.Begin();
  RtcDateTime compiled = RtcDateTime(__DATE__, __TIME__);
  // Set the current date and time
  RtcDateTime currentTime(2023, 11, 05, 17, 10, 0); // Year, Month, Day, Hour, Minute, Second
  Rtc.SetDateTime(compiled);
}

void loop(void) {
  nfc.SAMConfig();

  // Read the RTC time
  RtcDateTime now = Rtc.GetDateTime();

  // Calculate the current time in milliseconds
  unsigned long currentMillis = now.Second() * 1000; // Convert seconds to milliseconds

  // Read the weight from the scale
  scale.set_scale(calibration_factor);
  float weight_kg = scale.get_units(1); // Convert weight to kilograms

  // Check if N minutes have passed since the last reset
  if (currentMillis - previousResetMillis >= resetInterval) {
    sum_kg = 0.0; // Reset the cumulative weight
    weight_kg =0.0;
    previousResetMillis = currentMillis; // Update the previous reset time
  }

  if (weight_kg >= 0) {
    Serial.print("Weight: ");
    Serial.print(weight_kg, 2);
    Serial.println(" kg");

    // Update cumulative weight
    sum_kg += weight_kg;

    // Print cumulative weight
    Serial.print("Sum: ");
    Serial.print(sum_kg, 2);
    Serial.println(" kg");
  } else {
    weight_kg = 0.0;
    Serial.print("Weight: 0.0 kg\n");
  }

  // Check for an NFC card
  uint8_t success;
  uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };
  uint8_t uidLength;
  success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uidLength);

  if (success) {
    Serial.println("NFC card detected!");

    Serial.print("UID Value (Decimal): ");
    for (uint8_t i = 0; i < uidLength; i++) {
      Serial.print(uid[i]);
      if (i < uidLength - 1) {
        Serial.print(", ");
      }
    }
    Serial.println();

    // Send weight and UID over Bluetooth
    bluetooth.print("UID: ");
    for (uint8_t i = 0; i < uidLength; i++) {
      bluetooth.print(uid[i]);
      if (i < uidLength - 1) {
        bluetooth.print(" ");
      }
    }
    bluetooth.println();
  }

  bluetooth.print("Time: ");
  bluetooth.print(now.Year(), DEC);
  bluetooth.print('/');
  bluetooth.print(now.Month(), DEC);
  bluetooth.print('/');
  bluetooth.print(now.Day(), DEC);
  bluetooth.print(' ');
  bluetooth.print(now.Hour(), DEC);
  bluetooth.print(':');
  bluetooth.print(now.Minute(), DEC);
  bluetooth.print(':');
  bluetooth.print(now.Second(), DEC);
  bluetooth.println();

  bluetooth.print("Weight: ");
  bluetooth.print(weight_kg, 2);
  bluetooth.print(" kg, Sum: ");
  bluetooth.print(sum_kg, 2);
  bluetooth.print(" kg");
  bluetooth.println();

  delay(1000);
}
