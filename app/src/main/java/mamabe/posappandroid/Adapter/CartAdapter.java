package mamabe.posappandroid.Adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import mamabe.posappandroid.Activities.AddOrderActivity;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.R;


/**
 * Created by DedeEko on 5/29/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    public interface CartAdapterListener {
        void onItemClick(OrderDetail item, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView disc;
        public TextView price;
        public TextView discPrice;
        public TextView tvDisc;
        public TextView qty;
        public TextView note;
        public TextView tvNote;
        public LinearLayout item;


        public ViewHolder(View itemView, TextView name, TextView disc, TextView price, TextView discPrice,
                          TextView tvDisc, TextView qty, TextView note, TextView tvNote, LinearLayout item) {
            super(itemView);
            this.name = name;
            this.disc = disc;
            this.price = price;
            this.discPrice = discPrice;
            this.tvDisc = tvDisc;
            this.qty = qty;
            this.note = note;
            this.tvNote = tvNote;
            this.item = item;
        }
    }

    ArrayList<OrderDetail> orderList;

    private CartAdapterListener listener;
    private AddOrderActivity activity;

    public CartAdapter(ArrayList<OrderDetail> orderList, CartAdapterListener listener, AddOrderActivity activity) {
        this.orderList = orderList;
        this.listener = listener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart_item, parent, false);
        TextView name = (TextView)v.findViewById(R.id.list_cart_name);
        TextView disc = (TextView)v.findViewById(R.id.list_cart_disc);
        TextView price = (TextView)v.findViewById(R.id.list_cart_price);
        TextView discPrice = (TextView)v.findViewById(R.id.list_cart_disc_price);
        TextView tvDisc = (TextView)v.findViewById(R.id.list_cart_text_disc);
        TextView qty = (TextView)v.findViewById(R.id.list_cart_qty);
        TextView note = (TextView)v.findViewById(R.id.list_cart_note);
        TextView tvNote = (TextView)v.findViewById(R.id.list_cart_tv_note);
        LinearLayout item = (LinearLayout)v.findViewById(R.id.list_cart_item);


        name.setSelected(true);
        name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        name.setSingleLine(true);

        note.setSelected(true);
        note.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        note.setSingleLine(true);

        return new ViewHolder(v, name, disc, price, discPrice, tvDisc, qty, note, tvNote, item);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final OrderDetail item = orderList.get(position);

        holder.name.setText(item.getMenu().getMenuName());
        holder.qty.setText(item.getQty()+ "X");
        if(item.getNote().equalsIgnoreCase(""))
        {
            holder.note.setText("-");
        }else {
            holder.note.setText(item.getNote());
        }


        String prezzo = "";
        String discPrice = "";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("Rp #,###.00", symbols);
        prezzo = decimalFormat.format(Integer.parseInt(item.getMenu().getPrice()));
        holder.price.setText(prezzo);

        if(item.getMenu().getDiscount().equalsIgnoreCase("0"))
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
            holder.disc.setText(item.getMenu().getDiscount()+"%");
            float discount = (100 - Float.parseFloat(item.getMenu().getDiscount()));
            float priceDisc = (Integer.parseInt(item.getMenu().getPrice())*discount)/100;
            discPrice = decimalFormat.format(priceDisc);
            holder.discPrice.setText(discPrice);
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onItemClick(item, holder.getAdapterPosition());
            }
        });

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

}
