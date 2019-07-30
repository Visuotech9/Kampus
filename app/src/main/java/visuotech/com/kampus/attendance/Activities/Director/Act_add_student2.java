package visuotech.com.kampus.attendance.Activities.Director;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_add_student;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_student_list;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Department;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.HOD;
import visuotech.com.kampus.attendance.Model.Section;
import visuotech.com.kampus.attendance.Model.Semister;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;
import visuotech.com.kampus.attendance.retrofit.Utility;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static visuotech.com.kampus.attendance.Constants.COURSE_LIST;
import static visuotech.com.kampus.attendance.Constants.DEPT_LIST;
import static visuotech.com.kampus.attendance.Constants.HOD_LIST;
import static visuotech.com.kampus.attendance.Constants.SECTION_LIST;
import static visuotech.com.kampus.attendance.Constants.SEM_LIST;
import static visuotech.com.kampus.attendance.retrofit.WebServiceConstants.BASE_URL;

public class Act_add_student2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String IMAGE_DIRECTORY_NAME = "Directorregistrstion";
    static Dialog d ;
    private final int REQ_CODE_Gallery = 1;
    private final int REQ_CODE_Camera = 1888;
    public boolean datafinish = false;
    Spinner spinner_department,spinner_gender,spinner_sem,spinner_section,spinner_hod,spinner_scholarship,spinner_course;
    ArrayList<Department> department_list1;
    ArrayList<Director>director_list1;
    ArrayList<HOD>hod_list1;
    ArrayList<Course>courses_list1;
    ArrayList<Semister>sem_list1;
    ArrayList<Section>section_list1;
    String department,gender,director,hod,prefix,course,scholarship,sem,section;
    ImageView iv_profile_image,iv_cal_dob,iv_cal_doj;
    Button btn_add,btn_upload_image;
    SwipeRefreshLayout mSwipeRefreshLayout;
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
    File file;
    Uri selectedImage;
    Bitmap mainBitmap;
    Uri tempUri;
    ArrayList<String>  department_list= new ArrayList<String>();
    ArrayList<String>  department_id= new ArrayList<String>();

    ArrayList<String>  hod_list= new ArrayList<String>();
    ArrayList<String>  hod_id= new ArrayList<String>();

    ArrayList<String>  course_list= new ArrayList<String>();
    ArrayList<String>  course_id= new ArrayList<String>();

    ArrayList<String>  sem_list= new ArrayList<String>();

    ArrayList<String>  section_list= new ArrayList<String>();

    String[] gender_list = { "Male", "Female", "others","--Select gender--"};
    final int listsize = gender_list.length - 1;
    String[] scholarship_list = { "Yes", "No", "--Select gender--"};
    final int listsize2 = scholarship_list.length - 1;


    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    ArrayAdapter adapter_director,adapter_department,adapter_hod,adapter_course,adapter_sem,adapter_section;
    int year1 = Calendar.getInstance().get(Calendar.YEAR);
    private BaseRequest baseRequest;

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //  Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Add Student");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_add_student2, null);
        container.addView(rowView, container.getChildCount());

        user_typee= sessionParam.user_type;
        user_id= sessionParam.userId;
        organization_id=sessionParam.org_id;

        btn_add =  rowView.findViewById(R.id.btn_add);
        et_fname =  rowView.findViewById(R.id.et_fname);
        et_lname =  rowView.findViewById(R.id.et_lname);
        et_mname =  rowView.findViewById(R.id.et_mname);
        et_email =  rowView.findViewById(R.id.et_email);
        et_mobile =  rowView.findViewById(R.id.et_mobile);
        tv_dob =  rowView.findViewById(R.id.tv_dob);
        tv_doa =  rowView.findViewById(R.id.tv_doa);
        et_paddress =  rowView.findViewById(R.id.et_paddress);
        et_taddress =  rowView.findViewById(R.id.et_taddress);
        iv_profile_image =  rowView.findViewById(R.id.iv_profile_image);
        iv_cal_dob =  rowView.findViewById(R.id.iv_cal_dob);
        iv_cal_doj =  rowView.findViewById(R.id.iv_cal_doj);
        et_enrol =  rowView.findViewById(R.id.et_enrol);
        et_fat_name =  rowView.findViewById(R.id.et_fat_name);
        et_mot_name =  rowView.findViewById(R.id.et_mot_name);
        et_emer_mobile =  rowView.findViewById(R.id.et_emer_mobile);
        et_city =  rowView.findViewById(R.id.et_city);
        et_state =  rowView.findViewById(R.id.et_state);
        et_blood =  rowView.findViewById(R.id.et_blood);
        et_caste =  rowView.findViewById(R.id.et_caste);
        et_ssc =  rowView.findViewById(R.id.et_ssc);
        et_hsc =  rowView.findViewById(R.id.et_hsc);
        et_diploma =  rowView.findViewById(R.id.et_diploma);
        btn_upload_image =  rowView.findViewById(R.id.btn_upload_image);

        tv_from =  rowView.findViewById(R.id.tv_from);
        tv_to =  rowView.findViewById(R.id.tv_to);

        spinner_department = rowView.findViewById(R.id.spinner_department);
        spinner_gender = rowView.findViewById(R.id.spinner_gender);
        spinner_hod = rowView.findViewById(R.id.spinner_hod);
//        spinner_course = (Spinner) findViewById(R.id.spinner_course);
        spinner_scholarship = rowView.findViewById(R.id.spinner_scholarship);
        spinner_sem = rowView.findViewById(R.id.spinner_sem);
        spinner_section = rowView.findViewById(R.id.spinner_section);

        spinner_department.setOnItemSelectedListener(this);
        spinner_gender.setOnItemSelectedListener(this);
        spinner_hod.setOnItemSelectedListener(this);
//        spinner_course.setOnItemSelectedListener(this);
        spinner_scholarship.setOnItemSelectedListener(this);
        spinner_sem.setOnItemSelectedListener(this);
        spinner_section.setOnItemSelectedListener(this);

        department_list1=new ArrayList<>();
        hod_list1=new ArrayList<>();
        courses_list1=new ArrayList<>();
        section_list1=new ArrayList<>();
        sem_list1=new ArrayList<>();


        ArrayAdapter adapter_gender = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender_list);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter_gender);
        spinner_gender.setSelection(listsize);

        ArrayAdapter adapter_scholarship = new ArrayAdapter(this,android.R.layout.simple_spinner_item,scholarship_list);
        adapter_scholarship.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_scholarship.setAdapter(adapter_scholarship);
        spinner_scholarship.setSelection(listsize2);

//        ApigetCourse();
        ApigetDepartment();
        ApigetSemister();

        tv_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message="Select start year";
                showYearDialog(message,tv_from);
            }
        });
        tv_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message="Select end year";
                showYearDialog(message,tv_to);
            }
        });


        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datafinish = true;
                if (!marshMallowPermission.checkPermissionForCamera() && !marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForCamera();
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else{
                    selectImage();
                }

            }
        });
        iv_cal_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Act_add_student2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv_dob.setText(day + "-" + (month + 1) + "-" + year);
                            }
                        }, year, month, dayOfMonth);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });
        iv_cal_doj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Act_add_student2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv_doa.setText(day + "-" + (month + 1) + "-" + year);
                            }
                        }, year, month, dayOfMonth);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String MobilePattern = "[0-9]{10}";
                String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                if (et_enrol.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter enrolment no.", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (et_enrol.getText().toString().length() > 12) {
//                    Toast.makeText(getApplicationContext(), "Enrolment no must be of 12 characters", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (et_fname.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter first name", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (et_fname.getText().toString().length() > 25) {
//                    Toast.makeText(getApplicationContext(), "Please enter less than 25 characters.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (et_lname.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter last name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_lname.getText().toString().length() > 25) {
                    Toast.makeText(getApplicationContext(), "Please enter less than 25 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Utility.checkEmail(et_email.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Please enter valied email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_mobile.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!et_mobile.getText().toString().matches(MobilePattern)) {
                    Toast.makeText(getApplicationContext(), "Please enter valid 10 digit mobile number", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (et_emer_mobile.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter emergency contact number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!et_emer_mobile.getText().toString().matches(MobilePattern)) {
                    Toast.makeText(getApplicationContext(), "Please enter valid 10 digit mobile number", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (tv_dob.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter date of birth", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tv_doa.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter date of admission", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_paddress.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter parmanent address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_taddress.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter temprary address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_fat_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter fathers name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_mot_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter mothers name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_mot_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter mothers name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_city.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter current city", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_state.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter current state", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_blood.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter blood group", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_caste.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter caste", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_ssc.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter ssc marks", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    fname = et_fname.getText().toString();
                    mname = et_mname.getText().toString();
                    lname = et_lname.getText().toString();
                    email = et_email.getText().toString();
                    mobileNumber = et_mobile.getText().toString();
                    emer_mobbile = et_emer_mobile.getText().toString();
                    taddress = et_taddress.getText().toString();
                    paddress = et_paddress.getText().toString();
                    dob = tv_dob.getText().toString();
                    admission_date = tv_doa.getText().toString();
                    caste =et_caste.getText().toString();
                    blood =et_blood.getText().toString();
                    fat_name =et_fat_name.getText().toString();
                    mot_name =et_mot_name.getText().toString();
                    city =et_city.getText().toString();
                    state =et_state.getText().toString();
                    enrol_no =et_enrol.getText().toString();
                    session_start =tv_from.getText().toString();
                    session_end =tv_to.getText().toString();
                    hsc =et_hsc.getText().toString();
                    ssc =et_ssc.getText().toString();
                    diploma =et_diploma.getText().toString();



                    if (NetworkConnection.checkNetworkStatus(context)==true){
//
                        if (file!=null){
                            ApiPostAddStudent();
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Select Any Image", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });



    }

    private void ApiPostAddStudent() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
//                iv_profile_image.setImageBitmap(null);
//                et_title.setText("");
//                et_description.setText("");
////                Toast.makeText(getApplicationContext(),"sucess",Toast.LENGTH_SHORT).show();
                String sucessMessage="Student added sucessfully";
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

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body =MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody fname_ = RequestBody.create(MediaType.parse("text/plain"), fname);
        RequestBody mname_ = RequestBody.create(MediaType.parse("text/plain"), mname);
        RequestBody lname_ = RequestBody.create(MediaType.parse("text/plain"), lname);
        RequestBody email_ = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody mobile_no_ = RequestBody.create(MediaType.parse("text/plain"), mobileNumber);
        RequestBody emer_mobbile_ = RequestBody.create(MediaType.parse("text/plain"), emer_mobbile);
        RequestBody paddress_ = RequestBody.create(MediaType.parse("text/plain"), paddress);
        RequestBody taddress_ = RequestBody.create(MediaType.parse("text/plain"), taddress);
//        RequestBody cover_pic_ = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mainBitmap));
        RequestBody dob_ = RequestBody.create(MediaType.parse("text/plain"), Utility.changeDateDMYtoYMD(dob));
        RequestBody admission_date_= RequestBody.create(MediaType.parse("text/plain"), Utility.changeDateDMYtoYMD(admission_date));
        RequestBody fat_name_ = RequestBody.create(MediaType.parse("text/plain"), fat_name);
        RequestBody mot_name_ = RequestBody.create(MediaType.parse("text/plain"), mot_name);
        RequestBody enrol_no_ = RequestBody.create(MediaType.parse("text/plain"), enrol_no);
        RequestBody session_start_ = RequestBody.create(MediaType.parse("text/plain"), session_start);
        RequestBody session_end_ = RequestBody.create(MediaType.parse("text/plain"), session_end);
        RequestBody scholarship_ = RequestBody.create(MediaType.parse("text/plain"), scholarship);
        RequestBody caste_ = RequestBody.create(MediaType.parse("text/plain"), caste);
        RequestBody blood_ = RequestBody.create(MediaType.parse("text/plain"), blood);
        RequestBody gender_ = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody dept_id_ = RequestBody.create(MediaType.parse("text/plain"), dept_id);
        RequestBody organization_id_ = RequestBody.create(MediaType.parse("text/plain"), organization_id);
        RequestBody courseId_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.course_id);
        RequestBody hodId__ = RequestBody.create(MediaType.parse("text/plain"), hodId);
        RequestBody semId_ = RequestBody.create(MediaType.parse("text/plain"), semId);
        RequestBody sectionId_ = RequestBody.create(MediaType.parse("text/plain"), sectionId);
        RequestBody city_ = RequestBody.create(MediaType.parse("text/plain"), city);
        RequestBody state_ = RequestBody.create(MediaType.parse("text/plain"), state);
        RequestBody hsc_ = RequestBody.create(MediaType.parse("text/plain"), hsc);
        RequestBody ssc_ = RequestBody.create(MediaType.parse("text/plain"), ssc);
        RequestBody diploma_ = RequestBody.create(MediaType.parse("text/plain"), diploma);






        baseRequest.callAPIAddstudent(1,BASE_URL,body,fname_,mname_,lname_,email_,mobile_no_,emer_mobbile_,paddress_,taddress_
                ,dob_,admission_date_,fat_name_,mot_name_,enrol_no_,session_start_,session_end_,scholarship_,caste_,blood_
                ,gender_,dept_id_,organization_id_,courseId_,hodId__,semId_,sectionId_,city_,state_,hsc_,ssc_,diploma_);
    }

    private void ApigetCourse(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");


                    courses_list1=baseRequest.getDataList(jsonArray,Course.class);

                    for (int i=0;i<courses_list1.size();i++){
                        course_list.add(courses_list1.get(i).getCourse_name());
                        course_id.add(courses_list1.get(i).getCourse_id());
                    }
                    adapter_course = new ArrayAdapter(context,android.R.layout.simple_spinner_item,course_list);
                    adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_course.setAdapter(adapter_course);




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
        String remainingUrl2=COURSE_LIST+"&organization_id="+organization_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetDepartment(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");


                    department_list1=baseRequest.getDataList(jsonArray,Department.class);

                    for (int i=0;i<department_list1.size();i++){
                        department_list.add(department_list1.get(i).getDepartment_name());
                        department_id.add(department_list1.get(i).getDepartment_id());
                    }
                    adapter_department = new ArrayAdapter(context,android.R.layout.simple_spinner_item,department_list);
                    adapter_department.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_department.setAdapter(adapter_department);




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
        String remainingUrl2=DEPT_LIST+"&organization_id="+organization_id+"&course_id="+sessionParam.course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
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
        String remainingUrl2=SEM_LIST+"&organization_id="+organization_id+"&course_id="+sessionParam.course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetHod(){
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");
                    hod_list1=baseRequest.getDataList(jsonArray,HOD.class);
//
                    for (int i=0;i<hod_list1.size();i++){
                        hod_list.add(hod_list1.get(i).getHod_name());
                        hod_id.add(hod_list1.get(i).getHod_id());
                    }
                    adapter_hod = new ArrayAdapter(context,android.R.layout.simple_spinner_item,hod_list);
                    adapter_hod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_hod.setAdapter(adapter_hod);




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
        String remainingUrl2=HOD_LIST+"&organization_id="+organization_id+"&department_id="+dept_id+"&course_id="+sessionParam.course_id;
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
        String remainingUrl2=SECTION_LIST+"&organization_id="+organization_id+"&course_id="+sessionParam.course_id+"&department_id="+dept_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){

//            case R.id.spinner_course :
//                //Your Action Here.
//
//                course=course_list.get(i);
//                courseId=course_id.get(i);
//                department_list.clear();
//                sem_list.clear();
//                ApigetDepartment();
//                ApigetSemister();
//                Toast.makeText(getApplicationContext(),department, Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(),dept_id, Toast.LENGTH_LONG).show();
//                break;

            case R.id.spinner_department :
                //Your Action Here.

                department=department_list.get(i);
                dept_id=department_id.get(i);
                hod_list.clear();
                section_list.clear();
                ApigetHod();
                ApigetSection();
                break;


            case R.id.spinner_hod :
                hod=hod_list1.get(i).getHod_name();
                hodId=hod_list1.get(i).getHod_id();
                break;

            case R.id.spinner_sem :
                sem=sem_list1.get(i).getSem();
                semId=sem_list1.get(i).getSem_id();
                break;

            case R.id.spinner_section :
                section=section_list1.get(i).getSection();
                sectionId=section_list1.get(i).getSection_id();
                break;

            case R.id.spinner_gender :

                gender=gender_list[i];

                break;

            case R.id.spinner_scholarship :
                scholarship=scholarship_list[i];
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Act_add_student2.this);
        builder.setTitle("Add Photo");
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQ_CODE_Camera);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (pickPhoto.resolveActivity(getPackageManager()) != null) {
                        //Device has no app that handles gallery intent
                        startActivityForResult(pickPhoto, REQ_CODE_Gallery);
                    }else{
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Choose from Gallery"), REQ_CODE_Gallery);
                    }
//                    try {
//                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(pickPhoto, REQ_CODE_Gallery);
//
//                    } catch (ActivityNotFoundException e) {
//                        e.printStackTrace();
//                    }

                } else if (options[item].equals("Cancel")) {
                    datafinish = false;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_Gallery: {
                try {
                    selectedImage = data.getData();
                    mainBitmap = MediaStore.Images.Media.getBitmap(Act_add_student2.this.getContentResolver(), selectedImage);

//                System.out.println("*** gallery"+Utility._user_image_base64);

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    // Get file from file name
                    file = new File(filePath);
                    // Get length of file in bytes
                    long fileSizeInBytes = file.length();
                    // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;

                    mainBitmap = ShrinkBitmap(filePath, 550, 550);
                    Utility._user_image_base64 = Utility.bitmapToBase64(mainBitmap);
                    datafinish = false;
                    iv_profile_image.setImageBitmap(mainBitmap);
//                    iv_profile_camera.setVisibility(View.GONE);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
            case REQ_CODE_Camera: {
                if (resultCode == RESULT_OK) {


                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    tempUri = getImageUri(getApplicationContext(), photo);
                    file = new File(getRealPathFromURI(tempUri));
                    mainBitmap = ShrinkBitmap(getRealPathFromURI(tempUri), 600, 600);
                    Utility._user_image_base64 = Utility.bitmapToBase64(mainBitmap);
                    datafinish = false;
                    iv_profile_image.setImageBitmap(mainBitmap);
//                    iv_profile_camera.setVisibility(View.GONE);
                }
                break;
            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    Bitmap ShrinkBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    public void showYearDialog(String message, final TextView tv_session)
    {

        final Dialog d = new Dialog(Act_add_student2.this);
        d.setTitle("Year Picker");
        d.setContentView(R.layout.yeardialog);
        LinearLayout lay_set =  d.findViewById(R.id.lay_set);
        TextView tv_select= d.findViewById(R.id.tv_select);
        tv_select.setText(message);
        final NumberPicker nopicker = d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year1+50);
        nopicker.setMinValue(year1-50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year1);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        lay_set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tv_session.setText(String.valueOf(nopicker.getValue()));
                d.dismiss();
            }
        });
        d.show();


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_add_student2.this, Act_student_list2.class);
        startActivity(i);
        finish();
        return true;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_add_student2.this, Act_student_list2.class);
        startActivity(i);
        finish();
    }
}
