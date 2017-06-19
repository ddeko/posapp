package mamabe.posappandroid.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import mamabe.posappandroid.Activities.OrderActivity;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 6/19/2017.
 */

public class OrderDetailFragment extends BaseFragment implements View.OnClickListener {

    private OrderActivity activity;

    private RecyclerView listOrder;
    private TextView tvGuestName;
    private TextView tvTableNumber;
    private TextView tvTotalGuest;
    private TextView tvOrderDate;
    private TextView tvTotalItem;
    private TextView tvSubTotal;
    private TextView btnAddOrder;
    private TextView btnCheckOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (OrderActivity) getActivity();

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
        listOrder = (RecyclerView) view.findViewById(R.id.order_detail_list);
        tvGuestName = (TextView)view.findViewById(R.id.order_detail_customer_name);
        tvTableNumber = (TextView)view.findViewById(R.id.order_detail_table_number);
        tvTotalGuest = (TextView)view.findViewById(R.id.order_detail_total_customer);
        tvOrderDate = (TextView)view.findViewById(R.id.order_detail_date);
        tvTotalItem = (TextView)view.findViewById(R.id.order_detail_total_item);
        tvSubTotal = (TextView)view.findViewById(R.id.order_detail_sub_total);
        btnAddOrder = (TextView)view.findViewById(R.id.order_detail_btn_add);
        btnCheckOut = (TextView)view.findViewById(R.id.order_detail_btn_checkout);
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
        return R.layout.fragment_order_detail;
    }

    @Override
    public void onClick(View view) {

    }

    public void run() {
        setupActionBar();

    }
}
