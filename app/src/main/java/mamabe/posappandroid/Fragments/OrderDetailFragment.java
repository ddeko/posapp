package mamabe.posappandroid.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import mamabe.posappandroid.Activities.AddOrderActivity;
import mamabe.posappandroid.Activities.OrderActivity;
import mamabe.posappandroid.Application.Config;
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
    private Button btnAddOrder;
    private Button btnCheckOut;

    private SimpleDateFormat dateFormatter;
    private Calendar c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (OrderActivity) getActivity();

        c = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat(Config.DATE_FORMAT_LONG, Locale.US);

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
        btnAddOrder = (Button)view.findViewById(R.id.order_detail_btn_add);
        btnCheckOut = (Button)view.findViewById(R.id.order_detail_btn_checkout);

        btnAddOrder.setOnClickListener(this);
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

        String formattedDate = dateFormatter.format(c.getTime());
        tvOrderDate.setText(formattedDate);
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

        if(view==btnAddOrder)
        {
            Intent i = new Intent(activity.getApplicationContext(), AddOrderActivity.class);
            startActivity(i);
        }

    }

    public void run() {
        setupActionBar();

    }
}
