/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.VehicleRecords;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
/**
 * FXML Controller class
 *
 * @author ibraaheem
 */
public class BookingsController implements Initializable {
     
    @FXML
    private ComboBox myCombobox;
    @FXML
    private TableView mytable;
    @FXML
    private TableColumn bid;
    @FXML
    private TableColumn type;
    //@FXML
   // private TableColumn date;
    @FXML
    private TableColumn ts;
    @FXML
    private TableColumn te;
    @FXML
    private TableColumn bn;
    @FXML
    private TableColumn mid;
    @FXML
    private TableColumn bill;
    
    private String reg;

    /**
     * Initializes the controller class.
     */
   
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
          
        bid.setCellValueFactory(new PropertyValueFactory<booking, String>("bid"));
        type.setCellValueFactory(new PropertyValueFactory<booking, String>("type"));
        ts.setCellValueFactory(new PropertyValueFactory<booking, String>("ts"));
        te.setCellValueFactory(new PropertyValueFactory<booking, String>("te"));
        bn.setCellValueFactory(new PropertyValueFactory<booking, String>("bn"));
        mid.setCellValueFactory(new PropertyValueFactory<booking, Integer>("mid"));
        bill.setCellValueFactory(new PropertyValueFactory<booking, Integer>("bill"));
    }
    
     public void handleaction(String reg) throws SQLException {
        mytable.getItems().clear();
        ObservableList<booking> data =
        FXCollections.observableArrayList();   
        //data.add(new booking(1,"a",LocalDate.of(1999, 2, 21),LocalTime.of(10,00),LocalTime.of(11,00),1,"a"));
   
        Connection connection = DBConnection.dbConnect();       
            
        String query = "SELECT * FROM Vehicles WHERE Registration = ?"; 
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, reg);
        ResultSet set = ps.executeQuery();
        
        String sql = "SELECT * FROM BOOKINGS WHERE VehiclesID = ?";      
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, Integer.toString(set.getInt("ID")));
        ResultSet rs = preparedStatement.executeQuery();
 
        while(rs.next()) {
            
            String geta = "SELECT NAME FROM Booking_Type WHERE ID = ?";    
            PreparedStatement preparedStatementa =
            connection.prepareStatement(geta);
            preparedStatementa.setString(1, rs.getString("Booking_TypeID"));
            ResultSet getar = preparedStatementa.executeQuery();
        
            String getb = "SELECT FIRST_NAME FROM Mechanics WHERE ID = ?";    
            PreparedStatement preparedStatementb =
            connection.prepareStatement(getb);
            preparedStatementb.setString(1, rs.getString("MechanicsID"));
            ResultSet getbr = preparedStatementb.executeQuery();
            
            String getc = "SELECT Amount_Due FROM Bills WHERE BookingsID = ?";    
            PreparedStatement preparedStatementc =
            connection.prepareStatement(getc);
            preparedStatementc.setString(1, Integer.toString(rs.getInt("ID")));
            ResultSet getcr = preparedStatementc.executeQuery();
            int a = 0;
            try {
             a= getcr.getInt(1);
            } catch (Exception e) {}
            
            data.add(new booking(rs.getInt("ID"),getar.getString(1),rs.getString("Time_Start"),rs.getString("Time_End"),rs.getInt("Bay_Number"),getbr.getString(1),a));
            rs.next();
            
            preparedStatementa.close();
            preparedStatementb.close();
            preparedStatementc.close();
        }
            preparedStatement.close();
            connection.close();
            mytable.setItems(data);
    }
    
     @FXML
    public void setReg(String sent) throws SQLException{
        reg = sent;
        this.handleaction(sent);
    } 
     
}

