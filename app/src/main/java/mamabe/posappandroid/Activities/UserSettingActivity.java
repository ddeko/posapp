package mamabe.posappandroid.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.UserFragment;
import mamabe.posappandroid.R;

public class UserSettingActivity extends BaseActivity {

    UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRightIcon(R.drawable.usersetting);
        setLeftIcon(R.drawable.back);
        setActionBarTitle("User Setting");

        userFragment = new UserFragment();

        replaceFragment(R.id.fragment_container, userFragment, false);
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
        return R.layout.activity_user_setting;
    }

    @Override
    public void updateUI() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        userFragment.run();
    }
}
