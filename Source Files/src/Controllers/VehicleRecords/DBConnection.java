/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controllers.VehicleRecords;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Ibraaheem
 */
public class DBConnection {
    
    Connection connection = null;
    
    
 public  static Connection dbConnect(){
    
    Statement statement = null;
    ResultSet result = null;
    PreparedStatement ps = null;
     
     try {     
         Class.forName("org.sqlite.JDBC");
         String path = "resources//mechanic_universal_New.db3";
         Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path);
         //JOptionPane.showMessageDialog(null, "Succesfully connected to Database!");      
         return connection;
     } catch (Exception e){
         JOptionPane.showMessageDialog(null, e);
         return null;
     }
 }  
    
}
