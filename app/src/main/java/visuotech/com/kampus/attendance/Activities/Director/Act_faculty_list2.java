package visuotech.com.kampus.attendance.Activities.Director;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.Activities.Administrator.Act_add_faculty;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_faculty_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Administrator_Act_home;
import visuotech.com.kampus.attendance.Adapter.Ad_faculty;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_faculty_list2 extends AppCompatActivity {
    Ad_faculty adapter;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;

    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    EditText inputSearch;
    ArrayList<Faculty> faculty_list;
    ArrayList<String>faculty_name_list=new ArrayList<>();
    ImageView iv_add;
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_faculty_list);
        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Faculty List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//-------------------------classes------------------------------------------
        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

//-------------------------recyclerview------------------------------------------
        rv_list=findViewById(R.id.rv_list);
        progressbar=findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

        inputSearch = findViewById(R.id.inputSearch);
        iv_add =  findViewById(R.id.iv_add);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Act_faculty_list2.this, Act_add_faculty2.class);
                startActivity(i);
                finish();
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

        ApigetFaculty();

    }

    private void ApigetFaculty(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    faculty_list=baseRequest.getDataListreverse(jsonArray,Faculty.class);
                    for (int i=0;i<faculty_list.size();i++){
                        faculty_name_list.add(faculty_list.get(i).getFaculty_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }

                    adapter=new Ad_faculty(faculty_list,context);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=faculty_list&organization_id="+sessionParam.org_id+"&director_id="+sessionParam.userId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Faculty> faculty_list2 = new ArrayList<>();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_faculty_list2.this, Director_Act_home.class);
        startActivity(i);
        finish();
        return true;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_faculty_list2.this, Director_Act_home.class);
        startActivity(i);
        finish();
    }
}
