package Controllers.Parts;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Alexander_2
 */

//THIS CLASS IS CREATED TO ALLOW THE EXISTANCE OF A PART THAT WILL BE USED FOR WITHDRAWAL OR CHECKING OUT
public class BasketPart extends Part {
private final StringProperty withdraw;

    public BasketPart(int Diez,String Name, String Description, int LeftInstock, double Price, Boolean Order, String Withdraw) {
        super(Diez,Name, Description, LeftInstock, Price, Order,null);
        this.withdraw = new SimpleStringProperty(Withdraw);
    }
    public void setWithdraw(String input) {
        this.withdraw.set(input);
    }
    public String getWithdraw() {
        return withdraw.get();
    }
    public StringProperty getWithdrawPropery() {
        return withdraw;
    }
    
}
