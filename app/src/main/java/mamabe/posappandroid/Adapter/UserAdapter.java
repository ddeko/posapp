package mamabe.posappandroid.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import mamabe.posappandroid.Activities.UserSettingActivity;
import mamabe.posappandroid.Fragments.Dialogs.ChangePasswordDialog;
import mamabe.posappandroid.Fragments.Pickerview.RolePicker;
import mamabe.posappandroid.Fragments.UserFragment;
import mamabe.posappandroid.Models.Employee;
import mamabe.posappandroid.Models.EmployeeBody;
import mamabe.posappandroid.Models.EmployeePostResponse;
import mamabe.posappandroid.Models.Role;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 5/13/2017.
 */

public class UserAdapter extends RecyclerView.Adapter{

    ArrayList<Employee> userList;
    ArrayList<Employee> userListTemp;

    private Context context;
    private UserSettingActivity activity;
    private UserFragment fragment;
    private UserAdapterListener listener;

    private RolePicker rolePicker;
    private ChangePasswordDialog changePasswordDialog;

    private String selectedRole;
    private String idRole;


    public interface UserAdapterListener {
        void onSave(int position);
        void onDelete(int position);
        void onLoading();
        void onPasswordClick(int position);
    }

    public UserAdapter(ArrayList<Employee> userList, UserAdapterListener listener, Context context, UserSettingActivity activity
                        , UserFragment fragment) {
        this.userList = userList;
        this.listener = listener;
        this.context = context;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_item, parent, false);
        ImageButton btnDeleteUser = (ImageButton)v.findViewById(R.id.btn_delete_user);
        ImageButton btnSaveUser = (ImageButton)v.findViewById(R.id.btn_save_user);
        EditText etNama = (EditText)v.findViewById(R.id.et_nama);
        EditText etUser = (EditText)v.findViewById(R.id.et_user);
        EditText etAddress = (EditText)v.findViewById(R.id.et_address);
        EditText etPhone = (EditText)v.findViewById(R.id.et_phone);
        final EditText etRole = (EditText)v.findViewById(R.id.et_role);
        EditText etPassword = (EditText)v.findViewById(R.id.et_pass);

        rolePicker = new RolePicker(context) {
            @Override
            protected void onPickDone(String role, String id  ) {
                selectedRole = role;
                idRole = id;

                etRole.setText(selectedRole);
            }

            @Override
            protected String lastSelectedProvince() {
                selectedRole = etRole.getText().toString();

                return selectedRole;
            }
        };



        return  new ViewHolder(v, btnDeleteUser, btnSaveUser, etNama, etUser, etAddress, etPhone, etRole, etPassword, rolePicker, changePasswordDialog);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ViewHolder v = (ViewHolder) holder;
        final Employee user = userList.get(position);

        userListTemp=userList;

        final Employee userTemp = userListTemp.get(position);

        if(position==0&& user.getRoleId().equalsIgnoreCase("1"))
        {
            v.btnSaveUser.setVisibility(View.GONE);
            v.btnDeleteUser.setVisibility(View.GONE);
            v.etNama.setEnabled(false);
            v.etUser.setEnabled(false);
            v.etAddress.setEnabled(false);
            v.etPhone.setEnabled(false);
            v.etRole.setEnabled(false);
            v.etPassword.setEnabled(false);
        }
        else
        {
            v.btnSaveUser.setVisibility(View.VISIBLE);
            v.btnDeleteUser.setVisibility(View.VISIBLE);
            v.etNama.setEnabled(true);
            v.etUser.setEnabled(true);
            v.etAddress.setEnabled(true);
            v.etPhone.setEnabled(true);
            v.etRole.setEnabled(true);
            v.etPassword.setEnabled(true);
        }

        v.etNama.setText(user.getEmpName());
        v.etUser.setText(user.getUsername());
        v.etAddress.setText(user.getAddress());
        v.etPhone.setText(user.getPhone());
        v.etRole.setText(user.getRoleName());
        v.etPassword.setText(user.getPassword());


        v.etPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setEmpName(v.etNama.getText().toString());
                user.setUsername(v.etUser.getText().toString());
                user.setAddress(v.etAddress.getText().toString());
                user.setPhone(v.etPhone.getText().toString());
                user.setRoleName(v.etRole.getText().toString());

                if (listener != null)
                    listener.onPasswordClick(position);

            }
        });

        v.etRole.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                v.rp.show(view);
            }
        });

        v.btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EmployeeBody employeeBody = new EmployeeBody();

                employeeBody.setUsername(v.etUser.getText().toString());
                employeeBody.setPassword(v.etPassword.getText().toString());
                employeeBody.setEmp_name(v.etNama.getText().toString());
                employeeBody.setAddress(v.etAddress.getText().toString());
                employeeBody.setPhone(v.etPhone.getText().toString());
                employeeBody.setRole_name(v.etRole.getText().toString());

                if(v.etUser.getText().toString().equalsIgnoreCase(""))
                {
                    v.etUser.setHint("Cannot be empty!");
                }
                if(v.etPassword.getText().toString().equalsIgnoreCase("")){

                    v.etPassword.setHint("Cannot be empty!");
                }
                if(v.etRole.getText().toString().equalsIgnoreCase("")){

                    v.etRole.setHint("Cannot be empty!");
                }

                if(v.etRole.getText().toString().equalsIgnoreCase("")||v.etRole.getText().toString().equalsIgnoreCase("")||
                        v.etRole.getText().toString().equalsIgnoreCase("")){

                    Toast.makeText(activity.getApplicationContext(), "Please fill the empty data!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (listener != null)
                        listener.onLoading();
                    if(user.getEmpId()==null)
                    {
                        insertData(employeeBody, v.getAdapterPosition());
                    }
                    else {

                        if(userTemp.getUsername().equals(employeeBody.getUsername()))
                        {
                            employeeBody.setChange_username("0");
                        }else
                        {
                            employeeBody.setChange_username("1");
                        }
                        employeeBody.setEmp_id(user.getEmpId());
                        updateData(employeeBody, v.getAdapterPosition());
                    }

                }


            }
        });

        v.btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeBody employeeBody = new EmployeeBody();

                employeeBody.setEmp_id(user.getEmpId());
                employeeBody.setEmp_status("0");

                deleteData(employeeBody, v.getAdapterPosition());

            }
        });
    }
    public void updateData(final EmployeeBody employeeBody,final int position){
        activity.showLoading(true);
        Call<EmployeePostResponse> call = null;

        call = activity.api.updateEmployee(employeeBody);
        call.enqueue(new Callback<EmployeePostResponse>() {
            @Override
            public void onResponse(Call<EmployeePostResponse> call, Response<EmployeePostResponse> response) {

                if (201 == response.code()) {

                    final EmployeePostResponse postResponse = response.body();

                    Log.d("UserFragment", "response = " + new Gson().toJson(postResponse));

                    if(postResponse.getStatus()==0)
                    {
                        Toast.makeText(activity.getApplicationContext(), "Username is already in use", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (listener != null)
                            listener.onSave(position);
                    }
                    activity.showLoading(false);

                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot update data", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<EmployeePostResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed update data", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });

    }

    public void insertData(final EmployeeBody employeeBody,final int position){
        activity.showLoading(true);
        Call<EmployeePostResponse> call = null;

        call = activity.api.postEmployee(employeeBody);
        call.enqueue(new Callback<EmployeePostResponse>() {
            @Override
            public void onResponse(Call<EmployeePostResponse> call, Response<EmployeePostResponse> response) {

                if (201 == response.code()) {

                    final EmployeePostResponse postResponse = response.body();

                    Log.d("UserFragment", "response = " + new Gson().toJson(postResponse));

                    if(postResponse.getStatus()==0)
                    { 
                        Toast.makeText(activity.getApplicationContext(), "Username is already in use", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (listener != null)
                            listener.onSave(position);
                    }
                    activity.showLoading(false);

                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot insert data", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<EmployeePostResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed insert data", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });

    }

    public void deleteData(final EmployeeBody employeeBody,final int position){
        activity.showLoading(true);
        Call<EmployeePostResponse> call = null;

        call = activity.api.deleteEmployee(employeeBody);
        call.enqueue(new Callback<EmployeePostResponse>() {
            @Override
            public void onResponse(Call<EmployeePostResponse> call, Response<EmployeePostResponse> response) {

                if (201 == response.code()) {

                    final EmployeePostResponse postResponse = response.body();

                    Log.d("UserFragment", "response = " + new Gson().toJson(postResponse));


                    if (listener != null)
                        listener.onDelete(position);

                    activity.showLoading(false);

                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot delete data", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<EmployeePostResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed delete data", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageButton btnDeleteUser;
        public ImageButton btnSaveUser;
        public EditText etNama;
        public EditText etUser;
        public EditText etAddress;
        public EditText etPhone;
        public EditText etRole;
        public EditText etPassword;
        public RolePicker rp;
        public ChangePasswordDialog cpd;

        public ViewHolder(View itemView, ImageButton btnDeleteUser, ImageButton btnSaveUser, EditText etNama,
                          EditText etUser, EditText etAddress, EditText etPhone, EditText etRole, EditText etPassword, RolePicker rp, ChangePasswordDialog cpd) {
            super(itemView);
            this.btnDeleteUser = btnDeleteUser;
            this.btnSaveUser = btnSaveUser;
            this.etNama = etNama;
            this.etUser = etUser;
            this.etAddress = etAddress;
            this.etPhone = etPhone;
            this.etRole = etRole;
            this.etPassword = etPassword;
            this.rp = rp;
            this.cpd = cpd;
        }

    }
}
