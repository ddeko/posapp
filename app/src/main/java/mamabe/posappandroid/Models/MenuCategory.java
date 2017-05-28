package mamabe.posappandroid.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DedeEko on 5/28/2017.
 */

public class MenuCategory implements Serializable {

    @SerializedName("menuCategory_id")
    @Expose
    private String menuCategoryId;
    @SerializedName("menuCategory_name")
    @Expose
    private String menuCategoryName;
    @SerializedName("menu_type")
    @Expose
    private String menuType;

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
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
