package Controllers.ScheduledMaintenanceBookings;

import Controllers.ScheduledMaintenanceBookings.BookingHandler;
import Controllers.ScheduledMaintenanceBookings.Mechanic;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import main.BCrypt;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author Julians
 */
public class AuthFXMLController implements Initializable {
    @FXML
    private Button btnLogin;
    @FXML
    public Label lbMechName;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private ImageView imgLogo,imgExit;

    private boolean authed = false;
    private int otherMechID;

    public void setOtherMechID(int otherMechID) {
        BookingHandler dbH = new BookingHandler();
       Mechanic m = dbH.getMechanicByID(otherMechID);
        String mName = m.getFirstName()+" "+m.getLastName();
        lbMechName.setText(lbMechName.getText()+"  "+mName);
        this.otherMechID = otherMechID;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            BufferedImage bI;
            bI = ImageIO.read(new File("resources/login_logo.png"));
            Image image = SwingFXUtils.toFXImage(bI, null);
            imgLogo.setImage(image);
            
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
            Logger.getLogger(AuthFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void initLogin(ActionEvent event) {
        
        try {
            checkLogin(otherMechID, tfPassword.getText());
        } catch (SQLException ex) {
            Logger.getLogger(AuthFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void closeWindow(){
    Stage sb = (Stage)btnLogin.getScene().getWindow();
    sb.close();
    }
    
    
    private boolean checkLogin(int mID, String password) throws SQLException{
    
       Connection connection = null;
        PreparedStatement preparedStatement = null;
     try {  
         
         Class.forName("org.sqlite.JDBC");
          connection = DriverManager.getConnection("jdbc:sqlite:resources/mechanic_universal_New.db3");
             
             
      String sql = "SELECT * FROM Mechanics WHERE ID = ?;";  
      
       preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, mID);
      ResultSet set = preparedStatement.executeQuery(); 
      
      String pw = set.getString("Password");
      if (checkMatch(password, pw)){
          authed = true;
          closeWindow();
          preparedStatement.close();
          connection.close();
          return true;
      } 
      
      else {
      Dialogs.create()
                             .owner(btnLogin)
                             .title("Error!")
                             .masthead(null)
                             .message("Incorrect Password or Username!")
                             .showInformation();
      }
     preparedStatement.close();
        } catch (Exception e) {
                     System.out.println("Incorrect Password");
                    preparedStatement.close(); 
                    connection.close();
        }     
        
     return false;
     
    }
    
    private boolean checkMatch(String given, String db){
        
    if (BCrypt.checkpw(given, db))
	return true;
else
	return false;
    }
    
    public boolean getResult(){
    
        return authed;
    }
}
