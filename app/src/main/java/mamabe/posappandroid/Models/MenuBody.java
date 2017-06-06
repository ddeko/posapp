package mamabe.posappandroid.Models;

import java.io.Serializable;

/**
 * Created by DedeEko on 6/2/2017.
 */

public class MenuBody implements Serializable{

    private String menu_id;
    private String menuCategory_name;
    private String price;
    private String menu_name;
    private String availability;
    private String discount;
    private String flag_delete;

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getMenuCategory_name() {
        return menuCategory_name;
    }

    public void setMenuCategory_name(String menuCategory_name) {
        this.menuCategory_name = menuCategory_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getAvailbility() {
        return availability;
    }

    public void setAvailbility(String availability) {
        this.availability = availability;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFlag_delete() {
        return flag_delete;
    }

    public void setFlag_delete(String flag_delete) {
        this.flag_delete = flag_delete;
    }
}

