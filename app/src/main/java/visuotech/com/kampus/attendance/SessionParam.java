package visuotech.com.kampus.attendance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

/*import com.facebook.AccessToken;
import com.facebook.login.LoginManager;*/


public class SessionParam {
    String PREF_NAME = "MyPref";
    Context _context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefsEditor;





    public String signupStage = "0", loginSession = "n";

    public String mob_no,id,logo, email,address,designation,longitude,date, officeId, departmentId, orgId, office, department, token, organisation_logo, role;
    public String companyList = "";
    public String companyOrgId = "";
    public String userId = "",login="",dept_id,course_id,deviceId="",hod_id,director_id,org_logo,buy,mobile, login_name,org_name,logged_in="",data="",user_type="",org_id="",user_image="",website="";


    public SessionParam(Context context, String signupStage) {
        this.signupStage = signupStage;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("signupStage", signupStage);
        prefsEditor.commit();
    }

    @SuppressLint("LongLogTag")
    public SessionParam(Context context, JSONObject jsonObject) {
         if (jsonObject !=null){
             userId = String.valueOf(jsonObject.optInt("user_id"));
             org_logo = (jsonObject.optString("logo"));
             org_id = String.valueOf(jsonObject.optInt("organization_id"));
             login_name = jsonObject.optString("login_name");
             org_name = jsonObject.optString("organization_name");
             mobile = jsonObject.optString("mobile_no");
             email = jsonObject.optString("email_id");
             user_image = jsonObject.optString("login_pic");
             designation = jsonObject.optString("designation");
             date = jsonObject.optString("date");
             longitude = jsonObject.optString("longitude");
              address = jsonObject.optString("address");
             user_type = jsonObject.optString("login_user_type");

             Log.d("ID SESSION",userId);
             Log.d("NAME SESSION", login_name);
             Log.d("EMAIL SESSION",email);
             Log.d("DATE IN SESSION:",date);
             Log.d("LATITUDE IN SESSION:",designation);
             Log.d("LONGITUDE IN SESSION:",longitude);
             Log.d("ADDRESS IN SESSION:",address);
             Log.d("USERTYPE IN SESSION:",user_type);
             userId(context,userId);
             org_name(context,org_name);
             designation(context,designation);
             org_id(context,org_id);
             org_logo(context,org_logo);
             userType(context,user_type);
             user_image(context,user_image);
             emailId(context,email);
             mobileNo(context,mobile);
             name(context, login_name);
         }
    }




    public SessionParam(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.userId = sharedPreferences.getString("userId", "");
        this.org_name = sharedPreferences.getString("org_name", "");
        this.mobile = sharedPreferences.getString("mob", "");
        this.login_name = sharedPreferences.getString("name", "");
        this.login = sharedPreferences.getString("login", "");
        this.buy = sharedPreferences.getString("buy", "");
        this.deviceId = sharedPreferences.getString("deviceId", "");
        this.user_type = sharedPreferences.getString("user_type", "");
        this.logged_in = sharedPreferences.getString("logged_in", "");
        this.data = sharedPreferences.getString("data", "");
        this.org_id = sharedPreferences.getString("org_id", "");
        this.org_logo = sharedPreferences.getString("org_logo", "");
        this.user_image = sharedPreferences.getString("user_image", "");
        this.designation = sharedPreferences.getString("designation", "");
        this.email = sharedPreferences.getString("email", "");
        this.course_id = sharedPreferences.getString("course_id", "");
        this.dept_id = sharedPreferences.getString("dept_id", "");
        this.director_id = sharedPreferences.getString("director_id", "");
        this.hod_id = sharedPreferences.getString("hod_id", "");

    }


    public void org_name(Context context, String org_name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("org_name", org_name);
        prefsEditor.commit();
    }
    public void director_id(Context context, String director_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("director_id", director_id);
        prefsEditor.commit();
    }
    public void hod_id(Context context, String hod_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("hod_id", hod_id);
        prefsEditor.commit();
    }

    public void course_id(Context context, String course_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("course_id", course_id);
        prefsEditor.commit();
    }
    public void dept_id(Context context, String dept_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("dept_id", dept_id);
        prefsEditor.commit();
    }

    public void userId(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("userId", userId);
        prefsEditor.commit();
    }
    public void mobileNo(Context context, String mobile) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("mob", mobile);
        prefsEditor.commit();
    }

    public void name(Context context, String login_name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("name", login_name);
        prefsEditor.commit();
    }

    public void emailId(Context context, String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("email", email);
        prefsEditor.commit();
    }

    public void designation(Context context, String designation) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("designation", designation);
        prefsEditor.commit();
    }


    public void user_image(Context context, String user_image) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("user_image", user_image);
        prefsEditor.commit();
    }


    public void org_id(Context context, String org_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("org_id", org_id);
        prefsEditor.commit();
    }

    public void org_logo(Context context, String org_logo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("org_logo", org_logo);
        prefsEditor.commit();
    }



    public void deviceId(Context context, String device_id) {
        Log.d("DeviceId Session",device_id);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("deviceId", device_id);
        prefsEditor.commit();
    }

    public void userType(Context context, String user_type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("user_type", user_type);
        prefsEditor.commit();
    }

    public void loginSession(Context context, String logged_in) {
        Log.d("logged_in Session ",logged_in);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("logged_in", logged_in);
        prefsEditor.commit();
    }
    public void userSaveLocation(Context context, String data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("data", data);
        prefsEditor.commit();
    }

    public void clearPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        /*AccessToken.setCurrentAccessToken(null);
        LoginManager.getInstance().logOut();*/
        prefsEditor.clear();
        prefsEditor.commit();
    }





    @Override
    public String toString() {
        return "SessionParam [name=" + "]";
    }


    public void loginSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("login", "yes");
        prefsEditor.commit();
    }

    public void buyingStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("buy", "yes");
        prefsEditor.commit();
    }




}
