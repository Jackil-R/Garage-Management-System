/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.DiagnosisRepairBookings;
import java.sql.*;
/**
 *
 * @author jackilrajnicant
 */
public class DatabaseConnection {
    
   
    public static Connection dbConnect(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:resources//mechanic_universal_New.db3");
            return connection;
        }catch(Exception e){
            System.out.println("Error connecting to the database - stage 1");
        }   
        return null;
    }
}
