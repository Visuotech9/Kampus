package visuotech.com.kampus.attendance.Activities.Administrator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import java.util.EventListener;

import visuotech.com.kampus.attendance.Activities.Director.Act_add_Hod2;
import visuotech.com.kampus.attendance.Activities.Director.Act_hod_list2;
import visuotech.com.kampus.attendance.Adapter.Ad_Semister_list;
import visuotech.com.kampus.attendance.Adapter.Ad_course;
import visuotech.com.kampus.attendance.Adapter.Ad_department;
import visuotech.com.kampus.attendance.Adapter.Ad_subject;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Department;
import visuotech.com.kampus.attendance.Model.Semister;
import visuotech.com.kampus.attendance.Model.Subjects;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.RecyclerTouchListener;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

//import android.widget.SearchView;

public class Act_subject_list extends AppCompatActivity {
    Ad_course adapter;
    Ad_Semister_list adapter1;
    Ad_department ad_department;
    Ad_subject ad_subject;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;
    LinearLayout container;
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    EditText inputSearch;

    ArrayList<String> faculty_name_list = new ArrayList<>();
    ArrayList<Semister> sem_list2 = new ArrayList<>();
    ArrayList<Semister> sem_list = new ArrayList<>();
    ArrayList<Course> course_list = new ArrayList<>();
    ArrayList<String> course_list1 = new ArrayList<>();
    ArrayList<Department> department_list = new ArrayList<>();
    ArrayList<String> department_name_list = new ArrayList<>();
    ArrayList<Subjects> subj_list = new ArrayList<>();
    ArrayList<String> subj_list1 = new ArrayList<>();


    ImageView iv_add;
    TextView tv_station, tv_dept, tv_sem,tv_message, tv_sem1, tv_sem2, tv_sem3, tv_sem4, tv_sem5, tv_sem6, tv_sem7, tv_sem8;
    Dialog mDialog;
    TextView tv_alert;
    String dept_name, course, cour_id, dept_id, sem_id, sem_name;
    Spinner spinner_course;
    Button btn_view;
    private BaseRequest baseRequest;
    private String keyword, course_id, course_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.act_subject_list, null);

        tv_station = rowView.findViewById(R.id.tv_station);
        tv_dept = rowView.findViewById(R.id.tv_dept);
        tv_sem = rowView.findViewById(R.id.tv_sem);
        rv_list = rowView.findViewById(R.id.rv_list);
        btn_view = rowView.findViewById(R.id.btn_view);
        tv_message = rowView.findViewById(R.id.tv_message);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

//-------------------------recyclerview------------------------------------------

        tv_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_sem.setText("");
                tv_dept.setText("");
                sucessDialog1(context, tv_station, course_list);
            }
        });
        tv_dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (department_list.size() != 0) {
                    tv_sem.setText("");
                    sucessDialog2(context, tv_dept, department_list);
                } else {
                    Toast.makeText(getApplicationContext(), "First select course", Toast.LENGTH_SHORT).show();
                }

            }
        });
        tv_sem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sem_list.size() != 0) {
                    sucessDialog3(context, tv_sem, sem_list);
                } else {
                    Toast.makeText(getApplicationContext(), "First select department", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_station.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "First select course", Toast.LENGTH_SHORT).show();
                }
                if (tv_dept.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "First select department", Toast.LENGTH_SHORT).show();
                }
                if (tv_sem.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "First select semister", Toast.LENGTH_SHORT).show();
                } else {
                    callApi();
                }
            }
        });

        container.addView(rowView, container.getChildCount());
        ApigetCourse();


    }

    private void callApi() {

        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
            ApigetSubjectList();
        } else {
            sucessDialog(getResources().getString(R.string.Internet_connection), context);
        }

    }


    public void sucessDialog1(Context context, final TextView tv_station1, final ArrayList<Course> courses_list) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.search_layout_dialog);
        mDialog.setCanceledOnTouchOutside(true);


        TextView tv_retry;
        LinearLayout lay;
        EditText inputSearch;
        RecyclerView rv;
        rv = mDialog.findViewById(R.id.rv_list);
        tv_retry = mDialog.findViewById(R.id.tv_retry);
        inputSearch = mDialog.findViewById(R.id.inputSearch);
        TextView tv_title = mDialog.findViewById(R.id.tv_title);
        LinearLayout lay1 = mDialog.findViewById(R.id.lay1);
        lay1.setVisibility(View.GONE);
        tv_title.setText("Select Course");
        tv_retry.setText("OK");

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new Ad_course(courses_list, context);
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int j = position + 1;

                tv_station1.setText(courses_list.get(position).getCourse_name());
                course_id = courses_list.get(position).getCourse_id();
                course_name = courses_list.get(position).getCourse_name();

                ApigetDepartment(course_id, course_name);
                mDialog.cancel();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

//        inputSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                filter1(editable.toString(), adapter, sp_list);
//            }
//        });


        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
//                tv_station1.setEnabled(true);


            }
        });
        mDialog.show();


    }

    public void sucessDialog2(Context context, final TextView tv_station1, final ArrayList<Department> dept_list) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.search_layout_dialog);
        mDialog.setCanceledOnTouchOutside(true);


        TextView tv_retry;
        LinearLayout lay;
        EditText inputSearch;
        RecyclerView rv;
        rv = mDialog.findViewById(R.id.rv_list);
        tv_retry = mDialog.findViewById(R.id.tv_retry);
        inputSearch = mDialog.findViewById(R.id.inputSearch);
        TextView tv_title = mDialog.findViewById(R.id.tv_title);
        LinearLayout lay1 = mDialog.findViewById(R.id.lay1);
        lay1.setVisibility(View.GONE);
        tv_title.setText("Select Department");
        tv_retry.setText("OK");
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        ad_department = new Ad_department(dept_list, context);
        rv.setAdapter(ad_department);

        rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int j = position + 1;

                tv_station1.setText(dept_list.get(position).getDepartment_name());
                dept_id = dept_list.get(position).getDepartment_id();
                dept_name = dept_list.get(position).getDepartment_name();

                ApigetSemister(course_id, course_name, dept_id);
                mDialog.cancel();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

//        inputSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                filter1(editable.toString(), adapter, sp_list);
//            }
//        });


        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
//                tv_station1.setEnabled(true);


            }
        });
        mDialog.show();


    }


    public void sucessDialog3(Context context, final TextView tv_station1, final ArrayList<Semister> semister_list) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.search_layout_dialog);
        mDialog.setCanceledOnTouchOutside(true);


        TextView tv_retry;
        LinearLayout lay;
        EditText inputSearch;
        RecyclerView rv;
        rv = mDialog.findViewById(R.id.rv_list);
        tv_retry = mDialog.findViewById(R.id.tv_retry);
        inputSearch = mDialog.findViewById(R.id.inputSearch);
        TextView tv_title = mDialog.findViewById(R.id.tv_title);
        LinearLayout lay1 = mDialog.findViewById(R.id.lay1);
        lay1.setVisibility(View.GONE);
        tv_title.setText("Select Semister");
        tv_retry.setText("OK");
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter1 = new Ad_Semister_list(semister_list, context, dept_name);
        rv.setAdapter(adapter1);

        rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int j = position + 1;

                tv_station1.setText(semister_list.get(position).getSem());
                sem_id = semister_list.get(position).getSem_id();
                sem_name = semister_list.get(position).getSem();

//                ApigetSemister(course_id, course_name);
                mDialog.cancel();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
//                tv_station1.setEnabled(true);


            }
        });
        mDialog.show();


    }

    private void ApigetDepartment(String course_id, String course_name) {
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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=department_list&organization_id=" + sessionParam.org_id + "&course_id=" + course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    private void ApigetCourse() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    course_list = baseRequest.getDataListreverse(jsonArray, Course.class);
                    for (int i = 0; i < course_list.size(); i++) {
                        course_list1.add(course_list.get(i).getCourse_name());
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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=course_list&organization_id=" + sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    private void ApigetSemister(String course_id, final String course_name, String dept_id) {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");


                    sem_list = baseRequest.getDataList(jsonArray, Semister.class);


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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=sem_list&organization_id=" + sessionParam.org_id + "&course_id=" + course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetSubjectList() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    if (jsonObject.optString("message").equals("No Records Found")){
                        rv_list.setVisibility(View.GONE);
                        tv_message.setVisibility(View.VISIBLE);
                    }else {
                        rv_list.setVisibility(View.VISIBLE);
                        tv_message.setVisibility(View.GONE);
                        subj_list = baseRequest.getDataListreverse(jsonArray, Subjects.class);
                        ad_subject = new Ad_subject(subj_list, context);
                        rv_list.setAdapter(ad_subject);
                    }


//                    for (int i = 0; i < course_list.size(); i++) {
//                        course_list1.add(course_list.get(i).getCourse_name());
//                    }


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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=subject_list&organization_id=" + sessionParam.org_id + "&course_id=" + course_id + "&department_id=" + dept_id + "&sem_id=" + sem_id;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(Act_subject_list.this, Administrator_Act_home.class);
                startActivity(i);
                finish();
                break;

          /*  case R.id.add_user:
                Intent i2 = new Intent(Act_subject_list.this, Act_add_semister.class);
                startActivity(i2);
                finish();
                break;*/
        }

        return true;

//        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_subject_list.this, Administrator_Act_home.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem search_item = menu.findItem(R.id.mi_search);
        search_item.setVisible(false);
        return true;
    }


}
