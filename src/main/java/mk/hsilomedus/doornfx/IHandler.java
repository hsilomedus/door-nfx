/*
 * Copyright (C) 2014 by Netcetera AG.
 * All rights reserved.
 *
 * The copyright to the computer program(s) herein is the property of Netcetera AG, Switzerland.
 * The program(s) may be used and/or copied only with the written permission of Netcetera AG or
 * in accordance with the terms and conditions stipulated in the agreement/contract under which 
 * the program(s) have been supplied.
 */
package mk.hsilomedus.doornfx;

import javafx.scene.control.Button;


public interface IHandler {

  void registerButton(Button button);
  void processNFC(String passiveId);
  void processChar(char tickedChar);
  
  void init();
  
  
}
