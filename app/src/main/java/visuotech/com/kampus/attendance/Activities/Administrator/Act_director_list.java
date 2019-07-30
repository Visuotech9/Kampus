package visuotech.com.kampus.attendance.Activities.Administrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import visuotech.com.kampus.attendance.Activities.Act_Login;
import visuotech.com.kampus.attendance.Activities.Director.Director_Act_home;
import visuotech.com.kampus.attendance.Adapter.Ad_director;
import visuotech.com.kampus.attendance.Adapter.Ad_faculty;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

import static visuotech.com.kampus.attendance.Constants.DIRECTOR_LIST;

public class Act_director_list extends AppCompatActivity {
    Ad_director adapter;
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

    ArrayList<Director> director_list = new ArrayList<>();
    ArrayList<String> director_name_list = new ArrayList<>();
    ArrayList<Director> director_list2 = new ArrayList<>();
    ImageView iv_add;
    private BaseRequest baseRequest;
    CardView profileCard;
    Toolbar toolbar;
    ImageView search_icon,iv_voice;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public boolean datafinish = false;
    String strSpeechText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_basic);

       // --------------------toolbar------------------------------------------
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Directors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar = findViewById(R.id.toolbar);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        profileCard =  findViewById(R.id.profileCard);

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

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.GONE);
                profileCard.setVisibility(View.VISIBLE);
            }
        });




        search_icon=findViewById(R.id.search_icon);
        EditText inputSearch=findViewById(R.id.inputSearch);
         iv_voice=findViewById(R.id.iv_voice);
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

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.VISIBLE);
                profileCard.setVisibility(View.GONE);
            }
        });

        iv_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datafinish = true;
                promptSpeechInput();

            }
        });


//        mSwipeRefreshLayout = rowView.findViewById(R.id.activity_main_swipe_refresh_layout);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
//                    callApi();
//                    mSwipeRefreshLayout.setRefreshing(false);
//                } else {
//                    sucessDialog(getResources().getString(R.string.Internet_connection), context);
//                }
//
//            }
//        });








        callApi();



    }

    private void callApi() {

        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
            ApigetDirector();
        } else {
            sucessDialog(getResources().getString(R.string.Internet_connection), context);
        }
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
        String remainingUrl2 = DIRECTOR_LIST+"&organization_id=" + sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    private void filter(String text) {
        director_list2.clear();

//                keyword = s.toUpperCase();

        for (int i = 0; i < director_list.size(); i++) {
            if (director_list.get(i).getDirector_name().toLowerCase().contains(text.toLowerCase())) {
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    strSpeechText = inputSearch.getText().toString();

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (strSpeechText.length() == 0) {
                        inputSearch.setText(result.get(0));
                        strSpeechText = inputSearch.getText().toString();
                    } else if (strSpeechText.length() > 0 && strSpeechText != null) {
                        String temp = result.get(0);

                        inputSearch.setText(strSpeechText+" "+temp);

                        strSpeechText = inputSearch.getText().toString();
                    }
                    datafinish = false;
                }
                break;
            }

        }
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

            displayAlertDialogueWithOK(getResources().getString(R.string.app_name), getResources().getString(R.string.speech_not_supported));
        }
    }

    @SuppressWarnings({"deprecation", "unused"})
    private void displayAlertDialogueWithOK(String title, String Msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(Act_director_list.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(Msg);
        alertDialog.setCancelable(false);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alertDialog.show();
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
*/




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
