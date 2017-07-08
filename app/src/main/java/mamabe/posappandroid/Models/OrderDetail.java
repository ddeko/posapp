package mamabe.posappandroid.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DedeEko on 7/3/2017.
 */

public class OrderDetail implements Serializable{

    @SerializedName("orderdetail_id")
    @Expose
    private String orderDetailId;
    @SerializedName("menu_status")
    @Expose
    private String menuStatus;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("additional")
    @Expose
    private String additional;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("menu")
    @Expose
    private Menu menu;
    @SerializedName("takeaway")
    @Expose
    private String takeaway;

    public String getTakeaway() {
        return takeaway;
    }

    public void setTakeaway(String takeaway) {
        this.takeaway = takeaway;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(String menuStatus) {
        this.menuStatus = menuStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
