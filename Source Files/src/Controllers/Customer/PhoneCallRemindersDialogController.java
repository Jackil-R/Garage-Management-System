package Controllers.Customer;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PhoneCallRemindersDialogController {
   
    @FXML Label bodyMessageLbl;
    
    public void initialData(String alertText) {
        bodyMessageLbl.setText(alertText);
    }
    
    @FXML
    private void okPressed(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    private void checkForEnterPressed(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ENTER)
            ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    
}
