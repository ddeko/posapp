package mamabe.posappandroid.Adapter;

import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;
import com.rm.rmswitch.RMSwitch;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import mamabe.posappandroid.Activities.AddOrderActivity;
import mamabe.posappandroid.Activities.MenuSettingActivity;
import mamabe.posappandroid.Models.Menu;
import mamabe.posappandroid.Models.MenuBody;
import mamabe.posappandroid.Models.MenuPostResponse;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by DedeEko on 5/29/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    public interface MenuAdapterListener {
        void onItemClick(String menuId, int position);
        void onItemDelete(String menuId, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SwipeLayout swipeLayout;
        public TextView name;
        public TextView disc;
        public TextView price;
        public TextView discPrice;
        public TextView tvDisc;
        public RMSwitch available;
        public ConstraintLayout item;
        public ConstraintLayout deleteBtn;

        public ViewHolder(View itemView, SwipeLayout swipeLayout, TextView disc, TextView name, TextView price,
                          TextView discPrice, TextView tvDisc, RMSwitch available,
                          ConstraintLayout item, ConstraintLayout deleteBtn) {
            super(itemView);
            this.swipeLayout = swipeLayout;
            this.disc = disc;
            this.name = name;
            this.price = price;
            this.discPrice = discPrice;
            this.tvDisc = tvDisc;
            this.available = available;
            this.item = item;
            this.deleteBtn = deleteBtn;
        }
    }

    ArrayList<Menu> menuList;

    private MenuAdapterListener listener;
    private MenuSettingActivity activity;

    public MenuAdapter(ArrayList<Menu> menuList, MenuAdapterListener listener, MenuSettingActivity menuSettingActivity) {
        this.menuList = menuList;
        this.listener = listener;
        this.activity = menuSettingActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_item_swipe, parent, false);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(R.id.list_menu_item_swipe);
        TextView name = (TextView)v.findViewById(R.id.list_menu_name);
        TextView disc = (TextView)v.findViewById(R.id.list_menu_disc);
        TextView price = (TextView)v.findViewById(R.id.list_menu_price);
        TextView discPrice = (TextView)v.findViewById(R.id.list_menu_disc_price);
        TextView tvDisc = (TextView)v.findViewById(R.id.list_menu_text_disc);
        ConstraintLayout item = (ConstraintLayout)v.findViewById(R.id.list_menu_item);
        ConstraintLayout deleteBtn = (ConstraintLayout)v.findViewById(R.id.list_menu_item_delete);
        RMSwitch available = (RMSwitch)v.findViewById(R.id.list_menu_available);

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.setClickToClose(true);

        name.setSelected(true);
        name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        name.setSingleLine(true);

//        available.setEnabled(false);
        available.setClickable(false);

        return new ViewHolder(v, swipeLayout, disc, name, price, discPrice, tvDisc, available, item, deleteBtn);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Menu menu = menuList.get(position);

        holder.name.setText(menu.getMenuName());

        String prezzo = "";
        String discPrice = "";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("Rp #,###.00", symbols);
        prezzo = decimalFormat.format(Integer.parseInt(menu.getPrice()));

        holder.price.setText(prezzo);

        if(menu.getDiscount().equalsIgnoreCase("0"))
        {
            holder.disc.setVisibility(View.GONE);
            holder.tvDisc.setVisibility(View.GONE);
            holder.discPrice.setVisibility(View.GONE);
            holder.price.setPaintFlags(0);
        }
        else
        {
            holder.disc.setVisibility(View.VISIBLE);
            holder.tvDisc.setVisibility(View.VISIBLE);
            holder.discPrice.setVisibility(View.VISIBLE);
            holder.disc.setText(menu.getDiscount()+"%");
            float discount = (100 - Float.parseFloat(menu.getDiscount()));
            float priceDisc = (Integer.parseInt(menu.getPrice())*discount)/100;
            discPrice = decimalFormat.format(priceDisc);
            holder.discPrice.setText(discPrice);
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if(menu.getAvailability().equalsIgnoreCase("1")){
            holder.available.setChecked(true);

        }
        else{
            holder.available.setChecked(false);
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onItemClick(menu.getMenuId(), holder.getAdapterPosition());
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MenuBody menuBody = new MenuBody();

                menuBody.setMenu_id(menu.getMenuId());
                menuBody.setFlag_delete("0");

                deleteData(menuBody,holder.getAdapterPosition());

                if (listener != null)
                    listener.onItemDelete(menu.getMenuId(), holder.getAdapterPosition());

                holder.swipeLayout.close();
            }
        });
    }


    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public void deleteData(final MenuBody menuBody, final int position){
        activity.showLoading(true);
        Call<MenuPostResponse> call = null;

        call = activity.api.deleteMenu(menuBody);
        call.enqueue(new Callback<MenuPostResponse>() {
            @Override
            public void onResponse(Call<MenuPostResponse> call, Response<MenuPostResponse> response) {

                Toast.makeText(activity.getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();

                if (201 == response.code()) {

                    final MenuPostResponse postResponse = response.body();

                    Log.d("UserFragment", "response = " + new Gson().toJson(postResponse));

                    activity.showLoading(false);

                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot delete data", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<MenuPostResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed delete data", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });
    }
}
