#include <LiquidCrystal.h>
#include <SoftwareSerial.h>
#include <dht.h>
#include <Ultrasonic.h>

#define TRIGGER_PIN  3  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ECHO_PIN     4  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define MAX_DISTANCE 200 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

/* ==========================
    Author: Andersondev
    E-mail: contatoahenrique@gmail.com
    Version: 2.0
   ==========================
*/

//Object Instance
dht dhtHandler;
SoftwareSerial bluetooth(10, 11);

Ultrasonic ultrasonic(TRIGGER_PIN, ECHO_PIN);
const int pinLed = 12;
const int pinPir = 8;
const int dhtPin = 9;
const int fanPin = 6;

int temperatura = 0x00,
    umidade = 0x00;

String command;
int type = 0;
int quantidadeCaixa = 0 ;
int newDay = 1;

/* LIQUID CRYSTAL - LCD*/

int efeito = 2;

void setup() {
  // put your setup code here, to run once:
  pinMode(5, OUTPUT);
  bluetooth.begin(9600);
  Serial.begin(9600);
  //pinMode(pinPir, INPUT);
  pinMode(pinLed, OUTPUT);
  pinMode(fanPin, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  //char numero;
  //numero = Serial.read();
  command = ""; 
  float cmMsec, inMsec;
  long microsec = ultrasonic.timing();

  cmMsec = ultrasonic.convert(microsec, Ultrasonic::CM);
  inMsec = ultrasonic.convert(microsec, Ultrasonic::IN);
  dhtHandler.read11(dhtPin);

  temperatura = dhtHandler.temperature;
  umidade = dhtHandler.humidity;
 
    while (bluetooth.available()) {
      char caracter = bluetooth.read();
      command += caracter;
      delay(10);
    }

    if(command.indexOf("temperature") >= 0){
      type = 1;
    }

    if(command.indexOf("cmds") >= 0){
      type = 0;
    }

    if(command.indexOf("caixa") >= 0){
      type = 2;
    }


 
    if(type == 0){
      if (command.indexOf("led1") >= 0) {
        digitalWrite(pinLed, !digitalRead(pinLed));
      }
      if (command.indexOf("fan") >= 0) {
        digitalWrite(fanPin, !digitalRead(fanPin));
      }
       if(command.indexOf("alloff") >= 0){
        digitalWrite(pinLed, HIGH);
        digitalWrite(fanPin, LOW);
       }
       if(command.indexOf("allon") >= 0){
          digitalWrite(pinLed, LOW);
          digitalWrite(fanPin, HIGH);
       }
    }
    if(type == 1){
      bluetooth.println("{");
      bluetooth.print(temperatura);
      bluetooth.println(" ºC");
      bluetooth.println("}");
    }
    if(type == 2){
      bluetooth.println("{");
      bluetooth.print((int)cmMsec);
      bluetooth.println(" %");
      bluetooth.println("}");   
     delay(1000);
     }
 Serial.println((int)cmMsec);
     
}