package Controllers.VehicleRecords;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DetailsController implements Initializable {

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
    private Button get;
    
    private int ID;
    
    Connection connection = null;
    
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
        lsd.setCellValueFactory(new PropertyValueFactory<vr, String>("lsd"));
        mile.setCellValueFactory(new PropertyValueFactory<vr, Integer>("mile"));
        wid.setCellValueFactory(new PropertyValueFactory<vr, Integer>("wid"));
        
    }    
    
    public void tablecustomer(String ID) throws SQLException { 
        
        mytable.getItems().clear();
        ObservableList<vr> data =FXCollections.observableArrayList();
	connection = DBConnection.dbConnect();
        
        String geta = "SELECT * FROM Vehicles WHERE CustomerID = ?";    
        PreparedStatement preparedStatementa = connection.prepareStatement(geta);
        preparedStatementa.setString(1, ID);
        ResultSet getar = preparedStatementa.executeQuery();
        
        while(getar.next()) {
            
        String get = "SELECT Name FROM Car_Type WHERE ID = ?";    
        PreparedStatement preparedStatement = connection.prepareStatement(get);
        preparedStatement.setString(1, getar.getString("Car_Type"));
        ResultSet getr = preparedStatement.executeQuery();
    
        String getb = "SELECT NAME FROM Make WHERE ID = ?";    
        PreparedStatement preparedStatementb = connection.prepareStatement(getb);
        preparedStatementb.setString(1, getar.getString("MakeID"));
        ResultSet getbr = preparedStatementb.executeQuery();
        
        String getc = "SELECT NAME FROM Model WHERE ID = ?";    
        PreparedStatement preparedStatementc = connection.prepareStatement(getc);
        preparedStatementc.setString(1, getar.getString("ModelID"));
        ResultSet getcr = preparedStatementc.executeQuery();
   
        String getd = "SELECT Column FROM Engine_Size WHERE ID = ?";    
        PreparedStatement preparedStatementd = connection.prepareStatement(getd);
        preparedStatementd.setString(1, getar.getString("Engine_SizeID"));
        ResultSet getdr = preparedStatementd.executeQuery();
      
        String gete = "SELECT Size FROM Fuel_Type WHERE ID = ?";    
        PreparedStatement preparedStatemente = connection.prepareStatement(gete);
        preparedStatemente.setString(1, getar.getString("Fuel_TypeID"));
        ResultSet geter = preparedStatemente.executeQuery();
       
        String getf = "SELECT NAME FROM Colour WHERE ID = ?";    
        PreparedStatement preparedStatementf = connection.prepareStatement(getf);
        preparedStatementf.setString(1, getar.getString("ColourID"));
        ResultSet getfr = preparedStatementf.executeQuery();
        
       data.add(new vr(getar.getString("Registration"),getar.getInt("CustomerID"),getr.getString(1),getbr.getString(1),getcr.getString(1),getdr.getString(1),geter.getString(1),getfr.getString(1),getar.getString("MOT_Expire"),getar.getInt("Mileage"),getar.getString("Last_Service"),getar.getInt("WarrantyID")));
        mytable.setItems(data);
        
            preparedStatement.close();
            preparedStatementa.close();
                preparedStatementb.close();
                    preparedStatementc.close();
                        preparedStatementd.close();
                            preparedStatemente.close();
                                preparedStatementf.close();
                                getar.next();
        }
         connection.close();
            
        }
    
     public void tablecustomerregistration(String reg) throws SQLException { 
        
        mytable.getItems().clear();
        ObservableList<vr> data =FXCollections.observableArrayList();
        
        String geta = "SELECT * FROM Vehicles WHERE Registration = ?";    
        PreparedStatement preparedStatementa = connection.prepareStatement(geta);
        preparedStatementa.setString(1, reg);
        ResultSet getar = preparedStatementa.executeQuery();
        
        while(getar.next()) {
            
        String get = "SELECT Name FROM Car_Type WHERE ID = ?";    
        PreparedStatement preparedStatement = connection.prepareStatement(get);
        preparedStatement.setString(1, getar.getString("Car_Type"));
        ResultSet getr = preparedStatement.executeQuery();
    
        String getb = "SELECT NAME FROM Make WHERE ID = ?";    
        PreparedStatement preparedStatementb = connection.prepareStatement(getb);
        preparedStatementb.setString(1, getar.getString("MakeID"));
        ResultSet getbr = preparedStatementb.executeQuery();
        
        String getc = "SELECT NAME FROM Model WHERE ID = ?";    
        PreparedStatement preparedStatementc = connection.prepareStatement(getc);
        preparedStatementc.setString(1, getar.getString("ModelID"));
        ResultSet getcr = preparedStatementc.executeQuery();
   
        String getd = "SELECT Column FROM Engine_Size WHERE ID = ?";    
        PreparedStatement preparedStatementd = connection.prepareStatement(getd);
        preparedStatementd.setString(1, getar.getString("Engine_SizeID"));
        ResultSet getdr = preparedStatementd.executeQuery();
      
        String gete = "SELECT Size FROM Fuel_Type WHERE ID = ?";    
        PreparedStatement preparedStatemente = connection.prepareStatement(gete);
        preparedStatemente.setString(1, getar.getString("Fuel_TypeID"));
        ResultSet geter = preparedStatemente.executeQuery();
       
        String getf = "SELECT NAME FROM Colour WHERE ID = ?";    
        PreparedStatement preparedStatementf = connection.prepareStatement(getf);
        preparedStatementf.setString(1, getar.getString("ColourID"));
        ResultSet getfr = preparedStatementf.executeQuery();
        
       data.add(new vr(getar.getString("Registration"),getar.getInt("CustomerID"),getr.getString(1),getbr.getString(1),getcr.getString(1),getdr.getString(1),geter.getString(1),getfr.getString(1),getar.getString("MOT_Expire"),getar.getInt("Mileage"),getar.getString("Last_Service"),getar.getInt("WarrantyID")));
        mytable.setItems(data);
        
            preparedStatement.close();
            preparedStatementa.close();
                preparedStatementb.close();
                    preparedStatementc.close();
                        preparedStatementd.close();
                            preparedStatemente.close();
                                preparedStatementf.close();
                                getar.next();
        }
         connection.close();
            
        }
     
      @FXML
    private void handleButtonActionget (ActionEvent event) throws Exception {
        
	connection = DBConnection.dbConnect();

         vr info = (vr) mytable.getSelectionModel().getSelectedItem();
         
        String getf = "SELECT ID FROM Vehicles WHERE CustomerID= ?";    
        PreparedStatement preparedStatementf = connection.prepareStatement(getf);
        preparedStatementf.setString(1, info.getCid()+"");
        ResultSet getfr = preparedStatementf.executeQuery();    
       
        ID = getfr.getInt(1);
        
        preparedStatementf.close();
	connection.close();

 	((Node)(event.getSource())).getScene().getWindow().hide();

    }
    
     public void closeWindow(){
    Stage stage = (Stage) get.getScene().getWindow();
              stage.close();
    }
      
     
    public int getID() {  
        return ID;
    }
    
}
