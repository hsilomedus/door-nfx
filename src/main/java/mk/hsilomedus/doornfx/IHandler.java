
package mk.hsilomedus.doornfx;

import javafx.scene.control.Button;


public interface IHandler {

  void registerButton(Button button);
  void processNFC(String passiveId);
  void processChar(char tickedChar);
  
  void init();
  
  
}
