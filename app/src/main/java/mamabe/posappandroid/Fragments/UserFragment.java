package mamabe.posappandroid.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mamabe.posappandroid.Activities.UserSettingActivity;
import mamabe.posappandroid.Adapter.UserAdapter;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.Dialogs.ChangePasswordDialog;
import mamabe.posappandroid.Models.Employee;
import mamabe.posappandroid.Models.EmployeeBody;
import mamabe.posappandroid.Models.EmployeePostResponse;
import mamabe.posappandroid.Models.EmployeeResponse;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 5/2/2017.
 */

public class UserFragment extends BaseFragment implements View.OnClickListener, UserAdapter.UserAdapterListener, ChangePasswordDialog.ChangePasswordListener{

    UserSettingActivity activity;
    dummyFragment dummyFragment;

    private UserAdapter adapter;

    private RecyclerView recyclerView;
    private RelativeLayout btnAddUser;

    private ArrayList<Employee> listUser;

    private int lastposition;

//    private ProgressDialog nDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (UserSettingActivity) getActivity();
        listUser = new ArrayList<>();

        adapter = new UserAdapter(listUser, this, activity.getApplicationContext(), activity, this);

//        nDialog = new ProgressDialog(getActivity());

    }

    private void setupActionBar() {
        UserSettingActivity mainActivity = (UserSettingActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.usersetting);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());
    }

    @Override
    public void initView(View view) {

        btnAddUser = (RelativeLayout) view.findViewById(R.id.btn_add_user);
        recyclerView = (RecyclerView) view.findViewById(R.id.listUser);
        recyclerView.setHasFixedSize(false);
        setScroll(true);

        adapter = new UserAdapter(listUser, this, getActivity(), activity, this);

        recyclerView.setAdapter(adapter);

        btnAddUser.setOnClickListener(this);

//        fetchData();
    }

    public void setScroll(final boolean flag) {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext()){
            @Override
            public boolean canScrollVertically() {
                return flag;
            }
        };
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
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

    public void fetchData(){
        activity.showLoading(true);
        listUser.clear();
        Call<EmployeeResponse> call = null;
        call = activity.api.getEmployeeList();

        call.enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {

                if (2 == response.code() / 100) {

                    final EmployeeResponse employeeResponse = response.body();
                    Log.d("UserFragment", "response = " + new Gson().toJson(employeeResponse));

                    List<Employee> listemp = employeeResponse.getEmployee();
                    for (Employee userResponseItem : listemp) {
                        listUser.add(userResponseItem);
                        adapter.notifyDataSetChanged();
                    }

                    activity.showLoading(false);


                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot fetching data.", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<EmployeeResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Fail to fetching data.", Toast.LENGTH_SHORT).show();
                Log.d("UserFragment", t.getMessage()+t.getLocalizedMessage());
                activity.showLoading(false);
            }
        });
    }

    public void run() {
        setupActionBar();
        fetchData();
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
        if(v==btnAddUser)
        {
            boolean isNull = false;
            for (Employee employee : listUser) {
                if(employee.getEmpId()==null){
                    isNull = true;
                }
            }

            if(isNull== false)
            {
                addUser();
//                setScroll(false);
            }
            else
            {
                Toast.makeText(activity.getApplicationContext(), "Please fill the empty data, before add new user .", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void addUser()
    {
        activity.showLoading(true);
        Employee employeeBody = new Employee();
        employeeBody.setUsername("");
        employeeBody.setPassword("");
        employeeBody.setEmpName("");
        employeeBody.setAddress("");
        employeeBody.setPhone("");
        employeeBody.setRoleName("");

        listUser.add(employeeBody);
        adapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(adapter.getItemCount()-1);

        activity.showLoading(false);


    }

    @Override
    public void onSave(int position) {
        fetchData();
        activity.showLoading(false);
    }

    @Override
    public void onDelete(int position) {
        fetchData();
        recyclerView.scrollToPosition(position-1);
        activity.showLoading(false);
    }

    @Override
    public void onLoading() {
        activity.showLoading(true);
    }

    @Override
    public void onPasswordClick(int position) {
        lastposition = position;
        Employee user = listUser.get(position);
        ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(user.getPassword(),user.getEmpId(),activity.getApplicationContext(),this );
        changePasswordDialog.show(activity.getSupportFragmentManager(), null);
    }

    @Override
    public void onChangeDone(String password) {
        listUser.get(lastposition).setPassword(password);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(lastposition);
    }
}
