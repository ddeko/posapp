package mamabe.posappandroid.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 7/11/2017.
 */

public class KitchenStatusAdapter extends RecyclerView.Adapter<KitchenStatusAdapter.ViewHolder>{

    public static final String MENUSTATUS_NOTCONFIRM_CODE = "101";
    public static final String MENUSTATUS_CONFIRMATION_CODE = "1";
    public static final String MENUSTATUS_COOKING_CODE = "2";
    public static final String MENUSTATUS_COOKED_CODE = "3";
    public static final String MENUSTATUS_DELIVERING_CODE = "4";
    public static final String MENUSTATUS_DELIVERED_CODE = "5";

    private Context context;
    private ArrayList<OrderDetail> items;
    private KitchenStatusAdapterListener listener;

    public interface KitchenStatusAdapterListener {
        void onItemClick(OrderDetail item, int position);
    }

    public KitchenStatusAdapter(ArrayList<OrderDetail> items, KitchenStatusAdapterListener listener, Context context) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMenuName;
        public TextView tvTable;
        public TextView tvMenuQty;
        public TextView tvMenuNote;
        public TextView tvTakeaway;
        public ConstraintLayout menuStatusColor;
        public View item;

        public ViewHolder(View itemView, TextView tvMenuName, TextView tvTable, TextView tvMenuQty,
                          TextView tvMenuNote, TextView tvTakeaway, ConstraintLayout menuStatusColor, View item) {
            super(itemView);
            this.tvMenuName = tvMenuName;
            this.tvTable = tvTable;
            this.tvMenuQty = tvMenuQty;
            this.tvMenuNote = tvMenuNote;
            this.tvTakeaway = tvTakeaway;
            this.menuStatusColor = menuStatusColor;
            this.item = item;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kitchen_status_item, parent, false);
        TextView tvMenuName = (TextView)v.findViewById(R.id.kitchen_status_item_name);
        TextView tvTable = (TextView)v.findViewById(R.id.kitchen_status_item_table);
        TextView tvMenuQty = (TextView)v.findViewById(R.id.kitchen_status_item_count);
        TextView tvMenuNote = (TextView)v.findViewById(R.id.kitchen_status_item_note);
        TextView tvTakeaway = (TextView)v.findViewById(R.id.kitchen_status_item_takeaway);
        ConstraintLayout menuStatusColor = (ConstraintLayout)v.findViewById(R.id.kitchen_status_item_status_color);
        View item = v.findViewById(R.id.list_kitchen_status_item);

        tvMenuName.setSelected(true);
        tvMenuName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvMenuName.setSingleLine(true);

        tvMenuNote.setSelected(true);
        tvMenuNote.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvMenuNote.setSingleLine(true);

        return new ViewHolder(v, tvMenuName, tvTable, tvMenuQty, tvMenuNote, tvTakeaway, menuStatusColor, item);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final OrderDetail orderitems = items.get(position);

        holder.tvMenuName.setText(orderitems.getMenu().getMenuName());
        holder.tvMenuNote.setText(orderitems.getNote());
        holder.tvMenuQty.setText(orderitems.getQty()+ " X ");
        holder.tvTable.setText("Table : " +orderitems.getTable_no());

        if(orderitems.getTakeaway().equalsIgnoreCase("0")){
            holder.tvTakeaway.setVisibility(View.GONE);
        }
        else{
            holder.tvTakeaway.setVisibility(View.VISIBLE);
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

//        holder.item.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View view, DragEvent dragEvent) {
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

