package mamabe.posappandroid.Activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.ListTableFragment;
import mamabe.posappandroid.Fragments.UserFragment;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 6/8/2017.
 */

public class OrderActivity  extends BaseActivity {

    ListTableFragment listTableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRightIcon(R.drawable.order);
        setLeftIcon(R.drawable.back);
        setActionBarTitle("Order Station");

        listTableFragment = new ListTableFragment();

        replaceFragment(R.id.fragment_container, listTableFragment, false);
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
        return R.layout.activity_order;
    }

    @Override
    public void updateUI() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        listTableFragment.run();
    }
}
