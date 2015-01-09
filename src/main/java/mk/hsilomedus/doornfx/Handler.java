
package mk.hsilomedus.doornfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class Handler implements IHandler {
  
  private static final int INITIALIZING = -1;
  private static final int WAITING_FOR_NFC = 0;
  private static final int WAITING_FOR_CODE = 1;
  private static final int INVALIDATING = 5;
  private static final int SUCCESS = 10;
  private static final int FAIL = 11;
  
  private int state = INITIALIZING;
  private List<Button> buttons = new ArrayList<Button>();
  
  private String currentPassiveId;
  private String passCode;
  
  private Label statusLabel;
  private Pi4JOutputHandler pi4jOutputHandler;
  
  public Handler(Label statusLabel, Pi4JOutputHandler pi4jOutputHandler) {
    this.statusLabel = statusLabel;
    this.pi4jOutputHandler = pi4jOutputHandler;
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
      if (passCode.length() == 0) {
        statusLabel.setText("");
      }
      
      if (tickedChar == '<' && passCode.length() > 0) {
        passCode = passCode.substring(0, passCode.length()-1);
      } else if (tickedChar == '!') {
        switchToState(INVALIDATING);
        checkCredentials();
      } else {
        passCode += tickedChar;
        statusLabel.setText(statusLabel.getText() + "*");
      }
    }
  }
  
  @Override
  public void processNFC(String passiveId) {
    if (state == WAITING_FOR_NFC) {
      //this comes from a non-javafx thread
      Platform.runLater(()->{
        this.currentPassiveId = passiveId;
        switchToState(WAITING_FOR_CODE);
      });
    }
  }
  
  private boolean remoteAcessService = false;
  private boolean shouldPass = true;
  private void checkCredentials() {
    if (remoteAcessService) {
      String completeString = currentPassiveId + "_" + passCode;
      String hashed = "" + completeString.hashCode();
      System.out.println("Asked creds for hash: " + hashed);
      String URItoCall = "http://192.168.1.110:55506/DoorNFXWeb/web/access/getForKey?key=" + hashed;
      
      URL u;
      try {
        u = new URL(URItoCall);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        
        System.out.println("Response Code : " + responseCode);
     
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
     
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();
        
        String outResponse = response.toString();
        
        if (responseCode == 200 && "OK".equals(outResponse)) {
          switchToState(SUCCESS);
        } else {
          switchToState(FAIL);
        }
     
        //print result
        System.out.println(response.toString());
        
      } catch (MalformedURLException e) {
        System.out.println("Malformed URL Exception " + e);
        e.printStackTrace();
      } catch (IOException e) {
        System.out.println("IO Exception " + e);
        e.printStackTrace();
      }
      
      
    } else {
      Platform.runLater(()->{
        try {
          Thread.sleep(200);
        } catch (Exception e) { }
        if (shouldPass) {
          switchToState(SUCCESS);
        } else {
          switchToState(FAIL);
        }
        shouldPass = !shouldPass;
      });
    }
  }
  
  private void switchToState(int newState) {
    switch (newState) {
      case WAITING_FOR_NFC:
        buttons.stream().forEach(button -> button.setVisible(false));
        statusLabel.setText("Tag in");
        statusLabel.setStyle("-fx-font-size: 15pt; -fx-font-weight:bold; -fx-border-color:red; -fx-background-color: blue;");
        passCode = "";
        pi4jOutputHandler.closeLock();
        break;
      case WAITING_FOR_CODE:
        buttons.stream().forEach(button -> button.setVisible(true));
        statusLabel.setText("Enter code");
        statusLabel.setStyle("-fx-font-size: 15pt; -fx-font-weight:bold; -fx-border-color:red; -fx-background-color: blue;");
        passCode = "";
        pi4jOutputHandler.closeLock();
        break;
      case INVALIDATING:
        buttons.stream().forEach(button -> button.setVisible(false));
        statusLabel.setText("Wait...");
        statusLabel.setStyle("-fx-font-size: 15pt; -fx-font-weight:bold; -fx-border-color:red; -fx-background-color: yellow;");
        pi4jOutputHandler.closeLock();
        break;
      case FAIL:
        statusLabel.setText("Forbidden!");
        statusLabel.setStyle("-fx-font-size: 15pt; -fx-font-weight:bold; -fx-border-color:red; -fx-background-color: red;");
        pi4jOutputHandler.closeLock();
        Platform.runLater(() -> {
          try {
            Thread.sleep(4000);
          } catch (Exception e) {
            
          }
          switchToState(WAITING_FOR_NFC);
        });
        break;
      case SUCCESS:
        statusLabel.setText("Granted!");
        statusLabel.setStyle("-fx-font-size: 15pt; -fx-font-weight:bold; -fx-border-color:red; -fx-background-color: green;");
        pi4jOutputHandler.openLock();
        Platform.runLater(() -> {
          try {
            Thread.sleep(4000);
          } catch (Exception e) {
            
          }
          switchToState(WAITING_FOR_NFC);
        });
        break;
      default:
        pi4jOutputHandler.openLock();
        break;
    }
    
    state = newState;
  }
  

}
