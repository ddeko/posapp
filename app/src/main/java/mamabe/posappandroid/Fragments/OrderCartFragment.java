package mamabe.posappandroid.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import mamabe.posappandroid.Activities.AddOrderActivity;
import mamabe.posappandroid.Adapter.CartAdapter;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.BaseFragment;
import mamabe.posappandroid.Fragments.Dialogs.AddOrderDialog;
import mamabe.posappandroid.Fragments.Dialogs.UpdateOrderDialog;
import mamabe.posappandroid.Models.Menu;
import mamabe.posappandroid.Models.OrderDetailBody;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 7/3/2017.
 */

public class OrderCartFragment extends BaseFragment implements AddOrderActivity.SendCartData, CartAdapter.CartAdapterListener,
                                                               View.OnClickListener, UpdateOrderDialog.UpdateOrderDialogListener{

    AddOrderActivity activity;

    private CartAdapter adapter;

    private ArrayList<OrderDetailBody> listCart;

    private RecyclerView recyclerCart;
    private RelativeLayout sendOrderBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (AddOrderActivity) getActivity();
        listCart = new ArrayList<>();

        adapter = new CartAdapter(((AddOrderActivity)getActivity()).orderItemList,this,activity);
    }


    private void setupActionBar() {
        AddOrderActivity mainActivity = (AddOrderActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.menu);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());
    }

    @Override
    public void initView(View view) {
        recyclerCart = (RecyclerView) view.findViewById(R.id.order_cart_list);
        sendOrderBtn = (RelativeLayout) view.findViewById(R.id.order_cart_btn_send);

        recyclerCart.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerCart.setLayoutManager(llm);

        adapter = new CartAdapter(((AddOrderActivity)getActivity()).orderItemList,this,activity);

        recyclerCart.setAdapter(adapter);


        listCart = ((AddOrderActivity)getActivity()).orderItemList;
        adapter.notifyDataSetChanged();

        sendOrderBtn.setOnClickListener(this);
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public String getPageTitle() {
        return "Menu Setting";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_order_cart;
    }

    @Override
    public void onAddData(ArrayList<OrderDetailBody> orderItemList) {

    }

    @Override
    public void onClick(View view) {
        if(view==sendOrderBtn)
        {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void notifyAdapter(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(OrderDetailBody item, int position) {
        OrderDetailBody orderItem = ((AddOrderActivity)getActivity()).orderItemList.get(position);
        UpdateOrderDialog updateOrderDialog = new UpdateOrderDialog(true, this, orderItem, position);
        updateOrderDialog.show(activity.getSupportFragmentManager(),null);
    }

    @Override
    public void onUpdateItem(OrderDetailBody orderItem, String qty, String note, int position) {
        ((AddOrderActivity)getActivity()).orderItemList.get(position).setQty(qty);
        ((AddOrderActivity)getActivity()).orderItemList.get(position).setNote(note);
        notifyAdapter();
    }

    @Override
    public void onItemDelete(int position) {
        ((AddOrderActivity)getActivity()).orderItemList.remove(position);
        notifyAdapter();
    }
}
