package mamabe.posappandroid.Activities;

import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.Dialogs.AddOrderDialog;
import mamabe.posappandroid.Fragments.MenuFragment;
import mamabe.posappandroid.Fragments.OrderCartFragment;
import mamabe.posappandroid.Fragments.OrderMenuFragment;
import mamabe.posappandroid.Models.Menu;
import mamabe.posappandroid.Models.OrderDetailBody;
import mamabe.posappandroid.R;

public class AddOrderActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    TextView count2;
    TextView tabTwo2;

    OrderCartFragment orderCartFragment;
    OrderMenuFragment orderMenuFragment;

    public ArrayList<OrderDetailBody> orderItemList;

    public interface SendCartData {
        void onAddData(ArrayList<OrderDetailBody> orderItemList);
    }

    public SendCartData sendCartData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRightIcon(R.drawable.order);
        setLeftIcon(R.drawable.back);
        setActionBarTitle("Order Station");

        orderItemList = new ArrayList<>();

//        sendCartData.onAddData(orderItemList);



    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        LinearLayout linearLayout = (LinearLayout)tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getResources().getColor(R.color.softbrown));
        drawable.setSize(2, 1);
        linearLayout.setDividerPadding(0);
        linearLayout.setDividerDrawable(drawable);

        showLoading(false);
    }

    private void setupTabIcons() {

        View v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tabTwo = (TextView) v.findViewById(R.id.tab);
        TextView count = (TextView) v.findViewById(R.id.count);
        tabTwo.setText("Item");
        count.setVisibility(View.GONE);
        tabLayout.getTabAt(0).setCustomView(v);

        View v2 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo2 = (TextView) v2.findViewById(R.id.tab);
        count2 = (TextView) v2.findViewById(R.id.count);
        tabTwo2.setText("Ordering");
        if(orderItemList!=null)
            count2.setText(orderItemList.size());
        else
            count2.setText("0");
        tabLayout.getTabAt(1).setCustomView(v2);

    }

    private void setupViewPager(ViewPager viewPager) {
        orderCartFragment = new OrderCartFragment();
        orderMenuFragment = new OrderMenuFragment();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(orderMenuFragment, "Item");
        adapter.addFrag(orderCartFragment, "Ordering");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void setUICallbacks() {
        setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                onBackPressed();

            }

            @Override
            public void onRightIconClick() {

            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_add_order;
    }

    @Override
    public void updateUI() {

    }

    public void onAddItemToCart(OrderDetailBody orderDetailBody)
    {
        orderItemList.add(orderDetailBody);
        count2.setText(String.valueOf(orderItemList.size()));
        orderCartFragment.notifyAdapter();
//        sendCartData.onAddData(orderItemList);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
