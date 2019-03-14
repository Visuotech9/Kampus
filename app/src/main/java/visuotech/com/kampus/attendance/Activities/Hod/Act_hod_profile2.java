package visuotech.com.kampus.attendance.Activities.Hod;

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
import visuotech.com.kampus.attendance.Model.HOD;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_hod_profile2 extends AppCompatActivity {
    ImageView iv_back;
    //    CircleImageView iv_profile_image;
    ImageView iv_profile_image;
    TextView tv_name,tv_email,tv_mobile,tv_dept,tv_gender,tv_dob,tv_doj,tv_address,tv_id;
    String name,email,mobile,course,gender,dob,doj,address,id,pic,department;
    ArrayList<HOD> hod_list;

    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_hod_profile2);
        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        iv_back=findViewById(R.id.iv_back);
        iv_profile_image=findViewById(R.id.iv_profile_image);
        tv_name=findViewById(R.id.tv_name);
        tv_email=findViewById(R.id.tv_email);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_dept=findViewById(R.id.tv_dept);
        tv_gender=findViewById(R.id.tv_gender);
        tv_dob=findViewById(R.id.tv_dob);
        tv_doj=findViewById(R.id.tv_doj);
        tv_address=findViewById(R.id.tv_address);
        tv_id=findViewById(R.id.tv_id);


        ApigetHod();



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_hod_profile2.this, HOD_Act_home.class);
                startActivity(i);
                finish();
            }
        });
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
                    name=hod_list.get(0).getHod_name();
                    email=hod_list.get(0).getHod_email_id();
                    address=hod_list.get(0).getHod_address();
                    id=hod_list.get(0).getHod_username();
                    dob=hod_list.get(0).getHod_dob();
                    doj=hod_list.get(0).getHod_date_of_joining();
                    gender=hod_list.get(0).getHod_gender();
                    mobile=hod_list.get(0).getHod_mobile_no();
                    department=hod_list.get(0).getHod_department_name();
                    pic=hod_list.get(0).getHod_pic();


                    tv_name.setText(name);
                    tv_email.setText(email);
                    tv_address.setText(address);
//        tv_course.setText(course);
                    tv_id.setText(id);
                    tv_dob.setText(dob);
                    tv_doj.setText(doj);
                    tv_gender.setText(gender);
                    tv_mobile.setText(mobile);
                    tv_dept.setText(department);
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
        String remainingUrl2="/Kampus/Api2.php?apicall=hod_list&organization_id="+sessionParam.org_id+"&user_id="+sessionParam.userId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

}
