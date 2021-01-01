package Controllers.Customer;

import java.util.ArrayList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SurnameTable {
        private int ID = -1;
	private String firstname;
        private String lastname;
        private String addressline1;
        private String postcode;
        
	public SurnameTable(int ID, String firstname, String lastname, String addressline1, String postcode) {
            super();
            this.ID = ID;
            this.firstname = firstname;
            this.lastname = lastname;
            this.addressline1 = addressline1;
            this.postcode = postcode;
	}
        
        public int getID() {
            return ID;
        }

	public String getFirstname() {
            return firstname;
	}

	public void setFirstname(String firstname) {
            this.firstname = firstname;
	}

	public String getLastname() {
            return lastname;
	}

	public void setLastname(String lastname) {
            this.lastname = lastname;
	}
        
        public String getAddressline1() {
            return addressline1;
        }

        public void setAddressline1(String addressline1) {
            this.addressline1 = addressline1;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

	public static ArrayList<TableColumn<SurnameTable, ?>> getColumn(TableView table) {
            String[] columnNames = { "First Name", "Last Name", "Address Line 1", "Postcode" };
            String[] variableNames = { "firstname", "lastname", "addressline1", "postcode" };
            Integer[] column_width = { 20, 20, 30, 20 };

            int i = 0;
            TableColumn<SurnameTable, String> firstnameCol = new TableColumn<>(columnNames[i++]);
            TableColumn<SurnameTable, String> lastenameCol = new TableColumn<>(columnNames[i++]);
            TableColumn<SurnameTable, String> addressline1Col = new TableColumn<>(columnNames[i++]);
            TableColumn<SurnameTable, String> postcodeCol = new TableColumn<>(columnNames[i++]);

            i = 0;
            firstnameCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
            lastenameCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
            addressline1Col.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
            postcodeCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));

            i = 0;
            firstnameCol.setCellValueFactory(new PropertyValueFactory<SurnameTable, String>(variableNames[i++]));
            lastenameCol.setCellValueFactory(new PropertyValueFactory<SurnameTable, String>(variableNames[i++]));
            addressline1Col.setCellValueFactory(new PropertyValueFactory<SurnameTable, String>(variableNames[i++]));
            postcodeCol.setCellValueFactory(new PropertyValueFactory<SurnameTable, String>(variableNames[i++]));

            ArrayList<TableColumn<SurnameTable, ?>> columns = new ArrayList<TableColumn<SurnameTable, ?>>();

            columns.add(firstnameCol);
            columns.add(lastenameCol);
            columns.add(addressline1Col);
            columns.add(postcodeCol);

            return columns;
	}
}