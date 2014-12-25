package mk.hsilomedus.doornfx;
import mk.hsilomedus.pn532.IPN532Interface;
import mk.hsilomedus.pn532.PN532;
import mk.hsilomedus.pn532.PN532I2C;

/*
 * Copyright (C) 2014 by Netcetera AG. All rights reserved. The copyright to the computer program(s)
 * herein is the property of Netcetera AG, Switzerland. The program(s) may be used and/or copied
 * only with the written permission of Netcetera AG or in accordance with the terms and conditions
 * stipulated in the agreement/contract under which the program(s) have been supplied.
 */

public class Pi4JHandler implements Runnable {

  static final byte PN532_MIFARE_ISO14443A = 0x00;

  private IHandler handler;

  public Pi4JHandler(IHandler handler) {
    this.handler = handler;
  }

  public void run() {
    
    try {
      IPN532Interface pn532Interface = new PN532I2C();
      PN532 nfc = new PN532(pn532Interface);
  
      // Start
      System.out.println("Starting up...");
      nfc.begin();
      Thread.sleep(1000);
  
      long versiondata = nfc.getFirmwareVersion();
      if (versiondata == 0) {
        System.out.println("Didn't find PN53x board");
        throw new RuntimeException("Didn't find PN53x board");
      }
      // Got ok data, print it out!
      System.out.print("Found chip PN5");
      System.out.println(Long.toHexString((versiondata >> 24) & 0xFF));
  
      System.out.print("Firmware ver. ");
      System.out.print(Long.toHexString((versiondata >> 16) & 0xFF));
      System.out.print('.');
      System.out.println(Long.toHexString((versiondata >> 8) & 0xFF));
  
      // configure board to read RFID tags
      nfc.SAMConfig();
  
      System.out.println("Waiting for an ISO14443A Card ...");
  
      byte[] buffer = new byte[8];
      while (true) {
        int readLength = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, buffer);
  
        if (readLength > 0) {
          System.out.println("Found an ISO14443A card");
  
          System.out.print("  UID Length: ");
          System.out.print(readLength);
          System.out.println(" bytes");
  
          String passiveId = "";
          for (int i = 0; i < readLength; i++) {
            passiveId += Integer.toHexString(buffer[i]);
          }
          System.out.println("  UID Value: [" + passiveId + "]");
          handler.processNFC(passiveId);
        }
  
        Thread.sleep(500);
      }
    } catch (InterruptedException intException) {
      //fall gracefully
      System.out.println("Thread interrupted, closing PN handler.");
    } catch (Throwable exc) {
      System.out.println("No RasPi detected or error occured");
//      statusBox.setStyle("-fx-font-size: 30pt; -fx-font-weight:bold; -fx-border-color:red; -fx-background-color: red;");
      exc.printStackTrace();
    }
  }

}
