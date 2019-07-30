package visuotech.com.kampus.attendance.Activities.Administrator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import visuotech.com.kampus.attendance.Adapter.Ad_cou_dept;
import visuotech.com.kampus.attendance.Adapter.Ad_course;
import visuotech.com.kampus.attendance.Adapter.Ad_department;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Department;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

import static visuotech.com.kampus.attendance.Constants.COURSE_LIST;
import static visuotech.com.kampus.attendance.Constants.DEPT_LIST;
import static visuotech.com.kampus.attendance.retrofit.WebServiceConstants.BASE_URL;

public class Act_department_list extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Ad_cou_dept adapter;
    Ad_department adapter2;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;
    Spinner spinner_course;

    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    EditText inputSearch;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Department> department_list;
    ArrayList<String> department_name_list = new ArrayList<>();
    ArrayList<Course> course_list;
    ArrayList<String> course_name_list = new ArrayList<>();
    ArrayList<Course> course_list2;
    ArrayList<String> course_name_list2 = new ArrayList<>();
    ImageView iv_add;
    Dialog mDialog;
    TextView tv_alert;
    String dept_name, course, cour_id;
    LinearLayout container;
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_basic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Departments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_dept_list, null);
        container.addView(rowView, container.getChildCount());
//-------------------------recyclerview------------------------------------------
        rv_list = rowView.findViewById(R.id.rv_list);
        progressbar = rowView.findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

        adapter2 = new Ad_department(department_list, context);
        inputSearch = rowView.findViewById(R.id.inputSearch);
        iv_add = rowView.findViewById(R.id.iv_add);


        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new Dialog(context);

                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
                mDialog.setContentView(R.layout.add_department);
                mDialog.setCanceledOnTouchOutside(false);
                //dialog layout

                ImageView iv_cancel_dialog;
                Button btn_save;

                final EditText et_name;
//
                iv_cancel_dialog = mDialog.findViewById(R.id.iv_cancel_dialog);
                tv_alert = mDialog.findViewById(R.id.tv_alert);
                spinner_course = mDialog.findViewById(R.id.spinner_course);
                ApigetCourseList();

                et_name = mDialog.findViewById(R.id.et_name);
                btn_save = mDialog.findViewById(R.id.btn_save);
                spinner_course.setOnItemSelectedListener(Act_department_list.this);


                iv_cancel_dialog.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                        ApigetCourse();


                    }
                });
                btn_save.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (et_name.getText().toString().isEmpty()) {
                            tv_alert.setVisibility(View.VISIBLE);
                            tv_alert.setTextColor(getResources().getColor(R.color.DarkRed));
                            tv_alert.setText("please enter course name!!");
                        } else {
                            dept_name = et_name.getText().toString();
                            apiAdddepartment();
                            et_name.setText("");
                        }


                    }
                });
                mDialog.show();
            }
        });


        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

        mSwipeRefreshLayout = rowView.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
                    callApi();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    sucessDialog(getResources().getString(R.string.Internet_connection), context);
                }

            }
        });


    }

    private void callApi() {

        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
            ApigetCourse();
            ApigetDepartment();
        } else {
            sucessDialog(getResources().getString(R.string.Internet_connection), context);
        }
    }


    public void apiAdddepartment() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {

                tv_alert.setVisibility(View.VISIBLE);
                tv_alert.setTextColor(getResources().getColor(R.color.Green));
                tv_alert.setText("course saved sucessfully!!");

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
        RequestBody dept_name_ = RequestBody.create(MediaType.parse("text/plain"), dept_name);
        RequestBody org_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.org_id);
        RequestBody cour_id_ = RequestBody.create(MediaType.parse("text/plain"), cour_id);


        baseRequest.callAPIDept(1, BASE_URL, dept_name_, org_id_, cour_id_);

    }


    private void ApigetCourse() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    course_list = baseRequest.getDataList(jsonArray, Course.class);
                    for (int i = 0; i < course_list.size(); i++) {
                        course_name_list.add(course_list.get(i).getCourse_name());
                    }

                    adapter = new Ad_cou_dept(course_list, context);
                    rv_list.setAdapter(adapter);


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
        String remainingUrl2 = COURSE_LIST + "&organization_id=" + sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetCourseList() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    course_list2 = baseRequest.getDataList(jsonArray, Course.class);
                    for (int i = 0; i < course_list2.size(); i++) {
                        course_name_list2.add(course_list2.get(i).getCourse_name());
                    }

                    ArrayAdapter adapter_course = new ArrayAdapter(context, android.R.layout.simple_spinner_item, course_name_list2);
                    adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_course.setAdapter(adapter_course);


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
        String remainingUrl2 = COURSE_LIST + "&organization_id=" + sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
    }


    private void ApigetDepartment() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    department_list = baseRequest.getDataList(jsonArray, Department.class);
                    for (int i = 0; i < department_list.size(); i++) {
                        department_name_list.add(department_list.get(i).getDepartment_name());
                    }


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
        String remainingUrl2 = DEPT_LIST + "&organization_id=" + sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    public void sucessDialog(String message, Context context) {
//        textView.setText(getResources().getString(R.string.txt_hello));

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
        mDialog.setContentView(R.layout.notification_dailog2);
        mDialog.setCanceledOnTouchOutside(true);

        Button btn_ok;
        TextView tv_retry;
        TextView tv_notification;
        btn_ok = mDialog.findViewById(R.id.btn_ok);
        tv_retry = mDialog.findViewById(R.id.tv_retry);
        tv_notification = mDialog.findViewById(R.id.tv_notification);
        tv_notification.setText(message);
        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
                callApi();


            }
        });
        mDialog.show();


    }


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Department> dept_list2 = new ArrayList<>();

        //looping through existing elements
        for (int i = 0; i < department_list.size(); i++) {
            if (department_list.get(i).getDepartment_name().toLowerCase().contains(text.toLowerCase())) {
                Department department = new Department();
                department.setDepartment_name(department_list.get(i).getDepartment_name());
                dept_list2.add(department);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter2.filterList(dept_list2);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_course:
                course = course_list2.get(i).getCourse_name();
                cour_id = course_list2.get(i).getCourse_id();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(Act_department_list.this, Administrator_Act_home.class);
                startActivity(i);
                finish();
                break;

//            case R.id.add_user:
//                Intent i1 = new Intent(Act_department_list.this, Act_add_director.class);
//                startActivity(i1);
//                finish();
//                break;
        }

        return true;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_department_list.this, Administrator_Act_home.class);
        startActivity(i);
        finish();
    }


}
