package visuotech.com.kampus.attendance.Activities.Student;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import visuotech.com.kampus.attendance.Activities.Act_College_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_course_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_department_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_director_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_faculty_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_hod_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Administrator_Act_home;
import visuotech.com.kampus.attendance.Activities.Director.Director_Act_home;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;
import visuotech.com.kampus.attendance.retrofit.Utility;

public class Student_Act_home extends AppCompatActivity {
   TextView tv_full_profile,tv_attendence,tv_name,tv_branch,tv_sem;
   ImageView iv_profile;
    LinearLayout lay1,lay2,lay3,lay4,lay5,lay6,lay7,lay8,lay9,lay_full_prof;

    int attednce_percentage=76;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;

    String user_typee,user_id,organization_id,device_id;
    Dialog mDialog;
    TextView tv_alert;
    String old_pswd,new_pswd,cnfirm_pswd;

    static Dialog d ;
    int year = Calendar.getInstance().get(Calendar.YEAR);


    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__act_home);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tv_toolbar=findViewById(R.id.tv_toolbar);
        ImageView iv_toolbar=findViewById(R.id.iv_toolbar);
        tv_toolbar.setText(sessionParam.org_name);
        Picasso.get().load(sessionParam.org_logo).into(iv_toolbar);
        setSupportActionBar(toolbar);


/*
        Picasso.get()
                .load(sessionParam.org_logo)
                .into(new com.squareup.picasso.Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        if (Utility.getAPIVerison()){
                            d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 120, 120, true));
                        }else {
                            d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 80, 80, true));
                        }
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeAsUpIndicator(d);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
*/

        if (!marshMallowPermission.checkPermissionForPhoneState()) {
            marshMallowPermission.requestPermissionForPhoneState();
        } else {
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            }
            device_id = TelephonyMgr.getDeviceId();
        }

        user_typee= sessionParam.user_type;
        user_id= sessionParam.userId;
        organization_id=sessionParam.org_id;

        tv_full_profile=findViewById(R.id.tv_full_profile);
        tv_attendence=findViewById(R.id.tv_attendence);
        iv_profile=findViewById(R.id.iv_profile);
        tv_name=findViewById(R.id.tv_name);
        lay1=findViewById(R.id.lay1);
        lay2=findViewById(R.id.lay2);
        lay3=findViewById(R.id.lay3);
        lay4=findViewById(R.id.lay4);
        lay5=findViewById(R.id.lay5);
        lay6=findViewById(R.id.lay6);
        lay7=findViewById(R.id.lay7);
        lay8=findViewById(R.id.lay8);
        lay9=findViewById(R.id.lay9);
        lay_full_prof=findViewById(R.id.lay_full_prof);



        tv_name.setText(sessionParam.login_name);
        Picasso.get().load(sessionParam.user_image).into(iv_profile);

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog=new Dialog(context);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
                mDialog.setContentView(R.layout.full_image);
                mDialog.setCanceledOnTouchOutside(true);
                //dialog layout

                ImageView iv_profile;
                TextView tv_name2,tv_cancel;
//
                iv_profile=mDialog.findViewById(R.id.iv_profile);
                tv_name2=mDialog.findViewById(R.id.tv_name2);
                tv_cancel= mDialog.findViewById(R.id.tv_cancel);
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
                mDialog=new Dialog(context);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
                mDialog.setContentView(R.layout.full_image);
                mDialog.setCanceledOnTouchOutside(true);
                //dialog layout

                ImageView iv_profile;
                TextView tv_name2,tv_cancel;
//
                iv_profile=mDialog.findViewById(R.id.iv_profile);
                tv_name2=mDialog.findViewById(R.id.tv_name2);
                tv_cancel= mDialog.findViewById(R.id.tv_cancel);
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


        if (attednce_percentage >= 75){
            String attendence= String.valueOf(attednce_percentage);
            tv_attendence.setText(attendence+"%");
            tv_attendence.setTextColor(Color.parseColor("#008000"));
        }else{
            String attendence= String.valueOf(attednce_percentage);
            tv_attendence.setText(attendence+"%");
            tv_attendence.setTextColor(Color.parseColor("#8B0000"));

        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        lay_full_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Student_Act_home.this,Student_Act_profile.class);
                startActivity(intent);
            }
        });


        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_Act_home.this, Student_Act_profile.class);
                startActivity(i);
                finish();

            }
        });
        lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_Act_home.this, Student_Act_profile.class);
                startActivity(i);


                finish();
            }
        });

        lay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_Act_home.this, Student_Act_profile.class);
                startActivity(i);
                finish();

            }
        });

        lay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_Act_home.this, Student_Act_profile.class);
                startActivity(i);
                finish();
            }
        });
        lay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_Act_home.this, Student_Act_profile.class);
                startActivity(i);
                finish();
            }
        });
        lay6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_Act_home.this, Student_Act_profile.class);
                startActivity(i);
                finish();
            }
        });
        lay7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_Act_home.this, Student_Act_profile.class);
                startActivity(i);
                finish();
            }
        });

        lay8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_Act_home.this, Student_Act_profile.class);
                startActivity(i);
                finish();
            }
        });
        lay9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_Act_home.this, Student_Act_profile.class);
                startActivity(i);
                finish();
            }
        });



        //--------------horizontal scroll with dot layout----------//
/*

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);

       layouts = new int[]{
                R.layout.slide1,
                R.layout.slide2};

                  addBottomDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
*/


    }

/*
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_feedback:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_rate:
                break;

            case R.id.menu_chng_pswd:
                mDialog=new Dialog(context);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
                mDialog.setContentView(R.layout.change_password);
                mDialog.setCanceledOnTouchOutside(false);
                //dialog layout

                ImageView iv_cancel_dialog;
                Button btn_save_password;
//                 TextView tv_alert;
                final EditText et_old_password,et_new_password,et_cnfrm_password;
//
                iv_cancel_dialog=mDialog.findViewById(R.id.iv_cancel_dialog);
                tv_alert=mDialog.findViewById(R.id.tv_alert);
                et_old_password= mDialog.findViewById(R.id.et_old_password);
                et_new_password= mDialog.findViewById(R.id.et_new_password);
                et_cnfrm_password= mDialog.findViewById(R.id.et_cnfrm_password);
                btn_save_password= mDialog.findViewById(R.id.btn_save_password);
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
                        if (!et_new_password.getText().toString().equals(et_cnfrm_password.getText().toString())){
                            tv_alert.setVisibility(View.VISIBLE);
                            tv_alert.setTextColor(getResources().getColor(R.color.DarkRed));
                            tv_alert.setText("Password and confirm password doesn't match!!");
                        }else{
                            old_pswd=et_old_password.getText().toString();
                            new_pswd=et_new_password.getText().toString();
                            cnfirm_pswd=et_cnfrm_password.getText().toString();
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


        baseRequest.callAPIChangepswd(1,"http://collectorexpress.in/",user_type_,old_pswd_,new_pswd_,cnfirm_pswd_,user_id_,organization_id_);

    }


    @Override
    public void onBackPressed() {
        alertDialogExit();
    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            LinearLayout lay1=view.findViewById(R.id.lay1);
            LinearLayout lay2=view.findViewById(R.id.lay2);
            LinearLayout lay3=view.findViewById(R.id.lay3);
            LinearLayout lay4=view.findViewById(R.id.lay4);
            LinearLayout lay5=view.findViewById(R.id.lay5);
            LinearLayout lay6=view.findViewById(R.id.lay6);
            LinearLayout lay7=view.findViewById(R.id.lay7);
            LinearLayout lay8=view.findViewById(R.id.lay8);


            lay1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Student_Act_home.this, Director_Act_home.class);
                    startActivity(i);
                }
            });
            lay2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Student_Act_home.this, Director_Act_home.class);
                    startActivity(i);
                }
            });
            lay3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Student_Act_home.this, Director_Act_home.class);
                    startActivity(i);
                }
            });
            lay4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Student_Act_home.this, Director_Act_home.class);
                    startActivity(i);
                }
            });
            lay5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Student_Act_home.this, Director_Act_home.class);
                    startActivity(i);
                }
            });
            lay6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Student_Act_home.this, Director_Act_home.class);
                    startActivity(i);
                }
            });
            lay7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Student_Act_home.this, Director_Act_home.class);
                    startActivity(i);
                }
            });
            lay8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Student_Act_home.this, Director_Act_home.class);
                    startActivity(i);
                }
            });



            return view;
        }


        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


    public void logout() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {

                sessionParam.clearPreferences(context);
                Intent intent=new Intent(Student_Act_home.this,Act_College_list.class);
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


        baseRequest.callAPILogout(1,"http://collectorexpress.in/",user_type_,device_id_,user_id_,organization_id_);

    }

    public void alertDialogLogout(){
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    public void alertDialogExit(){
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2014, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
        }
        return dpd;
    }

/*
    public void showYearDialog()
    {

        final Dialog d = new Dialog(Student_Act_home.this);
        d.setTitle("Year Picker");
        d.setContentView(R.layout.yeardialog);
        LinearLayout lay_set =  d.findViewById(R.id.lay_set);
        TextView year_text=(TextView)d.findViewById(R.id.year_text);
        year_text.setText(""+year);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year+50);
        nopicker.setMinValue(year-50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        lay_set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }
*/

}
