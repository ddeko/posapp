package mamabe.posappandroid.Models;

/**
 * Created by DedeEko on 5/28/2017.
 */

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuType implements Serializable
{

    @SerializedName("menu_type")
    @Expose
    private String menuType;

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

}