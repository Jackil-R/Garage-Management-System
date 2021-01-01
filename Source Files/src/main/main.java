package main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/StartPage/LoginFXML.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Garage System");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/images/gears_icon.png"));
        stage.show();
       
    }

    public static void main(String[] args) {
        
        launch(args);
    }
    
}
