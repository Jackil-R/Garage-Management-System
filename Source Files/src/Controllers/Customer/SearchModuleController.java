package Controllers.Customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SearchModuleController {
    int selectedID = -1;
    boolean businessSearch;
    boolean windowOpen = false;
    @FXML Button customerSelectBtn;
    @FXML Button businessSelectBtn;
    @FXML Button postcodeSelectBtn;
    @FXML Button resetBtn;
    @FXML TextField searchCustomerTxtField;
    @FXML TextField searchBusinessTxtField;
    @FXML TextField searchPostcodeTxtField;

    /*
    Search methods
        These methods are called when a search term has been entered 
        and the Search button has been pressed.
        Checks are made to make sure the user cannot access more than one
        search feature at a time.
        Search results are displayed in a pop-up table. When a row is double clicked,
        the ID for that row is stored and the window is closed.
    */
    @FXML
    private void searchForBusiness(ActionEvent event) {   
        if(windowOpen)
            return;
        
        windowOpen = true;
        searchCustomerTxtField.clear();
        searchPostcodeTxtField.clear();
        if(!searchBusinessTxtField.getText().equals("")) {
            String searchTerm = searchBusinessTxtField.getText();
            SearchBusinessTable businessTbl = new SearchBusinessTable(searchTerm);
            businessTbl.init();
            VBox root = new VBox();
            root.getChildren().addAll(businessTbl.getTable());
            Scene scene = new Scene(root, 400, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Search results");
            stage.getIcons().add(new Image("/images/gears_icon.png"));
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            selectedID = businessTbl.getSelectedID();
            businessSearch = true;
        }
        windowOpen = false;
    }
    
    @FXML
    private void searchForCustomer(ActionEvent event) throws Exception  {
        if(windowOpen)
            return;
        
        windowOpen = true;
        searchBusinessTxtField.clear();
        searchPostcodeTxtField.clear();
        if(!searchCustomerTxtField.getText().equals(""))
        {
            String searchTerm = searchCustomerTxtField.getText();
            SearchSurnameTable surnameTbl = new SearchSurnameTable(searchTerm);
            surnameTbl.init();
            VBox root = new VBox();
            root.getChildren().addAll(surnameTbl.getTable());
            Scene scene = new Scene(root, 400, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Search results");
            stage.getIcons().add(new Image("/images/gears_icon.png"));
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            selectedID = surnameTbl.getSelectedID();
            businessSearch = false;
        }
        windowOpen = false;
    }
    
    @FXML    
    private void searchForPostcode(ActionEvent event) { 
        if(windowOpen)
            return;
        
        windowOpen = true;
        searchCustomerTxtField.clear();
        searchBusinessTxtField.clear();
        if(!searchPostcodeTxtField.getText().equals("")) {
            String searchTerm = searchPostcodeTxtField.getText();
            SearchPostcodeTable postcodeTbl = new SearchPostcodeTable(searchTerm);
            postcodeTbl.init();
            VBox root = new VBox();
            root.getChildren().addAll(postcodeTbl.getTable());
            Scene scene = new Scene(root, 700, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Search results");
            stage.getIcons().add(new Image("/images/gears_icon.png"));
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            selectedID = postcodeTbl.getSelectedID();
            businessSearch = postcodeTbl.getBusinessSelected();
        }
        windowOpen = false;
    }
    
    @FXML
    private void resetSearch(ActionEvent event) throws Exception {
        searchBusinessTxtField.clear();
        searchCustomerTxtField.clear();
        searchPostcodeTxtField.clear();
        selectedID = -1;
    }
    
    public void reset() throws Exception {
        resetSearch(new ActionEvent());
    }
    
    public int getSelectedID() {
        return selectedID;
    }
    
    public boolean isBusinessSearch() {
        return businessSearch;
    }
    
    public CustomerAccount getCustomerAccountFromSearch() {
        int businessType;
        if(businessSearch)
            businessType = 1;
        else
            businessType = 2;
        
        CustomerAccount account = new CustomerAccount(selectedID, businessType);
        return account; 
    }
}
