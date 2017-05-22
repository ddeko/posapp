package mamabe.posappandroid.Views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import mamabe.posappandroid.Adapter.BaseAdapter;
import mamabe.posappandroid.R;
import mamabe.posappandroid.Utils.FunctionUtil;


public class PickerView extends PopupWindow implements View.OnClickListener, AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public interface PickerViewListener {
        void onPickDone(int pickerMode, ArrayList<PickerObject> objects);
        void onActionButtonClick();
        void onButton1Click();
        void onButton2Click();
    }

    public static final int PICKER_MODE_SINGLE = 0;
    public static final int PICKER_MODE_MULTIPLE = 1;
    public static final int PICKER_MODE_SINGLE_TAP = 2;
    public static final int PICKER_MODE_BUTTONS = 3;

    private TextView tvTitle;
    private TextView tvSubtitle;
    private ListView listView;

    private ProgressBar progressBar;
//    private View pbNotifier;
//    private View btnDone;
    private Button btnDone;
    private ViewGroup vgRoot;
    private LinearLayout llDim;
    private FloatingActionButton fabAction;
    private TextView tvNotification;

    private View llDefaultHeader;
    private View llButtonsHeader;
    private TextView tvHeader1;
    private TextView tvHeader2;

    private PickerAdapter adapter;
    private ArrayList<PickerObject> objects;

    private int activeColorRes;
    private int buttonPadding;  // used to restore padding after changing background

    private boolean hasFAB;
    private boolean useFirstItemReset;
    private int activeMode;
    private PickerViewListener listener;

    private AnimatorSet backgroundDimAnim;
    private AnimatorSet actionButtonAnim;
    private ArrayList<PickerObject> selectedObjects;

    // class constructor
    public PickerView(Context context) {
        super(context);

        // initialize popup
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(false);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popup_animation);
        this.setBackgroundDrawable(new ColorDrawable(0xffffff));
        this.setOnDismissListener(this);

        // initialize
        adapter = new PickerAdapter(context);
        selectedObjects = new ArrayList<>();
        activeColorRes = context.getResources().getColor(R.color.blank_button_active);

        // attach layout to view
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.view_picker_window, null);
        tvTitle = (TextView)rootView.findViewById(R.id.view_picker_window_title);
        tvSubtitle = (TextView)rootView.findViewById(R.id.view_picker_window_subtitle);
        tvNotification = (TextView)rootView.findViewById(R.id.view_picker_window_notification);

        setProgressBar((ProgressBar)rootView.findViewById(R.id.progressBar));
        listView = (ListView)rootView.findViewById(R.id.view_picker_window_list);

        btnDone = (Button)rootView.findViewById(R.id.view_picker_window_btn_done);
        fabAction = (FloatingActionButton)rootView.findViewById(R.id.view_picker_floating_action_button);
        vgRoot = (ViewGroup)((Activity)context).findViewById(android.R.id.content);
        llDefaultHeader = rootView.findViewById(R.id.view_picker_window_default_header);
        llButtonsHeader = rootView.findViewById(R.id.view_picker_window_buttons_header);
        tvHeader1 = (TextView)rootView.findViewById(R.id.view_picker_window_button_a);
        tvHeader2 = (TextView)rootView.findViewById(R.id.view_picker_window_button_b);

        // left - right padding for that buttons is equal
        buttonPadding = tvHeader1.getPaddingLeft();

        // init layout for dim
        llDim = new LinearLayout(context);
        LayoutParams llDimParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        llDim.setLayoutParams(llDimParams);
        llDim.setBackgroundColor(context.getResources().getColor(R.color.background_dim));

        // attach listener
        btnDone.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        // initialize animations
        backgroundDimAnim = new AnimatorSet();
        actionButtonAnim = new AnimatorSet();

        // animate dim the background
        ObjectAnimator dimBackground = ObjectAnimator.ofFloat(llDim, "alpha", 0f, 1f);
        dimBackground.setDuration(500);
        backgroundDimAnim.play(dimBackground);

        // animate floating action button
        ObjectAnimator fabFadeIn = ObjectAnimator.ofFloat(fabAction, "alpha", 0f, 1f);
        fabFadeIn.setDuration(1000);
        actionButtonAnim.play(fabFadeIn);

        // initials
        fabAction.setVisibility(View.GONE);
        llButtonsHeader.setVisibility(View.GONE);
        llDefaultHeader.setVisibility(View.VISIBLE);

        // list view scroll event for animating floating action button
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if(hasFAB) {
                    switch(scrollState) {
                        case SCROLL_STATE_IDLE:
                            fabAction.setVisibility(View.VISIBLE);
                            actionButtonAnim.start();
                            break;

                        case SCROLL_STATE_FLING:
                        case SCROLL_STATE_TOUCH_SCROLL:
                            fabAction.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        setContentView(rootView);
    }

    // View.OnClickListener
    @Override
    public void onClick(View view) {
        if(view == btnDone) {
            if(listener != null) {
                generateSelectedObjects();

                listener.onPickDone(activeMode, selectedObjects);
            }

            dismiss();
        }
        else if(view == fabAction) {
            if(listener != null)
                listener.onActionButtonClick();
        }
        else if(view == tvHeader1) {
            if(listener != null)
                listener.onButton1Click();
        }
        else if(view == tvHeader2) {
            if(listener != null)
                listener.onButton2Click();
        }
    }

    // AdapterView.OnItemClickListener
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        // reset selection on single mode
        if(activeMode == PICKER_MODE_SINGLE ||
                activeMode == PICKER_MODE_BUTTONS ||
                activeMode == PICKER_MODE_SINGLE_TAP ||
                (activeMode == PICKER_MODE_MULTIPLE && position == 0 && useFirstItemReset))
            for(PickerObject object : objects)
                object.setIsSelected(false);

        if(activeMode == PICKER_MODE_MULTIPLE && position != 0 && useFirstItemReset) {
            PickerObject object = objects.get(0);
            object.setIsSelected(false);
            objects.set(0, object);
        }

        // set the flag
        PickerObject selectedObject = objects.get(position);
        selectedObject.setIsSelected(!selectedObject.isSelected());
        objects.set(position, selectedObject);
        adapter.notifyDataSetChanged();

        // dismiss on single tap mode
        if(activeMode == PICKER_MODE_SINGLE_TAP && listener != null) {
            generateSelectedObjects();

            if(!selectedObjects.isEmpty())
                listener.onPickDone(activeMode, selectedObjects);

            dismiss();
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);

        // dim the background
        vgRoot.addView(llDim);

        // add floating action button
        flushFloatingButton();

        // start the animation
        backgroundDimAnim.start();

        // hide keyboard
        FunctionUtil.hideSoftKeyboard((Activity) getContentView().getContext());
    }

    @Override
    public void onDismiss() {
        // un-dim the screen
        vgRoot.removeView(llDim);

        // remove floating action button
        if(hasFAB) {
            fabAction.setVisibility(View.GONE);
            fabAction.setOnClickListener(null);
        }
    }

    // toggle buttons state
    public void toggleButtonsState(boolean button1, boolean button2) {
        if(button1) {
            tvHeader1.setBackgroundColor(activeColorRes);
            tvHeader2.setBackgroundResource(R.drawable.selector_button_blank_no_corners);
            tvHeader2.setPadding(buttonPadding, 0, buttonPadding, 0);
        }
        else if(button2) {
            tvHeader1.setBackgroundResource(R.drawable.selector_button_blank_no_corners);
            tvHeader1.setPadding(buttonPadding, 0, buttonPadding, 0);
            tvHeader2.setBackgroundColor(activeColorRes);
        }
        else {
            tvHeader1.setBackgroundResource(R.drawable.selector_button_blank_no_corners);
            tvHeader2.setBackgroundResource(R.drawable.selector_button_blank_no_corners);
            tvHeader1.setPadding(buttonPadding, 0, buttonPadding, 0);
            tvHeader2.setPadding(buttonPadding, 0, buttonPadding, 0);
        }
    }

    // wrapper to refresh floating action button
    public void flushFloatingButton() {
        fabAction.setVisibility(hasFAB ? View.VISIBLE : View.GONE);
        fabAction.setOnClickListener(hasFAB ? this : null);
    }

    // set title and subtitle
    public void setTitle(String title, String subtitle) {
        if(title == null || title.isEmpty())
            return;

        if(subtitle == null || subtitle.isEmpty())
            tvSubtitle.setVisibility(View.GONE);
        else
            tvSubtitle.setText(subtitle);

        tvTitle.setText(title);
    }

    // set picker mode
    public void setPickerMode(int pickerMode) {
        activeMode = pickerMode;

        if(activeMode == PICKER_MODE_BUTTONS) {
            llDefaultHeader.setVisibility(View.GONE);
            llButtonsHeader.setVisibility(View.VISIBLE);
        }
        else if(activeMode == PICKER_MODE_SINGLE_TAP)
            btnDone.setVisibility(View.GONE);
    }

    // set data
    public void setListData(ArrayList<PickerObject> pickerObjects) {
        adapter.setData(pickerObjects);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        objects = pickerObjects;    // copy reference
    }

    // set listener
    public void setListener(PickerViewListener listener) {
        this.listener = listener;
    }

    // show loading notifier
    public void toggleLoading(boolean isShow) {
//        pbNotifier.setVisibility(isShow ? View.VISIBLE : View.GONE);
        listView.setVisibility(!isShow ? View.VISIBLE : View.GONE);
    }

    // show floating action button
    public void toggleFloatingActionButton(boolean isShow) {
        hasFAB = isShow;
    }

    // set buttons title, only applies to PICKER_MODE_BUTTONS mode only
    public void setButtonsProperty(String button1, String button2) {
        if(activeMode == PICKER_MODE_BUTTONS) {
            if(button1 != null && !button1.isEmpty()) {
                tvHeader1.setText(button1);
                tvHeader1.setOnClickListener(this);
            }
            else
                tvHeader1.setVisibility(View.GONE);

            if(button2 != null && !button2.isEmpty()) {
                tvHeader2.setText(button2);
                tvHeader2.setOnClickListener(this);
            }
            else
                tvHeader2.setVisibility(View.GONE);
        }
    }

    // show notification message, this will force all content views to be hidden
    public void showNotification(String message) {
        tvNotification.setText(message);

        listView.setVisibility(View.GONE);
//        pbNotifier.setVisibility(View.GONE);
        tvNotification.setVisibility(View.VISIBLE);
    }

    // hide notification message, this will restore all content views to be hidden
    public void hideNotification() {
        listView.setVisibility(View.VISIBLE);
//        pbNotifier.setVisibility(View.GONE);
        tvNotification.setVisibility(View.GONE);
    }

    // reset picker state when first item is selected, only works at multiple mode
    public void setFirstItemAsReset(boolean needToReset) {
        if(activeMode == PICKER_MODE_MULTIPLE)
            useFirstItemReset = needToReset;
    }

    // generate selected objects, pulled from only selected picker object
    private void generateSelectedObjects() {
        if(objects == null)
            return;

        if(!selectedObjects.isEmpty())
            selectedObjects.clear();

        for(PickerObject object : objects)
            if(object.isSelected())
                selectedObjects.add(object);
    }

    //======== public object to store the list
    public static class PickerObject {
        private int extId;
        private int extIndex;
        private int iconDrawable;
        private String title;
        private String subtitle;
        private boolean isSelected;

        public int getExtId() {
            return extId;
        }

        public void setExtId(int extId) {
            this.extId = extId;
        }

        public int getExtIndex() {
            return extIndex;
        }

        public void setExtIndex(int extIndex) {
            this.extIndex = extIndex;
        }

        public int getIconDrawable() {
            return iconDrawable;
        }

        public void setIconDrawable(int iconDrawable) {
            this.iconDrawable = iconDrawable;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }

    //======== adapter to show the list
    private static class PickerAdapter extends BaseAdapter<PickerObject> {
        public PickerAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.view_picker_window_list, parent, false);

                holder = new ViewHolder();
                holder.selectorMark = (ImageView)convertView.findViewById(R.id.list_view_picker_window_mark);
                holder.icon = (ImageView)convertView.findViewById(R.id.list_view_picker_window_icon);
                holder.title = (TextView)convertView.findViewById(R.id.list_view_picker_window_text);
                holder.subtitle = (TextView)convertView.findViewById(R.id.list_view_picker_window_subtext);

                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder)convertView.getTag();

            final PickerObject object = getItem(position);

            // show title
            holder.title.setText(object.getTitle());

            // show or hide subtitle
            if(object.getSubtitle() == null || object.getSubtitle().isEmpty())
                holder.subtitle.setVisibility(View.GONE);
            else
                holder.subtitle.setText(object.getSubtitle());

            // show or hide mark or in icon mode
            if(object.getIconDrawable() == 0) {
                int selectorMarkVisibility = object.isSelected() ? View.VISIBLE : View.INVISIBLE;
                holder.selectorMark.setVisibility(selectorMarkVisibility);
            }
            else {
                holder.selectorMark.setVisibility(View.GONE);
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(object.getIconDrawable());
            }

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).extIndex;
        }

        private static class ViewHolder {
            ImageView selectorMark;
            ImageView icon;
            TextView title;
            TextView subtitle;
        }
    }
}
