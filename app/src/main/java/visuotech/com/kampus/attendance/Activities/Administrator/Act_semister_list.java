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
import android.support.v7.widget.SearchView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.Adapter.Ad_Semister_list;
import visuotech.com.kampus.attendance.Adapter.Ad_course;
import visuotech.com.kampus.attendance.Adapter.Ad_faculty;
import visuotech.com.kampus.attendance.Adapter.Ad_subject;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.Model.Semister;
import visuotech.com.kampus.attendance.Model.Subjects;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.RecyclerTouchListener;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

//import android.widget.SearchView;

public class Act_semister_list extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Ad_course adapter;
    Ad_Semister_list adapter1;
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
    ImageView iv_add;
    TextView tv_station,tv_message, tv_sem1, tv_sem2, tv_sem3, tv_sem4, tv_sem5, tv_sem6, tv_sem7, tv_sem8;
    Dialog mDialog;
    TextView tv_alert;
    String dept_name, course, cour_id;
    Spinner spinner_course;
    private BaseRequest baseRequest;
    private String keyword, course_id, course_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Semister");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.act_semister_list, null);
        container.addView(rowView, container.getChildCount());

        tv_station = rowView.findViewById(R.id.tv_station);
        tv_message = rowView.findViewById(R.id.tv_message);
        rv_list = rowView.findViewById(R.id.rv_list);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

//-------------------------recyclerview------------------------------------------

        tv_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sucessDialog1(context, tv_station, course_list);
            }
        });


        ApigetCourse();


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

                ApigetSemister(course_id, course_name);
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


    private void ApigetSemister(String course_id, final String course_name) {
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
                        sem_list = baseRequest.getDataList(jsonArray, Semister.class);
                        adapter1 = new Ad_Semister_list(sem_list, context, course_name);
                        rv_list.setAdapter(adapter1);
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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=sem_list&organization_id=" + sessionParam.org_id + "&course_id=" + course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_course:
                course = course_list.get(i).getCourse_name();
                cour_id = course_list.get(i).getCourse_id();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(Act_semister_list.this, Administrator_Act_home.class);
                startActivity(i);
                finish();
                break;

            case R.id.add_user:
                Intent i2 = new Intent(Act_semister_list.this, Act_add_semister.class);
                startActivity(i2);
                finish();
                break;
        }

        return true;

//        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem search_item = menu.findItem(R.id.mi_search);
        search_item.setVisible(false);
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_semister_list.this, Administrator_Act_home.class);
        startActivity(i);
        finish();
    }
}
