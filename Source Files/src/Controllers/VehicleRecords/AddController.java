/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.VehicleRecords;

/**
 * FXML Controller class
 *
 * @author ibraaheem
 */

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ibraaheem
 */
public class AddController implements Initializable {

    @FXML
    private ComboBox cid;
    @FXML
    private ComboBox make;
    @FXML
    private ComboBox es;
    @FXML
    private ComboBox model;
    @FXML
    private ComboBox colour;
    @FXML
    private DatePicker expire;
    @FXML
    private DatePicker service;
    @FXML
    private ComboBox fuel;
    @FXML
    private TextField reg;
    @FXML
    private TextField cn;
    @FXML
    private TextField address;
    @FXML
    private DatePicker warranty;
    @FXML
    private CheckBox check;
    @FXML
    private ComboBox ct;
    private boolean pick = false;
    @FXML
    private NumberTextField mile;
    @FXML
    private Button add;
    private boolean huh = true;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            this.getFuel();
            this.getColour();
            this.getCid();
            this.getEngine();
            this.getType();
            cn.setEditable(false);
            address.setEditable(false);
            warranty.setEditable(false);            
        } catch (SQLException ex) {
            Logger.getLogger(AddController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }        
     
    public void getCid() throws SQLException {
        
        Connection connection = DBConnection.dbConnect();       
         Statement stmt = connection.createStatement();     
         
         try {
        connection = DBConnection.dbConnect();       
        stmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Customer";      
        ResultSet row = stmt.executeQuery(sql); 
        String[] rows = new String[row.getInt(1)];
        
        String sql1 = "SELECT * FROM Customer";      
        ResultSet row1 = stmt.executeQuery(sql1); 
        
        int i =0;
        cid.getItems().clear();
        while(row1.next()) {
             rows[i] = row1.getString("ID");
            i++;
        }
        
        Arrays.sort(rows);
        cid.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Customers could not be found");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
        
    }
    
    public void getMake() throws SQLException {
        
        Connection connection = DBConnection.dbConnect();       
         Statement stmt = connection.createStatement();     
         
         try {
        connection = DBConnection.dbConnect();       
        stmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Make";      
        ResultSet row = stmt.executeQuery(sql); 
        String[] rows = new String[row.getInt(1)];
             
        String sql1 = "SELECT * FROM Make";      
        ResultSet row1 = stmt.executeQuery(sql1); 
        
        int i =0;
        make.getItems().clear();
        while(row1.next()) {
            rows[i] = row1.getString("Name");
            i++;
        }
        
        Arrays.sort(rows);
        make.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Makes could not be found");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
        
        
    }
    
    public void getModel() throws SQLException {
        
        Connection connection = DBConnection.dbConnect();       
         Statement stmt = connection.createStatement();     
         
         try {
        connection = DBConnection.dbConnect();       
        stmt = connection.createStatement();     
        
        String sql = "SELECT COUNT(*) FROM Model WHERE Make =? AND Car_Type =?";      
        PreparedStatement preparedStatementa = connection.prepareStatement(sql);
        preparedStatementa.setString(1, (String) make.getValue());
        preparedStatementa.setString(2, (String) ct.getValue());
        ResultSet row = preparedStatementa.executeQuery();
        String[] rows = new String[row.getInt(1)];
        
        if(row.getInt(1) == 0) {
           JOptionPane.showMessageDialog(null, "No models avaibale for this make and car type");
        }

        String sql1 = "SELECT * FROM Model WHERE Make =? AND Car_Type =?";      
        PreparedStatement preparedStatementb = connection.prepareStatement(sql1);
        preparedStatementb.setString(1, (String) make.getValue());
        preparedStatementb.setString(2, (String) ct.getValue());
        ResultSet row1 = preparedStatementb.executeQuery();
        
        int i =0;
        model.getItems().clear();
        while(row1.next()) {
             rows[i] = row1.getString("Name");
            i++;
        }
        
        Arrays.sort(rows);
        model.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Models could not be found");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
        
    }
    
     public void getColour() throws SQLException {
        
         Connection connection = DBConnection.dbConnect();       
         Statement stmt = connection.createStatement();     
         
         try {
        connection = DBConnection.dbConnect();       
        stmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Colour";      
        ResultSet row = stmt.executeQuery(sql); 
        String[] rows = new String[row.getInt(1)];
        
        String sql1 = "SELECT * FROM Colour";      
        ResultSet row1 = stmt.executeQuery(sql1); 
        
        int i =0;
        colour.getItems().clear();
        while(row1.next()) {
             rows[i] = row1.getString("Name");
            i++;
        }
        
        Arrays.sort(rows);
        colour.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Colours could not be found");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
         
         
    }
     
     public void getFuel() throws SQLException {
        
         Connection connection = DBConnection.dbConnect();       
         Statement stmt = connection.createStatement();     
         
         try {
        connection = DBConnection.dbConnect();       
        stmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Fuel_Type";      
        ResultSet row = stmt.executeQuery(sql); 
        String[] rows = new String[row.getInt(1)];
        
        String sql1 = "SELECT * FROM Fuel_Type";      
        ResultSet row1 = stmt.executeQuery(sql1); 
        
        int i =0;
        fuel.getItems().clear();
        while(row1.next()) {
             rows[i] = row1.getString("Size");
            i++;
        }
        
        Arrays.sort(rows);
        fuel.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Fuel types could not be found");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
         
    }
    
         public void getEngine() throws SQLException {
        
         Connection connection = DBConnection.dbConnect();       
         Statement stmt = connection.createStatement();     
         
         try {
        connection = DBConnection.dbConnect();       
        stmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Engine_Size";      
        ResultSet row = stmt.executeQuery(sql); 
        String[] rows = new String[row.getInt(1)];
        
        String sql1 = "SELECT * FROM Engine_Size";      
        ResultSet row1 = stmt.executeQuery(sql1); 
        
        int i =0;
        es.getItems().clear();
        while(row1.next()) {
             rows[i] = row1.getString("Column");
            i++;
        }
        
        Arrays.sort(rows);
        es.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Colours could not be found");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
         
         
    }
         
         public void getType() throws SQLException {
        
         Connection connection = DBConnection.dbConnect();       
         Statement stmt = connection.createStatement();     
         
         try {
        connection = DBConnection.dbConnect();       
        stmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Car_Type";      
        ResultSet row = stmt.executeQuery(sql); 
        String[] rows = new String[row.getInt(1)];
        
        String sql1 = "SELECT * FROM Car_Type";      
        ResultSet row1 = stmt.executeQuery(sql1); 
        
        int i =0;
        ct.getItems().clear();
        while(row1.next()) {
             rows[i] = row1.getString("Name");
            i++;
        }
        
        Arrays.sort(rows);
        ct.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Colours could not be found");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
         
         
    }
     
        @FXML
    private void handleButtonadd (ActionEvent event) throws Exception {
        
         LocalDate hello = LocalDate.now();
         
         if(warranty.getValue() != null) {
         if(warranty.getValue().isBefore(hello)) {
             
             JOptionPane.showMessageDialog(null, "Service date can not be in the past");
         }
         }
         
         if(service.getValue() != null) {
         if(service.getValue().isAfter(hello)) {
             
             JOptionPane.showMessageDialog(null, "Last Service date can not be in the future");
         }
         }
         
         if(!(reg.getText().equals(""))) {
         if(reg.getText().length() != 7) {
             
             JOptionPane.showMessageDialog(null, "Registration Must have 7 characters");
         }
           
         if(Character.isLetter(reg.getText().charAt(0)) && Character.isLetter(reg.getText().charAt(1)) && Character.isDigit(reg.getText().charAt(2)) && Character.isDigit(reg.getText().charAt(3)) && Character.isLetter(reg.getText().charAt(4)) && Character.isLetter(reg.getText().charAt(5)) && Character.isLetter(reg.getText().charAt(6))) {
                 
             } else {
                 
                 huh= false;
             JOptionPane.showMessageDialog(null, "Vehicle Registration must match UK number plate format");
                 
             }
         
         }
     
               
         if( (service.getValue() != null) && (expire.getValue() != null) &&  (reg.getText() != null) && (cid.getValue() != null) && (make.getValue() != null) && (es.getValue() != null) && (model.getValue() != null) && (colour.getValue() != null) && (fuel.getValue() != null) && (service.getValue().isBefore(hello)) && (reg.getText().length() == 7) && (huh = true)) {
             
            
             
              Connection connection = DBConnection.dbConnect();       
                           
              String geta = "SELECT Count(*) FROM Vehicles WHERE ID = ?";    
              PreparedStatement preparedStatementa = connection.prepareStatement(geta);
              preparedStatementa.setString(1, reg.getText());
              ResultSet getar = preparedStatementa.executeQuery();
             
             if(getar.getInt(1) == 1) {
                 JOptionPane.showMessageDialog(null, "Registration already belongs to a vehicle");  
                 return;
             }
             
             if ((pick) && (warranty.getValue().isAfter(hello))) {
             String sql = "INSERT INTO Vehicles " +
                   "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,1)";
             PreparedStatement ps = connection.prepareStatement(sql);
             
              String sqlb = "INSERT INTO Warranty " +
                   "VALUES(?,?,?,?)";
             PreparedStatement psg = connection.prepareStatement(sqlb);
             
             String query = "SELECT ID FROM Car_Type WHERE Name = ?"; 
             PreparedStatement psa = connection.prepareStatement(query);
             psa.setString(1, (String) ct.getValue());
             ResultSet set = psa.executeQuery();
             
             String queryb = "SELECT ID FROM Make WHERE Name = ?"; 
             PreparedStatement psb = connection.prepareStatement(queryb);
             psb.setString(1, (String) make.getValue());
             ResultSet setb = psb.executeQuery();
             
             String queryc = "SELECT ID FROM Model WHERE Name = ?"; 
             PreparedStatement psc = connection.prepareStatement(queryc);
             psc.setString(1, (String) model.getValue());
             ResultSet setc = psc.executeQuery();
             
             String queryd = "SELECT ID FROM Engine_Size WHERE Column = ?"; 
             PreparedStatement psd = connection.prepareStatement(queryd);
             psd.setString(1, (String) es.getValue());
             ResultSet setd = psd.executeQuery();
             
             String querye = "SELECT ID FROM Fuel_Type WHERE Size = ?"; 
             PreparedStatement pse = connection.prepareStatement(querye);
             pse.setString(1, (String) fuel.getValue());
             ResultSet sete = pse.executeQuery();
             
             String queryf = "SELECT ID FROM Colour WHERE Name = ?"; 
             PreparedStatement psf = connection.prepareStatement(queryf);
             psf.setString(1, (String) colour.getValue());
             ResultSet setf = psf.executeQuery();
                
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
             
             ps.setInt(1,maxnum);
             ps.setInt(2, Integer.parseInt((String )cid.getValue()));
             ps.setInt(3, set.getInt(1));
             ps.setInt(4, setb.getInt(1));
             ps.setInt(5, setc.getInt(1));
             ps.setInt(6, setd.getInt(1));
             ps.setInt(7, sete.getInt(1));
             ps.setInt(8, setf.getInt(1));
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
             LocalDate datea = expire.getValue();
             String expirestring = datea.format(formatter);
             ps.setString(9, expirestring);
             ps.setString(10, mile.getText());
             LocalDate dateb = service.getValue();
             String servicestring = dateb.format(formatter);
             ps.setString(11, servicestring);
             
             String count = "SELECT * FROM Warranty";      
             Statement newstmt = connection.createStatement();
             ResultSet countrows = newstmt.executeQuery(count); 
             
             int maxwar =0;
             
             while(countrows.next()){
                 if(countrows.getInt("ID")>maxwar) {
                   maxwar = countrows.getInt("ID");  
                 }
             }
              maxwar = maxwar + 1;
             newstmt.close();

             ps.setInt(12, maxwar);
             ps.setString(13, reg.getText().toUpperCase());
             
             psg.setInt(1,maxwar);
             psg.setString(2,cn.getText());
             
             LocalDate dated = warranty.getValue();
             String warexpire = dated.format(formatter);
            
             psg.setString(3,warexpire);
             psg.setString(4,address.getText());
             
             ps.executeUpdate();
             psg.executeUpdate();
             
             ps.close();
             psa.close();
             psb.close();
             psc.close();
             psd.close();
             pse.close();
             psf.close();
             psg.close();
            
             connection.close();
                       
             } else {
                 
                        String sql = "INSERT INTO Vehicles " +
                   "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,1)";
             PreparedStatement ps = connection.prepareStatement(sql);
             
             String query = "SELECT ID FROM Car_Type WHERE Name = ?"; 
             PreparedStatement psa = connection.prepareStatement(query);
             psa.setString(1, (String) ct.getValue());
             ResultSet set = psa.executeQuery();
             
             String queryb = "SELECT ID FROM Make WHERE Name = ?"; 
             PreparedStatement psb = connection.prepareStatement(queryb);
             psb.setString(1, (String) make.getValue());
             ResultSet setb = psb.executeQuery();
             
             String queryc = "SELECT ID FROM Model WHERE Name = ?"; 
             PreparedStatement psc = connection.prepareStatement(queryc);
             psc.setString(1, (String) model.getValue());
             ResultSet setc = psc.executeQuery();
             
             String queryd = "SELECT ID FROM Engine_Size WHERE Column = ?"; 
             PreparedStatement psd = connection.prepareStatement(queryd);
             psd.setString(1, (String) es.getValue());
             ResultSet setd = psd.executeQuery();
             
             String querye = "SELECT ID FROM Fuel_Type WHERE Size = ?"; 
             PreparedStatement pse = connection.prepareStatement(querye);
             pse.setString(1, (String) fuel.getValue());
             ResultSet sete = pse.executeQuery();
             
             String queryf = "SELECT ID FROM Colour WHERE Name = ?"; 
             PreparedStatement psf = connection.prepareStatement(queryf);
             psf.setString(1, (String) colour.getValue());
             ResultSet setf = psf.executeQuery();
             
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
             
             ps.setInt(1,maxnum);
             ps.setInt(2, Integer.parseInt((String) cid.getValue()));
             ps.setInt(3, set.getInt(1));
             ps.setInt(4, setb.getInt(1));
             ps.setInt(5, setc.getInt(1));
             ps.setInt(6, setd.getInt(1));
             ps.setInt(7, sete.getInt(1));
             ps.setInt(8, setf.getInt(1));
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
             LocalDate datea = expire.getValue();
             String expirestring = datea.format(formatter);
             ps.setString(9, expirestring);
             ps.setInt(10, Integer.parseInt(mile.getText()));
             LocalDate dateb = service.getValue();
             String servicestring = dateb.format(formatter);
             ps.setString(11, servicestring);
            
             ps.setString(12, " ");
             ps.setString(13, reg.getText().toUpperCase());
             
             ps.executeUpdate();

             ps.close();
             psa.close();
             psb.close();
             psc.close();
             psd.close();
             pse.close();
             psf.close();
             
             connection.close();
                      
             }
              JOptionPane.showMessageDialog(null, "Vehicle successfully added");
               ((Node)(event.getSource())).getScene().getWindow().hide();
         } else {
               
              JOptionPane.showMessageDialog(null, "Please fill in all fields");
             
         }
        
        
    } 
    
    @FXML
    private void clicked (ActionEvent event) throws Exception {
         
        if(!pick) {
        cn.setEditable(true);
        address.setEditable(true);
        warranty.setEditable(true);
        pick = true;
        } else {
            cn.setEditable(false);
             address.setEditable(false); 
              warranty.setEditable(false);
              cn.setText("");
              address.setText("");
              pick = false;
            
        }
    }
    
    @FXML
    private void getcarmake (ActionEvent event) throws Exception {
            this.getMake();
    
    }
    
    @FXML
    private void getcarmodel (ActionEvent event) throws Exception {
            this.getModel();
    }
        
}
