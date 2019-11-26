

#include "BluetoothSerial.h"
#include <string.h>

#include <GxEPD.h>

#include <GxGDEH029A1/GxGDEH029A1.h>

#include <Fonts/FreeMono9pt7b.h>
#include <Fonts/FreeMono12pt7b.h>

#include <GxIO/GxIO_SPI/GxIO_SPI.h>
#include <GxIO/GxIO.h>


GxIO_Class io(SPI, SS, 22, 21);
GxEPD_Class display(io, 16, 4);
#define INPUT_SIZE 200

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;

void setup()
{
  Serial.begin(115200);
  SerialBT.begin("ESP32test"); //Bluetooth device name

  display.init();
  display.setRotation(1);
  display.setTextColor(GxEPD_BLACK);
  delay(6000);  
}

void loop()
{
  char input[INPUT_SIZE + 1];
    char ch_arr[16][21]; 
    int i = 0;
  if (SerialBT.available())
  {
//       Serial.write(SerialBT.readString());
    //    display.fillScreen(GxEPD_WHITE);
    //    display.setRotation(1);
    //    display.setCursor(50, 15);
    //    display.println(SerialBT.readString());
    //    display.update();

    
    byte size = SerialBT.readBytes(input, INPUT_SIZE);
    input[size] = 0;

    char *command = strtok(input, ";");
    while (command != NULL)
    {
      strcpy(ch_arr[i], command);
      command = strtok(NULL, ";");
      Serial.write(ch_arr[i]);
      Serial.write("/n");
      i++;
    }
    display.fillScreen(GxEPD_WHITE);
    
    display.setRotation(atoi(ch_arr[0]));
    showFont("FreeMono12pt7b", &FreeMono12pt7b);
    display.setCursor(atoi(ch_arr[1]), atoi(ch_arr[2]));
    display.println(ch_arr[3]);
    display.setCursor(atoi(ch_arr[4]), atoi(ch_arr[5]));
    showFont("FreeMono9pt7b", &FreeMono9pt7b);
    display.println(ch_arr[6]);
    display.setCursor(atoi(ch_arr[7]), atoi(ch_arr[8]));
    display.println(ch_arr[9]);
    display.setCursor(atoi(ch_arr[10]), atoi(ch_arr[11]));
    display.println(ch_arr[12]);
    display.setCursor(atoi(ch_arr[13]), atoi(ch_arr[14]));
    display.println(ch_arr[15]);
    display.update();
    
  }
  delay(20);
}

void showFont(const char name[], const GFXfont* f)
{
  display.setFont(f);
}
