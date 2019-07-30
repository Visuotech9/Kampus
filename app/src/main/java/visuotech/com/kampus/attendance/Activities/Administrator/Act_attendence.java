package visuotech.com.kampus.attendance.Activities.Administrator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Department;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.HOD;
import visuotech.com.kampus.attendance.Model.Section;
import visuotech.com.kampus.attendance.Model.Semister;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

import static visuotech.com.kampus.attendance.Constants.COURSE_LIST;
import static visuotech.com.kampus.attendance.Constants.DEPT_LIST;
import static visuotech.com.kampus.attendance.Constants.SECTION_LIST;
import static visuotech.com.kampus.attendance.Constants.SEM_LIST;

public class Act_attendence extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner_department, spinner_sem, spinner_section, spinner_course;
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    LinearLayout container, lay;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Department> department_list1;
    ArrayList<Course> courses_list1;
    ArrayList<Semister> sem_list1;
    ArrayList<Section> section_list1;
    ArrayList<String> department_list = new ArrayList<String>();
    ArrayList<String> department_id = new ArrayList<String>();
    ArrayList<String> hod_list = new ArrayList<String>();
    ArrayList<String> hod_id = new ArrayList<String>();
    ArrayList<String> course_list = new ArrayList<String>();
    ArrayList<String> course_id = new ArrayList<String>();
    String fname, lname, mname, email, mobileNumber, caste, city, state, admission_date, enrol_no, mot_name, fat_name, session_start, session_end,
            blood, hsc, ssc, diploma, emer_mobbile, dob, paddress, taddress, dept_id, semId, sectionId, hodId, courseId,
            department, gender, director, hod, prefix, course, scholarship, sem, section;
    ArrayList<String> sem_list = new ArrayList<String>();
    ArrayList<String> section_list = new ArrayList<String>();
    String other_device_active, user_typee, organization_id, user_id;
    ArrayAdapter adapter_director, adapter_department, adapter_hod, adapter_course, adapter_sem, adapter_section;
    Button btn_search;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_basic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Add Student");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.activity_act_attendence, null);
        container.addView(rowView, container.getChildCount());


        department_list1 = new ArrayList<>();
        courses_list1 = new ArrayList<>();
        section_list1 = new ArrayList<>();
        sem_list1 = new ArrayList<>();

        user_typee = sessionParam.user_type;
        user_id = sessionParam.userId;
        organization_id = sessionParam.org_id;


        spinner_department = rowView.findViewById(R.id.spinner_department);
        spinner_sem = rowView.findViewById(R.id.spinner_sem);
        spinner_section = rowView.findViewById(R.id.spinner_section);
        spinner_course = rowView.findViewById(R.id.spinner_course);
        btn_search = rowView.findViewById(R.id.btn_search);
        rv_list = rowView.findViewById(R.id.rv_list);
        lay = rowView.findViewById(R.id.lay);


        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

        spinner_department.setOnItemSelectedListener(this);
        spinner_course.setOnItemSelectedListener(this);
        spinner_sem.setOnItemSelectedListener(this);
        spinner_section.setOnItemSelectedListener(this);

        callApi();

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

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay.setVisibility(View.GONE);
                rv_list.setVisibility(View.VISIBLE);
            }
        });
    }

    private void callApi2() {

        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
            ApigetAttendence();
        } else {
            sucessDialog2(getResources().getString(R.string.Internet_connection), context);
        }
    }


    private void callApi() {

        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
            ApigetCourse();
        } else {
            sucessDialog(getResources().getString(R.string.Internet_connection), context);
        }
    }

    private void ApigetAttendence() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");


                    courses_list1 = baseRequest.getDataList(jsonArray, Course.class);

                    for (int i = 0; i < courses_list1.size(); i++) {
                        course_list.add(courses_list1.get(i).getCourse_name());
                        course_id.add(courses_list1.get(i).getCourse_id());
                    }
                    adapter_course = new ArrayAdapter(context, android.R.layout.simple_spinner_item, course_list);
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
        String remainingUrl2 = COURSE_LIST+"&organization_id=" + organization_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetCourse() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");


                    courses_list1 = baseRequest.getDataList(jsonArray, Course.class);

                    for (int i = 0; i < courses_list1.size(); i++) {
                        course_list.add(courses_list1.get(i).getCourse_name());
                        course_id.add(courses_list1.get(i).getCourse_id());
                    }
                    adapter_course = new ArrayAdapter(context, android.R.layout.simple_spinner_item, course_list);
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
        String remainingUrl2 = COURSE_LIST+"&organization_id=" + organization_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetDepartment() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");


                    department_list1 = baseRequest.getDataList(jsonArray, Department.class);

                    for (int i = 0; i < department_list1.size(); i++) {
                        department_list.add(department_list1.get(i).getDepartment_name());
                        department_id.add(department_list1.get(i).getDepartment_id());
                    }
                    adapter_department = new ArrayAdapter(context, android.R.layout.simple_spinner_item, department_list);
                    adapter_department.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_department.setAdapter(adapter_department);


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
        String remainingUrl2 = DEPT_LIST+"&organization_id=" + organization_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetSemister() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");


                    sem_list1 = baseRequest.getDataList(jsonArray, Semister.class);

                    for (int i = 0; i < sem_list1.size(); i++) {
                        sem_list.add(sem_list1.get(i).getSem());

                    }
                    adapter_sem = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sem_list);
                    adapter_sem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_sem.setAdapter(adapter_sem);


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
        String remainingUrl2 = SEM_LIST+"&organization_id=" + organization_id + "&course_id=" + courseId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    private void ApigetSection() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");
                    section_list1 = baseRequest.getDataList(jsonArray, Section.class);
//
                    for (int i = 0; i < section_list1.size(); i++) {
                        section_list.add(section_list1.get(i).getSection());

                    }
                    adapter_section = new ArrayAdapter(context, android.R.layout.simple_spinner_item, section_list);
                    adapter_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_section.setAdapter(adapter_section);


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
        String remainingUrl2 = SECTION_LIST+"&organization_id=" + organization_id + "&course_id=" + courseId + "&department_id=" + dept_id;
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

    public void sucessDialog2(String message, Context context) {
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
                callApi2();


            }
        });
        mDialog.show();


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_course:
                //Your Action Here.

                course = course_list.get(i);
                courseId = course_id.get(i);
                department_list.clear();
                sem_list.clear();
                ApigetDepartment();
                ApigetSemister();
                Toast.makeText(getApplicationContext(), department, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), dept_id, Toast.LENGTH_LONG).show();
                break;

            case R.id.spinner_department:
                //Your Action Here.

                department = department_list.get(i);
                dept_id = department_id.get(i);
                hod_list.clear();
                section_list.clear();
                ApigetSection();
                break;

            case R.id.spinner_sem:
                sem = sem_list1.get(i).getSem();
                semId = sem_list1.get(i).getSem_id();
                break;

            case R.id.spinner_section:
                section = section_list1.get(i).getSection();
                sectionId = section_list1.get(i).getSection_id();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
