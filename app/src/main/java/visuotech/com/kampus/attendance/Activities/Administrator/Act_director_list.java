package visuotech.com.kampus.attendance.Activities.Administrator;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.Activities.Act_Login;
import visuotech.com.kampus.attendance.Activities.Director.Director_Act_home;
import visuotech.com.kampus.attendance.Adapter.Ad_director;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_director_list extends AppCompatActivity {
    Ad_director adapter;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;

    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;
    private SearchableSpinner mSearchableSpinner;
    EditText inputSearch;

    ArrayList<Director>director_list;
    ArrayList<String>director_name_list=new ArrayList<>();

    ImageView iv_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_director_list);

//-------------------------toolbar------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Director List");
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
                Intent i = new Intent(Act_director_list.this, Act_add_director.class);
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

        ApigetDirector();

    }

    private void ApigetDirector(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    director_list=baseRequest.getDataListreverse(jsonArray,Director.class);
                    for (int i=0;i<director_list.size();i++){
                        director_name_list.add(director_list.get(i).getDirector_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }

                    adapter=new Ad_director(director_list,context);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=director_list&organization_id="+sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Director> director_list2 = new ArrayList<>();

        //looping through existing elements
        for (int i=0;i<director_list.size();i++){
            if (director_list.get(i).getDirector_name().toLowerCase().contains(text.toLowerCase())){
                Director director=new Director();
                director.setDirector_name(director_list.get(i).getDirector_name());
                director.setDir_course_name(director_list.get(i).getDir_course_name());
                director.setDirector_username(director_list.get(i).getDirector_username());
                director.setDirector_pic(director_list.get(i).getDirector_pic());
                director_list2.add(director);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(director_list2);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_director_list.this, Administrator_Act_home.class);
        startActivity(i);
        finish();
        return true;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_director_list.this, Administrator_Act_home.class);
        startActivity(i);
        finish();
    }

}
