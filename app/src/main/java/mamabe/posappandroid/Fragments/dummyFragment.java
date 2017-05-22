package mamabe.posappandroid.Fragments;

import android.os.Bundle;
import android.view.View;

import mamabe.posappandroid.Activities.UserSettingActivity;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 5/2/2017.
 */

public class dummyFragment extends BaseFragment implements View.OnClickListener{

    UserSettingActivity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (UserSettingActivity) getActivity();


    }

    private void setupActionBar() {
        UserSettingActivity mainActivity = (UserSettingActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.usersetting);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());
    }

    @Override
    public void initView(View view) {

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
        return R.layout.fragment_user_setting;
    }

    @Override
    public void onClick(View v) {

    }
}
