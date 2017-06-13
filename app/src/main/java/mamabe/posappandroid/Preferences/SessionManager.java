package mamabe.posappandroid.Preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import mamabe.posappandroid.Activities.LoginActivity;
import mamabe.posappandroid.Models.Employee;
import mamabe.posappandroid.Models.Setting;


public class SessionManager {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "PosAppPref";

    // All Shared Preferences Keys

    //START EMP-DATA
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMPID = "emp_id";
    public static final String KEY_EMPNAME = "emp_name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMPSTATUS = "emp_status";
    public static final String KEY_ROLEID = "role_id";
    public static final String KEY_ROLENAME = "role_name";
    //END USER-DATA

    //SETTING DATA
    public static final String KEY_TABLES = "tables";
    public static final String KEY_TAX = "tax";
    public static final String KEY_SERVICE = "service";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(Employee user) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing  in pref
        editor.putString(KEY_EMPID, user.getEmpId());
        editor.putString(KEY_EMPNAME, user.getEmpName());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_ADDRESS, user.getAddress());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_EMPSTATUS, user.getEmpStatus());
        editor.putString(KEY_ROLEID, user.getRoleId());
        editor.putString(KEY_ROLENAME, user.getRoleName());

        // commit changes
        editor.commit();
    }

    public void setSetting(Setting setting){
        editor.putString(KEY_TABLES, setting.getTables());
        editor.putString(KEY_TAX, setting.getTax());
        editor.putString(KEY_SERVICE, setting.getService());

        editor.commit();
    }

//    public void setCurrentToken(String Token)
//    {
//        editor.putString(KEY_TOKEN, Token);
//
//        editor.commit();
//    }
//
//    public String getCurrentToken()
//    {
//        return pref.getString(KEY_TOKEN, null);
//    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        // user data
        user.put(KEY_EMPID, pref.getString(KEY_EMPID, null));
        user.put(KEY_EMPNAME, pref.getString(KEY_EMPNAME, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
        user.put(KEY_EMPSTATUS, pref.getString(KEY_EMPSTATUS, null));
        user.put(KEY_ROLEID, pref.getString(KEY_ROLEID, null));
        user.put(KEY_ROLENAME, pref.getString(KEY_ROLENAME, null));

        return user;
    }

    public HashMap<String, String> getSettings() {
        HashMap<String, String> setting = new HashMap<String, String>();

        // user data
        setting.put(KEY_TABLES, pref.getString(KEY_TABLES, null));
        setting.put(KEY_TAX, pref.getString(KEY_TAX, null));
        setting.put(KEY_SERVICE, pref.getString(KEY_SERVICE, null));

        return setting;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


}
