package mamabe.posappandroid.Fragments.Pickerview;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;

import mamabe.posappandroid.Utils.FunctionUtil;
import mamabe.posappandroid.Views.PickerView;


public abstract class BasePickerView implements PickerView.PickerViewListener {
    private PickerView pickerView;
    private Runnable completeLoading;
    protected Context context;
    protected ArrayList<PickerView.PickerObject> pickerObjects;
    protected boolean needAllOption;

    // base constructor
    public BasePickerView(Context context) {
        this.context = context;

        pickerObjects = new ArrayList<>();
        pickerView = new PickerView(context);

        pickerView.setListener(this);
        pickerView.setPickerMode(getPickerMode());

        if(getPickerMode() != PickerView.PICKER_MODE_BUTTONS)
            pickerView.setTitle(getPickerTitle(), getPickerSubtitle());

        completeLoading = new Runnable() {
            @Override
            public void run() {
                pickerView.setListData(pickerObjects);
                pickerView.toggleLoading(false);
            }
        };
    }

    @Override
    public void onActionButtonClick() {
        // override-able by the child class
    }

    @Override
    public void onButton1Click() {
        // override-able by the child class
    }

    @Override
    public void onButton2Click() {
        // override-able by the child class
    }

    // public function
    public final void show(View parent) {
        // make sure no keyboard is active
        FunctionUtil.hideSoftKeyboard((Activity) context);

        // show the dialog
        pickerView.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        pickerView.toggleLoading(true);

        loadData();
    }

    // public function
    public final boolean isShowing() {
        return pickerView.isShowing();
    }

    // public function
    public void setNeedAllOption(boolean isNeeded) {
        this.needAllOption = isNeeded;
    }

    // public function
    public void useFloatingActionButton(boolean use) {
        pickerView.toggleFloatingActionButton(use);
    }

    // public function
    public void forceRefresh() {
        pickerObjects.clear();
        pickerView.toggleLoading(true);

        loadData();
    }

    // public function
    public void setButtonsActive(boolean needToActiveButton1, boolean needToActiveButton2) {
        pickerView.setButtonsProperty(needToActiveButton1 ? getPickerTitle() : null,
                needToActiveButton2 ? getPickerSubtitle() : null);
    }

    // public function
    public void setFirstItemAsReset(boolean needToReset) {
        pickerView.setFirstItemAsReset(needToReset);
    }

    // only for children class
    protected final void completeLoading() {
        if(Looper.myLooper() != Looper.getMainLooper())     // if we are not in main thread
            ((Activity)context).runOnUiThread(completeLoading);
        else
            completeLoading.run();
    }

    // only for children class
    protected final void dismiss() {
        pickerView.dismiss();
    }

    // only for children class
    protected final void resetSelectorsState() {
        for(PickerView.PickerObject object : pickerObjects)
            object.setIsSelected(false);
    }

    // only for children class
    protected final void addPickerObject(int id, int index, String title, String subtitle, boolean isSelected) {
        addPickerObject(id, index, title, subtitle, isSelected, 0);
    }

    // only for children class
    protected final void addPickerObject(int id, int index, String title, String subtitle, boolean isSelected, int iconDrawable) {
        PickerView.PickerObject object = new PickerView.PickerObject();
        object.setExtId(id);
        object.setExtIndex(index);
        object.setTitle(title);
        object.setSubtitle(subtitle);
        object.setIsSelected(isSelected);
        object.setIconDrawable(iconDrawable);

        pickerObjects.add(object);
    }

    // only for children class
    protected final void showNotification(String notification) {
        pickerView.showNotification(notification);
    }

    // only for children class
    protected final void hideNotification() {
        pickerView.hideNotification();
    }

    // only for children class
    protected final void refreshFAB() {
        pickerView.flushFloatingButton();
    }

    // only for children class
    protected final void toggleButtonsState(boolean button1, boolean button2) {
        pickerView.toggleButtonsState(button1, button2);
    }


    protected final void visibleProgressBar()
    {
        pickerView.getProgressBar().setVisibility(View.VISIBLE);
    }
    protected final void goneProgressBar()
    {
        pickerView.getProgressBar().setVisibility(View.GONE);
    }


    // need to implement
    protected abstract void loadData();
    protected abstract String getPickerTitle();
    protected abstract String getPickerSubtitle();
    protected abstract int getPickerMode();
}
