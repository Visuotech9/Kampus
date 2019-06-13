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

import visuotech.com.kampus.attendance.Activities.Administrator.Act_add_Hod;
import visuotech.com.kampus.attendance.Activities.Administrator.Administrator_Act_home;
import visuotech.com.kampus.attendance.Adapter.Ad_hod;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.HOD;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_hod_list2 extends AppCompatActivity {
    Ad_hod adapter;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;
    String course_id;

    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;
    EditText inputSearch;

    ArrayList<HOD> hod_list;
    ArrayList<String>hod_list_name=new ArrayList<>();

    ImageView iv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_hod_list);
        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hod List");
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
                Intent i = new Intent(Act_hod_list2.this, Act_add_Hod2.class);
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

        ApigetHod();
    }

    private void ApigetHod(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    hod_list=baseRequest.getDataListreverse(jsonArray,HOD.class);
                    for (int i=0;i<hod_list.size();i++){
                        hod_list_name.add(hod_list.get(i).getHod_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }

                    adapter=new Ad_hod(hod_list,context);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=hod_list&organization_id="+sessionParam.org_id+"&course_id="+sessionParam.course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<HOD> hod_list2 = new ArrayList<>();

        //looping through existing elements
        for (int i=0;i<hod_list.size();i++){
            if (hod_list.get(i).getHod_name().toLowerCase().contains(text.toLowerCase())){
                HOD hod=new HOD();
                hod.setHod_name(hod_list.get(i).getHod_name());
                hod.setHod_department_name(hod_list.get(i).getHod_department_name());
                hod.setHod_username(hod_list.get(i).getHod_username());
                hod.setHod_pic(hod_list.get(i).getHod_pic());
                hod_list2.add(hod);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(hod_list2);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_hod_list2.this, Director_Act_home.class);
        startActivity(i);
        finish();
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_hod_list2.this, Director_Act_home.class);
        startActivity(i);
        finish();
    }
}
