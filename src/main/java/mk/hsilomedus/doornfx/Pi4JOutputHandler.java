
package mk.hsilomedus.doornfx;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


public class Pi4JOutputHandler {
  
  public Pi4JOutputHandler() {
    
  }
  
  private GpioController gpio;
  
  private GpioPinDigitalOutput doorPin;
  private GpioPinDigitalOutput beepPin;
  
  public void init() {
 // create gpio controller
    gpio = GpioFactory.getInstance();
    doorPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
    beepPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
    
  }
  
  public void openLock() {
    if (doorPin != null) {
      doorPin.high();
    } 
  }
  
  
  public void closeLock() {
    if (doorPin != null) {
      doorPin.low();
    }
  }
  
  public void beepNFC() {
    beep(500);
  }
  
  public void beepButton() {
    beep(200);
  }
  
  public void beepOK() {
    beep(1000);
  }
  
  public void beepNotOK() {
    try {
      for (int i = 0; i < 5; i++) {
        beep(200);
        Thread.sleep(100);
      }
    } catch (InterruptedException exc) {
      // Do nothing
    } finally {
      beepPin.low();
    }
  }
  
  private void beep(int millis) {
    beepPin.high();
    try {
      Thread.sleep(millis);
    } catch (InterruptedException exc) {
      //Do nothing
    } finally {
      beepPin.low();
    }
  }

}
