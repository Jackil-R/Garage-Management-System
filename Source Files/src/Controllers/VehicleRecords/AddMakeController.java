/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.VehicleRecords;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ibraaheem
 */



public class AddMakeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TextField make;
    @FXML
    private Button add;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void addmake (ActionEvent event) throws Exception {
    
          if(!make.getText().equals("")) {
          Connection connection = DBConnection.dbConnect();
          
          String geta = "SELECT Count(*) FROM Vehicles WHERE ID = ?";    
              PreparedStatement preparedStatementa = connection.prepareStatement(geta);
              preparedStatementa.setString(1, make.getText());
              ResultSet getar = preparedStatementa.executeQuery();
             
             if(getar.getInt(1) == 1) {
                 JOptionPane.showMessageDialog(null, "Make already in entred in system");  
                 return;
             }
 
          String sql = "INSERT INTO Make " +
                   "VALUES(?,?)";
          PreparedStatement ps = connection.prepareStatement(sql);
          
          Statement stmt = connection.createStatement();
             String max = "SELECT * FROM Vehicles"; 
             ResultSet row = stmt.executeQuery(max); 
             
             int maxnum =0;
             
             while(row.next()){
                 if(row.getInt("ID") > maxnum) {
                   maxnum = row.getInt("ID"); 
                 }           
             }
              maxnum = maxnum + 1;
             stmt.close();
  
          ps.setString(1, Integer.toString(maxnum));
          ps.setString(2, (String) make.getText());
          
         
          ps.close();
          connection.close(); 
          } else {
             
              JOptionPane.showMessageDialog(null, "Plese enter a make name"); 
              
          }
          JOptionPane.showMessageDialog(null, "Make Inserted"); 
          ((Node)(event.getSource())).getScene().getWindow().hide();
        
    }
    
}
