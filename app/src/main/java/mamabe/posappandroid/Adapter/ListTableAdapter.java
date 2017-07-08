package mamabe.posappandroid.Adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mamabe.posappandroid.Activities.OrderActivity;
import mamabe.posappandroid.Models.Menu;
import mamabe.posappandroid.Models.Order;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.Models.OrderDetailPostResponse;
import mamabe.posappandroid.Models.OrderResponse;
import mamabe.posappandroid.Models.OrderTableResponse;
import mamabe.posappandroid.Models.Table;
import mamabe.posappandroid.R;
import mamabe.posappandroid.Utils.OnSwipeTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 6/12/2017.
 */

public class ListTableAdapter extends RecyclerView.Adapter<ListTableAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Order> items;
    private ListTableAdapterListener listener;

    public interface ListTableAdapterListener {
        void onItemClick(int position);
    }

    public ListTableAdapter(ArrayList<Order> items, ListTableAdapterListener listener, Context context) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_table_item, parent, false);
        TextView tableNumber = (TextView)v.findViewById(R.id.tv_table_number);
        TextView tvStatus = (TextView)v.findViewById(R.id.table_item_tv_status);
        TextView tvTotalGst = (TextView)v.findViewById(R.id.table_item_tv_total);
        TextView tvName = (TextView)v.findViewById(R.id.table_item_tv_name);
        TextView tvDate = (TextView)v.findViewById(R.id.table_item_tv_date);
        ConstraintLayout occupied = (ConstraintLayout)v.findViewById(R.id.table_item_fill_holder);
        ConstraintLayout empty = (ConstraintLayout)v.findViewById(R.id.table_item_empty_holder);
        View item = v.findViewById(R.id.table_item);

        tvName.setSelected(true);
        tvName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvName.setSingleLine(true);

        tvDate.setSelected(true);
        tvDate.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvDate.setSingleLine(true);

        return new ViewHolder(v, tableNumber, tvStatus, tvTotalGst, tvName, tvDate, occupied, empty, item);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Order table = items.get(position);

        if(table.getOrderId()!=null)
        {
            holder.empty.setVisibility(View.GONE);
            holder.occupied.setVisibility(View.VISIBLE);

            holder.tvDate.setText(table.getOrderDate());
            holder.tvName.setText("Name - " + table.getCustomerName());
            holder.tvStatus.setText("Status - " + table.getOrderStatus());
            holder.tvTotalGst.setText("Guest - " + table.getNumberOfCustomer());

        }
        else{
            holder.empty.setVisibility(View.VISIBLE);
            holder.occupied.setVisibility(View.GONE);
        }

        if(table.getTableNo().equalsIgnoreCase("0"))
        {
            holder.tableNumber.setText("Take Away");
        }else{
            holder.tableNumber.setText("Table "+table.getTableNo());
        }


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onItemClick(holder.getAdapterPosition());
            }
        });


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tableNumber;
        public TextView tvStatus;
        public TextView tvTotalGst;
        public TextView tvName;
        public TextView tvDate;
        public ConstraintLayout occupied;
        public ConstraintLayout empty;
        public View item;

        public ViewHolder(View itemView, TextView tableNumber, TextView tvStatus, TextView tvTotalGst,
                          TextView tvName, TextView tvDate, ConstraintLayout occupied, ConstraintLayout empty, View item) {
            super(itemView);
            this.tableNumber = tableNumber;
            this.tvStatus = tvStatus;
            this.tvTotalGst = tvTotalGst;
            this.tvName = tvName;
            this.tvDate = tvDate;
            this.occupied = occupied;
            this.empty = empty;
            this.item = item;
        }
    }
}
