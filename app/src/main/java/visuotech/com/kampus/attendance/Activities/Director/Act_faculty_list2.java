package visuotech.com.kampus.attendance.Activities.Director;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.Activities.Administrator.Act_add_faculty;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_faculty_list;
import visuotech.com.kampus.attendance.Activities.Administrator.Administrator_Act_home;
import visuotech.com.kampus.attendance.Activities.Faculty.Act_add_assignment;
import visuotech.com.kampus.attendance.Activities.Faculty.Act_assignment_list;
import visuotech.com.kampus.attendance.Activities.Faculty.Faculty_Act_home;
import visuotech.com.kampus.attendance.Adapter.Ad_assignment;
import visuotech.com.kampus.attendance.Adapter.Ad_faculty;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Assignment;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

import static visuotech.com.kampus.attendance.Constants.FACULTY_LIST;

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
    ArrayList<Faculty> faculty_list = new ArrayList<>();
    ArrayList<Faculty> faculty_list2 = new ArrayList<>();
    ArrayList<String> faculty_name_list = new ArrayList<>();
    ImageView iv_add;
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Faculties");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_faculty_list, null);
        container.addView(rowView, container.getChildCount());


//-------------------------recyclerview------------------------------------------
        rv_list = rowView.findViewById(R.id.rv_list);
        progressbar = rowView.findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

        inputSearch = rowView.findViewById(R.id.inputSearch);
        iv_add = rowView.findViewById(R.id.iv_add);

        ApigetFaculty();

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
        String remainingUrl2 = FACULTY_LIST+"&organization_id=" + sessionParam.org_id + "&director_id=" + sessionParam.userId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
    }


/*
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

                for (int i = 0; i < faculty_list.size(); i++) {
                    if (faculty_list.get(i).getFaculty_name().toLowerCase().contains(s.toLowerCase())) {
                        Faculty faculty = new Faculty();
                        faculty.setFaculty_name(faculty_list.get(i).getFaculty_name());
                        faculty.setFaculty_department_name(faculty_list.get(i).getFaculty_department_name());
                        faculty.setFaculty_username(faculty_list.get(i).getFaculty_username());
                        faculty.setFaculty_pic(faculty_list.get(i).getFaculty_pic());
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

                for (int i = 0; i < faculty_list.size(); i++) {
                    if (faculty_list.get(i).getFaculty_name().toLowerCase().contains(s.toLowerCase())) {
                        Faculty faculty = new Faculty();
                        faculty.setFaculty_name(faculty_list.get(i).getFaculty_name());
                        faculty.setFaculty_department_name(faculty_list.get(i).getFaculty_department_name());
                        faculty.setFaculty_username(faculty_list.get(i).getFaculty_username());
                        faculty.setFaculty_pic(faculty_list.get(i).getFaculty_pic());
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
*/

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(Act_faculty_list2.this, Director_Act_home.class);
                startActivity(i);
                finish();
                break;

            case R.id.add_user:
                Intent i2 = new Intent(Act_faculty_list2.this, Act_add_faculty2.class);
                startActivity(i2);
                finish();
                break;
        }
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
