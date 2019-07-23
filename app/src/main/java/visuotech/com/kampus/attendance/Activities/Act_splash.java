package visuotech.com.kampus.attendance.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import visuotech.com.kampus.attendance.Activities.Administrator.Administrator_Act_home;
import visuotech.com.kampus.attendance.Activities.Director.Director_Act_home;
import visuotech.com.kampus.attendance.Activities.Faculty.Faculty_Act_home;
import visuotech.com.kampus.attendance.Activities.Hod.HOD_Act_home;
import visuotech.com.kampus.attendance.Activities.Student.Student_Act_home;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.NetworkAddress;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;
import visuotech.com.kampus.attendance.retrofit.Utility;

import static visuotech.com.kampus.attendance.MarshMallowPermission.READ_PHONE_STATE;
import static visuotech.com.kampus.attendance.retrofit.WebServiceConstants.BASE_URL;

public class Act_splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    SessionParam sessionParam;
    Context context;
    MarshMallowPermission marshMallowPermission;
    public String id, device_id;
    Activity activity;
    private BaseRequest baseRequest;
    String splash  ="Yes";
    String other_device_active,user_typee,organization_id,user_id,login_status,payment_status,account_status;
    RelativeLayout lin_spl_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_splash);

        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);
        context = this;
        user_typee= sessionParam.user_type;
        user_id= sessionParam.userId;
        organization_id=sessionParam.org_id;

        lin_spl_layout=findViewById(R.id.lin_spl_layout);
        permissionPhone();

       NetworkAddress.getInterfaces();


    }
    private void permissionPhone(){
        if (!marshMallowPermission.checkPermissionForPhoneState()) {
            marshMallowPermission.requestPermissionForPhoneState();
        } else {
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            }
            device_id = TelephonyMgr.getDeviceId();

//              if (Utility.getAPIVerison()>=24){
//
//              }
            Log.d("API LEVEL>>>>>", String.valueOf(android.os.Build.VERSION.SDK_INT));
            Log.d("API LEVEL LOLISPOP>>>>>", String.valueOf(Build.VERSION_CODES.N));
            startTimer();
        }

    }
    private void startTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!user_id.isEmpty() && !user_typee.isEmpty() && !organization_id.isEmpty()) {

                    if (NetworkConnection.checkNetworkStatus(context) == true) {
                        api_loginStatus();

                    } else {
                        Snackbar.make(lin_spl_layout, "No internet connection", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Intent i = new Intent(Act_splash.this, Act_Login.class);
                    startActivity(i);
                    finish();
                }
            }

        }, SPLASH_TIME_OUT);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTimer();
                }
                break;
        }
    }

    public void api_loginStatus() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {


                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    if (jsonObject.getJSONObject("user")!=null){
                        JSONObject jsonObject1=jsonObject.getJSONObject("user");
                        other_device_active=jsonObject1.getString("other_device_active");
                        login_status=jsonObject1.getString("login_status");
                        payment_status=jsonObject1.getString("payment_status");
                        account_status=jsonObject1.getString("account_status");
                    }

                     if (account_status.equals("Active")&&payment_status.equals("Active")){
                        if (login_status.equals("Active")){
                            if (other_device_active.equals("No")){
                                if (sessionParam.login.equals("yes")) {
                                    if (user_typee.equals("Administrator")) {
                                        Intent i = new Intent(Act_splash.this, Administrator_Act_home.class);
                                        startActivity(i);
                                        finish();
                                    } if (user_typee.equals("Director")) {
                                        Intent i = new Intent(Act_splash.this, Director_Act_home.class);
                                        startActivity(i);
                                        finish();
                                    } if (user_typee.equals("HOD")) {
                                        Intent i = new Intent(Act_splash.this, HOD_Act_home.class);
                                        startActivity(i);
                                        finish();
                                    } if (user_typee.equals("Faculty")) {
                                        Intent i = new Intent(Act_splash.this, Faculty_Act_home.class);
                                        startActivity(i);
                                        finish();
                                    } if (user_typee.equals("Student")) {
                                        Intent i = new Intent(Act_splash.this, Student_Act_home.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }else {
                                sessionParam.clearPreferences(context);
                                Intent i = new Intent(Act_splash.this, Act_College_list.class);
                                startActivity(i);
                                finish();
                            }
                        }else{
                            Intent i = new Intent(Act_splash.this, Act_College_list.class);
                            startActivity(i);
                            finish();
                        }
                     }else{
                         String sucessMessage="Your account is suspended";
                         Utility.sucessDialog(sucessMessage,context);

                     }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            }
        });
        RequestBody user_type_ = RequestBody.create(MediaType.parse("text/plain"), user_typee);
        RequestBody device_id_ = RequestBody.create(MediaType.parse("text/plain"), device_id);
        RequestBody user_id_ = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody organization_id_ = RequestBody.create(MediaType.parse("text/plain"), organization_id);


        baseRequest.callAPILoginStatus(1,BASE_URL,user_type_,device_id_,user_id_,organization_id_);

    }


}
/*

 */