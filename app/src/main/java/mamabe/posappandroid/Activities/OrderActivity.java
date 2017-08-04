package mamabe.posappandroid.Activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.HashMap;

import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.ListTableFragment;
import mamabe.posappandroid.Fragments.UserFragment;
import mamabe.posappandroid.Preferences.SessionManager;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 6/8/2017.
 */

public class OrderActivity  extends BaseActivity {

    ListTableFragment listTableFragment;

    private TextView tvName, tvRole;

    SessionManager sessions;

    private HashMap<String,String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRightIcon(R.drawable.order);
        setLeftIcon(R.drawable.back);
        setActionBarTitle("Order Station");

        listTableFragment = new ListTableFragment();

        sessions = new SessionManager(this);

        userData = sessions.getUserDetails();

        replaceFragment(R.id.fragment_container, listTableFragment, false);
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvRole = (TextView) findViewById(R.id.tv_role);
        showLoading(false);
    }

    @Override
    public void setUICallbacks() {
        setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                if(userData.get(SessionManager.KEY_ROLENAME).equalsIgnoreCase("admin"))
                {
                    onBackPressed();
                }
                else{
                    finish();
                }

            }

            @Override
            public void onRightIconClick() {

            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_order;
    }

    @Override
    public void updateUI() {
        userData = sessions.getUserDetails();

        tvName.setText(userData.get(SessionManager.KEY_EMPNAME));
        tvRole.setText(userData.get(SessionManager.KEY_ROLENAME));
    }

    @Override
    protected void onStart() {
        super.onStart();
        listTableFragment.run();
    }

}
