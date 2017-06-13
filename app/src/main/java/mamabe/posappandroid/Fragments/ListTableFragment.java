package mamabe.posappandroid.Fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mamabe.posappandroid.Activities.MenuSettingActivity;
import mamabe.posappandroid.Activities.OrderActivity;
import mamabe.posappandroid.Adapter.ListTableAdapter;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Models.Table;
import mamabe.posappandroid.Preferences.SessionManager;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 5/2/2017.
 */

public class ListTableFragment extends BaseFragment implements View.OnClickListener, ListTableAdapter.ListTableAdapterListener{

    private OrderActivity activity;

    private GridLayoutManager layoutManager;
    private RecyclerView listTable;
    private TextView tvCustomer, tvTotalTable;

    private ArrayList<Table> tableItems;
    private ListTableAdapter adapter;

    SessionManager sessions;

    private HashMap<String,String> settingData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (OrderActivity) getActivity();

        tableItems = new ArrayList<>();
        adapter = new ListTableAdapter(tableItems, this);

        sessions = new SessionManager(getBaseActivity());

    }

    private void setupActionBar() {
        OrderActivity mainActivity = (OrderActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.order);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void initView(View view) {
        listTable = (RecyclerView)view.findViewById(R.id.table_list);
        tvCustomer = (TextView)view.findViewById(R.id.tv_customer);
        tvTotalTable = (TextView)view.findViewById(R.id.tv_total_table);

        layoutManager = new GridLayoutManager(getActivity(), 2);
        listTable.setHasFixedSize(false);
        listTable.setLayoutManager(layoutManager);
        listTable.setAdapter(adapter);
        listTable.addItemDecoration(new TableItemSpacingDecoration(2, 8, false));
    }


    @Override
    public void setUICallbacks() {
        getBaseActivity().setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                getActivity().onBackPressed();
            }

            @Override
            public void onRightIconClick() {

            }
        });
    }

    @Override
    public void updateUI() {
        setupActionBar();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public String getPageTitle() {
        return "Order Station";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_list_table;
    }

    @Override
    public void onClick(View v) {

    }

    public void run() {
        setupActionBar();
        fetchData();
        getTotalCustomer();
    }

    public void fetchData() {
        settingData = sessions.getSettings();
        int total = Integer.parseInt(settingData.get(SessionManager.KEY_TABLES));

        for(int i = 1; i<=total; i++){
            Table table = new Table();
            table.setTableNumber(String.valueOf(i));
            table.setNumberOfCustomer("0");
            tableItems.add(table);
        }
        tvTotalTable.setText(String.valueOf(total +" Tabels"));
    }

    public void getTotalCustomer(){
        int total = 0;
        for (Table Item : tableItems) {
            total = total+Integer.parseInt(Item.getNumberOfCustomer());
        }
        tvCustomer.setText(String.valueOf(total)+" Customers");
    }

    @Override
    public void onItemClick(int position) {
        tableItems.get(position).setNumberOfCustomer(String.valueOf(position+1));
        getTotalCustomer();
    }

    public static class TableItemSpacingDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public TableItemSpacingDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if(includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if(position < spanCount) // top edge
                    outRect.top = spacing;

                outRect.bottom = spacing; // item bottom
            }
            else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)

                if(position >= spanCount)
                    outRect.top = spacing; // item top
            }
        }
    }
}
