package visuotech.com.kampus.attendance.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import visuotech.com.kampus.attendance.Activities.Administrator.Administrator_Act_home;
import visuotech.com.kampus.attendance.Activities.Director.Director_Act_home;
import visuotech.com.kampus.attendance.Activities.Faculty.Faculty_Act_home;
import visuotech.com.kampus.attendance.Activities.Hod.HOD_Act_home;
import visuotech.com.kampus.attendance.Activities.Student.Student_Act_home;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;
import visuotech.com.kampus.attendance.retrofit.Utility;

import static visuotech.com.kampus.attendance.retrofit.WebServiceConstants.BASE_URL;

public class Act_Login extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public boolean datafinish = false;
    Button btn_login, btn_reg;
    CircleImageView iv_profile_image;
    TextView tv_org_name;
    EditText et_username;
    Context context;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    Activity activity;
    String splash = "No";
    String other_device_active, user_typee, organization_id, user_id, logo, org_id, org_name;
    String[] user_type = {"Administrator", "Director", "HOD", "Faculty", "Student"};
    ShowHidePasswordEditText et_password;
    EditText et_password2;
    private String username, password, device_id;
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smdnvfsdnfcd);
        init();
        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        permission();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_username.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter Password", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(Act_Login.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    device_id = TelephonyMgr.getDeviceId();


                    username = et_username.getText().toString();
                    password = et_password.getText().toString();
                    if (NetworkConnection.checkNetworkStatus(context) == true) {
                        api_login();
                        et_username.setText("");
                        et_password.setText("");

                    } else {
                        Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    private void permission() {

        datafinish = true;
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
//        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
//            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_NETWORK_STATE))
            permissionsNeeded.add("Network state");
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Location");
        if (!addPermission(permissionsList, Manifest.permission.INTERNET))
            permissionsNeeded.add("Internet");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write storage");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read storage");


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        }
                    }
                });
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }

        init();

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Act_Login.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    // All Permissions Granted
                    init();
                }
//                else {
//                    AlertDialog.Builder ad = new AlertDialog.Builder(Act_Login.this);
//                    ad.setMessage("Some Permission is Denied,You could not able to access this App!Please allow for all permission").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            permission();
//                        }
//                    }).setCancelable(false).create().show();
//                    // Permission Denied
//                    //  Toast.makeText(HomeActivity.this, "Some Permission is Denied,You could not able to access some functionalities of app.", Toast.LENGTH_LONG).show();
//                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }


    private void init() {
        btn_login = findViewById(R.id.btn_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);


    }

    private void api_login() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                sessionParam.loginSession(context);
                Toast.makeText(getApplicationContext(), "Login sucessfully", Toast.LENGTH_SHORT).show();
              /*  try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                    SessionParam sessionParam = new SessionParam(context, jsonObject1);
                    user_typee = jsonObject1.getString("login_user_type");
                    user_id = String.valueOf(jsonObject1.optInt("user_id"));
                    organization_id = String.valueOf(jsonObject1.optInt("organization_id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (user_typee.equals("Administrator")) {
                    Intent i = new Intent(Act_Login.this, Administrator_Act_home.class);
                    startActivity(i);
                    finish();
                }
                if (user_typee.equals("Director")) {
                    Intent i = new Intent(Act_Login.this, Director_Act_home.class);
                    startActivity(i);
                    finish();
                }
                if (user_typee.equals("HOD")) {
                    Intent i = new Intent(Act_Login.this, HOD_Act_home.class);
                    startActivity(i);
                    finish();
                }
                if (user_typee.equals("Faculty")) {
                    Intent i = new Intent(Act_Login.this, Faculty_Act_home.class);
                    startActivity(i);
                    finish();
                }
                if (user_typee.equals("Student")) {
                    Intent i = new Intent(Act_Login.this, Student_Act_home.class);
                    startActivity(i);
                    finish();
                }
*/
//                api_loginStatus();


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
//        RequestBody user_type_ = RequestBody.create(MediaType.parse("text/plain"), "Administrator");
        RequestBody device_id_ = RequestBody.create(MediaType.parse("text/plain"), device_id);
        RequestBody email_ = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody password_ = RequestBody.create(MediaType.parse("text/plain"), password);
//        RequestBody org_id_ = RequestBody.create(MediaType.parse("text/plain"), "1");

        baseRequest.callAPILogin(1, BASE_URL, device_id_, email_, password_);

    }


}
