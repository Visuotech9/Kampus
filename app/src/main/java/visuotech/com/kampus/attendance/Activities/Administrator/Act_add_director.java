package visuotech.com.kampus.attendance.Activities.Administrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import visuotech.com.kampus.attendance.GalleryAdapter;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;
import visuotech.com.kampus.attendance.retrofit.Utility;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Act_add_director extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    LinearLayout container;
    String course,gender;
    ImageView iv_profile_image,iv_cal_dob,iv_cal_doj;
    Button btn_add,btn_upload_image;
    Spinner spin_course,spin_gender;
    ArrayList<Course>course_list1;
    private BaseRequest baseRequest;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    String other_device_active,user_typee,organization_id,user_id;
    EditText et_name,et_email,et_mobile,et_address,et_dir_id;
    TextView tv_dob,tv_doj;
    public   String name,email,mobileNumber,dob,doj,address, dir_clg_id, cour_id;
    File file;
    public boolean datafinish = false;
    private final int REQ_CODE_Gallery = 1;
    Uri selectedImage;
    private final int REQ_CODE_Camera = 1888;
    Bitmap mainBitmap;
    Uri tempUri;
    private static final String IMAGE_DIRECTORY_NAME = "Directorregistrstion";

    ArrayList<String>  course_list= new ArrayList<String>();
    ArrayList<String>  course_id= new ArrayList<String>();

    //    String[] department_list = { "Administrator", "Director", "HOD", "Faculty", "Student"};
    String[] gender_list = { "male", "Female", "others","--Select gender--"};
    final int listsize = gender_list.length - 1;


    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    MultipartBody.Part body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

//-------------------------toolbar------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Add Director");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_add_director, null);
        container.addView(rowView, container.getChildCount());

        user_typee= sessionParam.user_type;
        user_id= sessionParam.userId;
        organization_id=sessionParam.org_id;

        btn_add =  rowView.findViewById(R.id.btn_add);
        btn_upload_image =  rowView.findViewById(R.id.btn_upload_image);
        et_name =  rowView.findViewById(R.id.et_name);
        et_email =  rowView.findViewById(R.id.et_email);
        et_mobile =  rowView.findViewById(R.id.et_mobile);
        tv_dob =  rowView.findViewById(R.id.tv_dob);
        et_address =  rowView.findViewById(R.id.et_address);
        et_dir_id =  rowView.findViewById(R.id.et_dir_id);
        tv_doj =  rowView.findViewById(R.id.tv_doj);
        iv_profile_image =  rowView.findViewById(R.id.iv_profile_image);
        iv_cal_dob =  rowView.findViewById(R.id.iv_cal_dob);
        iv_cal_doj =  rowView.findViewById(R.id.iv_cal_doj);

         spin_course = rowView.findViewById(R.id.spinner_department);
         spin_gender = rowView.findViewById(R.id.spinner_gender);
        spin_course.setOnItemSelectedListener(this);
        spin_gender.setOnItemSelectedListener(this);
        course_list1=new ArrayList<>();

        //Creating the ArrayAdapter instance having the country list


        ArrayAdapter adapter_gender = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender_list);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_gender.setAdapter(adapter_gender);
        spin_gender.setSelection(listsize);

        ApigetCourse();

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
                getDate(tv_dob);
            }
        });
        iv_cal_doj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate(tv_doj);
            }
        });



        btn_add.setOnClickListener(new View.OnClickListener() {
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
                else if(!et_mobile.getText().toString().matches(MobilePattern)) {
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
                }if (et_dir_id.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    name = et_name.getText().toString();
                    email = et_email.getText().toString();
                    mobileNumber = et_mobile.getText().toString();
                    address = et_address.getText().toString();
                    dob = tv_dob.getText().toString();
                    doj = tv_doj.getText().toString();
                    dir_clg_id =et_dir_id.getText().toString();


                    if (NetworkConnection.checkNetworkStatus(context)==true){

                        if (file!=null){
                            ApiPostAddDirector();
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.spinner_department :
                //Your Action Here.
                course=course_list1.get(i).getCourse_name();
                cour_id =course_list1.get(i).getCourse_id();
//                Toast.makeText(getApplicationContext(),course, Toast.LENGTH_LONG).show();
                break;
            case R.id.spinner_gender :
                //Your Another Action Here.
                gender=gender_list[i];
//                Toast.makeText(getApplicationContext(),gender_list[i] , Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void ApigetCourse(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    course_list1=baseRequest.getDataList(jsonArray,Course.class);

                    for (int i=0;i<course_list1.size();i++){
                       course_list.add(course_list1.get(i).getCourse_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }
                    ArrayAdapter adapter_course = new ArrayAdapter(context,android.R.layout.simple_spinner_item,course_list);
                    adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_course.setAdapter(adapter_course);




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
        String remainingUrl2="/Kampus/Api2.php?apicall=course_list&organization_id="+organization_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    private void ApiPostAddDirector() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
//                iv_profile_image.setImageBitmap(null);
//                et_title.setText("");
//                et_description.setText("");
////                Toast.makeText(getApplicationContext(),"sucess",Toast.LENGTH_SHORT).show();
                String sucessMessage="Director added sucessfully";
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
        body =MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody name_ = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody email_ = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody mobile_no_ = RequestBody.create(MediaType.parse("text/plain"), mobileNumber);
        RequestBody address_ = RequestBody.create(MediaType.parse("text/plain"), address);
//        RequestBody cover_pic_ = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mainBitmap));
        RequestBody dob_ = RequestBody.create(MediaType.parse("text/plain"), Utility.changeDateDMYtoYMD(dob));
        RequestBody doj_ = RequestBody.create(MediaType.parse("text/plain"), Utility.changeDateDMYtoYMD(doj));
        RequestBody dir_id_ = RequestBody.create(MediaType.parse("text/plain"), dir_clg_id);
        RequestBody cour_id_ = RequestBody.create(MediaType.parse("text/plain"), cour_id);
        RequestBody organization_id_ = RequestBody.create(MediaType.parse("text/plain"), organization_id);
        RequestBody gender_ = RequestBody.create(MediaType.parse("text/plain"), gender);



        baseRequest.callAPIAddDirector(1,"http://collectorexpress.in/",body,name_,email_,mobile_no_
                ,address_,dob_,doj_,dir_id_,cour_id_,organization_id_,gender_);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Act_add_director.this);
        builder.setTitle("Add Photo");
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQ_CODE_Camera);

                } else if (options[item].equals("Choose from Gallery")) {

/*             //-----------------MULTIPLE IMAGE PICK------------------

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
                    */
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

/*              //-------------------MULTIPLE IMAGE PICK------------------

                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    imagesEncodedList = new ArrayList<String>();
                    if(data.getData()!=null) {

                        Uri mImageUri = data.getData();

                        // Get the cursor
                        Cursor cursor = getContentResolver().query(mImageUri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        cursor.close();

                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        mArrayUri.add(mImageUri);
                        galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                        gvGallery.setAdapter(galleryAdapter);
                        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                .getLayoutParams();
                        mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
                    }

                    */
                    selectedImage = data.getData();
                    mainBitmap = MediaStore.Images.Media.getBitmap(Act_add_director.this.getContentResolver(), selectedImage);

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

    public  void getDate(final TextView tv){
        DatePickerDialog datePickerDialog;
        int year;
        int month;
        int dayOfMonth;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(Act_add_director.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        tv.setText(day + "-" + (month + 1) + "-" + year);
                    }
                }, year, month, dayOfMonth);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_add_director.this, Act_director_list.class);
        startActivity(i);
        finish();
        return true;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_add_director.this, Act_director_list.class);
        startActivity(i);
        finish();
    }
}
