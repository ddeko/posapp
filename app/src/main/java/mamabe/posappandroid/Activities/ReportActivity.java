package mamabe.posappandroid.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.DailyReportFragment;
import mamabe.posappandroid.Fragments.KitchenStationFragment;
import mamabe.posappandroid.Fragments.MonthReportFragment;
import mamabe.posappandroid.Fragments.WaiterStationFragment;
import mamabe.posappandroid.R;

public class ReportActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public DailyReportFragment dailyReportFragment;
    public MonthReportFragment monthReportFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRightIcon(R.drawable.report);
        setLeftIcon(R.drawable.back);
        setActionBarTitle("Report");
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
        TextView tab = (TextView) v.findViewById(R.id.tab);
        TextView count = (TextView) v.findViewById(R.id.count);
        tab.setText("Daily Report");
        count.setVisibility(View.GONE);
        tabLayout.getTabAt(0).setCustomView(v);

        View v2 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tab2 = (TextView) v2.findViewById(R.id.tab);
        TextView count2 = (TextView) v2.findViewById(R.id.count);
        tab2.setText("Monthly Report");
        count2.setVisibility(View.GONE);
        tabLayout.getTabAt(1).setCustomView(v2);

    }

    private void setupViewPager(ViewPager viewPager) {
        dailyReportFragment = new DailyReportFragment();
        monthReportFragment = new MonthReportFragment();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(dailyReportFragment, "Item");
        adapter.addFrag(monthReportFragment, "Ordering");
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
        return R.layout.activity_kitchen;
    }

    @Override
    public void updateUI() {

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
