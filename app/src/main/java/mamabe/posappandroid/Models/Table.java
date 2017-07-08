package mamabe.posappandroid.Models;

import java.io.Serializable;

/**
 * Created by DedeEko on 6/13/2017.
 */

public class Table implements Serializable {

    private String tableNumber;
    private String numberOfCustomer;
    private boolean isTakeaway;

    public boolean isTakeaway() {
        return isTakeaway;
    }

    public void setTakeaway(boolean takeaway) {
        isTakeaway = takeaway;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getNumberOfCustomer() {
        return numberOfCustomer;
    }

    public void setNumberOfCustomer(String numberOfCustomer) {
        this.numberOfCustomer = numberOfCustomer;
    }
}
