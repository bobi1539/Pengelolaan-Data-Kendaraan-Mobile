package zero.programmer.data.kendaraan.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

import zero.programmer.data.kendaraan.utils.RoleId;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor sharedPreferencesEditor;
    Context context;

    private static final String IS_LOGIN = "isLoggedIn";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULL_NAME = "fullName";
    public static final String KEY_EMPLOYEE_NUMBER = "employeeNumber";
    public static final String KEY_POSITION = "position";
    public static final String KEY_WORK_UNIT = "workUnit";
    public static final String KEY_ROLE_ID = "roleId";

    public SessionManager(Context context) {
        this.context = context;
        userSession = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferencesEditor = userSession.edit();
    }

    public void createLoginSession(
            String username, String fullName, String employeeNumber,
            String position, String workUnit, RoleId roleId
    ){

        sharedPreferencesEditor.putBoolean(IS_LOGIN, true);
        sharedPreferencesEditor.putString(KEY_USERNAME, username);
        sharedPreferencesEditor.putString(KEY_FULL_NAME, fullName);
        sharedPreferencesEditor.putString(KEY_EMPLOYEE_NUMBER, employeeNumber);
        sharedPreferencesEditor.putString(KEY_POSITION, position);
        sharedPreferencesEditor.putString(KEY_WORK_UNIT, workUnit);
        sharedPreferencesEditor.putString(KEY_ROLE_ID, String.valueOf(roleId));

        sharedPreferencesEditor.commit();
    }

    public Map<String, String> getUserSessionDetail(){
        Map<String, String> userDetail = new HashMap<>();

        userDetail.put(KEY_USERNAME, userSession.getString(KEY_USERNAME, null));
        userDetail.put(KEY_FULL_NAME, userSession.getString(KEY_FULL_NAME, null));
        userDetail.put(KEY_EMPLOYEE_NUMBER, userSession.getString(KEY_EMPLOYEE_NUMBER, null));
        userDetail.put(KEY_POSITION, userSession.getString(KEY_POSITION, null));
        userDetail.put(KEY_WORK_UNIT, userSession.getString(KEY_WORK_UNIT, null));
        userDetail.put(KEY_ROLE_ID, userSession.getString(KEY_ROLE_ID, null));

        return userDetail;
    }

    public boolean isLogin(){
        return userSession.getBoolean(IS_LOGIN, false);
    }

    public void createLogoutSession(){
        sharedPreferencesEditor.clear();
        sharedPreferencesEditor.commit();
    }

    public String getRoleId(){
        return userSession.getString(KEY_ROLE_ID, null);
    }
}
