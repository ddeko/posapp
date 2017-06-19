package mamabe.posappandroid.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import mamabe.posappandroid.Models.Menu;
import mamabe.posappandroid.Models.Table;
import mamabe.posappandroid.R;
import mamabe.posappandroid.Utils.OnSwipeTouchListener;

/**
 * Created by DedeEko on 6/12/2017.
 */

public class ListTableAdapter extends RecyclerView.Adapter<ListTableAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Table> items;
    private ListTableAdapterListener listener;

    private ListTableItemAdapter foodAdapter, beverageAdapter;
    private ArrayList<Menu> listFoodItem, listBeverageItem;




    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    public interface ListTableAdapterListener {
        void onItemClick(int position);
    }

    public ListTableAdapter(ArrayList<Table> items, ListTableAdapterListener listener, Context context) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_table_item, parent, false);
        RecyclerView listFood = (RecyclerView)v.findViewById(R.id.list_table_list_food);
        RecyclerView listBeverage = (RecyclerView)v.findViewById(R.id.list_table_list_drink);
        final SwipeLayout swipeList = (SwipeLayout)v.findViewById(R.id.list_table_list_swipe);
        TextView tableNumber = (TextView)v.findViewById(R.id.tv_table_number);
        View item = v.findViewById(R.id.table_item);

        listBeverageItem = new ArrayList<>();
        listFoodItem =  new ArrayList<>();

        foodAdapter = new ListTableItemAdapter(listFoodItem);
        beverageAdapter = new ListTableItemAdapter(listBeverageItem);

        listFood.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listFood.setLayoutManager(llm);

        listBeverage.setHasFixedSize(false);
        LinearLayoutManager llm2 = new LinearLayoutManager(context);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        listBeverage.setLayoutManager(llm2);

        listFood.setAdapter(foodAdapter);
        listBeverage.setAdapter(beverageAdapter);


        return new ViewHolder(v, listFood, listBeverage, swipeList, tableNumber, item, foodAdapter, beverageAdapter);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Table table = items.get(position);

        if(table.getTableNumber().equalsIgnoreCase("0"))
        {
            holder.tableNumber.setText("Take Away");
        }else{
            holder.tableNumber.setText("Table "+table.getTableNumber());
        }


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onItemClick(holder.getAdapterPosition());
            }
        });

        Menu menu =  new Menu();
        Menu menu2 =  new Menu();

        menu.setMenuType("Food");
        menu.setMenuName("French Fries");

        menu2.setMenuName("Espresso (Hot)");
        menu2.setMenuType("Beverage");

        for(int i = 1; i<=5; i++){

            listFoodItem.add(menu);
            listBeverageItem.add(menu2);
        }

        holder.listFood.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE)
                        {
                            // Left to Right swipe action
                            if (x2 > x1)
                            {


                            }

                            // Right to left swipe action
                            else
                            {
                                holder.swipeList.open();


                            }

                        }
                        else
                        {
                            view.getParent().requestDisallowInterceptTouchEvent(true);

                            // consider as something else - a screen tap for example
                        }
                        break;
                }

                return false;
            }

        });

        holder.listBeverage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE)
                        {
                            // Left to Right swipe action
                            if (x2 > x1)
                            {
                                holder.swipeList.close();



                            }

                            // Right to left swipe action
                            else
                            {

                            }

                        }
                        else
                        {
                            view.getParent().requestDisallowInterceptTouchEvent(true);

                            // consider as something else - a screen tap for example
                        }
                        break;
                }

                return false;
            }

        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public RecyclerView listFood;
        public RecyclerView listBeverage;
        public SwipeLayout swipeList;
        public TextView tableNumber;
        public View item;
        public ListTableItemAdapter foodAdapter;
        public ListTableItemAdapter beverageAdapter;

        public ViewHolder(View itemView, RecyclerView listFood, RecyclerView listBeverage, SwipeLayout swipeList,
                          TextView tableNumber, View item, ListTableItemAdapter foodAdapter,
                          ListTableItemAdapter beverageAdapter) {
            super(itemView);
            this.listFood = listFood;
            this.listBeverage = listBeverage;
            this.swipeList = swipeList;
            this.tableNumber = tableNumber;
            this.item = item;
            this.foodAdapter = foodAdapter;
            this.beverageAdapter = beverageAdapter;
        }
    }
}
