package visuotech.com.kampus.attendance.Activities.Faculty;

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

import visuotech.com.kampus.attendance.Activities.Administrator.Act_add_Hod;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_hod_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Administrator_Act_home;
import visuotech.com.kampus.attendance.Adapter.Ad_assignment;
import visuotech.com.kampus.attendance.Adapter.Ad_hod;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Assignment;
import visuotech.com.kampus.attendance.Model.HOD;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_assignment_list extends AppCompatActivity {
    Ad_assignment adapter;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;

    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;
    EditText inputSearch;

    ArrayList<Assignment> assignment_list;
    ArrayList<String>assignment_list_name=new ArrayList<>();

    ImageView iv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_assignment_list);

        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assignment List");
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

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        iv_add =  findViewById(R.id.iv_add);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_assignment_list.this, Act_add_assignment.class);
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

        Apigetassignment();
    }

    private void Apigetassignment(){
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    assignment_list=baseRequest.getDataListreverse(jsonArray,Assignment.class);

                    adapter=new Ad_assignment(assignment_list,context);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=assignment_list&organization_id="+sessionParam.org_id+"&faculty_id="+sessionParam.userId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Assignment> assignment_list2 = new ArrayList<>();

        //looping through existing elements
        for (int i=0;i<assignment_list.size();i++){
            if (assignment_list.get(i).getTitle().toLowerCase().contains(text.toLowerCase())){
                Assignment assignment=new Assignment();
                assignment.setTitle(assignment_list.get(i).getTitle());
                assignment.setAssgDepartmentName(assignment_list.get(i).getAssgDepartmentName());
                assignment.setAssgSemester(assignment_list.get(i).getAssgSemester());
                assignment.setAssgSection(assignment_list.get(i).getAssgSection());
                assignment_list2.add(assignment);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(assignment_list2);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_assignment_list.this, Faculty_Act_home.class);
        startActivity(i);
        finish();
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_assignment_list.this, Faculty_Act_home.class);
        startActivity(i);
        finish();
    }
}
