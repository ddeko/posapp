package mamabe.posappandroid.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DedeEko on 5/30/2017.
 */

public class Menu implements Serializable
{

    @SerializedName("menu_id")
    @Expose
    private String menuId;
    @SerializedName("menuCategory_id")
    @Expose
    private String menuCategoryId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("menu_name")
    @Expose
    private String menuName;
    @SerializedName("availability")
    @Expose
    private String availability;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("flag_delete")
    @Expose
    private String flagDelete;
    @SerializedName("menuCategory_name")
    @Expose
    private String menuCategoryName;
    @SerializedName("menu_type")
    @Expose
    private String menuType;


    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFlagDelete() {
        return flagDelete;
    }

    public void setFlagDelete(String flagDelete) {
        this.flagDelete = flagDelete;
    }

    public String getMenuCategoryName() {
        return menuCategoryName;
    }

    public void setMenuCategoryName(String menuCategoryName) {
        this.menuCategoryName = menuCategoryName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
}
