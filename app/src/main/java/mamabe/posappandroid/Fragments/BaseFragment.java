package mamabe.posappandroid.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mamabe.posappandroid.Activities.BaseActivity;
import mamabe.posappandroid.Base.FragmentInterface;


/**
 * Created by dede_eko on 7/1/15.
 */
public abstract class BaseFragment extends Fragment implements FragmentInterface {
    protected Activity activity;
    protected boolean hasLoadDataFromAPI;
    protected Bundle saveInstanceState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        this.saveInstanceState = savedInstanceState;
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateUI();
        setUICallbacks();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public BaseActivity getBaseActivity() {
        return ((BaseActivity) activity);
    }

    public void replaceFragment(int container, BaseFragment fragment, boolean addBackToStack) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (addBackToStack) {
            ft.addToBackStack(fragment.getPageTitle());
        }
        //ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        ft.replace(container, fragment);
        ft.commit();
    }

    public void replaceFragment(int container, Fragment fragment, boolean addBackToStack) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (addBackToStack) {
            ft.addToBackStack(null);
        }
        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(container, fragment);
        ft.commit();
    }

    public void addFragment(int container, BaseFragment fragment, boolean addBackToStack) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (addBackToStack) {
            ft.addToBackStack(fragment.getPageTitle());
        }
        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.add(container, fragment);
        ft.commit();
    }

    public void addChildFragment(int container, BaseFragment fragment, boolean addBackToStack) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (addBackToStack) {
            ft.addToBackStack(fragment.getPageTitle());
        }
        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.add(container, fragment);
        ft.commitAllowingStateLoss();
    }

    public String checkNullString(String string) {
        return (string == null) ? "" : string;
    }
}

