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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
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
import visuotech.com.kampus.attendance.retrofit.RequestReciever;
import visuotech.com.kampus.attendance.retrofit.Utility;

public class Act_add_timetable extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spinner_sem,spinner_section,spinner_day,spinner_faculty,spinner_subject;
    ArrayList<Semister>sem_list1;
    ArrayList<Section>section_list1;
    ArrayList<Faculty>faculty_list1;
    ArrayList<Subjects>subject_list1;
    ArrayList<TimeTableData>timeTableData_list;

    String department,gender,director,hod,prefix,course,scholarship,sem,section,day,faculty,fac_id,subject,subject_id;
    ImageView iv_profile_image,iv_cal_dob,iv_cal_doj;
    Button btn_add,btn_upload_image,btn_submit;
    private BaseRequest baseRequest;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String>faculty_name_list=new ArrayList<>();
    ArrayList<String>subject_name_list=new ArrayList<>();

    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    String other_device_active,user_typee,organization_id,user_id;
    EditText et_fname,et_lname,et_mname,et_email,et_mobile,et_address,et_enrol,et_fat_name,et_mot_name,
            et_emer_mobile,et_city,et_state,et_paddress,et_taddress,et_blood,et_caste,et_ssc,et_hsc,et_diploma;
    TextView tv_dob,tv_doa,tv_to,tv_from;
    String fname,lname,mname,email,mobileNumber,caste,city,state,admission_date,enrol_no,mot_name,fat_name,session_start,session_end,
            blood,hsc,ssc,diploma,emer_mobbile,dob,paddress,taddress,dept_id,semId,sectionId,hodId,courseId;
    private LinearLayout parentLinearLayout;
    LinearLayout container;
    FloatingActionButton btn_fab;
    TextView tv_starthrs,tv_endhrs,tv_startmin,tv_endmin;
    File file;
    public boolean datafinish = false;
    private final int REQ_CODE_Gallery = 1;
    Uri selectedImage;
    private final int REQ_CODE_Camera = 1888;
    Bitmap mainBitmap;
    Uri tempUri;
    LinearLayout lay_start,lay_end;
    private static final String IMAGE_DIRECTORY_NAME = "Directorregistrstion";


    ArrayList<String>  sem_list= new ArrayList<String>();

    ArrayList<String>  section_list= new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();

//    ArrayList<String>starthrs=new ArrayList<>();
//    ArrayList<String>endhrs=new ArrayList<>();
//    ArrayList<String>startmin=new ArrayList<>();
//    ArrayList<String>endmin=new ArrayList<>();
//    ArrayList<String>facId=new ArrayList<>();
//    ArrayList<String>subId=new ArrayList<>();

    String[] starthrs;
    String[] endhrs;
    String[] startmin;
    String[] endmin;
    String[] facId;
    String[] subId;

//    String[] starthrs = { "11", "11", "11"};
//    String[] endhrs = { "11", "11", "11"};
//    String[] startmin = { "11", "11", "11"};
//    String[] endmin = { "11", "11", "11"};
//    String[] facId = { "11", "11", "11"};
//    String[] subId = { "11", "11", "11"};








    String[] gender_list = { "Male", "Female", "others","--Select gender--"};
    String[] day_list = { "Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","--Select day--"};
    final int listsize = gender_list.length - 1;
    final int listsize2 = day_list.length - 1;
    int sel_fac;
    int sel_sub;

  Button btn_see;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    ArrayAdapter adapter_subject,adapter_faculty,adapter_sem,adapter_section;
    int cardNo=0;
    EditText text;

    static Dialog d ;
    int year1 = Calendar.getInstance().get(Calendar.YEAR);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_timetable);

        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Timetable");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentLinearLayout =  findViewById(R.id.parentLinearLayout);
        container = (LinearLayout)findViewById(R.id.container);
        btn_fab=findViewById(R.id.btn_fab);

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

        faculty_list1=new ArrayList<>();
        subject_list1=new ArrayList<>();
        section_list1=new ArrayList<>();
        sem_list1=new ArrayList<>();
        timeTableData_list=new ArrayList<>();

//        starthrs.clear();
//        startmin.clear();
//        endhrs.clear();
//        endmin.clear();
//        facId.clear();
//        subId.clear();


        spinner_day.setOnItemSelectedListener(this);
        spinner_section.setOnItemSelectedListener(this);
        spinner_sem.setOnItemSelectedListener(this);
        spinner_subject.setOnItemSelectedListener(this);
        spinner_faculty.setOnItemSelectedListener(this);

        ArrayAdapter adapter_day = new ArrayAdapter(this,android.R.layout.simple_spinner_item,day_list);
        adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapter_day);
        spinner_day.setSelection(listsize2);

        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNo=cardNo+1;
                Toast.makeText(getApplicationContext(),String.valueOf(cardNo),Toast.LENGTH_SHORT).show();
                onAddField(v);



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

                getTime(tv_starthrs,tv_startmin);

            }
        });

        tv_endhrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTime(tv_endhrs,tv_endmin);

            }
        });
        tv_startmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTime(tv_starthrs,tv_startmin);

            }
        });

        tv_endmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTime(tv_endhrs,tv_endmin);

            }
        });



        ApigetSemister();
        ApigetSection();
        ApigetFaculty();


    }
    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.test1, null);
//        final TextView et_starthrs1 = (TextView)rowView.findViewById(R.id.et_starthrs1);
//        final TextView et_endhrs1 = (TextView)rowView.findViewById(R.id.et_endhrs1);
//        final TextView et_startmin1 = (TextView)rowView.findViewById(R.id.et_startmin1);
//        final TextView et_endmin1 = (TextView)rowView.findViewById(R.id.et_endmin1);

        final TextView tv_no = (TextView)rowView.findViewById(R.id.tv_no);
        final TextView tv_fac = (TextView)rowView.findViewById(R.id.tv_fac);
        final TextView tv_sub = (TextView)rowView.findViewById(R.id.tv_sub);
        final TextView tv_starttime = (TextView)rowView.findViewById(R.id.tv_starttime);
        final TextView tv_endtime = (TextView)rowView.findViewById(R.id.tv_endtime);
        final ImageView iv_cross = (ImageView)rowView.findViewById(R.id.iv_cross);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LinearLayout)rowView.getParent()).removeView(rowView);
                tv_no.setText(cardNo+"");
            }
        });



//        final Spinner spinner_faculty1 = (Spinner)rowView.findViewById(R.id.spinner_faculty1);
//        final Spinner spinner_subject1 = (Spinner)rowView.findViewById(R.id.spinner_subject1);
        tv_no.setText(cardNo+"");
        tv_starttime.setText(tv_starthrs.getText().toString()+":"+tv_startmin.getText().toString());
        tv_endtime.setText(tv_endhrs.getText().toString()+":"+tv_endmin.getText().toString());
        tv_fac.setText(faculty);
        tv_sub.setText(subject);

//        ArrayAdapter adapter_faculty1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,faculty_name_list);
//        adapter_faculty1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_faculty1.setAdapter(adapter_faculty1);
//        spinner_faculty1.setSelection(sel_fac);

//        ArrayAdapter adapter_subject1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,subject_name_list);
//        adapter_subject1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_subject1.setAdapter(adapter_subject1);
//        spinner_subject1.setSelection(sel_sub);

        TimeTableData timeTableData=new TimeTableData();

        starthrs = new String[cardNo];
        endhrs = new String[cardNo];
        startmin = new String[cardNo];
        endmin = new String[cardNo];
        subId = new String[cardNo];
        facId = new String[cardNo];

        for (int i=0;i<cardNo;i++){

            starthrs[i] = tv_starthrs.getText().toString();
            endhrs[i] = tv_endhrs.getText().toString();
            startmin[i] = tv_startmin.getText().toString();
            endmin[i] = tv_endmin.getText().toString();
            subId[i] = subject_id;
            facId[i] = fac_id;

//            starthrs.add(i,et_starthrs.getText().toString());
//            endhrs.add(i,et_endhrs.getText().toString());
//            startmin.add(i,et_startmin.getText().toString());
//            endmin.add(i,et_endmin.getText().toString());
//            facId.add(i,fac_id);
//            subId.add(i,subject_id);

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
        switch(adapterView.getId()){

            case R.id.spinner_sem :
                sem=sem_list1.get(i).getSem();
                semId=sem_list1.get(i).getSem_id();
                subject_name_list.clear();
                ApigetSubject();
                break;

            case R.id.spinner_section :
                section=section_list1.get(i).getSection();
                sectionId=section_list1.get(i).getSection_id();
                break;

            case R.id.spinner_day :
                day=day_list[i];
                break;

            case R.id.spinner_faculty :
                faculty=faculty_list1.get(i).getFaculty_name();
                fac_id=faculty_list1.get(i).getFaculty_id();
                sel_fac=i;
                break;

            case R.id.spinner_subject :
                subject=subject_list1.get(i).getSubject_name();
                subject_id=subject_list1.get(i).getSubject_id();
                sel_sub=i;
                break;

//            case R.id.spinner_faculty1 :
//                sem=sem_list1.get(i).getSem();
//                semId=sem_list1.get(i).getSem_id();
//                ApigetSubject();
//                break;
//
//            case R.id.spinner_subject1 :
//                sem=sem_list1.get(i).getSem();
//                semId=sem_list1.get(i).getSem_id();
//                ApigetSubject();
//                break;
        }

    }

    private void ApigetSemister(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");


                    sem_list1=baseRequest.getDataList(jsonArray,Semister.class);

                    for (int i=0;i<sem_list1.size();i++){
                        sem_list.add(sem_list1.get(i).getSem());

                    }
                    adapter_sem = new ArrayAdapter(context,android.R.layout.simple_spinner_item,sem_list);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=sem_list&organization_id="+sessionParam.org_id+"&course_id="+sessionParam.course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }
    private void ApigetSection(){
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");
                    section_list1=baseRequest.getDataList(jsonArray,Section.class);
//
                    for (int i=0;i<section_list1.size();i++){
                        section_list.add(section_list1.get(i).getSection());

                    }
                    adapter_section = new ArrayAdapter(context,android.R.layout.simple_spinner_item,section_list);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=section_list&organization_id="+sessionParam.org_id+"&course_id="+sessionParam.course_id+"&department_id="+sessionParam.dept_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void ApigetFaculty(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    faculty_list1=baseRequest.getDataList(jsonArray,Faculty.class);
                    for (int i=0;i<faculty_list1.size();i++){
                        faculty_name_list.add(faculty_list1.get(i).getFaculty_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }

                    adapter_faculty = new ArrayAdapter(context,android.R.layout.simple_spinner_item,faculty_name_list);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=faculty_list&organization_id="+sessionParam.org_id+"&department_id="+sessionParam.dept_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetSubject(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    subject_list1=baseRequest.getDataList(jsonArray,Subjects.class);
                    for (int i=0;i<subject_list1.size();i++){
                        subject_name_list.add(subject_list1.get(i).getSubject_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }
                    adapter_subject = new ArrayAdapter(context,android.R.layout.simple_spinner_item,subject_name_list);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=subject_list&organization_id="+sessionParam.org_id+"&department_id="+sessionParam.dept_id+"&course_id="+sessionParam.course_id+"&sem_id="+semId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApiPostAddtimeTable() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
//                iv_profile_image.setImageBitmap(null);
//                et_title.setText("");
//                et_description.setText("");
////                Toast.makeText(getApplicationContext(),"sucess",Toast.LENGTH_SHORT).show();
                String sucessMessage="Time table added sucessfully";
                Utility.sucessDialog(sucessMessage,context);

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

//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);


//        body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
      /*  @PUT("/")
        @Multipart
        Call<ResponseBody> uploadPhotos(
                @Part MultipartBody.Part placeId,
                @Part MultipartBody.Part name,
                @Part List<MultipartBody.Part> desclist, // <-- use such for list of same parameter
                @Part List<MultipartBody.Part> files  // <-- multiple photos here
);*/

        RequestBody semId_ = RequestBody.create(MediaType.parse("text/plain"), semId);
        RequestBody sectionId_ = RequestBody.create(MediaType.parse("text/plain"), sectionId);
        RequestBody day_ = RequestBody.create(MediaType.parse("text/plain"), day);
        RequestBody org_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.org_id);
        RequestBody course_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.course_id);
        RequestBody dept_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.dept_id);
        RequestBody userId_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.userId);
        RequestBody hod_director_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.hod_director_id);




        baseRequest.callAPIAddTimeTable(1,"http://collectorexpress.in/",semId_,sectionId_,day_,org_id_
                ,course_id_,dept_id_,userId_,hod_director_id_,starthrs,endhrs,startmin,endmin,facId,subId);
    }




    public void getTime(final TextView et_hrs, final TextView et_min){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(Act_add_timetable.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Toast.makeText(getApplicationContext(), selectedHour + ":" + selectedMinute,Toast.LENGTH_SHORT).show();
//                eReminderTime.setText();
                et_hrs.setText(selectedHour+"");
                et_min.setText(selectedMinute+"");
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
