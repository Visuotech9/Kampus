package visuotech.com.kampus.attendance.Activities.Administrator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.Activities.Act_Login;
import visuotech.com.kampus.attendance.Activities.Director.Director_Act_home;
import visuotech.com.kampus.attendance.Adapter.Ad_director;
import visuotech.com.kampus.attendance.Adapter.Ad_faculty;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_director_list extends AppCompatActivity {
    Ad_director adapter;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;
    LinearLayout container;

    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    EditText inputSearch;
    ArrayList<Director> director_list = new ArrayList<>();
    ArrayList<String> director_name_list = new ArrayList<>();
    ArrayList<Director> director_list2 = new ArrayList<>();
    ImageView iv_add;
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

       // --------------------toolbar------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Director List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_director_list, null);
        container.addView(rowView, container.getChildCount());
//-------------------------recyclerview------------------------------------------
        rv_list = rowView.findViewById(R.id.rv_list);
        progressbar = rowView.findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

        inputSearch = (EditText) rowView.findViewById(R.id.inputSearch);
        iv_add = rowView.findViewById(R.id.iv_add);

        ApigetDirector();

    }

    private void ApigetDirector() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    director_list = baseRequest.getDataListreverse(jsonArray, Director.class);
                    for (int i = 0; i < director_list.size(); i++) {
                        director_name_list.add(director_list.get(i).getDirector_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }

                    adapter = new Ad_director(director_list, context);
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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=director_list&organization_id=" + sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

/*
    private void filter(String text) {
        //new array list that will hold the filtered data


        //looping through existing elements
        for (int i = 0; i < director_list.size(); i++) {
            if (director_list.get(i).getDirector_name().toLowerCase().contains(text.toLowerCase())) {
                Director director = new Director();
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

                director_list2.clear();

//                keyword = s.toUpperCase();

                for (int i = 0; i < director_list.size(); i++) {
                    if (director_list.get(i).getDirector_name().toLowerCase().contains(s.toLowerCase())) {
                        Director faculty = new Director();
                        faculty.setDirector_name(director_list.get(i).getDirector_name());
                        faculty.setDir_course_name(director_list.get(i).getDir_course_name());
                        faculty.setDirector_username(director_list.get(i).getDirector_username());
                        faculty.setDirector_pic(director_list.get(i).getDirector_pic());
                        faculty.setDirector_mobile_no(director_list.get(i).getDirector_mobile_no());
                        faculty.setDirector_email_id(director_list.get(i).getDirector_email_id());
                        faculty.setDiretor_dob(director_list.get(i).getDiretor_dob());
                        faculty.setDirector_date_of_joining(director_list.get(i).getDirector_date_of_joining());
                        faculty.setDirector_address(director_list.get(i).getDirector_address());
                        faculty.setDirector_gender(director_list.get(i).getDirector_gender());
                        director_list2.add(faculty);
                    }
                }
                adapter = new Ad_director(director_list2, context);
                rv_list.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                director_list2.clear();

//                keyword = s.toUpperCase();

                for (int i = 0; i < director_list.size(); i++) {
                    if (director_list.get(i).getDirector_name().toLowerCase().contains(s.toLowerCase())) {
                        Director faculty = new Director();
                        faculty.setDirector_name(director_list.get(i).getDirector_name());
                        faculty.setDir_course_name(director_list.get(i).getDir_course_name());
                        faculty.setDirector_username(director_list.get(i).getDirector_username());
                        faculty.setDirector_pic(director_list.get(i).getDirector_pic());
                        faculty.setDirector_mobile_no(director_list.get(i).getDirector_mobile_no());
                        faculty.setDirector_email_id(director_list.get(i).getDirector_email_id());
                        faculty.setDiretor_dob(director_list.get(i).getDiretor_dob());
                        faculty.setDirector_date_of_joining(director_list.get(i).getDirector_date_of_joining());
                        faculty.setDirector_address(director_list.get(i).getDirector_address());
                        faculty.setDirector_gender(director_list.get(i).getDirector_gender());
                        director_list2.add(faculty);
                    }
                }
                adapter = new Ad_director(director_list2, context);
                rv_list.setAdapter(adapter);
                return false;
            }
        });


        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(Act_director_list.this, Administrator_Act_home.class);
                startActivity(i);
                finish();
                break;

            case R.id.add_user:
                Intent i1 = new Intent(Act_director_list.this, Act_add_director.class);
                startActivity(i1);
                finish();
                break;
        }

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
