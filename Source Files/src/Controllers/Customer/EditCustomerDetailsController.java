package Controllers.Customer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditCustomerDetailsController {
    int customerID;
    boolean successfulEdit;
    ArrayList<String> oldValues;
    ArrayList<String> newValues = new ArrayList<String>();;
    Connection connection = null;
    EditDatabase access = null;
    @FXML TextField businessNameTxt;
    @FXML TextField forenameTxt;
    @FXML TextField surnameTxt;
    @FXML TextField addressLine1Txt;
    @FXML TextField addressLine2Txt;
    @FXML TextField countyTxt;
    @FXML TextField postcodeTxt;
    @FXML TextField phoneNumberTxt;
    @FXML Button submitChangesBtn;
    @FXML Label businessErrorLbl;
    @FXML Label forenameErrorLbl;
    @FXML Label surnameErrorLbl;
    @FXML Label addressLine1ErrorLbl;
    @FXML Label countyErrorLbl;
    @FXML Label postcodeErrorLbl;
    @FXML Label phoneNumberErrorLbl;
    
    public void initialize(URL url, ResourceBundle rb) {
    } 

    public void initialData(int customer_ID) {        
        customerID = customer_ID;
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new EditDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        oldValues = access.loadTextFieldsFromID(customerID);
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("initialData catch: "+ex);
        } 
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        setLabelsAndButtons(oldValues);
    }
    
    @FXML
    private void cancelEdit(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    private void changesSubmitted (ActionEvent event) {
        boolean validEdit = checkFieldsForErrors(oldValues.get(0).isEmpty());
        
        if(!validEdit)
            return;
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new EditDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        successfulEdit = access.commitCustomerChanges(newValues, customerID);
              
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("changesSubmitted catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    private boolean checkFieldsForErrors(boolean individual) {
        resetWarningText();
        newValues.clear();
        ArrayList<String> fieldsUnchecked = new ArrayList<String>();
        fieldsUnchecked.add(businessNameTxt.getText());      //0
        fieldsUnchecked.add(forenameTxt.getText());          //1
        fieldsUnchecked.add(surnameTxt.getText());           //2
        fieldsUnchecked.add(addressLine1Txt.getText());      //3
        fieldsUnchecked.add(addressLine2Txt.getText());      //4
        fieldsUnchecked.add(countyTxt.getText());            //5
        fieldsUnchecked.add(postcodeTxt.getText());          //6
        fieldsUnchecked.add(phoneNumberTxt.getText());       //7
        
        if(individual) {
            //forename
            if(checkText(fieldsUnchecked.get(1)) && fieldsUnchecked.get(1).length()<=255) {
                 newValues.add("");
                newValues.add(fieldsUnchecked.get(1).replaceAll("\\s",""));
            }
            else {
                forenameErrorLbl.setText("Error, "+fieldsUnchecked.get(1)+" is not valid.");
                System.out.println(fieldsUnchecked.get(1)+ " is not valid.");
                return false;
            }

            //surname
            if(checkText(fieldsUnchecked.get(2)) && fieldsUnchecked.get(2).length()<=255) {
                newValues.add(fieldsUnchecked.get(2).replaceAll("\\s",""));
            }
            else {
                surnameErrorLbl.setText("Error, "+fieldsUnchecked.get(2)+" is not valid.");
                System.out.println(fieldsUnchecked.get(2)+ " is not valid.");
                return false;
            }
        }
        else {
            //business
            if(checkText(fieldsUnchecked.get(0)) && fieldsUnchecked.get(0).length()<=255) {
                newValues.add(fieldsUnchecked.get(0).replaceAll("\\s",""));
                newValues.add("");
                newValues.add("");
            }
            else {
                businessErrorLbl.setText("Error, "+fieldsUnchecked.get(0)+" is not valid.");
                System.out.println(fieldsUnchecked.get(0)+ " is not valid.");
                return false;
            }
        }
        
        //address line 1
        if(checkAlphanumeric(fieldsUnchecked.get(3)) && fieldsUnchecked.get(3).length()<=255) {
            newValues.add(fieldsUnchecked.get(3));
        }
        else {
            addressLine1ErrorLbl.setText("Error, "+fieldsUnchecked.get(3)+" is not valid.");
            System.out.println(fieldsUnchecked.get(3)+ " is not valid.");
            return false;
        }
        //address line 2
        if(checkAlphanumeric(fieldsUnchecked.get(4)) && fieldsUnchecked.get(4).length()<=255) {
            newValues.add(fieldsUnchecked.get(4));
        }
        else {
            newValues.add(""); //field is empty but accepted as it can be.
        }
        
        //county
        if(checkText(fieldsUnchecked.get(5)) && fieldsUnchecked.get(5).length()<=255) {
            newValues.add(fieldsUnchecked.get(5).replaceAll("\\s",""));
        }
        else {
            countyErrorLbl.setText("Error, "+fieldsUnchecked.get(5)+" is not valid.");
            System.out.println(fieldsUnchecked.get(5)+ " is not valid.");
            return false;
        }
        
        //postcodeErrorLbl
        if(checkPostcode(fieldsUnchecked.get(6)) && fieldsUnchecked.get(6).length()<=10) {
            newValues.add(fieldsUnchecked.get(6));
        }
        else {
            postcodeErrorLbl.setText("Error, "+fieldsUnchecked.get(6)+" is not valid.");
            System.out.println(fieldsUnchecked.get(6)+ " is not valid.");
            return false;
        } 
        
        //phone number
        if(checkNumbers(fieldsUnchecked.get(7)) && fieldsUnchecked.get(7).length()<=11 && fieldsUnchecked.get(7).length()>=6) {
            newValues.add(fieldsUnchecked.get(7).replaceAll("\\s",""));
        }
        else {
            phoneNumberErrorLbl.setText("Error, "+fieldsUnchecked.get(7)+" is not valid.");
            System.out.println(fieldsUnchecked.get(7)+ " is not valid.");
            return false;
        }
        
        return true;
    }

    private void resetWarningText() {
        businessErrorLbl.setText("");
        forenameErrorLbl.setText("");
        surnameErrorLbl.setText("");
        addressLine1ErrorLbl.setText("");
        countyErrorLbl.setText("");
        postcodeErrorLbl.setText("");
        phoneNumberErrorLbl.setText("");
    }
    
    private void setLabelsAndButtons(ArrayList<String> oldValues) {
        businessNameTxt.setText(oldValues.get(0));
        forenameTxt.setText(oldValues.get(1));
        surnameTxt.setText(oldValues.get(2));
        addressLine1Txt.setText(oldValues.get(3)); 
        addressLine2Txt.setText(oldValues.get(4));
        countyTxt.setText(oldValues.get(5));      
        postcodeTxt.setText(oldValues.get(6));
        phoneNumberTxt.setText(oldValues.get(7));
        
        if (((oldValues.get(1) == null) || (oldValues.get(1).equals(""))) && (oldValues.get(0) != null)) {
            //business
            businessNameTxt.setDisable(false);
            forenameTxt.setDisable(true);
            surnameTxt.setDisable(true);
            forenameTxt.clear();
            surnameTxt.clear();
        }       
        else {           
            businessNameTxt.setDisable(true);
            forenameTxt.setDisable(false);
            surnameTxt.setDisable(false);
            businessNameTxt.clear();
        }
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
    
    public boolean isSuccessfulEdit() {
        return successfulEdit;
    }
}
