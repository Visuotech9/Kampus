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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import visuotech.com.kampus.attendance.Activities.Act_College_list;
import visuotech.com.kampus.attendance.Adapter.Ad_course;
import visuotech.com.kampus.attendance.Adapter.Ad_director;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_course_list extends AppCompatActivity {

    Ad_course adapter;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;
    LinearLayout container;
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;
    EditText inputSearch;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<Course> course_list=new ArrayList<>();
    ArrayList<Course> course_list2=new ArrayList<>();
    ArrayList<String>course_name_list=new ArrayList<>();

    ImageView iv_add;
    Dialog mDialog;
    TextView tv_alert;
    String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Courses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_course_list, null);
        container.addView(rowView, container.getChildCount());
//-------------------------recyclerview------------------------------------------
        rv_list=rowView.findViewById(R.id.rv_list);
        progressbar=rowView.findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

        inputSearch = (EditText) rowView.findViewById(R.id.inputSearch);
        iv_add =  rowView.findViewById(R.id.iv_add);


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

        callApi();

    }

    private void callApi() {

        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
            ApigetCourse();
        } else {
            sucessDialog(getResources().getString(R.string.Internet_connection), context);
        }
    }

    private void callApi2() {

        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
            apiAddCourse();
        } else {
            sucessDialog1(getResources().getString(R.string.Internet_connection), context);
        }
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

    public void sucessDialog1(String message, Context context) {
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



    private void ApigetCourse(){
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    course_list=baseRequest.getDataListreverse(jsonArray,Course.class);
                    for (int i=0;i<course_list.size();i++){
                        course_name_list.add(course_list.get(i).getCourse_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }

                    adapter=new Ad_course(course_list,context);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=course_list&organization_id="+sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    public void apiAddCourse() {
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
        RequestBody courseName_ = RequestBody.create(MediaType.parse("text/plain"), courseName);
        RequestBody org_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.org_id);


        baseRequest.callAPICourse(1,"https://collectorexpress.in/",courseName_,org_id_);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem search_item = menu.findItem(R.id.mi_search);

        SearchView searchView = (SearchView) search_item.getActionView();


        searchView.setFocusable(false);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                course_list2.clear();

//                keyword = s.toUpperCase();

                for (int i=0;i<course_list.size();i++){
                    if (course_list.get(i).getCourse_name().toLowerCase().contains(s.toLowerCase())){
                        Course course=new Course();
                        course.setCourse_name(course_list.get(i).getCourse_name());
                        course_list2.add(course);
                    }
                }
                adapter = new Ad_course(course_list2, context);
                rv_list.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                course_list2.clear();

                for (int i=0;i<course_list.size();i++){
                    if (course_list.get(i).getCourse_name().toLowerCase().contains(s.toLowerCase())){
                        Course course=new Course();
                        course.setCourse_name(course_list.get(i).getCourse_name());
                        course_list2.add(course);
                    }
                }
                adapter = new Ad_course(course_list2, context);
                rv_list.setAdapter(adapter);
                return false;
            }
        });


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(Act_course_list.this, Administrator_Act_home.class);
                startActivity(i);
                finish();
                break;

            case R.id.add_user:
                mDialog=new Dialog(context);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
                mDialog.setContentView(R.layout.add_course);
                mDialog.setCanceledOnTouchOutside(false);
                //dialog layout

                ImageView iv_cancel_dialog;
                Button btn_save;
//                 TextView tv_alert;
                final EditText et_name;
//
                iv_cancel_dialog=mDialog.findViewById(R.id.iv_cancel_dialog);
                tv_alert=mDialog.findViewById(R.id.tv_alert);
                et_name= mDialog.findViewById(R.id.et_name);
                btn_save= mDialog.findViewById(R.id.btn_save);
                iv_cancel_dialog.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                        callApi2();


                    }
                });
                btn_save.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (et_name.getText().toString().isEmpty()) {
                            tv_alert.setVisibility(View.VISIBLE);
                            tv_alert.setTextColor(getResources().getColor(R.color.DarkRed));
                            tv_alert.setText("please enter course name!!");
                        } else{
                            courseName=et_name.getText().toString();

                            if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
                                apiAddCourse();
                            } else {
                                sucessDialog(getResources().getString(R.string.Internet_connection), context);
                            }
                            et_name.setText("");
                        }


                    }
                });
                mDialog.show();
                break;
        }

        return true;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_course_list.this, Administrator_Act_home.class);
        startActivity(i);
        finish();
    }

}
