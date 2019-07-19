package visuotech.com.kampus.attendance.Activities.Hod;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.Model.Section;
import visuotech.com.kampus.attendance.Model.Semister;
import visuotech.com.kampus.attendance.Model.Subjects;
import visuotech.com.kampus.attendance.Model.TimeTableData;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.FileUtils;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;
import visuotech.com.kampus.attendance.retrofit.Utility;

public class Act_add_timetable extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String IMAGE_DIRECTORY_NAME = "Directorregistrstion";
    static Dialog d;
    private final int REQ_CODE_Gallery = 1;
    private final int REQ_CODE_Camera = 1888;
    public boolean datafinish = false;
    Spinner spinner_sem, spinner_section, spinner_day, spinner_faculty, spinner_subject;
    ArrayList<Semister> sem_list1;
    ArrayList<Section> section_list1;
    ArrayList<Faculty> faculty_list1;
    ArrayList<Subjects> subject_list1;
    ArrayList<TimeTableData> timeTableData_list;
    String department, gender, director, hod, prefix, course, scholarship, sem, section, day, faculty, fac_id, subject, subject_id;
    ImageView iv_profile_image, iv_cal_dob, iv_cal_doj;
    Button btn_add, btn_upload_image, btn_submit;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String> faculty_name_list = new ArrayList<>();
    ArrayList<String> subject_name_list = new ArrayList<>();
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    String other_device_active, user_typee, organization_id, user_id;
    EditText et_fname, et_lname, et_mname, et_email, et_mobile, et_address, et_enrol, et_fat_name, et_mot_name,
            et_emer_mobile, et_city, et_state, et_paddress, et_taddress, et_blood, et_caste, et_ssc, et_hsc, et_diploma;
    TextView tv_dob, tv_doa, tv_to, tv_from;
    String fname, lname, mname, email, mobileNumber, caste, city, state, admission_date, enrol_no, mot_name, fat_name, session_start, session_end,
            blood, hsc, ssc, diploma, emer_mobbile, dob, paddress, taddress, dept_id, semId, sectionId, hodId, courseId;
    LinearLayout container;
    FloatingActionButton btn_fab;
    TextView tv_starthrs, tv_endhrs, tv_startmin, tv_endmin;
    File file;
    Uri selectedImage;
    Bitmap mainBitmap;
    Uri tempUri;
    LinearLayout lay_start, lay_end;
    ArrayList<String> sem_list = new ArrayList<String>();
    ArrayList<String> section_list = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> starthrs = new ArrayList<>();
    ArrayList<String> endhrs = new ArrayList<>();
    ArrayList<String> startmin = new ArrayList<>();
    ArrayList<String> endmin = new ArrayList<>();
    ArrayList<String> facId = new ArrayList<>();
    ArrayList<String> subId = new ArrayList<>();
    String[] gender_list = {"Male", "Female", "others", "--Select gender--"};
    final int listsize = gender_list.length - 1;
    String[] day_list = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "--Select day--"};
    final int listsize2 = day_list.length - 1;
    int sel_fac;
    int sel_sub;
    Button btn_see;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    ArrayAdapter adapter_subject, adapter_faculty, adapter_sem, adapter_section;
    int cardNo = 0;
    EditText text;
    int year1 = Calendar.getInstance().get(Calendar.YEAR);
    String flag = "";
    int card_del;
    private BaseRequest baseRequest;
    private LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_timetable);

        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Timetable");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        parentLinearLayout = findViewById(R.id.parentLinearLayout);
        container = (LinearLayout) findViewById(R.id.container);
        btn_fab = findViewById(R.id.btn_fab);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        spinner_day = findViewById(R.id.spinner_day);
        spinner_section = findViewById(R.id.spinner_section);
        spinner_sem = findViewById(R.id.spinner_sem);
        spinner_subject = findViewById(R.id.spinner_subject);
        spinner_faculty = findViewById(R.id.spinner_faculty);
        tv_starthrs = findViewById(R.id.tv_starthrs);
        tv_endhrs = findViewById(R.id.tv_endhrs);
        tv_startmin = findViewById(R.id.tv_startmin);
        tv_endmin = findViewById(R.id.tv_endmin);
        btn_submit = findViewById(R.id.btn_submit);
        lay_end = findViewById(R.id.lay_end);
        lay_start = findViewById(R.id.lay_start);

        faculty_list1 = new ArrayList<>();
        subject_list1 = new ArrayList<>();
        section_list1 = new ArrayList<>();
        sem_list1 = new ArrayList<>();
        timeTableData_list = new ArrayList<>();


        spinner_day.setOnItemSelectedListener(this);
        spinner_section.setOnItemSelectedListener(this);
        spinner_sem.setOnItemSelectedListener(this);
        spinner_subject.setOnItemSelectedListener(this);
        spinner_faculty.setOnItemSelectedListener(this);

        ArrayAdapter adapter_day = new ArrayAdapter(this, android.R.layout.simple_spinner_item, day_list);
        adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapter_day);
        spinner_day.setSelection(listsize2);

        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNo = cardNo + 1;
                Toast.makeText(getApplicationContext(), String.valueOf(cardNo), Toast.LENGTH_SHORT).show();
                onAddField(v);
                tv_starthrs.setText("");
                tv_endhrs.setText("");
                tv_startmin.setText("");
                tv_endmin.setText("");


            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApiPostAddtimeTable();

            }
        });
        tv_starthrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTime(tv_starthrs, tv_startmin);

            }
        });

        tv_endhrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTime(tv_endhrs, tv_endmin);

            }
        });
        tv_startmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTime(tv_starthrs, tv_startmin);

            }
        });

        tv_endmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTime(tv_endhrs, tv_endmin);

            }
        });


        ApigetSemister();
        ApigetSection();
        ApigetFaculty();


    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.add_field, null);

        final TextView tv_no = (TextView) rowView.findViewById(R.id.tv_no);
        final TextView tv_fac = (TextView) rowView.findViewById(R.id.tv_fac);
        final TextView tv_sub = (TextView) rowView.findViewById(R.id.tv_sub);
        final TextView tv_starttime = (TextView) rowView.findViewById(R.id.tv_starttime);
        final TextView tv_endtime = (TextView) rowView.findViewById(R.id.tv_endtime);
        final ImageView iv_cross = (ImageView) rowView.findViewById(R.id.iv_cross);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LinearLayout) rowView.getParent()).removeView(rowView);
                tv_no.setText(cardNo + "");
                cardNo = cardNo - 1;
                flag = "yes";
                card_del = Integer.parseInt(tv_no.getText().toString());
            }
        });

        if (!flag.equals("")) {
            tv_no.setText(card_del + "");
            flag = "";
        } else {
            tv_no.setText(cardNo + "");
        }

        tv_starttime.setText(tv_starthrs.getText().toString() + ":" + tv_startmin.getText().toString());
        tv_endtime.setText(tv_endhrs.getText().toString() + ":" + tv_endmin.getText().toString());
        tv_fac.setText(faculty);
        tv_sub.setText(subject);


        TimeTableData timeTableData = new TimeTableData();
        for (int i = 0; i < cardNo; i++) {
            starthrs.add(tv_starthrs.getText().toString());
            endhrs.add(tv_endhrs.getText().toString());
            startmin.add(tv_startmin.getText().toString());
            endmin.add(tv_endmin.getText().toString());
            facId.add(fac_id);
            subId.add(subject_id);
        }

        timeTableData.setStarthrs(starthrs);
        timeTableData.setEndhrs(endhrs);
        timeTableData.setStartmin(startmin);
        timeTableData.setEndmin(endmin);
        timeTableData.setFac_id(facId);
        timeTableData.setSub_id(subId);

        Log.d("SIZE>>>>>", String.valueOf(timeTableData_list.size()));

        container.addView(rowView, container.getChildCount());

    }

    public void onDelete(View v) {
        container.removeView((View) v.getParent());
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_sem:
                sem = sem_list1.get(i).getSem();
                semId = sem_list1.get(i).getSem_id();
                subject_name_list.clear();
                ApigetSubject();
                break;

            case R.id.spinner_section:
                section = section_list1.get(i).getSection();
                sectionId = section_list1.get(i).getSection_id();
                break;

            case R.id.spinner_day:
                day = day_list[i];
                break;

            case R.id.spinner_faculty:
                faculty = faculty_list1.get(i).getFaculty_name();
                fac_id = faculty_list1.get(i).getFaculty_id();
                sel_fac = i;
                break;

            case R.id.spinner_subject:
                subject = subject_list1.get(i).getSubject_name();
                subject_id = subject_list1.get(i).getSubject_id();
                sel_sub = i;
                break;


        }

    }

    private void ApigetSemister() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");


                    sem_list1 = baseRequest.getDataList(jsonArray, Semister.class);

                    for (int i = 0; i < sem_list1.size(); i++) {

                        sem_list.add(sem_list1.get(i).getSem());

                    }

                    adapter_sem = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sem_list);
                    adapter_sem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_sem.setAdapter(adapter_sem);


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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=sem_list&organization_id=" + sessionParam.org_id + "&course_id=" + sessionParam.course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetSection() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");
                    section_list1 = baseRequest.getDataList(jsonArray, Section.class);
//
                    for (int i = 0; i < section_list1.size(); i++) {
                        section_list.add(section_list1.get(i).getSection());

                    }
                    adapter_section = new ArrayAdapter(context, android.R.layout.simple_spinner_item, section_list);
                    adapter_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_section.setAdapter(adapter_section);


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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=section_list&organization_id=" + sessionParam.org_id + "&course_id=" + sessionParam.course_id + "&department_id=" + sessionParam.dept_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void ApigetFaculty() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    faculty_list1 = baseRequest.getDataList(jsonArray, Faculty.class);
                    for (int i = 0; i < faculty_list1.size(); i++) {
                        faculty_name_list.add(faculty_list1.get(i).getFaculty_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }

                    adapter_faculty = new ArrayAdapter(context, android.R.layout.simple_spinner_item, faculty_name_list);
                    adapter_faculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_faculty.setAdapter(adapter_faculty);


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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=faculty_list&organization_id=" + sessionParam.org_id + "&department_id=" + sessionParam.dept_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetSubject() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    subject_list1 = baseRequest.getDataList(jsonArray, Subjects.class);
                    for (int i = 0; i < subject_list1.size(); i++) {
                        subject_name_list.add(subject_list1.get(i).getSubject_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }
                    adapter_subject = new ArrayAdapter(context, android.R.layout.simple_spinner_item, subject_name_list);
                    adapter_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_subject.setAdapter(adapter_subject);

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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=subject_list&organization_id=" + sessionParam.org_id + "&department_id=" + sessionParam.dept_id + "&course_id=" + sessionParam.course_id + "&sem_id=" + semId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApiPostAddtimeTable() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(object.toString());
                    String message = jsonObject.optString("message");
                    String sucessMessage = "Time table added sucessfully";
                    Log.d("RESULT", message);
                    Utility.sucessDialog(message, context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            }
        });

        JsonArray array1 = new JsonArray();
        for (int i = 0; i < starthrs.size(); i++) {
            array1.add(starthrs.get(i));
        }
        JsonArray array2 = new JsonArray();
        for (int i = 0; i < endhrs.size(); i++) {
            array2.add(endhrs.get(i));
        }
        JsonArray array3 = new JsonArray();
        for (int i = 0; i < startmin.size(); i++) {
            array3.add(startmin.get(i));
        }
        JsonArray array4 = new JsonArray();
        for (int i = 0; i < endmin.size(); i++) {
            array4.add(endmin.get(i));
        }
        JsonArray array5 = new JsonArray();
        for (int i = 0; i < facId.size(); i++) {
            array5.add(facId.get(i));
        }
        JsonArray array6 = new JsonArray();
        for (int i = 0; i < subId.size(); i++) {
            array6.add(subId.get(i));
        }
        JsonObject object = new JsonObject();
        object.add("starting_hour", array1);
        object.add("ending_hour", array2);
        object.add("starting_min", array3);
        object.add("ending_min", array4);
        object.add("faculty_id", array5);
        object.add("subject_id", array6);
        object.addProperty("course_id", sessionParam.course_id);
        object.addProperty("organization_id", sessionParam.org_id);
        object.addProperty("department_id", sessionParam.dept_id);
        object.addProperty("hod_id", sessionParam.userId);
        object.addProperty("director_id", sessionParam.director_id);
        object.addProperty("sem_id", semId);
        object.addProperty("section_id", sectionId);
        object.addProperty("day", day);

        baseRequest.callAPIPostCustomURL(1, object, "Kampus/Api2.php?apicall=add_timetable");

    }

    public void getTime(final TextView et_hrs, final TextView et_min) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(Act_add_timetable.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Toast.makeText(getApplicationContext(), selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
//                eReminderTime.setText();
                et_hrs.setText(selectedHour + "");
                et_min.setText(selectedMinute + "");
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_add_timetable.this, HOD_Act_home.class);
        startActivity(i);
        finish();
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_add_timetable.this, HOD_Act_home.class);
        startActivity(i);
        finish();
    }


}
