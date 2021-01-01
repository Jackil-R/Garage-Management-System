/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controllers.ScheduledMaintenanceBookings;

import java.sql.*;

/**
 *
 * @author Julians
 */
public class DBConnection {
    
    Connection connection = null;
    
 public static Connection dbConnect(){
     if (main.GLOBAL.isAuthenticated())
     try {     
         Class.forName("org.sqlite.JDBC");
         Connection connection = DriverManager.getConnection("jdbc:sqlite:resources/mechanic_universal_New.db3");
       //  System.out.println("Connection Succesful");
         return connection;
     } catch (Exception e) {
         System.out.println(e);
         return null;
     }
     return null;
 }  
    
}
