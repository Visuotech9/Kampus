package visuotech.com.kampus.attendance.Activities.Faculty;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import visuotech.com.kampus.attendance.Activities.Act_College_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_course_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_department_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_director_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_faculty_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_hod_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_student_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Administrator_Act_home;
import visuotech.com.kampus.attendance.Activities.Director.Act_director_profile2;
import visuotech.com.kampus.attendance.Activities.Director.Director_Act_home;
import visuotech.com.kampus.attendance.MainActivity;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.Model.HOD;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Faculty_Act_home extends AppCompatActivity {
    String user_typee, user_id, organization_id, device_id, dept_id, course_id, dept_name, fac_director_id, fac_hod_id;
    LinearLayout lay1, lay2, lay3, lay4, lay5, lay6, lay_full_prof, lay;
    TextView tv_designation, tv_name, tv_course;
    ImageView iv_image;
    Dialog mDialog;
    String old_pswd, new_pswd, cnfirm_pswd;
    TextView tv_alert;
    ArrayList<Faculty> faculty_list;


    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty__act_home);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Kampus");
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            Drawable drawable= getResources().getDrawable(R.drawable.app_logo);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));
//            newdrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(newdrawable);

        }
     */

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        ImageView iv_toolbar = findViewById(R.id.iv_toolbar);
        lay_full_prof = findViewById(R.id.lay_full_prof);
        tv_toolbar.setText(sessionParam.org_name);
        Picasso.get().load(sessionParam.org_logo).into(iv_toolbar);
//        toolbar.setTitle(sessionParam.org_name);
        setSupportActionBar(toolbar);

        if (!marshMallowPermission.checkPermissionForPhoneState()) {
            marshMallowPermission.requestPermissionForPhoneState();
        } else {
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            }
            device_id = TelephonyMgr.getDeviceId();
        }

        user_typee = sessionParam.user_type;
        user_id = sessionParam.userId;
        organization_id = sessionParam.org_id;

        lay1 = findViewById(R.id.lay1);
        lay2 = findViewById(R.id.lay2);
        lay3 = findViewById(R.id.lay3);
        lay4 = findViewById(R.id.lay4);
        lay5 = findViewById(R.id.lay5);
        lay6 = findViewById(R.id.lay6);
        lay = findViewById(R.id.lay);
        tv_designation = findViewById(R.id.tv_designation);
        tv_course = findViewById(R.id.tv_course);
        tv_name = findViewById(R.id.tv_name);
        iv_image = findViewById(R.id.iv_image);

        faculty_list = new ArrayList<>();


        tv_name.setText(sessionParam.login_name);
        tv_designation.setText("(" + sessionParam.designation + ")");
        Picasso.get().load(sessionParam.user_image).into(iv_image);

        ApigetFaculty();

        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new Dialog(context);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
                mDialog.setContentView(R.layout.full_image);
                mDialog.setCanceledOnTouchOutside(true);
                //dialog layout

                ImageView iv_profile;
                TextView tv_name2, tv_cancel;
//
                iv_profile = mDialog.findViewById(R.id.iv_profile);
                tv_name2 = mDialog.findViewById(R.id.tv_name2);
                tv_cancel = mDialog.findViewById(R.id.tv_cancel);
                Picasso.get().load(sessionParam.user_image).into(iv_profile);
                tv_name2.setText(sessionParam.login_name);
                tv_cancel.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();

                    }
                });
                mDialog.show();

            }
        });
        iv_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new Dialog(context);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
                mDialog.setContentView(R.layout.full_image);
                mDialog.setCanceledOnTouchOutside(true);
                //dialog layout

                ImageView iv_profile;
                TextView tv_name2, tv_cancel;
//
                iv_profile = mDialog.findViewById(R.id.iv_profile);
                tv_name2 = mDialog.findViewById(R.id.tv_name2);
                tv_cancel = mDialog.findViewById(R.id.tv_cancel);
                Picasso.get().load(sessionParam.org_logo).into(iv_profile);
                tv_name2.setText(sessionParam.org_name);
                tv_cancel.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();

                    }
                });
                mDialog.show();

            }
        });
        lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Faculty_Act_home.this, Act_faculty_profile2.class);
                startActivity(intent);
                finish();
            }
        });


        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Faculty_Act_home.this, Act_student_list4.class);
                i.putExtra("DEPT_NAME", dept_name);
                startActivity(i);
                finish();

            }
        });
        lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Faculty_Act_home.this, Act_assignment_list.class);
                startActivity(i);
                finish();
            }
        });

        lay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Faculty_Act_home.this, Act_study_list.class);
                startActivity(i);
                finish();

            }
        });

        lay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Faculty_Act_home.this, Act_hod_list.class);
//                startActivity(i);
//                finish();
            }
        });
        lay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Faculty_Act_home.this, Act_faculty_list.class);
//                startActivity(i);
//                finish();
            }
        });
        lay6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Faculty_Act_home.this, Act_student_list.class);
//                startActivity(i);
//                finish();
            }
        });

    }

    private void ApigetFaculty() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    faculty_list = baseRequest.getDataListreverse(jsonArray, Faculty.class);
                    dept_name = faculty_list.get(0).getFaculty_department_name();
                    dept_id = faculty_list.get(0).getFaculty_department_id();
                    course_id = faculty_list.get(0).getFaculty_course_id();
                    fac_director_id = faculty_list.get(0).getFaculty_director_id();
                    fac_hod_id = faculty_list.get(0).getFaculty_hod_id();
                    sessionParam.dept_id(context, dept_id);
                    sessionParam.course_id(context, course_id);
                    sessionParam.director_id(context, fac_director_id);
                    sessionParam.hod_id(context, fac_hod_id);
                    tv_course.setText(dept_name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });
        String remainingUrl2 = "/Kampus/Api2.php?apicall=faculty_list&organization_id=" + sessionParam.org_id + "&user_id=" + sessionParam.userId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_feedback:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_rate:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_chng_pswd:
                mDialog = new Dialog(context);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
                mDialog.setContentView(R.layout.change_password);
                mDialog.setCanceledOnTouchOutside(false);
                //dialog layout

                ImageView iv_cancel_dialog;
                Button btn_save_password;
//                 TextView tv_alert;
                final EditText et_old_password, et_new_password, et_cnfrm_password;
//
                iv_cancel_dialog = mDialog.findViewById(R.id.iv_cancel_dialog);
                tv_alert = mDialog.findViewById(R.id.tv_alert);
                et_old_password = mDialog.findViewById(R.id.et_old_password);
                et_new_password = mDialog.findViewById(R.id.et_new_password);
                et_cnfrm_password = mDialog.findViewById(R.id.et_cnfrm_password);
                btn_save_password = mDialog.findViewById(R.id.btn_save_password);
                iv_cancel_dialog.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();

                    }
                });
                btn_save_password.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (et_old_password.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please enter old password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (et_new_password.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please enter new password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (et_cnfrm_password.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please re-enter password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!et_new_password.getText().toString().equals(et_cnfrm_password.getText().toString())) {
                            tv_alert.setVisibility(View.VISIBLE);
                            tv_alert.setTextColor(getResources().getColor(R.color.DarkRed));
                            tv_alert.setText("Password and confirm password doesn't match!!");
                        } else {
                            old_pswd = et_old_password.getText().toString();
                            new_pswd = et_new_password.getText().toString();
                            cnfirm_pswd = et_cnfrm_password.getText().toString();
                            changePassword();


                            et_old_password.setText("");
                            et_new_password.setText("");
                            et_cnfrm_password.setText("");
                        }


                    }
                });
                mDialog.show();
                break;

            case R.id.menu_logout:

                alertDialogLogout();

                break;

            case R.id.menu_notification:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                break;


        }
        return true;
    }

    @Override
    public void onBackPressed() {
        alertDialogExit();
    }


    public void logout() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {

                sessionParam.clearPreferences(context);
                Intent intent = new Intent(Faculty_Act_home.this, Act_College_list.class);
                startActivity(intent);
                finish();

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


        baseRequest.callAPILogout(1, "https://collectorexpress.in/", user_type_, device_id_, user_id_, organization_id_);

    }

    public void changePassword() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
//                mDialog.cancel();
                tv_alert.setVisibility(View.VISIBLE);
                tv_alert.setTextColor(getResources().getColor(R.color.Green));
                tv_alert.setText("your password changed sucessfully!!");//                sessionParam.clearPreferences(context);
//                Intent intent=new Intent(Administrator_Act_home.this,Act_College_list.class);
//                startActivity(intent);
//                finish();

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
        RequestBody old_pswd_ = RequestBody.create(MediaType.parse("text/plain"), old_pswd);
        RequestBody new_pswd_ = RequestBody.create(MediaType.parse("text/plain"), new_pswd);
        RequestBody cnfirm_pswd_ = RequestBody.create(MediaType.parse("text/plain"), cnfirm_pswd);
        RequestBody user_id_ = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody organization_id_ = RequestBody.create(MediaType.parse("text/plain"), organization_id);


        baseRequest.callAPIChangepswd(1, "https://collectorexpress.in/", user_type_, old_pswd_, new_pswd_, cnfirm_pswd_, user_id_, organization_id_);

    }


    public void alertDialogLogout() {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    public void alertDialogExit() {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }
}
