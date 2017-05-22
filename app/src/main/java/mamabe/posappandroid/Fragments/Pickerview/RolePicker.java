package mamabe.posappandroid.Fragments.Pickerview;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mamabe.posappandroid.APIs.API;
import mamabe.posappandroid.APIs.ServiceGenerator;
import mamabe.posappandroid.Activities.UserSettingActivity;
import mamabe.posappandroid.Models.Employee;
import mamabe.posappandroid.Models.EmployeeResponse;
import mamabe.posappandroid.Models.Role;
import mamabe.posappandroid.Models.RoleResponse;
import mamabe.posappandroid.Views.PickerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class RolePicker extends BasePickerView {
    private ArrayList<String> roles;
    private ArrayList<Role> listRole;

    public API api = null;
    private Context context;

    public RolePicker(Context context ) {
        super(context);

        roles = new ArrayList<>();
        api = ServiceGenerator.createService(API.class);
    }

    @Override
    protected void loadData() {

        if(roles.isEmpty() || pickerObjects.isEmpty()) {
            visibleProgressBar();
            Call<RoleResponse> call = null;
            call = api.getRoleList();
            call.enqueue(new Callback<RoleResponse>() {
                @Override
                public void onResponse(Call<RoleResponse> call, Response<RoleResponse> response) {
                    goneProgressBar();
                    if (2 == response.code() / 100) {

                        final RoleResponse roleResponse = response.body();
                        Log.d("RolePicker", "response = " + new Gson().toJson(roleResponse));

                        listRole = roleResponse.getRoles();

                        roles.clear();
                        pickerObjects.clear();
                        String role;
                        int index = 0;
                        for (int i = 0; i < listRole.size(); i++) {
                            role = listRole.get(i).getRoleName();

                            roles.add(role);
                            addPickerObject(index, index, role, null, false);
                            index++;
                        }

                        completeLoading();

                    } else {
                        showErrorMessage();
                    }
                }

                @Override
                public void onFailure(Call<RoleResponse> call, Throwable t) {

                    goneProgressBar();
                    Toast.makeText(context, "Failed to load province", Toast.LENGTH_LONG).show();
                }

            });
        }
        else {
            resetSelectorsState();
            restoreSelectorsState();
            completeLoading();
        }

    }

    @Override
    protected String getPickerTitle() {
        return "Choose Role";
    }

    @Override
    protected String getPickerSubtitle() {
        return null;
    }

    @Override
    protected int getPickerMode() {
        return PickerView.PICKER_MODE_SINGLE_TAP;
    }

    @Override
    public void onPickDone(int pickerMode, ArrayList<PickerView.PickerObject> objects) {
        if(pickerMode == PickerView.PICKER_MODE_SINGLE_TAP) {
            int countryIndex = objects.get(0).getExtIndex();
            String state = roles.get(countryIndex);

            onPickDone(state, listRole.get(countryIndex).getRoleId());
        }
    }

    // restore selected object, this must be implemented on each subclass
    private void restoreSelectorsState() {
        String lastProvince = lastSelectedProvince();
        if(lastProvince == null || lastProvince.isEmpty())
            return;

        int indexToFind = roles.indexOf(lastProvince);
        for(PickerView.PickerObject object : pickerObjects) {
            if((indexToFind >= 0 && indexToFind == object.getExtIndex()) || lastProvince.equals(object.getTitle())) {
                object.setIsSelected(true);
                break;
            }
        }
    }

    protected abstract void onPickDone(String province, String id);
    protected abstract String lastSelectedProvince();

    private void showErrorMessage() {
        Toast.makeText(context, "failed to get data", Toast.LENGTH_SHORT).show();

    }
}

