
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
  
  public void init() {
 // create gpio controller
    gpio = GpioFactory.getInstance();
    doorPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
    
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

}
