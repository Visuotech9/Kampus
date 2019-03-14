package visuotech.com.kampus.attendance.Activities.Director;

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

import visuotech.com.kampus.attendance.Activities.Administrator.Act_Director_profile;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_director_list;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_director_profile2 extends AppCompatActivity {
    ImageView iv_back;
    //    CircleImageView iv_profile_image;
    ImageView iv_profile_image;
    TextView tv_name,tv_email,tv_mobile,tv_course,tv_gender,tv_dob,tv_doj,tv_address,tv_id;
    String name,email,mobile,course,gender,dob,doj,address,id,pic;
    ArrayList<Director> director_list;

    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_director_profile2);

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

        ApigetDirector();



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_director_profile2.this, Director_Act_home.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void ApigetDirector(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    director_list=baseRequest.getDataList(jsonArray,Director.class);
                    name=director_list.get(0).getDirector_name();
                    email=director_list.get(0).getDirector_email_id();
                    address=director_list.get(0).getDirector_address();
                    course=director_list.get(0).getDir_course_name();
                    id=director_list.get(0).getDirector_username();
                    dob=director_list.get(0).getDiretor_dob();
                    doj=director_list.get(0).getDirector_date_of_joining();
                    gender=director_list.get(0).getDirector_gender();
                    mobile=director_list.get(0).getDirector_mobile_no();
                    pic=director_list.get(0).getDirector_pic();

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
        String remainingUrl2="/Kampus/Api2.php?apicall=director_list&organization_id="+sessionParam.org_id+"&user_id="+sessionParam.userId;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

}
