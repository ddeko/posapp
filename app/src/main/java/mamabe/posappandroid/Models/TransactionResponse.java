package mamabe.posappandroid.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DedeEko on 7/7/2017.
 */

public class TransactionResponse implements Serializable
{
    @SerializedName("transaction")
    @Expose
    private ArrayList<Transaction> transaction = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = -110636907896083284L;

    public ArrayList<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(ArrayList<Transaction> transaction) {
        this.transaction = transaction;
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

