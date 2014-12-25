package mk.hsilomedus.doornfx;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
  

  public static void main(String[] args) {
    launch(args);
  }
  
  private IHandler handler; 

  @Override
  public void start(Stage primaryStage) {
       
    int width = 480;
    int height = 640;
    
    primaryStage.setTitle("DoorNFX");
           
    Pane root = new Pane();
    
    root.setPrefWidth(width);
    root.setPrefHeight(height);
    
    Label statusBox = new Label();
    statusBox.setPrefWidth(420);
    statusBox.setPrefHeight(70);
    statusBox.setAlignment(Pos.CENTER);
    statusBox.setTextFill(Color.web("#bbbbff"));
    statusBox.setStyle("-fx-font-size: 30pt; -fx-font-weight:bold; -fx-border-color:red; -fx-background-color: blue;");
    statusBox.setLayoutY(10);
    statusBox.setLayoutX(30);
    statusBox.setText("Tag in");
    root.getChildren().add(statusBox);
    
    handler = new Handler(statusBox);

    Pi4JHandler pi4jHandler = new Pi4JHandler(handler); 
    Thread thrpij4 = new Thread(pi4jHandler);
    thrpij4.start();
    
    
    root.getChildren().add(createAndRegisterNumButton("<", 500, 30));
    root.getChildren().add(createAndRegisterNumButton("0", 500, 180));
    root.getChildren().add(createAndRegisterNumButton("!", 500, 330));
    
    root.getChildren().add(createAndRegisterNumButton("7", 370, 30));
    root.getChildren().add(createAndRegisterNumButton("8", 370, 180));
    root.getChildren().add(createAndRegisterNumButton("9", 370, 330));
    
    root.getChildren().add(createAndRegisterNumButton("4", 240, 30));
    root.getChildren().add(createAndRegisterNumButton("5", 240, 180));
    root.getChildren().add(createAndRegisterNumButton("6", 240, 330));
    
    root.getChildren().add(createAndRegisterNumButton("1", 110, 30));
    root.getChildren().add(createAndRegisterNumButton("2", 110, 180));
    root.getChildren().add(createAndRegisterNumButton("3", 110, 330));
    
    
    Scene mainScene = new Scene(root, width, height);
    mainScene.setCursor(Cursor.NONE);
     
    handler.init();
    
    primaryStage.setScene(mainScene);    
    primaryStage.setFullScreen(true);
    primaryStage.show();
  }
  
  private Button createAndRegisterNumButton(String caption, int y, int x) {
    Button roundButton = new Button();

    roundButton.setStyle(
            "-fx-background-radius: 5em; " +
            "-fx-min-width: 120px; " +
            "-fx-min-height: 120px; " +
            "-fx-max-width: 120px; " +
            "-fx-max-height: 120px; " +
            "-fx-font-size: 36pt; -fx-font-weight:bold; "
    );
    
    roundButton.setText(caption);
    roundButton.setLayoutX(x);
    roundButton.setLayoutY(y);
    roundButton.setFocusTraversable(false);
    roundButton.setOnAction(event -> handler.processChar(caption.charAt(0)));
    
    handler.registerButton(roundButton);
    
    return roundButton;
    
  }

}
