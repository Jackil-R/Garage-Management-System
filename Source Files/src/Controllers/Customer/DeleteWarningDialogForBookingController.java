package Controllers.Customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class DeleteWarningDialogForBookingController {
    
    @FXML
    private void okPressed(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
