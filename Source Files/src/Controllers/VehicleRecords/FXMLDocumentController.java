    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.VehicleRecords;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import javax.swing.*;
import main.GLOBAL;


/**
 *
 * @author ibraaheem
 */

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private TableView mytable;
    @FXML
    private TableColumn vid;
    @FXML
    private TableColumn cid;
    @FXML
    private TableColumn ctid;
    @FXML
    private TableColumn mid;
    @FXML
    private TableColumn mdid;
    @FXML
    private TableColumn esid;
    @FXML
    private TableColumn ftid;
    @FXML
    private TableColumn coid;
    @FXML
    private TableColumn mote;
    @FXML
    private TableColumn mile;
    @FXML
    private TableColumn lsd;
    @FXML
    private TableColumn wid;
    @FXML
    private ComboBox mybox;
    @FXML
    private ComboBox mybox2;
    
    Connection connection = null;
         
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        
        vid.setCellValueFactory(new PropertyValueFactory<vr, String>("vid"));
        cid.setCellValueFactory(new PropertyValueFactory<vr, Integer>("cid"));
        ctid.setCellValueFactory(new PropertyValueFactory<vr, String>("ctid"));
        mid.setCellValueFactory(new PropertyValueFactory<vr, String>("mid"));
        mdid.setCellValueFactory(new PropertyValueFactory<vr, String>("mdid"));
        esid.setCellValueFactory(new PropertyValueFactory<vr, String>("esid"));
        ftid.setCellValueFactory(new PropertyValueFactory<vr, String>("ftid"));
        coid.setCellValueFactory(new PropertyValueFactory<vr, String>("coid"));
        mote.setCellValueFactory(new PropertyValueFactory<vr, String>("mote"));
        mile.setCellValueFactory(new PropertyValueFactory<vr, String>("mile"));
        lsd.setCellValueFactory(new PropertyValueFactory<vr, String>("lsd"));
        mile.setCellValueFactory(new PropertyValueFactory<vr, Integer>("mile"));
        wid.setCellValueFactory(new PropertyValueFactory<vr, Integer>("wid"));
        try {
            this.table();
            this.setdata();
            this.setdata2();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    public void table() throws SQLException {
          
        mytable.getItems().clear();
        
        ObservableList<vr> data =FXCollections.observableArrayList();
        
        
        connection = DBConnection.dbConnect();       
        Statement stmt = connection.createStatement();     
        String sql = "SELECT * FROM Vehicles WHERE Registered = 1";    
        ResultSet rs = stmt.executeQuery(sql); 

        while (rs.next()) {
        
        String geta = "SELECT Name FROM Car_Type WHERE ID = ?";    
        PreparedStatement preparedStatementa = connection.prepareStatement(geta);
        preparedStatementa.setString(1, rs.getString("Car_Type"));
        ResultSet getar = preparedStatementa.executeQuery();
    
        String getb = "SELECT NAME FROM Make WHERE ID = ?";    
        PreparedStatement preparedStatementb = connection.prepareStatement(getb);
        preparedStatementb.setString(1, rs.getString("MakeID"));
        ResultSet getbr = preparedStatementb.executeQuery();
        
        String getc = "SELECT NAME FROM Model WHERE ID = ?";    
        PreparedStatement preparedStatementc = connection.prepareStatement(getc);
        preparedStatementc.setString(1, rs.getString("ModelID"));
        ResultSet getcr = preparedStatementc.executeQuery();
   
        String getd = "SELECT Column FROM Engine_Size WHERE ID = ?";    
        PreparedStatement preparedStatementd = connection.prepareStatement(getd);
        preparedStatementd.setString(1, rs.getString("Engine_SizeID"));
        ResultSet getdr = preparedStatementd.executeQuery();
      
        String gete = "SELECT Size FROM Fuel_Type WHERE ID = ?";    
        PreparedStatement preparedStatemente = connection.prepareStatement(gete);
        preparedStatemente.setString(1, rs.getString("Fuel_TypeID"));
        ResultSet geter = preparedStatemente.executeQuery();
       
        String getf = "SELECT NAME FROM Colour WHERE ID = ?";    
        PreparedStatement preparedStatementf = connection.prepareStatement(getf);
        preparedStatementf.setString(1, rs.getString("ColourID"));
        ResultSet getfr = preparedStatementf.executeQuery();
        
       data.add(new vr(rs.getString("Registration"),rs.getInt("CustomerID"),getar.getString(1),getbr.getString(1),getcr.getString(1),getdr.getString(1),geter.getString(1),getfr.getString(1),rs.getString("MOT_Expire"),rs.getInt("Mileage"),rs.getString("Last_Service"),rs.getInt("WarrantyID")));
        mytable.setItems(data);
        
            preparedStatementa.close();
                preparedStatementb.close();
                    preparedStatementc.close();
                        preparedStatementd.close();
                            preparedStatemente.close();
                                preparedStatementf.close();
        }
         stmt.close();
         connection.close();
        
    }
    
    @FXML
    private void handleButtonActionadd (ActionEvent event) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/VehicleRecords/Add.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        GLOBAL.StartPagePointer.disableTab();
        stage.showAndWait();  
        GLOBAL.StartPagePointer.enableTab();
       
    }
    
    @FXML
    private void handleButtonActionbooking (ActionEvent event) throws IOException, Exception {
        
        vr info = (vr) mytable.getSelectionModel().getSelectedItem();
        
       if(info != null) {
       
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/VehicleRecords/Bookings.fxml"));
        Parent root = (Parent)loader.load();   
        BookingsController controller = loader.<BookingsController>getController();
        
        controller.setReg(info.getVid());
        
        Stage editStage = new Stage();
        Scene editScene = new Scene(root); 

        editStage.setScene(editScene);    
        
        GLOBAL.StartPagePointer.disableTab();
        editStage.showAndWait();
        GLOBAL.StartPagePointer.enableTab();
   
        } else {
            JOptionPane.showMessageDialog(null, "Please Select a Vehicle");
        }
    }
    
    @FXML
    private void handleButtonActionparts (ActionEvent event) throws Exception {
        
        connection = DBConnection.dbConnect(); 
        
       vr infoparts = (vr) mytable.getSelectionModel().getSelectedItem();
        
       if(infoparts != null) {
       
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/VehicleRecords/Parts.fxml"));
        Parent root = (Parent)loader.load();   
        PartsController controller = loader.<PartsController>getController();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        
        String query = "SELECT * FROM Vehicles WHERE Registration = ?"; 
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, infoparts.getVid());
        ResultSet set = ps.executeQuery();
        
        controller.setReg(infoparts.getVid(),set.getInt("ID"));
        
        stage.setScene(scene);
        stage.setResizable(false);
        GLOBAL.StartPagePointer.disableTab();
        stage.showAndWait(); 
        GLOBAL.StartPagePointer.enableTab();
        
        } else {
            JOptionPane.showMessageDialog(null, "Please Select a Vehicle");
        }
    }
    
    @FXML
    private void handleaction (ActionEvent event) throws Exception {
       
        mytable.getItems().clear();
        
        ObservableList<vr> data =FXCollections.observableArrayList();    
            
        if(mybox.getValue() == null) {
            return;  
        }
        
        connection = DBConnection.dbConnect();       
        String sql = "SELECT * FROM Vehicles WHERE Registration = ? AND Registered = 1"; 
        PreparedStatement preparedStatement = connection.prepareStatement(sql);       
        preparedStatement.setString(1, ((String) mybox.getValue().toString()));
        ResultSet rs = preparedStatement.executeQuery();

        String geta = "SELECT Name FROM Car_Type WHERE ID = ?";    
        PreparedStatement preparedStatementa = connection.prepareStatement(geta);
        preparedStatementa.setString(1, rs.getString("Car_Type"));
        ResultSet getar = preparedStatementa.executeQuery();
    
        String getb = "SELECT NAME FROM Make WHERE ID = ?";    
        PreparedStatement preparedStatementb = connection.prepareStatement(getb);
        preparedStatementb.setString(1, rs.getString("MakeID"));
        ResultSet getbr = preparedStatementb.executeQuery();
        
        String getc = "SELECT NAME FROM Model WHERE ID = ?";    
        PreparedStatement preparedStatementc = connection.prepareStatement(getc);
        preparedStatementc.setString(1, rs.getString("ModelID"));
        ResultSet getcr = preparedStatementc.executeQuery();
   
        String getd = "SELECT Column FROM Engine_Size WHERE ID = ?";    
        PreparedStatement preparedStatementd = connection.prepareStatement(getd);
        preparedStatementd.setString(1, rs.getString("Engine_SizeID"));
        ResultSet getdr = preparedStatementd.executeQuery();
      
        String gete = "SELECT Size FROM Fuel_Type WHERE ID = ?";    
        PreparedStatement preparedStatemente = connection.prepareStatement(gete);
        preparedStatemente.setString(1, rs.getString("Fuel_TypeID"));
        ResultSet geter = preparedStatemente.executeQuery();
        
        String getf = "SELECT NAME FROM Colour WHERE ID = ?";    
        PreparedStatement preparedStatementf = connection.prepareStatement(getf);
        preparedStatementf.setString(1, rs.getString("ColourID"));
        ResultSet getfr = preparedStatementf.executeQuery();
        
       data.add(new vr(rs.getString("Registration"),rs.getInt("CustomerID"),getar.getString(1),getbr.getString(1),getcr.getString(1),getdr.getString(1),geter.getString(1),getfr.getString(1),rs.getString("MOT_Expire"),rs.getInt("Mileage"),rs.getString("Last_Service"),rs.getInt("WarrantyID")));
        mytable.setItems(data);
        preparedStatement.close();
            preparedStatementa.close();
                preparedStatementb.close();
                    preparedStatementc.close();
                        preparedStatementd.close();
                            preparedStatemente.close();
                                preparedStatementf.close();
        connection.close(); 
        
    }
    
        @FXML
         private void handleactioncust (ActionEvent event) throws Exception {
       
        mytable.getItems().clear();
        
        ObservableList<vr> data =FXCollections.observableArrayList();    
            
        if(mybox2.getValue() == null) {
            return;
        }
        
        try {
        
        connection = DBConnection.dbConnect();       
        String sql = "SELECT * FROM Vehicles WHERE CustomerID = ? AND Registered = 1"; 
        PreparedStatement preparedStatement = connection.prepareStatement(sql);       
        preparedStatement.setString(1, ((String) mybox2.getValue().toString()));
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
        
        String geta = "SELECT Name FROM Car_Type WHERE ID = ?";    
        PreparedStatement preparedStatementa = connection.prepareStatement(geta);
        preparedStatementa.setString(1, rs.getString("Car_Type"));
        ResultSet getar = preparedStatementa.executeQuery();
    
        String getb = "SELECT NAME FROM Make WHERE ID = ?";    
        PreparedStatement preparedStatementb = connection.prepareStatement(getb);
        preparedStatementb.setString(1, rs.getString("MakeID"));
        ResultSet getbr = preparedStatementb.executeQuery();
        
        String getc = "SELECT NAME FROM Model WHERE ID = ?";    
        PreparedStatement preparedStatementc = connection.prepareStatement(getc);
        preparedStatementc.setString(1, rs.getString("ModelID"));
        ResultSet getcr = preparedStatementc.executeQuery();
   
        String getd = "SELECT Column FROM Engine_Size WHERE ID = ?";    
        PreparedStatement preparedStatementd = connection.prepareStatement(getd);
        preparedStatementd.setString(1, rs.getString("Engine_SizeID"));
        ResultSet getdr = preparedStatementd.executeQuery();
      
        String gete = "SELECT Size FROM Fuel_Type WHERE ID = ?";    
        PreparedStatement preparedStatemente = connection.prepareStatement(gete);
        preparedStatemente.setString(1, rs.getString("Fuel_TypeID"));
        ResultSet geter = preparedStatemente.executeQuery();
        
        String getf = "SELECT NAME FROM Colour WHERE ID = ?";    
        PreparedStatement preparedStatementf = connection.prepareStatement(getf);
        preparedStatementf.setString(1, rs.getString("ColourID"));
        ResultSet getfr = preparedStatementf.executeQuery();
        
       data.add(new vr(rs.getString("Registration"),rs.getInt("CustomerID"),getar.getString(1),getbr.getString(1),getcr.getString(1),getdr.getString(1),geter.getString(1),getfr.getString(1),rs.getString("MOT_Expire"),rs.getInt("Mileage"),rs.getString("Last_Service"),rs.getInt("WarrantyID")));
        mytable.setItems(data);
            preparedStatementa.close();
                preparedStatementb.close();
                    preparedStatementc.close();
                        preparedStatementd.close();
                            preparedStatemente.close();
                                preparedStatementf.close();
        }
         preparedStatement.close();
        connection.close(); 
         
        } catch(Exception e) {
              JOptionPane.showMessageDialog(null, "Customer has no vehicles");  
        }
     
    }
    
    @FXML
    private void handleButtonreset (ActionEvent event) throws Exception {
        
        this.table();
        this.setdata();
        this.setdata2();
    }
    
    
    @FXML
    private void handleButtonActiondelete (ActionEvent event) throws Exception {
        
        LocalDate hello = LocalDate.now();
        
        connection = DBConnection.dbConnect();       
        Statement newstmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Vehicles WHERE Registered = 1";      
        ResultSet row = newstmt.executeQuery(sql); 
        Object [] ids = new Object[row.getInt(1)]; 
      
        String sql2 = "SELECT * FROM Vehicles WHERE Registered = 1";   
        ResultSet rows = newstmt.executeQuery(sql2);
        
        int i=0;
      
        while(rows.next()){
           ids[i]  = rows.getString("Registration");
            i++;
            
       }
       
        
        String ans = (String) JOptionPane.showInputDialog(null,"What vehicle would you like to delete?","Delete Vehicle",JOptionPane.QUESTION_MESSAGE,null,ids, 
        ids[0]);
     
        
        if(ans == null) {
             JOptionPane.showMessageDialog(null, "Vehicle was not selected");
            return;
        } 
        
        String sql3 = "UPDATE Vehicles SET Registered = 0 WHERE Registration = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql3);
        preparedStatement.setString(1, ans);
        preparedStatement.executeUpdate();
        
        JOptionPane.showMessageDialog(null, "Vehicle Removed");
        
        Statement stmt = connection.createStatement();     
        String sql5 = "SELECT * FROM Bookings";      
        ResultSet row5 = stmt.executeQuery(sql5);
        stmt.close();
        
        String query = "SELECT * FROM Vehicles WHERE Registration = ?"; 
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, ans);
        ResultSet set = ps.executeQuery();
        
        ps.close();
        
        while(row5.next()) {
            String[] split = row5.getString("Time_Start").split(" ");
            LocalDate myDate =  LocalDate.parse(split[0]);
            
            if(myDate.isAfter(hello)) {
                stmt = connection.createStatement();     
                String sql9 = "DELETE FROM Bookings WHERE VehiclesID = '"+set.getInt(1)+"';";          
                stmt.executeUpdate(sql9);    
                stmt.close();
            }
            row.next();
        }
        
        newstmt.close();
        preparedStatement.close();
        connection.close();
        
        this.table();
        this.setdata();
        this.setdata2();
        
    }
    
    public void setdata() throws SQLException{
       
        try {
        connection = DBConnection.dbConnect();       
        Statement stmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Vehicles WHERE Registered = 1";      
        ResultSet row = stmt.executeQuery(sql); 
        String[] rows = new String[row.getInt(1)];
        
        String sql1 = "SELECT * FROM Vehicles WHERE Registered = 1";      
        ResultSet row1 = stmt.executeQuery(sql1); 
        int i =0;
        mybox.getItems().clear();
        while(row.next()) {
            rows[i] = row1.getString("Registration");
            i++;
                    
        }
        
        Arrays.sort(rows);
        mybox.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Could not find vehicle IDentfiactions");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
    }  
       public void setdata2() throws SQLException{
       
        try {
        connection = DBConnection.dbConnect();       
        Statement stmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Customer WHERE Registered = 1";      
        ResultSet row = stmt.executeQuery(sql); 
        String[] rows = new String[row.getInt(1)];
        
        String sql1 = "SELECT * FROM Customer WHERE Registered = 1;";      
        ResultSet row1 = stmt.executeQuery(sql1); 
        
        int i =0;
        mybox2.getItems().clear();
        while(row.next()) {
             rows[i] = Integer.toString(row1.getInt("ID"));
            i++;
        }
        
        Arrays.sort(rows);
        mybox2.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Could not find vehicle IDentfiactions");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
     
}
   

 
    @FXML
     private void addmake (ActionEvent event) throws Exception { 
         
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/VehicleRecords/AddMake.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        GLOBAL.StartPagePointer.disableTab();
        stage.showAndWait();  
        GLOBAL.StartPagePointer.enableTab();
         
     }
     
     @FXML
     private void addmodel (ActionEvent event) throws Exception { 
         
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/VehicleRecords/AddModel.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        GLOBAL.StartPagePointer.disableTab();
        stage.showAndWait();  
        GLOBAL.StartPagePointer.enableTab();
     }
     
     @FXML
     private void updatewarranty (ActionEvent event) throws Exception { 
         
        vr info = (vr) mytable.getSelectionModel().getSelectedItem();
        
        if(info != null) {
         
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/VehicleRecords/updatewarranty.fxml"));
        Parent root = (Parent)loader.load();   
        UpdateWarrantyController controller = loader.<UpdateWarrantyController>getController();
        
        controller.setReg(info.getVid());
        
        Stage editStage = new Stage();
        Scene editScene = new Scene(root); 

        editStage.setScene(editScene);    
        GLOBAL.StartPagePointer.disableTab();
        editStage.showAndWait();
        GLOBAL.StartPagePointer.enableTab();
        
        } else{
            
             JOptionPane.showMessageDialog(null, "Please Select a Vehicle");
            
        }
     }
     
      @FXML
     private void viewwarranty (ActionEvent event) throws Exception { 
         
        vr info = (vr) mytable.getSelectionModel().getSelectedItem();
        
        if(info != null) {
         
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/VehicleRecords/warranty.fxml"));
        Parent root = (Parent)loader.load();   
        WarrantyController controller = loader.<WarrantyController>getController();
        
        controller.setReg(info.getWid());
        
        Stage editStage = new Stage();
        Scene editScene = new Scene(root); 

        editStage.setScene(editScene);    
        GLOBAL.StartPagePointer.disableTab();
        editStage.showAndWait();
        GLOBAL.StartPagePointer.enableTab();
        
        } else{
            
             JOptionPane.showMessageDialog(null, "Please Select a Vehicle");
            
        }
     } 
    
    
}