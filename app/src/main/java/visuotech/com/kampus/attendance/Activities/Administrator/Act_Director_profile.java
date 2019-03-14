package visuotech.com.kampus.attendance.Activities.Administrator;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import visuotech.com.kampus.attendance.Activities.Student.Student_Act_home;
import visuotech.com.kampus.attendance.Activities.Student.Student_Act_profile;
import visuotech.com.kampus.attendance.R;

public class Act_Director_profile extends AppCompatActivity {
    ImageView iv_back;
//    CircleImageView iv_profile_image;
    ImageView iv_profile_image;
    TextView tv_name,tv_email,tv_mobile,tv_course,tv_gender,tv_dob,tv_doj,tv_address,tv_id;
    String name,email,mobile,course,gender,dob,doj,address,id,pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__director_profile);

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



        Intent intent=getIntent();
        name=intent.getStringExtra("Name");
        email=intent.getStringExtra("Email");
        mobile=intent.getStringExtra("Mobile");
        id=intent.getStringExtra("ClgId");
        pic=intent.getStringExtra("Pic");
        dob=intent.getStringExtra("Dob");
        doj=intent.getStringExtra("Doj");
        gender=intent.getStringExtra("Gender");
        course=intent.getStringExtra("Course");
        address=intent.getStringExtra("Address");

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



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Act_Director_profile.this, Act_director_list.class);
                startActivity(i);
                finish();
            }
        });
    }
}
