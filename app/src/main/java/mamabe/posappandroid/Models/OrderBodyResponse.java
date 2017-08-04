package mamabe.posappandroid.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DedeEko on 7/7/2017.
 */

public class OrderBodyResponse implements Serializable
{
    @SerializedName("order")
    @Expose
    private ArrayList<OrderBody> orders = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = -110636907896083284L;

    public ArrayList<OrderBody> getOrder() {
        return orders;
    }

    public void setOrder(ArrayList<OrderBody> orders) {
        this.orders = orders;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

