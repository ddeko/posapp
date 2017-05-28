package mamabe.posappandroid.Activities;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.MenuFragment;
import mamabe.posappandroid.R;

public class MenuSettingActivity extends BaseActivity {

    MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRightIcon(R.drawable.menu);
        setLeftIcon(R.drawable.back);
        setActionBarTitle("Menu Setting");

        menuFragment = new MenuFragment();

        replaceFragment(R.id.fragment_container, menuFragment, false);
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        showLoading(false);
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
        return R.layout.activity_menu_setting;
    }

    @Override
    public void updateUI() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        menuFragment.run();
    }
}
