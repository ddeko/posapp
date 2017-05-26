package mamabe.posappandroid.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DedeEko on 5/24/2017.
 */

public class Setting implements Serializable {

    @SerializedName("tables")
    @Expose
    private String tables;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("service")
    @Expose
    private String service;

    public String getDiscount() {
        return tables;
    }

    public void setDiscount(String discount) {
        this.tables = discount;
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
}
