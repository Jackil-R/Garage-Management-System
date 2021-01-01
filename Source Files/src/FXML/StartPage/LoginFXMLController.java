package FXML.StartPage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.System.in;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import main.BCrypt;
import main.GLOBAL;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * FXML Controller class
 *
 * @author Julians
 */
public class LoginFXMLController implements Initializable {

    private Stage stage;
    public TextField tfUsername;
    public PasswordField tfPassword;
    public Button btnLogin;
    public ImageView imgLogo;
    public ImageView imgExit;
    private MotionBlur mb = new MotionBlur();
    private boolean loaded = false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tfUsername.requestFocus();
            }
       });
        
        mb.setRadius(0);
        try {
            // TODO
            BufferedImage bI;
            bI = ImageIO.read(new File("resources/login_logo.png"));
            Image image = SwingFXUtils.toFXImage(bI, null);
            imgLogo.setImage(image);
             imgLogo.setEffect(mb);
             
             
             BufferedImage bI2;
            bI2 = ImageIO.read(new File("resources/exit_icon.png"));
            Image image2 = SwingFXUtils.toFXImage(bI2, null);
            imgExit.setImage(image2);
            
            imgExit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

     @Override
     public void handle(MouseEvent event) {
         closeWindow();
         event.consume();
     }
});
            
        } catch (IOException ex) {
            Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void loadMain() throws IOException{
        
        
        
        loaded = true;
    
        FXMLLoader loader = new FXMLLoader(
    getClass().getResource(
      "/FXML/StartPage/StartPage.fxml"
    )
  );

  Stage stage = new Stage();
  stage.setTitle("Garage Management System: Group 2D");
  stage.setResizable(false);

  stage.getIcons().add(new Image("/images/gears_icon.png"));
  stage.setScene(
    new Scene(
      (Pane) loader.load()
    )
  );
       
              
     closeWindow();
    stage.showAndWait();
  
        
    }
    
    private void closeWindow(){
    Stage sb = (Stage)btnLogin.getScene().getWindow();
    sb.close();
    }
    
    public void playSound(){
        
        new Thread(
            new Runnable() {
                public void run() {
                    try {
                        
                        
                                         
            FileInputStream in = new FileInputStream("resources/login.wav");
            AudioStream as = new AudioStream(in);
            AudioPlayer.player.start(as);            
      
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        
    }
    
    public void initLogin() throws SQLException, IOException{
        if (loaded) return;
        
    String un = tfUsername.getText();
    String pw = tfPassword.getText();
    
    if (checkLogin(un,pw)) {
            playSound();
            loadMain();
   
    }
    else
    {
//    Dialogs.create()
//                             .owner(stage)
//                             .title("Error!")
//                             .masthead(null)
//                             .message("Incorrect Password or Username!")
//                             .showInformation();
        mb.setRadius(0);
    
    }
    
    }
    
    
    private boolean checkLogin(String username, String password) throws SQLException{
    
        
     try {           
           Class.forName("org.sqlite.JDBC");  
          Connection connection = DriverManager.getConnection("jdbc:sqlite:resources/mechanic_universal_New.db3");   
      String sql = "SELECT * FROM Mechanics WHERE Username = ?;";  
      
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, username);
      ResultSet set = preparedStatement.executeQuery();     
      String pw = set.getString("Password");
      
      int id = 0;
      if (checkMatch(password, pw)){
          id = set.getInt("ID");
          GLOBAL.setMechanicID(id);
          GLOBAL.setMechName(set.getString("First_Name")+" "+set.getString("Last_Name"));
          GLOBAL.setAuthenticated(true);
          preparedStatement.close();
          return true;
      }     
     preparedStatement.close();
        } catch (SQLException e) {
           
        //    System.out.println("SQL Exception");
                   //  e.printStackTrace();
        }  catch (Exception e) {
                  //   e.printStackTrace();
        } 
     
     displayFail();
     return false;
     
    }
    
    public void displayFail(){
        
    
   new Thread(new Runnable() {
    @Override public void run() {
        for (int i=1; i<=10000; i++) {
            final int counter = i;
            Platform.runLater(new Runnable() {
                @Override public void run() {
                   mb.setRadius(counter/250);
                   
                   
                   if (counter == 10000) 
                       
                       
                       new Thread(new Runnable() {
    @Override public void run() {
        for (int i=10000; i >= 0; i--) {
            final int counter = i;
            Platform.runLater(new Runnable() {
                @Override public void run() {
                   mb.setRadius(counter/250);
                  
                }
            });
        }
    }
}).start();
                   
                   
                   
                   
                }
            });
        }
    }
}).start();
   
   
    
    
    
    }
    
    private boolean checkMatch(String given, String db){
        
    if (BCrypt.checkpw(given, db))
	return true;
    else
	return false;
    }
    
    @FXML
    private void checkForEnterPressed(KeyEvent event) throws SQLException, IOException {
        if (event.getCode() == KeyCode.ENTER)
            initLogin();
    }
    
    
}
