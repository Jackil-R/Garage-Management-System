package Controllers.Parts;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ConcurrentModificationException;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import javax.swing.JOptionPane;

/**
 *
 * @author Alexander_2
 */
public class TakeOutController implements Initializable{
// fxml variables
  @FXML private PartsController parts;
  @FXML
   private Button closer;
  @FXML
  private Button back;
    @FXML
    private TableView<BasketPart> basketView;
    @FXML
    private TableColumn<BasketPart, Integer> basketId;
    @FXML
    private TableColumn<BasketPart, String> basketName;
    @FXML
    private TableColumn<BasketPart,String> basketDesc;
    @FXML
    private TableColumn<BasketPart, Integer> basketStock;
    @FXML
    private TableColumn<BasketPart, Integer> basketPrice;
    @FXML
    private TableColumn<BasketPart, Boolean> basketOrder;
    @FXML
    private TableColumn<BasketPart, String> basketWithdraw;
    Boolean toObject=true;
    public ObservableList<BasketPart> basketTable=FXCollections.observableArrayList();
    @FXML
    private Label warrantyStart;
    @FXML
    private Label warrantyEnd;
    @FXML
    private Label total;
    @FXML
    private Button calculate;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    //get data from stock
    public void getData(ObservableList<Part> myObject, PartsController p)
    {   //connector to the parent controller
        parts=p;
        //fill up the model
        for(int i=0; i<myObject.size();i++)
        {
            BasketPart inter=new BasketPart(myObject.get(i).getId(),myObject.get(i).getName(),
                                            myObject.get(i).getDesc(),
                                            myObject.get(i).getQuantaty(),
                                            myObject.get(i).getPrice(), toObject, "1");
            basketTable.add(inter);
        }
         //set up the table
        basketId.setCellValueFactory(new PropertyValueFactory<>("id"));
        basketName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        basketDesc.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        basketStock.setCellValueFactory(new PropertyValueFactory<>("Quantaty"));
        basketPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        basketOrder.setCellValueFactory(new PropertyValueFactory<BasketPart, Boolean>("Basket"));
        basketOrder.setCellFactory(new Callback<TableColumn<BasketPart, Boolean>, TableCell<BasketPart, Boolean>>() {

                public TableCell<BasketPart, Boolean> call(TableColumn<BasketPart, Boolean> p) {
                CheckBoxTableCell<BasketPart, Boolean> cell=new CheckBoxTableCell<BasketPart, Boolean>();
                BooleanProperty selected= new SimpleBooleanProperty();
                selected.setValue(true);
                cell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
                                @Override
                                public ObservableValue<Boolean> call(Integer index) {
                                    return selected ;
                                }
                            });
                            selected.addListener(new ChangeListener<Boolean>() {
                                @Override
                                public void changed(ObservableValue<? extends Boolean> obs, Boolean wasSelected, Boolean isSelected) {
                                    System.out.println(isSelected);
                                    int chosen=cell.getTableRow().getIndex();
                                    basketTable.get(chosen).setOrdered(isSelected);
                                }
                            });
                                            return cell;
                                        }
                                    });
        basketWithdraw.setCellValueFactory(new PropertyValueFactory<>("withdraw"));
        basketWithdraw.setCellFactory(new Callback<TableColumn<BasketPart,String>, TableCell<BasketPart,String>>() {

            @Override
            public TableCell<BasketPart, String> call(
                    TableColumn<BasketPart, String> param)
            {
                TextFieldTableCell<BasketPart, String>myEditableTableCell = new TextFieldTableCell<BasketPart, String>(new DefaultStringConverter())
                {
                    @Override
                    public void commitEdit(String val) 
                    {
                        int index = this.getTableRow().getIndex();
                        BasketPart newUser = this.getTableView().getItems().get(index);
                        StringProperty oldval = newUser.getWithdrawPropery();
                        int comp=Integer.parseInt(val);
                        if(comp<Integer.parseInt(basketTable.get(index).getWithdraw()))
                        {
                            JOptionPane.showMessageDialog(null,"You cannot withdraw more parts!");
                        }else{
                        basketTable.get(index).setWithdraw(val);
                        this.setItem(val);}
                    }
               };
               
                
                myEditableTableCell.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation(10));
                return myEditableTableCell; 
            }
        });
        basketView.setItems(basketTable);
    }
    //numeric validation
    public EventHandler<KeyEvent> numeric_Validation(final Integer max_Lengh) {
    return new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
           if(!e.getCharacter().matches("[0-9.]")){ 
                 e.consume();
            }
        }
    };
}  //check items out
    public void closeBasket(ActionEvent w) throws NoSuchFieldException, IOException, ClassNotFoundException, SQLException, ConcurrentModificationException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Parts/Parts.fxml"));
        Parent root = (Parent)loader.load();
        PartsController updater= loader.<Controllers.Parts.PartsController>getController();
        parts.updateTables(basketTable);
        Stage stage = (Stage) closer.getScene().getWindow();
        stage.close();
        }    
    //close the window without saving
    public void close(ActionEvent e)
    {
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }
}
