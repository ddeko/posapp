package mamabe.posappandroid.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DedeEko on 7/7/2017.
 */

public class Transaction implements Serializable
{
    @SerializedName("trans_date")
    @Expose
    private String transDate;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("cash")
    @Expose
    private String cash;
    @SerializedName("change")
    @Expose
    private String change;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("trans_id")
    @Expose
    private String transId;
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

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

