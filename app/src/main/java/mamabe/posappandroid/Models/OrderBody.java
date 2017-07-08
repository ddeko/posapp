package mamabe.posappandroid.Models;

import java.io.Serializable;

/**
 * Created by DedeEko on 7/5/2017.
 */

public class OrderBody implements Serializable {
    private String order_id;
    private String table_no;
    private String number_of_customer;
    private String order_status;
    private String customer_name;
    private String order_date;
    private String takeaway;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public String getNumber_of_customer() {
        return number_of_customer;
    }

    public void setNumber_of_customer(String number_of_customer) {
        this.number_of_customer = number_of_customer;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getTakeaway() {
        return takeaway;
    }

    public void setTakeaway(String takeaway) {
        this.takeaway = takeaway;
    }
}
