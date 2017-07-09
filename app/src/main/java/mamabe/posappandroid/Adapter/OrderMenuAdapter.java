package mamabe.posappandroid.Adapter;

import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class OrderMenuAdapter extends RecyclerView.Adapter<OrderMenuAdapter.ViewHolder>{

    public interface MenuAdapterListener {
        void onItemClick(String menuId, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView disc;
        public TextView price;
        public TextView discPrice;
        public TextView tvDisc;
        public ConstraintLayout item;


        public ViewHolder(View itemView, TextView disc, TextView name, TextView price, TextView discPrice, TextView tvDisc, ConstraintLayout item) {
            super(itemView);
            this.disc = disc;
            this.name = name;
            this.price = price;
            this.discPrice = discPrice;
            this.tvDisc = tvDisc;
            this.item = item;
        }
    }

    ArrayList<Menu> menuList;

    private MenuAdapterListener listener;
    private AddOrderActivity activity;

    public OrderMenuAdapter(ArrayList<Menu> menuList, MenuAdapterListener listener, AddOrderActivity activity) {
        this.menuList = menuList;
        this.listener = listener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_item, parent, false);
        TextView name = (TextView)v.findViewById(R.id.list_menu_name);
        TextView disc = (TextView)v.findViewById(R.id.list_menu_disc);
        TextView price = (TextView)v.findViewById(R.id.list_menu_price);
        TextView discPrice = (TextView)v.findViewById(R.id.list_menu_disc_price);
        TextView tvDisc = (TextView)v.findViewById(R.id.list_menu_text_disc);
        ConstraintLayout item = (ConstraintLayout)v.findViewById(R.id.list_menu_item);

        name.setSelected(true);
        name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        name.setSingleLine(true);

        return new ViewHolder(v, disc, name, price, discPrice, tvDisc, item);
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

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onItemClick(menu.getMenuId(), holder.getAdapterPosition());
            }
        });

    }


    @Override
    public int getItemCount() {
        return menuList.size();
    }

}
