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

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class Handler implements IHandler {
  
  private static final int INITIALIZING = -1;
  private static final int WAITING_FOR_NFC = 0;
  private static final int WAITING_FOR_CODE = 1;
  private static final int SUCCESS = 10;
  private static final int FAIL = 11;
  
  private int state = INITIALIZING;
  private List<Button> buttons = new ArrayList<Button>();
  
  private String passCode;
  
  private Label statusLabel;
  
  public Handler(Label statusLabel) {
    this.statusLabel = statusLabel;
  }
  
  @Override
  public void init() {
    switchToState(WAITING_FOR_NFC);    
  }
  
  @Override
  public void registerButton(Button button) {
    buttons.add(button);
  }
  
  @Override
  public void processChar(char tickedChar) {
    if (state == WAITING_FOR_CODE) {
      passCode += tickedChar;
      statusLabel.setText(statusLabel.getText() + "*");
      if (passCode.length() >= 4) {
        
        
        
      }
          
    }
    
  }
  
  @Override
  public void processNFC(String passiveId) {
    // TODO Auto-generated method stub
    
  }
  
  private void switchToState(int newState) {
    
  }
  

}
