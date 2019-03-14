package visuotech.com.kampus.attendance.Activities.Administrator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import visuotech.com.kampus.attendance.R;

public class Act_faculty_profile extends AppCompatActivity {
    ImageView iv_back;
//    CircleImageView iv_profile_image;
    ImageView iv_profile_image;
    TextView tv_name,tv_email,tv_mobile,tv_course,tv_gender,tv_dob,tv_doj,tv_address,tv_id,tv_dept,tv_exp,tv_hod_name,tv_designation;
    String name,email,mobile,course,gender,dob,doj,address,id,pic,department,hod_name,designation,exp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_faculty_profile);

        iv_back=findViewById(R.id.iv_back);
        iv_profile_image=findViewById(R.id.iv_profile_image);
        tv_name=findViewById(R.id.tv_name);
        tv_email=findViewById(R.id.tv_email);
        tv_mobile=findViewById(R.id.tv_mobile);
//        tv_course=findViewById(R.id.tv_course);
        tv_gender=findViewById(R.id.tv_gender);
        tv_dob=findViewById(R.id.tv_dob);
        tv_doj=findViewById(R.id.tv_doj);
        tv_address=findViewById(R.id.tv_address);
        tv_id=findViewById(R.id.tv_id);
        tv_dept=findViewById(R.id.tv_dept);
        tv_designation=findViewById(R.id.tv_designation);
        tv_exp=findViewById(R.id.tv_exp);
        tv_hod_name=findViewById(R.id.tv_hod_name);



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
        department=intent.getStringExtra("Department");
        exp=intent.getStringExtra("Experience");
        designation=intent.getStringExtra("Designation");
        hod_name=intent.getStringExtra("HodName");

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
        tv_hod_name.setText(hod_name);
        tv_exp.setText(exp);
        tv_designation.setText(designation);
        Picasso.get().load(pic).into(iv_profile_image);



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Act_faculty_profile.this, Act_faculty_list.class);
//                startActivity(i);
                finish();
            }
        });
    }
}
