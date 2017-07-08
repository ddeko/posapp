package mamabe.posappandroid.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DedeEko on 7/7/2017.
 */

public class Order implements Serializable
{

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("table_no")
    @Expose
    private String tableNo;
    @SerializedName("number_of_customer")
    @Expose
    private String numberOfCustomer;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("takeaway")
    @Expose
    private String takeaway;
    @SerializedName("order_detail")
    @Expose
    private ArrayList<OrderDetail> orderDetail = null;
    private final static long serialVersionUID = -2420094961927592225L;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getNumberOfCustomer() {
        return numberOfCustomer;
    }

    public void setNumberOfCustomer(String numberOfCustomer) {
        this.numberOfCustomer = numberOfCustomer;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTakeaway() {
        return takeaway;
    }

    public void setTakeaway(String takeaway) {
        this.takeaway = takeaway;
    }

    public ArrayList<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(ArrayList<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }
}

