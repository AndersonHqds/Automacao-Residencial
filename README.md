# Home automation
Home automation software, developed in java (Android studio) also containing the program used for arduino. With it it is possible to manage light and fan through voice commands or push button, check water level, local temperature and amount spent in bath.

## Requirements

* Android Studio
* Arduino
    * Arduino board
    * Arduino IDE
    * HC06 bluetooth module
    * 2 relay modules (if using real fan and lamp instead of led and motor fan)
    * Ultrasonic (sonar) sensor for water level checking (other)
    * Resistors
    * Temperature Sensor - ** DHT11 **
* Bluetooth-enabled mobile phone and voice commands


## How to use
Import the program into android studio, [generate a signed apk] (https://developer.android.com/studio/publish/app-signing). Download the apk on your mobile device, now import the file ** home-automation / home-automation.ino ** into the arduino IDE, plug your arduino card into the USB port and run the program. Then install the downloaded apk on the mobile device and run the app. The app will search for devices via ** bluetooth ** select your device. For more questions you can read my [course completion paper] (https://drive.google.com/open?id=1P0ldJ0zhQmZbo53xhM7W8o4EEBLaNnEE).

## App Images

! [Splash Screen] (home-automation/images/images-027.png)
! [Home] (home-automation/images/images-028.png)
! [Water level] (home-automation/images/images-029.png)