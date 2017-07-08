package mamabe.posappandroid.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mamabe.posappandroid.Models.Menu;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.R;

/**
 *
 * Created by DedeEko on 6/15/2017.
 */

public class ListTableItemAdapter extends RecyclerView.Adapter<ListTableItemAdapter.ViewHolder> {

    ArrayList<OrderDetail> listItem;

    public ListTableItemAdapter(ArrayList<OrderDetail> listItem) {
        this.listItem = listItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_table_list_item, parent, false);

        TextView tvName = (TextView)v.findViewById(R.id.list_table_list_item_name);
        TextView tvCount = (TextView)v.findViewById(R.id.list_table_list_item_count);


        return new ViewHolder(v, tvCount, tvName);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetail order = listItem.get(position);
        Log.d("Orse", order.getMenu().getMenuName() );
        holder.tvName.setText(order.getMenu().getMenuName());
        holder.tvCount.setText("X "+ order.getQty());
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCount;
        public TextView tvName;

        public ViewHolder(View itemView, TextView tvCount, TextView tvName) {
            super(itemView);
            this.tvCount = tvCount;
            this.tvName = tvName;
        }
    }
}
