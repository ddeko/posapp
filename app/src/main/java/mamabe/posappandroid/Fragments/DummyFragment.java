package mamabe.posappandroid.Fragments;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import mamabe.posappandroid.Activities.MenuSettingActivity;
import mamabe.posappandroid.Activities.UserSettingActivity;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 5/2/2017.
 */

public class DummyFragment extends BaseFragment implements View.OnClickListener{

    MenuSettingActivity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MenuSettingActivity) getActivity();


    }

    private void setupActionBar() {
        MenuSettingActivity mainActivity = (MenuSettingActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.usersetting);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void initView(View view) {

        TextView btnClear = (TextView)view.findViewById(R.id.textView34);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "CLEAR", Toast.LENGTH_SHORT).show();
            }
        });
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
        return "User Setting";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_add_menu;
    }

    @Override
    public void onClick(View v) {

    }

    public void run() {
        setupActionBar();
//        fetchData();
    }
}
