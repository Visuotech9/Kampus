package visuotech.com.kampus.attendance.Activities.Faculty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.Activities.Director.Act_director_profile2;
import visuotech.com.kampus.attendance.Activities.Director.Director_Act_home;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

import static visuotech.com.kampus.attendance.Constants.FACULTY_LIST;

public class Act_faculty_profile2 extends AppCompatActivity {
    ImageView iv_back;
    //    CircleImageView iv_profile_image;
    ImageView iv_profile_image;
    TextView tv_name,tv_email,tv_mobile,tv_course,tv_gender,tv_dob,tv_doj,tv_address,tv_id;
    String name,email,mobile,course,gender,dob,doj,address,id,pic;
    ArrayList<Faculty> faculty_list;

    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_faculty_profile2);
        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        iv_back=findViewById(R.id.iv_back);
        iv_profile_image=findViewById(R.id.iv_profile_image);
        tv_name=findViewById(R.id.tv_name);
        tv_email=findViewById(R.id.tv_email);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_course=findViewById(R.id.tv_course);
        tv_gender=findViewById(R.id.tv_gender);
        tv_dob=findViewById(R.id.tv_dob);
        tv_doj=findViewById(R.id.tv_doj);
        tv_address=findViewById(R.id.tv_address);
        tv_id=findViewById(R.id.tv_id);

        Apigetfaculty();



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_faculty_profile2.this,Faculty_Act_home.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void Apigetfaculty(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    faculty_list=baseRequest.getDataList(jsonArray,Faculty.class);
                    name=faculty_list.get(0).getFaculty_name();
                    email=faculty_list.get(0).getF_email_id();
                    address=faculty_list.get(0).getF_address();
                    course=faculty_list.get(0).getFaculty_department_name();
                    id=faculty_list.get(0).getFaculty_username();
                    dob=faculty_list.get(0).getF_dob();
                    doj=faculty_list.get(0).getF_date_of_joining();
                    gender=faculty_list.get(0).getF_gender();
                    mobile=faculty_list.get(0).getF_mobile_no();
                    pic=faculty_list.get(0).getFaculty_pic();

                    tv_name.setText(name);
                    tv_email.setText(email);
                    tv_address.setText(address);
                    tv_course.setText(course);
                    tv_id.setText(id);
                    tv_dob.setText(dob);
                    tv_doj.setText(doj);
                    tv_gender.setText(gender);
                    tv_mobile.setText(mobile);
                    Picasso.get().load(pic).into(iv_profile_image);



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
        String remainingUrl2= FACULTY_LIST+"&organization_id="+sessionParam.org_id+"&user_id="+sessionParam.userId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }
}
