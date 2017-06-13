package mamabe.posappandroid.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import mamabe.posappandroid.Models.Table;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 6/12/2017.
 */

public class ListTableAdapter extends RecyclerView.Adapter<ListTableAdapter.ViewHolder>{

    private ArrayList<Table> items;
    private ListTableAdapterListener listener;

    public interface ListTableAdapterListener {
        void onItemClick(int position);
    }

    public ListTableAdapter(ArrayList<Table> items, ListTableAdapterListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_table_item, parent, false);
        ListView listFood = (ListView)v.findViewById(R.id.list_table_list_food);
        ListView listBeverage = (ListView)v.findViewById(R.id.list_table_list_drink);
        SwipeLayout swipeList = (SwipeLayout)v.findViewById(R.id.list_table_list_swipe);
        TextView tableNumber = (TextView)v.findViewById(R.id.tv_table_number);
        View item = v.findViewById(R.id.table_item);

        return new ViewHolder(v, listFood, listBeverage, swipeList, tableNumber, item);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Table table = items.get(position);

        holder.tableNumber.setText("Table "+table.getTableNumber());


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

        public ListView listFood;
        public ListView listBeverage;
        public SwipeLayout swipeList;
        public TextView tableNumber;
        public View item;

        public ViewHolder(View itemView, ListView listFood, ListView listBeverage, SwipeLayout swipeList,
                          TextView tableNumber, View item) {
            super(itemView);
            this.listFood = listFood;
            this.listBeverage = listBeverage;
            this.swipeList = swipeList;
            this.tableNumber = tableNumber;
            this.item = item;
        }
    }
}
