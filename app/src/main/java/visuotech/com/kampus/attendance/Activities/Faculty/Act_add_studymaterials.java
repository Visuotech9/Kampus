package visuotech.com.kampus.attendance.Activities.Faculty;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import visuotech.com.kampus.attendance.ApiService;
import visuotech.com.kampus.attendance.GalleryAdapter;
import visuotech.com.kampus.attendance.InternetConnection;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Section;
import visuotech.com.kampus.attendance.Model.Semister;
import visuotech.com.kampus.attendance.Model.Subjects;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.FileUtils;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;
import visuotech.com.kampus.attendance.retrofit.Utility;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static visuotech.com.kampus.attendance.Constants.SECTION_LIST;
import static visuotech.com.kampus.attendance.Constants.SEM_LIST;
import static visuotech.com.kampus.attendance.Constants.SUB_LIST;
import static visuotech.com.kampus.attendance.retrofit.WebServiceConstants.BASE_URL;

public class Act_add_studymaterials extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Button btn_attachment,btn_add,btn_cancel;
    Spinner spinner_sem,spinner_section,spinner_subject;
    String semId,sectionId,subId,title,description,startdate,enddate,starttime,endtime,sem,subject,section;
    TextView tv_startdate,tv_enddate,tv_starttime,tv_endtime;
    LinearLayout lay_startdate,lay_enddate,lay_starttime,lay_endtime;
    EditText et_title,et_description;

    Context context;
    Activity activity;
    private BaseRequest baseRequest;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;

    ArrayList<Semister> sem_list1;
    ArrayList<Section>section_list1;
    ArrayList<Subjects>subject_list1;
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

    ArrayList<String>  sem_list= new ArrayList<String>();
    ArrayList<String>  subject_name_list= new ArrayList<String>();
    ArrayList<String>  section_list= new ArrayList<String>();

    ArrayAdapter adapter_subject,adapter_sem,adapter_section;



    File file;
    public boolean datafinish = false;
    private final int REQ_CODE_Gallery = 1;
    private final int PICK_IMAGE_MULTIPLE = 1;
    Uri selectedImage;
    private final int REQ_CODE_Camera = 1888;
    Bitmap mainBitmap;
    Uri tempUri;
    private static final String IMAGE_DIRECTORY_NAME = "Directorregistrstion";
    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;


    private static final String TAG = Act_add_assignment.class.getSimpleName();
    private static final int REQUEST_CODE = 6384;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 124;

    private View parentView;
    private ListView listView;
    private ProgressBar mProgressBar;
    private Button btnChoose, btnUpload;
    MultipartBody.Part body;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Add Study Materials");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        LinearLayout  container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_add_studymaterials, null);
        container.addView(rowView, container.getChildCount());

        gvGallery = rowView.findViewById(R.id.gv);
        btn_add = rowView.findViewById(R.id.btn_add);
        btn_attachment = rowView.findViewById(R.id.btn_attachment);
        btn_cancel = rowView.findViewById(R.id.btn_cancel);
        spinner_section = rowView.findViewById(R.id.spinner_section);
        spinner_sem = rowView.findViewById(R.id.spinner_sem);
        spinner_subject = rowView.findViewById(R.id.spinner_subject);
        et_title = rowView.findViewById(R.id.et_title);
        et_description = rowView.findViewById(R.id.et_description);


        spinner_section.setOnItemSelectedListener(this);
        spinner_sem.setOnItemSelectedListener(this);
        spinner_subject.setOnItemSelectedListener(this);




        btn_attachment.setOnClickListener(new View.OnClickListener() {
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
//                    selectImage();
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
                }

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String MobilePattern = "[0-9]{10}";
                String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


                if (et_title.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_title.getText().toString().length() > 25) {
                    Toast.makeText(getApplicationContext(), "Please enter less than 25 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }if (et_description.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter  Desription", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    title = et_title.getText().toString();
                    description = et_description.getText().toString();

                    if (NetworkConnection.checkNetworkStatus(context)==true){

                        if (mArrayUri!=null){
                            ApiPostAddAssignment();
//                            uploadImagesToServer();
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Select Any Image", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });
        ApigetSemister();
        ApigetSection();
    }
/*

    private void uploadImagesToServer() {
        if (InternetConnection.checkConnection(Act_add_studymaterials.this)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://collectorexpress.in/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();



            // create list of file parts (photo, video, ...)
            List<MultipartBody.Part> parts = new ArrayList<>();
            ApiService service = retrofit.create(ApiService.class);

            if (mArrayUri != null) {
                // create part for file (photo, video, ...)
                for (int i = 0; i < mArrayUri.size(); i++) {
                    parts.add(prepareFilePart("image"+i, mArrayUri.get(i)));
                }
            }
            file= new File("/storage/emulated/0/Pictures/Screenshots/Screenshot_20190308-161354.png");
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body =MultipartBody.Part.createFormData("file", file.getName(), requestFile);


            RequestBody title_ = RequestBody.create(MediaType.parse("text/plain"), title);
            RequestBody description_ = RequestBody.create(MediaType.parse("text/plain"), description);
            RequestBody starttime_ = RequestBody.create(MediaType.parse("text/plain"), starttime);
            RequestBody endtime_ = RequestBody.create(MediaType.parse("text/plain"), endtime);
//        RequestBody cover_pic_ = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mainBitmap));
            RequestBody startdate_ = RequestBody.create(MediaType.parse("text/plain"), Utility.changeDateDMYtoYMD(startdate));
            RequestBody enddate_ = RequestBody.create(MediaType.parse("text/plain"), Utility.changeDateDMYtoYMD(enddate));
            RequestBody dept_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.dept_id);
            RequestBody course_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.course_id);
            RequestBody userId_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.userId);
            RequestBody hod_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.hod_id);
            RequestBody director_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.director_id);
            RequestBody org_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.org_id);
            RequestBody semId_ = RequestBody.create(MediaType.parse("text/plain"),semId);
            RequestBody sectionId_ = RequestBody.create(MediaType.parse("text/plain"), sectionId);
            RequestBody subId_ = RequestBody.create(MediaType.parse("text/plain"), subId);
            // finally, execute the request
            Call<ResponseBody> call = service.uploadMultiple(parts,title_,description_
                    ,starttime_,endtime_,startdate_,enddate_,dept_id_,course_id_,userId_,hod_id_,director_id_,org_id_,
                    semId_,sectionId_,subId_);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                    if(response.isSuccessful()) {
                        Toast.makeText(Act_add_studymaterials.this,
                                "Images successfully uploaded!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                }
            });

        } else {

            Toast.makeText(Act_add_studymaterials.this,
                    R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }

*/



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

            case R.id.spinner_subject :
                subject=subject_list1.get(i).getSubject_name();
                subId=subject_list1.get(i).getSubject_id();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void ApigetSemister(){
        baseRequest = new BaseRequest();
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
        String remainingUrl2=SEM_LIST+"&organization_id="+sessionParam.org_id+"&course_id="+sessionParam.course_id;
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
        String remainingUrl2=SECTION_LIST+"&organization_id="+sessionParam.org_id+"&course_id="+sessionParam.course_id+"&department_id="+sessionParam.dept_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }
    private void ApigetSubject(){
        baseRequest = new BaseRequest();
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
        String remainingUrl2=SUB_LIST+"&organization_id="+sessionParam.org_id+"&department_id="+sessionParam.dept_id+"&course_id="+sessionParam.course_id+"&sem_id="+semId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }



    private void ApiPostAddAssignment() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {

                String sucessMessage="Study materials added sucessfully";
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



        List<MultipartBody.Part> parts = new ArrayList<>();
        if (mArrayUri != null) {
            // create part for file (photo, video, ...)
            for (int i = 0; i < mArrayUri.size(); i++) {
                parts.add(prepareFilePart("image"+i, mArrayUri.get(i)));
            }
        }
        RequestBody title_ = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody description_ = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody dept_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.dept_id);
        RequestBody course_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.course_id);
        RequestBody userId_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.userId);
        RequestBody hod_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.hod_id);
        RequestBody director_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.director_id);
        RequestBody org_id_ = RequestBody.create(MediaType.parse("text/plain"), sessionParam.org_id);
        RequestBody semId_ = RequestBody.create(MediaType.parse("text/plain"),semId);
        RequestBody sectionId_ = RequestBody.create(MediaType.parse("text/plain"), sectionId);
        RequestBody subId_ = RequestBody.create(MediaType.parse("text/plain"), subId);



        baseRequest.callAPIAddStudymate(1,BASE_URL,parts,title_,description_
                ,dept_id_,course_id_,userId_,hod_id_,director_id_,org_id_,
                semId_,sectionId_,subId_);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(Objects.requireNonNull(getContentResolver().getType(fileUri))),
                        file
                );
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Act_add_studymaterials.this);
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
                        startActivityForResult(pickPhoto, PICK_IMAGE_MULTIPLE);
                    }else{
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
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
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();


                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
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

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(Act_add_studymaterials.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        tv.setText(day + "-" + (month + 1) + "-" + year);
                    }
                }, year, month, dayOfMonth);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }

    public void getTime(final TextView tv){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(Act_add_studymaterials.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Toast.makeText(getApplicationContext(), selectedHour + ":" + selectedMinute,Toast.LENGTH_SHORT).show();
//                eReminderTime.setText();
                tv.setText(selectedHour+""+":"+selectedMinute+"");
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_add_studymaterials.this, Act_study_list.class);
        startActivity(i);
        finish();
        return true;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_add_studymaterials.this, Act_study_list.class);
        startActivity(i);
        finish();
    }

}


/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE_MULTIPLE: {
                try {
                    if (resultCode == RESULT_OK) {

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        imagesEncodedList = new ArrayList<String>();
                        if (data.getData() != null) {

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
                        } else {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {

                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    mArrayUri.add(uri);
                                    // Get the cursor
                                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                                    // Move to first row
                                    cursor.moveToFirst();

                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    imageEncoded = cursor.getString(columnIndex);
                                    imagesEncodedList.add(imageEncoded);
                                    cursor.close();

                                    galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                                    gvGallery.setAdapter(galleryAdapter);
                                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                            .getLayoutParams();
                                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                                }
                                Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                            }

                        }


                    } else {
                        Toast.makeText(this, "You haven't picked Image",
                                Toast.LENGTH_LONG).show();
                    }



                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                            .show();
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
//                    iv_profile_image.setImageBitmap(mainBitmap);
//                    iv_profile_camera.setVisibility(View.GONE);
                }
                break;
            }

        }
    }*/
