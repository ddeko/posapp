package mamabe.posappandroid.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.IntegerRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import mamabe.posappandroid.Models.Order;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 7/8/2017.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{

    public static final String MENUSTATUS_NOTCONFIRM_CODE = "101";
    public static final String MENUSTATUS_CONFIRMATION_CODE = "1";
    public static final String MENUSTATUS_COOKING_CODE = "2";
    public static final String MENUSTATUS_COOKED_CODE = "3";
    public static final String MENUSTATUS_DELIVERING_CODE = "4";
    public static final String MENUSTATUS_DELIVERED_CODE = "5";

    private Context context;
    private ArrayList<OrderDetail> items;
    private OrderDetailAdapterListener listener;

    public interface OrderDetailAdapterListener {
        void onItemClick(OrderDetail item, int position);
    }

    public OrderDetailAdapter(ArrayList<OrderDetail> items, OrderDetailAdapterListener listener, Context context) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMenuName;
        public TextView tvMenuPrice;
        public TextView tvMenuQty;
        public TextView tvMenuDiscPrice;
        public TextView tvMenuDisc;
        public TextView tvMenuTotalPrice;
        public TextView tvMenuNote;
        public TextView tvTakeaway;
        public TextView tvAdditional;
        public ConstraintLayout menuStatusColor;
        public LinearLayout discHolder;
        public View item;

        public ViewHolder(View itemView, TextView tvMenuName, TextView tvMenuPrice, TextView tvMenuQty,
                          TextView tvMenuDiscPrice, TextView tvMenuDisc, TextView tvMenuTotalPrice,
                          TextView tvMenuNote, TextView tvTakeaway, TextView tvAdditional, ConstraintLayout menuStatusColor, LinearLayout discHolder, View item) {
            super(itemView);
            this.tvMenuName = tvMenuName;
            this.tvMenuPrice = tvMenuPrice;
            this.tvMenuQty = tvMenuQty;
            this.tvMenuDiscPrice = tvMenuDiscPrice;
            this.tvMenuDisc = tvMenuDisc;
            this.tvMenuTotalPrice = tvMenuTotalPrice;
            this.tvMenuNote = tvMenuNote;
            this.tvTakeaway = tvTakeaway;
            this.tvAdditional = tvAdditional;
            this.menuStatusColor = menuStatusColor;
            this.discHolder = discHolder;
            this.item = item;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_detail_item, parent, false);
        TextView tvMenuName = (TextView)v.findViewById(R.id.order_detail_item_name);
        TextView tvMenuPrice = (TextView)v.findViewById(R.id.order_detail_item_price);
        TextView tvMenuQty = (TextView)v.findViewById(R.id.order_detail_item_count);
        TextView tvMenuDiscPrice = (TextView)v.findViewById(R.id.order_detail_item_disc_price);
        TextView tvMenuDisc = (TextView)v.findViewById(R.id.order_detail_item_disc);
        TextView tvMenuTotalPrice = (TextView)v.findViewById(R.id.order_detail_item_total_price);
        TextView tvMenuNote = (TextView)v.findViewById(R.id.order_detail_item_note);
        TextView tvTakeaway = (TextView)v.findViewById(R.id.order_detail_item_takeaway);
        TextView tvAdditional = (TextView)v.findViewById(R.id.order_detail_item_additional);
        ConstraintLayout menuStatusColor = (ConstraintLayout)v.findViewById(R.id.order_detail_item_status_color);
        LinearLayout discHolder = (LinearLayout)v.findViewById(R.id.order_detail_disc_holder);
        View item = v.findViewById(R.id.list_order_detail_item);


        tvMenuName.setSelected(true);
        tvMenuName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvMenuName.setSingleLine(true);

        tvMenuNote.setSelected(true);
        tvMenuNote.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvMenuNote.setSingleLine(true);

        return new ViewHolder(v, tvMenuName, tvMenuPrice, tvMenuQty, tvMenuDiscPrice, tvMenuDisc, tvMenuTotalPrice,
                tvMenuNote, tvTakeaway, tvAdditional,  menuStatusColor, discHolder, item);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final OrderDetail orderitems = items.get(position);

        holder.tvMenuName.setText(orderitems.getMenu().getMenuName());
        holder.tvMenuNote.setText(orderitems.getNote());
        holder.tvMenuQty.setText("X " + orderitems.getQty());

        String prezzo = "";
        String discPrice = "";
        float totalPrice =0;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("Rp #,###.00", symbols);
        prezzo = decimalFormat.format(Integer.parseInt(orderitems.getMenu().getPrice()));
        holder.tvMenuPrice.setText(prezzo);

        if(orderitems.getMenu().getDiscount().equalsIgnoreCase("0"))
        {
            holder.discHolder.setVisibility(View.GONE);
            holder.tvMenuPrice.setPaintFlags(0);
            totalPrice = Integer.parseInt(orderitems.getQty())*Integer.parseInt(orderitems.getMenu().getPrice());
        }
        else
        {
            holder.discHolder.setVisibility(View.VISIBLE);
            holder.tvMenuDisc.setText("disc. "+orderitems.getMenu().getDiscount()+"%");
            float discount = (100 - Float.parseFloat(orderitems.getMenu().getDiscount()));
            float priceDisc = (Integer.parseInt(orderitems.getMenu().getPrice())*discount)/100;
            discPrice = decimalFormat.format(priceDisc);
            holder.tvMenuDiscPrice.setText(discPrice);
            holder.tvMenuPrice.setPaintFlags(holder.tvMenuPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            totalPrice = (Integer.parseInt(orderitems.getQty())*priceDisc);
        }

        String subTotal = "";
        DecimalFormat decimalFormats = new DecimalFormat("#,###.00", symbols);
        subTotal = decimalFormat.format(totalPrice);
        holder.tvMenuTotalPrice.setText(subTotal);

        if(orderitems.getTakeaway().equalsIgnoreCase("0")){
            holder.tvTakeaway.setVisibility(View.GONE);
        }
        else{
            holder.tvTakeaway.setVisibility(View.VISIBLE);
        }
        if(orderitems.getAdditional().equalsIgnoreCase("0")){
            holder.tvAdditional.setVisibility(View.GONE);
        }
        else{
            holder.tvAdditional.setVisibility(View.VISIBLE);
        }

        if(orderitems.getMenuStatus().equals(MENUSTATUS_CONFIRMATION_CODE)) {
            holder.menuStatusColor.setBackgroundColor(context.getResources().getColor(R.color.red4));
        }else if(orderitems.getMenuStatus().equals(MENUSTATUS_COOKING_CODE)){
            holder.menuStatusColor.setBackgroundColor(context.getResources().getColor(R.color.fbutton_color_orange));
        }else if(orderitems.getMenuStatus().equals(MENUSTATUS_COOKED_CODE)){
            holder.menuStatusColor.setBackgroundColor(context.getResources().getColor(R.color.blue6));
        }else if(orderitems.getMenuStatus().equals(MENUSTATUS_DELIVERING_CODE)){
            holder.menuStatusColor.setBackgroundColor(context.getResources().getColor(R.color.yellow1));
        }else if(orderitems.getMenuStatus().equals(MENUSTATUS_DELIVERED_CODE)){
            holder.menuStatusColor.setBackgroundColor(context.getResources().getColor(R.color.green1));
        }else if(orderitems.getMenuStatus().equals(MENUSTATUS_NOTCONFIRM_CODE)){
            holder.menuStatusColor.setBackgroundColor(context.getResources().getColor(R.color.fbutton_color_wet_asphalt));
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onItemClick(orderitems, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
