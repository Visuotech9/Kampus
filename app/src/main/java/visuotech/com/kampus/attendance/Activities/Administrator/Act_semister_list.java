package visuotech.com.kampus.attendance.Activities.Administrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import visuotech.com.kampus.attendance.Adapter.Ad_Semister_list;
import visuotech.com.kampus.attendance.Adapter.Ad_course;
import visuotech.com.kampus.attendance.Adapter.Ad_director;
import visuotech.com.kampus.attendance.Adapter.Ad_faculty;
import visuotech.com.kampus.attendance.Adapter.Ad_subject;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.Model.Semister;
import visuotech.com.kampus.attendance.Model.Subjects;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.RecyclerTouchListener;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

import static visuotech.com.kampus.attendance.Constants.COURSE_LIST;
import static visuotech.com.kampus.attendance.Constants.SEM_LIST;

//import android.widget.SearchView;

public class Act_semister_list extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Ad_course adapter;
    Ad_Semister_list adapter1;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;
    LinearLayout container;
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    EditText inputSearch;

    ArrayList<String> faculty_name_list = new ArrayList<>();
    ArrayList<Semister> sem_list2 = new ArrayList<>();
    ArrayList<Semister> sem_list = new ArrayList<>();
    ArrayList<Course> course_list = new ArrayList<>();
    ArrayList<String> course_list1 = new ArrayList<>();
    ImageView iv_add;
    TextView tv_station,tv_message, tv_sem1, tv_sem2, tv_sem3, tv_sem4, tv_sem5, tv_sem6, tv_sem7, tv_sem8;
    Dialog mDialog;
    TextView tv_alert;
    String dept_name, course, cour_id;
    Spinner spinner_course;
    private BaseRequest baseRequest;
    private String keyword, course_id, course_name;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public boolean datafinish = false;
    String strSpeechText;
    CardView profileCard;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_basic);

         toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Semisters");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        profileCard =  findViewById(R.id.profileCard);


        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.act_semister_list, null);
        container.addView(rowView, container.getChildCount());

        tv_station = rowView.findViewById(R.id.tv_station);
        tv_message = rowView.findViewById(R.id.tv_message);
        rv_list = rowView.findViewById(R.id.rv_list);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());

//-------------------------recyclerview------------------------------------------

        tv_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sucessDialog1(context, tv_station, course_list);
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.GONE);
                profileCard.setVisibility(View.VISIBLE);
            }
        });


        ImageView search_icon=findViewById(R.id.search_icon);
        EditText inputSearch=findViewById(R.id.inputSearch);
        ImageView iv_voice=findViewById(R.id.iv_voice);
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



        ApigetCourse();


    }

    public void sucessDialog1(Context context, final TextView tv_station1, final ArrayList<Course> courses_list) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.search_layout_dialog);
        mDialog.setCanceledOnTouchOutside(true);


        TextView tv_retry;
        LinearLayout lay;
        EditText inputSearch;
        RecyclerView rv;
        rv = mDialog.findViewById(R.id.rv_list);
        tv_retry = mDialog.findViewById(R.id.tv_retry);
        inputSearch = mDialog.findViewById(R.id.inputSearch);
        TextView tv_title = mDialog.findViewById(R.id.tv_title);
        tv_title.setText("Select Course");
        tv_retry.setText("OK");
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new Ad_course(courses_list, context);
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int j = position + 1;

                tv_station1.setText(courses_list.get(position).getCourse_name());
                course_id = courses_list.get(position).getCourse_id();
                course_name = courses_list.get(position).getCourse_name();

                ApigetSemister(course_id, course_name);
                mDialog.cancel();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

//        inputSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                filter1(editable.toString(), adapter, sp_list);
//            }
//        });


        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
//                tv_station1.setEnabled(true);


            }
        });
        mDialog.show();


    }

    private void ApigetCourse() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    course_list = baseRequest.getDataListreverse(jsonArray, Course.class);
                    for (int i = 0; i < course_list.size(); i++) {
                        course_list1.add(course_list.get(i).getCourse_name());
                    }


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
        String remainingUrl2 =COURSE_LIST+"&organization_id=" + sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    private void ApigetSemister(String course_id, final String course_name) {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    if (jsonObject.optString("message").equals("No Records Found")){
                        rv_list.setVisibility(View.GONE);
                        tv_message.setVisibility(View.VISIBLE);
                    }else {
                        rv_list.setVisibility(View.VISIBLE);
                        tv_message.setVisibility(View.GONE);
                        sem_list = baseRequest.getDataList(jsonArray, Semister.class);
                        adapter1 = new Ad_Semister_list(sem_list, context, course_name);
                        rv_list.setAdapter(adapter1);
                    }





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
        String remainingUrl2 = SEM_LIST+"&organization_id=" + sessionParam.org_id + "&course_id=" + course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_course:
                course = course_list.get(i).getCourse_name();
                cour_id = course_list.get(i).getCourse_id();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(Act_semister_list.this, Administrator_Act_home.class);
                startActivity(i);
                finish();
                break;

            case R.id.add_user:
                Intent i2 = new Intent(Act_semister_list.this, Act_add_semister.class);
                startActivity(i2);
                finish();
                break;
        }

        return true;

//        return super.onOptionsItemSelected(item);
    }


/*
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
*/


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
        AlertDialog alertDialog = new AlertDialog.Builder(Act_semister_list.this).create();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_semister_list.this, Administrator_Act_home.class);
        startActivity(i);
        finish();
    }
}
