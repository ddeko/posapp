package mamabe.posappandroid.Base;

import android.view.View;

/**
 * Created by dedeeko on 7/1/15.
 */
public interface FragmentInterface {
    public void initView(View view);
    public void setUICallbacks();
    public void updateUI();
    public String getPageTitle();
    public int getFragmentLayout();
    public String getTag();
}
