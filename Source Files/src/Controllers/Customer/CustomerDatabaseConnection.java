package Controllers.Customer;

import java.sql.*;

public class CustomerDatabaseConnection {
    
    public static Connection dbConnect() {
        try {
            Class.forName("org.sqlite.JDBC");  
            String path = "resources/mechanic_universal_New.db3";  
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path);
            return connection;
        }                                                                                                                       
        catch(Exception e) {
            System.out.println(e);
            System.out.println("Error connecting to the database - stage 1");
        }   
        return null;
    }  
}
