package visuotech.com.kampus.attendance.Activities.Director;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import visuotech.com.kampus.attendance.Activities.Administrator.Act_add_Hod;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_hod_list;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Department;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;
import visuotech.com.kampus.attendance.retrofit.Utility;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Act_add_Hod2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spinner_department,spinner_gender;
    ArrayList<Department>department_list1;


    String department,gender,course,course_id;
    ImageView iv_profile_image,iv_cal_dob,iv_cal_doj;
    Button btn_add,btn_upload_image,btn_cancel;
    private BaseRequest baseRequest;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    String other_device_active,user_typee,organization_id,user_id;
    EditText et_name,et_email,et_mobile,et_address,et_hod_id;
    TextView tv_dob,tv_doj;
    TextView tv_error1,tv_error2,tv_error3,tv_error4,tv_error5,tv_error6,tv_error7,tv_error8,tv_error9,tv_error10,tv_error11;
    public   String name,email,mobileNumber,dob,doj,address,hod_clg_id,dept_id,directorId;
    File file;
    public boolean datafinish = false;
    private final int REQ_CODE_Gallery = 1;
    Uri selectedImage;
    private final int REQ_CODE_Camera = 1888;
    Bitmap mainBitmap;
    Uri tempUri;
    private static final String IMAGE_DIRECTORY_NAME = "Directorregistrstion";

    ArrayList<String>  course_list= new ArrayList<String>();

    ArrayList<String>  department_list= new ArrayList<String>();
    ArrayList<String>  department_id= new ArrayList<String>();

    ArrayList<String>  director_list= new ArrayList<String>();
    ArrayList<String>  director_id= new ArrayList<String>();

    ArrayList<Director>director_list1;
    ArrayList<String>director_name_list1=new ArrayList<>();

    ArrayList<String>error_list;




    String[] gender_list = { "Male", "Female", "others","--Select gender--"};
    final int listsize = gender_list.length - 1;

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    ArrayAdapter adapter_director,adapter_department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_hod2);

        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Hod");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//-------------------------classes------------------------------------------
        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);


        user_typee= sessionParam.user_type;
        user_id= sessionParam.userId;



        organization_id=sessionParam.org_id;
        btn_add =  findViewById(R.id.btn_add);
        btn_cancel =  findViewById(R.id.btn_cancel);
        btn_upload_image =  findViewById(R.id.btn_upload_image);
        et_name =  findViewById(R.id.et_name);
        et_email =  findViewById(R.id.et_email);
        et_mobile =  findViewById(R.id.et_mobile);
        tv_dob =  findViewById(R.id.tv_dob);
        et_address =  findViewById(R.id.et_address);
        et_hod_id =  findViewById(R.id.et_hod_id);
        tv_doj =  findViewById(R.id.tv_doj);
        iv_profile_image =  findViewById(R.id.iv_profile_image);
        iv_cal_dob =  findViewById(R.id.iv_cal_dob);
        iv_cal_doj =  findViewById(R.id.iv_cal_doj);

        spinner_gender = findViewById(R.id.spinner_gender);
        spinner_department = findViewById(R.id.spinner_department);
        spinner_department.setOnItemSelectedListener(this);
        spinner_gender.setOnItemSelectedListener(this);
        department_list1=new ArrayList<>();

        ArrayAdapter adapter_gender = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender_list);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter_gender);
        spinner_gender.setSelection(listsize);

        ApigetDepartment();

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
                datePickerDialog = new DatePickerDialog(Act_add_Hod2.this,
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
                datePickerDialog = new DatePickerDialog(Act_add_Hod2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv_doj.setText(day + "-" + (month + 1) + "-" + year);
                            }
                        }, year, month, dayOfMonth);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                String MobilePattern = "[0-9]{10}";
                String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



                if (et_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your  name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_name.getText().toString().length() > 25) {
                    Toast.makeText(getApplicationContext(), "Please enter less than 25 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Utility.checkEmail(et_email.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Please enter valied email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_mobile.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gender.equals("--Select gender--")) {
                    Toast.makeText(getApplicationContext(), "Please select gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!et_mobile.getText().toString().matches(MobilePattern)) {
                    Toast.makeText(getApplicationContext(), "Please enter valid 10 digit mobile number", Toast.LENGTH_SHORT).show();
                    return ;
                }if (tv_dob.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }if (tv_doj.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }if (et_address.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }if (et_hod_id.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }if (file==null){
                    Toast.makeText(getApplicationContext(), "Please Select Any Image", Toast.LENGTH_SHORT).show();
                    return;
                }if (department_list1.isEmpty()){
                    String sucessMessage="Add department for course "+"'"+course+"'";
                    Utility.sucessDialog(sucessMessage,context);
                    return;
                }else{

                    name = et_name.getText().toString();
                    email = et_email.getText().toString();
                    mobileNumber = et_mobile.getText().toString();
                    address = et_address.getText().toString();
                    dob = tv_dob.getText().toString();
                    doj = tv_doj.getText().toString();
                    hod_clg_id =et_hod_id.getText().toString();


                    if (NetworkConnection.checkNetworkStatus(context)==true){
                        ApiPostAddHod();
                    }else{
                        Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
                    }


                }



            }
        });
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
        String remainingUrl2="/Kampus/Api2.php?apicall=department_list&organization_id="+organization_id+"&director_id="+sessionParam.userId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApiPostAddHod() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
//                iv_profile_image.setImageBitmap(null);
//                et_title.setText("");
//                et_description.setText("");
////                Toast.makeText(getApplicationContext(),"sucess",Toast.LENGTH_SHORT).show();
                String sucessMessage="HOD added sucessfully";
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
        RequestBody name_ = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody email_ = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody mobile_no_ = RequestBody.create(MediaType.parse("text/plain"), mobileNumber);
        RequestBody address_ = RequestBody.create(MediaType.parse("text/plain"), address);
//        RequestBody cover_pic_ = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mainBitmap));
        RequestBody dob_ = RequestBody.create(MediaType.parse("text/plain"), Utility.changeDateDMYtoYMD(dob));
        RequestBody doj_ = RequestBody.create(MediaType.parse("text/plain"), Utility.changeDateDMYtoYMD(doj));
        RequestBody hod_clg_id_ = RequestBody.create(MediaType.parse("text/plain"), hod_clg_id);
        RequestBody dept_id_ = RequestBody.create(MediaType.parse("text/plain"), dept_id);
        RequestBody organization_id_ = RequestBody.create(MediaType.parse("text/plain"), organization_id);
        RequestBody gender_ = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody course_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.course_id);




        baseRequest.callAPIAddhod(1,"https://collectorexpress.in/",body,name_,email_,mobile_no_
                ,address_,dob_,doj_,hod_clg_id_,dept_id_,organization_id_,gender_,course_id_);
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.spinner_department:
                department=department_list1.get(i).getDepartment_name();
                dept_id=department_list1.get(i).getDepartment_id();
                break;

            case R.id.spinner_gender :
                gender=gender_list[i];
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Act_add_Hod2.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_Gallery: {
                try {
                    selectedImage = data.getData();
                    mainBitmap = MediaStore.Images.Media.getBitmap(Act_add_Hod2.this.getContentResolver(), selectedImage);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_add_Hod2.this, Act_hod_list2.class);
        startActivity(i);
        finish();
        return true;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_add_Hod2.this, Act_hod_list2.class);
        startActivity(i);
        finish();
    }
}
