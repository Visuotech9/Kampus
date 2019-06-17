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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.Adapter.Ad_director;
import visuotech.com.kampus.attendance.Adapter.Ad_faculty;
import visuotech.com.kampus.attendance.Adapter.Ad_hod;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.Model.HOD;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_hod_list extends AppCompatActivity {
    Ad_hod adapter;
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


    ArrayList<String>hod_list_name=new ArrayList<>();
    ArrayList<HOD> hod_list2 = new ArrayList<>();
    ArrayList<HOD> hod_list=new ArrayList<>();
    ImageView iv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Head of departments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_hod_list, null);
        container.addView(rowView, container.getChildCount());

//-------------------------recyclerview------------------------------------------
        rv_list=rowView.findViewById(R.id.rv_list);
        progressbar=rowView.findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

        inputSearch = (EditText) rowView.findViewById(R.id.inputSearch);
        iv_add =  rowView.findViewById(R.id.iv_add);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=hod_list&organization_id="+sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void filter(String text) {
        //new array list that will hold the filtered data


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
                hod_list2.clear();

                for (int i=0;i<hod_list.size();i++){
                    if (hod_list.get(i).getHod_name().toLowerCase().contains(s.toLowerCase())){
                        HOD hod=new HOD();
                        hod.setHod_name(hod_list.get(i).getHod_name());
                        hod.setHod_department_name(hod_list.get(i).getHod_department_name());
                        hod.setHod_username(hod_list.get(i).getHod_username());
                        hod.setHod_pic(hod_list.get(i).getHod_pic());
                        hod.setHod_mobile_no(hod_list.get(i).getHod_mobile_no());
                        hod.setHod_email_id(hod_list.get(i).getHod_email_id());
                        hod.setHod_dob(hod_list.get(i).getHod_dob());
                        hod.setHod_pic(hod_list.get(i).getHod_date_of_joining());
                        hod.setHod_date_of_joining(hod_list.get(i).getHod_address());
                        hod.setHod_gender(hod_list.get(i).getHod_gender());
                        hod.setHod_course_name(hod_list.get(i).getHod_course_name());
                        hod_list2.add(hod);
                    }
                }
                adapter = new Ad_hod(hod_list2, context);
                rv_list.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                hod_list2.clear();

                for (int i=0;i<hod_list.size();i++){
                    if (hod_list.get(i).getHod_name().toLowerCase().contains(s.toLowerCase())){
                        HOD hod=new HOD();
                        hod.setHod_name(hod_list.get(i).getHod_name());
                        hod.setHod_department_name(hod_list.get(i).getHod_department_name());
                        hod.setHod_username(hod_list.get(i).getHod_username());
                        hod.setHod_pic(hod_list.get(i).getHod_pic());
                        hod.setHod_mobile_no(hod_list.get(i).getHod_mobile_no());
                        hod.setHod_email_id(hod_list.get(i).getHod_email_id());
                        hod.setHod_dob(hod_list.get(i).getHod_dob());
                        hod.setHod_pic(hod_list.get(i).getHod_date_of_joining());
                        hod.setHod_date_of_joining(hod_list.get(i).getHod_address());
                        hod.setHod_gender(hod_list.get(i).getHod_gender());
                        hod.setHod_course_name(hod_list.get(i).getHod_course_name());
                        hod_list2.add(hod);
                    }
                }
                adapter = new Ad_hod(hod_list2, context);
                rv_list.setAdapter(adapter);

                return false;
            }
        });


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(Act_hod_list.this, Administrator_Act_home.class);
                startActivity(i);
                finish();
                break;

            case R.id.add_user:
                Intent i1 = new Intent(Act_hod_list.this, Act_add_Hod.class);
                startActivity(i1);
                finish();
                break;
        }

        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_hod_list.this, Administrator_Act_home.class);
        startActivity(i);
        finish();
    }
}
