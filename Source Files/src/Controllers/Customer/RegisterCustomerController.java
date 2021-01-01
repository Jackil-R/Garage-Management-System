package Controllers.Customer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class RegisterCustomerController implements Initializable { 
    ObservableList<String> contents = FXCollections.observableArrayList("Business", "Individual"); 
    ArrayList<String> fieldsUnchecked = new ArrayList<String>();
    ArrayList<String> fields = new ArrayList<String>();
    Connection connection = null;
    EditDatabase access = null;
    AccountDatabase balanceAccess = null;
    @FXML ComboBox customerType;
    @FXML TextField forenameTxt;
    @FXML TextField surnameTxt;
    @FXML TextField addressLineOneTxt;
    @FXML TextField addressLineTwoTxt;
    @FXML TextField countyTxt;
    @FXML TextField postcodeTxt;
    @FXML TextField phoneNumberTxt;
    @FXML TextField businessNameTxt;
    @FXML Button submitButton;
    @FXML Label businessErrorLbl;
    @FXML Label forenameErrorLbl;
    @FXML Label surnameErrorLbl;
    @FXML Label addressLine1ErrorLbl;
    @FXML Label countyErrorLbl;
    @FXML Label postcodeErrorLbl;
    @FXML Label phoneNumberErrorLbl;
    @FXML Label typeErrorLbl;
    @FXML Label successLbl;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerType.setItems(contents);
        customerType.setPromptText("select type");
    }  
    
    @FXML
    private void registerNewCustomer(ActionEvent event) { 
        resetWarningText();
        
        fields.clear();
        fieldsUnchecked.clear();
        fieldsUnchecked.add(businessNameTxt.getText());      //0
        fieldsUnchecked.add(forenameTxt.getText());          //1
        fieldsUnchecked.add(surnameTxt.getText());           //2
        fieldsUnchecked.add(countyTxt.getText());            //3
        fieldsUnchecked.add(phoneNumberTxt.getText());       //4
        fieldsUnchecked.add(addressLineOneTxt.getText());    //5
        fieldsUnchecked.add(addressLineTwoTxt.getText());    //6
        fieldsUnchecked.add(postcodeTxt.getText());          //7
        
        if(customerType.getValue() == null) { //If a value is selected from combo box 
            typeErrorLbl.setText("Error, set type of customer.");
            return;
        }

        if(customerType.getValue().equals("Business")) {
            if(checkText(fieldsUnchecked.get(0)) && fieldsUnchecked.get(0).length()<=255) {
                fields.add(fieldsUnchecked.get(0).replaceAll("\\s",""));
            }
            else {
                businessErrorLbl.setText("Error, "+fieldsUnchecked.get(0)+" is not valid.");
                System.out.println(fieldsUnchecked.get(0)+ " is not valid.");
                return;
            }
        }
        else {
            //forename
            if(checkText(fieldsUnchecked.get(1)) && fieldsUnchecked.get(1).length()<=255) {
                fields.add(fieldsUnchecked.get(1).replaceAll("\\s",""));
            }
            else {
                forenameErrorLbl.setText("Error, "+fieldsUnchecked.get(1)+" is not valid.");
                System.out.println(fieldsUnchecked.get(1)+ " is not valid.");
                return;
            }

            //surname
            if(checkText(fieldsUnchecked.get(2)) && fieldsUnchecked.get(2).length()<=255) {
                fields.add(fieldsUnchecked.get(2).replaceAll("\\s",""));
            }
            else {
                surnameErrorLbl.setText("Error, "+fieldsUnchecked.get(2)+" is not valid.");
                System.out.println(fieldsUnchecked.get(2)+ " is not valid.");
                return;
            }
        }
        
        //address line 1
        if(checkAlphanumeric(fieldsUnchecked.get(5)) && fieldsUnchecked.get(5).length()<=255) {
            fields.add(fieldsUnchecked.get(5));
        }
        else {
            addressLine1ErrorLbl.setText("Error, "+fieldsUnchecked.get(5)+" is not valid.");
            System.out.println(fieldsUnchecked.get(5)+ " is not valid.");
            return;
        }
        //address line 2
        if(checkAlphanumeric(fieldsUnchecked.get(6)) && fieldsUnchecked.get(6).length()<=255) {
            fields.add(fieldsUnchecked.get(6));
        }
        else {
            fields.add(""); //field is empty but accepted as it can be.
        }
        
        //county pre-check
        if(checkText(fieldsUnchecked.get(3)) && fieldsUnchecked.get(3).length()<=255) {
        }
        else {
            countyErrorLbl.setText("Error, "+fieldsUnchecked.get(3)+" is not valid.");
            System.out.println(fieldsUnchecked.get(3)+ " is not valid.");
            return;
        }
        
        //postcode
        if(checkPostcode(fieldsUnchecked.get(7)) && fieldsUnchecked.get(7).length()<=10) {
           fields.add(fieldsUnchecked.get(7));
        }
        else {
            postcodeErrorLbl.setText("Error, "+fieldsUnchecked.get(7)+" is not valid.");
            System.out.println(fieldsUnchecked.get(7)+ " is not valid.");
            return;
        } 
        
        //county post-check
        if(checkText(fieldsUnchecked.get(3)) && fieldsUnchecked.get(3).length()<=255) {
            fields.add(fieldsUnchecked.get(3).replaceAll("\\s",""));
        }
        else {
            countyErrorLbl.setText("Error, "+fieldsUnchecked.get(3)+" is not valid.");
            System.out.println(fieldsUnchecked.get(3)+ " is not valid.");
            return;
        }
        
        //phone number
        if(checkNumbers(fieldsUnchecked.get(4)) && fieldsUnchecked.get(4).length()<=11 && fieldsUnchecked.get(4).length()>=6) {
            fields.add(fieldsUnchecked.get(4).replaceAll("\\s",""));
        }
        else {
            phoneNumberErrorLbl.setText("Error, "+fieldsUnchecked.get(4)+" is not valid.");
            System.out.println(fieldsUnchecked.get(4)+ " is not valid.");
            return;
        }

        boolean isBusiness = true;
        
        if(fields.size()==7)
            isBusiness = false;
              
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new EditDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        int newCustomerID = access.registerNewCustomer(fields, isBusiness);
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("searchBusiness catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        balanceAccess = new AccountDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        boolean successful = balanceAccess.createBalanceAccountForCustomer(newCustomerID);
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("searchBusiness catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        if(successful) {
            resetFields(new ActionEvent());
            successLbl.setText("Customer registered successfully.");
        }
        else {
            successLbl.setText("Error registering customer. Please try again.");
        }           
    }
    
    @FXML
    private void resetFields(ActionEvent event) {
        fieldsUnchecked.clear();
        fields.clear();
        forenameTxt.clear();
        surnameTxt.clear();
        addressLineOneTxt.clear();
        addressLineTwoTxt.clear();
        countyTxt.clear();
        postcodeTxt.clear();
        phoneNumberTxt.clear();
        businessNameTxt.clear();
        customerType.getSelectionModel().clearSelection();
        customerType.getItems().clear();
        customerType.getItems().addAll("Business", "Individual");
        customerType.setPromptText("select type"); 
        
        resetWarningText();
    }
    
    @FXML
    private void checkForEnterPressed(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ENTER)
            registerNewCustomer(new ActionEvent());
    }
    
    @FXML
    private void customerChoiceBox(ActionEvent event) {    
        String currentCustomerType = (String) customerType.getValue();
        if(customerType.getValue() == null)
            return;
        
        if(currentCustomerType.equals("Business")) {
            businessNameTxt.setDisable(false);
            forenameTxt.setDisable(true);
            surnameTxt.setDisable(true);
            forenameTxt.clear();
            surnameTxt.clear();
        }
        else { //Individual 
            businessNameTxt.setDisable(true);
            forenameTxt.setDisable(false);
            surnameTxt.setDisable(false);
            businessNameTxt.clear();
        }
    }
    
    private void resetWarningText() {
        businessErrorLbl.setText("");
        forenameErrorLbl.setText("");
        surnameErrorLbl.setText("");
        addressLine1ErrorLbl.setText("");
        countyErrorLbl.setText("");
        postcodeErrorLbl.setText("");
        phoneNumberErrorLbl.setText("");
        typeErrorLbl.setText("");
        successLbl.setText("");
    }

    private boolean checkText(String uncheckedText) {
        return uncheckedText.matches("[a-zA-Z ]+");
    }
    
    private boolean checkNumbers(String uncheckedNumber) {
        return uncheckedNumber.matches("[0-9 ]+");
    }
    
    private boolean checkAlphanumeric(String uncheckedNumber) {
        return uncheckedNumber.matches("[a-zA-z0-9 ]+");
    }   
    
    private boolean checkPostcode(String uncheckedPostcode) {
        return uncheckedPostcode.matches("(?i)^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$");
    } 
}