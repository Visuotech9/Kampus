package visuotech.com.kampus.attendance.Activities.Administrator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
//import android.widget.SearchView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.Adapter.Ad_director;
import visuotech.com.kampus.attendance.Adapter.Ad_faculty;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_faculty_list extends AppCompatActivity {
    Ad_faculty adapter;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;
    LinearLayout container;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    EditText inputSearch;
    ArrayList<Faculty> faculty_list = new ArrayList<>();
    ArrayList<String> faculty_name_list = new ArrayList<>();
    ArrayList<Faculty> faculty_list2 = new ArrayList<>();
    ImageView iv_add;
    private BaseRequest baseRequest;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Faculties");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_faculty_list, null);
        container.addView(rowView, container.getChildCount());

//-------------------------recyclerview------------------------------------------
        rv_list = rowView.findViewById(R.id.rv_list);
        progressbar = rowView.findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

        inputSearch = (EditText) rowView.findViewById(R.id.inputSearch);
        iv_add = rowView.findViewById(R.id.iv_add);

/*
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_faculty_list.this, Act_add_faculty.class);
                startActivity(i);
                finish();
            }
        });
*/


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
//                filter(editable.toString());
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
        callApi();




    }

    private void callApi() {

        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
            ApigetFaculty();
        } else {
            sucessDialog(getResources().getString(R.string.Internet_connection), context);
        }
    }


    private void ApigetFaculty() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    faculty_list = baseRequest.getDataListreverse(jsonArray, Faculty.class);
                    for (int i = 0; i < faculty_list.size(); i++) {
                        faculty_name_list.add(faculty_list.get(i).getFaculty_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }

                    adapter = new Ad_faculty(faculty_list, context);
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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=faculty_list&organization_id=" + sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }
/*
    private void filter(String text) {
        //new array list that will hold the filtered data


        //looping through existing elements
        for (int i=0;i<faculty_list.size();i++){
            if (faculty_list.get(i).getFaculty_name().toLowerCase().contains(text.toLowerCase())){
                Faculty faculty=new Faculty();
                faculty.setFaculty_name(faculty_list.get(i).getFaculty_name());
                faculty.setFaculty_department_name(faculty_list.get(i).getFaculty_department_name());
                faculty.setFaculty_username(faculty_list.get(i).getFaculty_username());
                faculty.setFaculty_pic(faculty_list.get(i).getFaculty_pic());
                faculty_list2.add(faculty);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(faculty_list2);
    }
*/


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
                faculty_list2.clear();

                keyword = s.toUpperCase();

                for (int i = 0; i < faculty_list.size(); i++) {
                    if (faculty_list.get(i).getFaculty_name().toLowerCase().contains(s.toLowerCase())) {
                        Faculty faculty = new Faculty();
                        faculty.setFaculty_name(faculty_list.get(i).getFaculty_name());
                        faculty.setFaculty_department_name(faculty_list.get(i).getFaculty_department_name());
                        faculty.setFaculty_username(faculty_list.get(i).getFaculty_username());
                        faculty.setFaculty_pic(faculty_list.get(i).getFaculty_pic());
                        faculty.setF_mobile_no(faculty_list.get(i).getF_mobile_no());
                        faculty.setF_email_id(faculty_list.get(i).getF_email_id());
                        faculty.setF_dob(faculty_list.get(i).getF_dob());
                        faculty.setF_date_of_joining(faculty_list.get(i).getF_date_of_joining());
                        faculty.setF_address(faculty_list.get(i).getF_address());
                        faculty.setF_gender(faculty_list.get(i).getF_gender());
                        faculty.setFaculty_course_name(faculty_list.get(i).getFaculty_course_name());
                        faculty.setDesignation(faculty_list.get(i).getDesignation());
                        faculty.setExperience(faculty_list.get(i).getExperience());
                        faculty.setFaculty_hod_name(faculty_list.get(i).getFaculty_hod_name());
                        faculty_list2.add(faculty);
                    }
                }
                adapter = new Ad_faculty(faculty_list2, context);
                rv_list.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                faculty_list2.clear();

                keyword = s.toUpperCase();

                for (int i = 0; i < faculty_list.size(); i++) {
                    if (faculty_list.get(i).getFaculty_name().toLowerCase().contains(s.toLowerCase())) {
                        Faculty faculty = new Faculty();
                        faculty.setFaculty_name(faculty_list.get(i).getFaculty_name());
                        faculty.setFaculty_department_name(faculty_list.get(i).getFaculty_department_name());
                        faculty.setFaculty_username(faculty_list.get(i).getFaculty_username());
                        faculty.setFaculty_pic(faculty_list.get(i).getFaculty_pic());
                        faculty.setF_mobile_no(faculty_list.get(i).getF_mobile_no());
                        faculty.setF_email_id(faculty_list.get(i).getF_email_id());
                        faculty.setF_dob(faculty_list.get(i).getF_dob());
                        faculty.setF_date_of_joining(faculty_list.get(i).getF_date_of_joining());
                        faculty.setF_address(faculty_list.get(i).getF_address());
                        faculty.setF_gender(faculty_list.get(i).getF_gender());
                        faculty.setFaculty_course_name(faculty_list.get(i).getFaculty_course_name());
                        faculty.setDesignation(faculty_list.get(i).getDesignation());
                        faculty.setExperience(faculty_list.get(i).getExperience());
                        faculty.setFaculty_hod_name(faculty_list.get(i).getFaculty_hod_name());
                        faculty_list2.add(faculty);
                    }
                }
                adapter = new Ad_faculty(faculty_list2, context);
                rv_list.setAdapter(adapter);

                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(Act_faculty_list.this, Administrator_Act_home.class);
                startActivity(i);
                finish();
                break;

            case R.id.add_user:
                Intent i1 = new Intent(Act_faculty_list.this, Act_add_faculty.class);
                startActivity(i1);
                finish();
                break;
        }

        return true;

//        return super.onOptionsItemSelected(item);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_faculty_list.this, Administrator_Act_home.class);
        startActivity(i);
        finish();
    }
}
