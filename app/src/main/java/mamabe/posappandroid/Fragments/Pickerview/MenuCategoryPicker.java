package mamabe.posappandroid.Fragments.Pickerview;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import mamabe.posappandroid.APIs.API;
import mamabe.posappandroid.APIs.ServiceGenerator;
import mamabe.posappandroid.Models.MenuCategory;
import mamabe.posappandroid.Models.MenuCategoryResponse;
import mamabe.posappandroid.Models.Role;
import mamabe.posappandroid.Models.RoleResponse;
import mamabe.posappandroid.Views.PickerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class MenuCategoryPicker extends BasePickerView {
    private ArrayList<String> categories;
    private ArrayList<MenuCategory> listCategory;

    public API api = null;
    private Context context;

    public MenuCategoryPicker(Context context ) {
        super(context);

        categories = new ArrayList<>();
        api = ServiceGenerator.createService(API.class);
    }

    @Override
    protected void loadData() {

        if(categories.isEmpty() || pickerObjects.isEmpty()) {
            visibleProgressBar();
            Call<MenuCategoryResponse> call = null;
            call = api.getMenuCategory("");
            call.enqueue(new Callback<MenuCategoryResponse>() {
                @Override
                public void onResponse(Call<MenuCategoryResponse> call, Response<MenuCategoryResponse> response) {
                    goneProgressBar();
                    if (2 == response.code() / 100) {

                        final MenuCategoryResponse menuCategoryResponse = response.body();
                        Log.d("RolePicker", "response = " + new Gson().toJson(menuCategoryResponse));

                        listCategory = menuCategoryResponse.getResult();

                        categories.clear();
                        pickerObjects.clear();
                        String category;
                        int index = 0;
                        for (int i = 0; i < listCategory.size(); i++) {
                            category = listCategory.get(i).getMenuCategoryName();

                            categories.add(category);
                            addPickerObject(index, index, category, null, false);
                            index++;
                        }

                        completeLoading();

                    } else {
                        showErrorMessage();
                    }
                }

                @Override
                public void onFailure(Call<MenuCategoryResponse> call, Throwable t) {

                    goneProgressBar();
                    Toast.makeText(context, "Failed to load categories", Toast.LENGTH_LONG).show();
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
        return "Choose Category";
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
            String state = categories.get(countryIndex);

            onPickDone(state, listCategory.get(countryIndex).getMenuCategoryId());
        }
    }

    // restore selected object, this must be implemented on each subclass
    private void restoreSelectorsState() {
        String lastProvince = lastSelectedProvince();
        if(lastProvince == null || lastProvince.isEmpty())
            return;

        int indexToFind = categories.indexOf(lastProvince);
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

