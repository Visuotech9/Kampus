package visuotech.com.kampus.attendance.Activities.Administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.Model.Student;
import visuotech.com.kampus.attendance.R;

public class Act_Student_profile extends AppCompatActivity {
    ImageView iv_back;
    //    CircleImageView iv_profile_image;
    ImageView iv_profile_image;
    TextView tv_name, tv_email, tv_mobile, tv_course, tv_gender, tv_dob, tv_id, tv_dept, tv_doa,
            tv_bgroup,tv_caste,tv_fname,tv_mname,tv_parent_mobile,tv_city,tv_state,tv_paddress,tv_taddress,tv_enrol_no,
            tv_session,tv_ssc,tv_hsc,tv_deploma,tv_schlorship;

    String name, email, mobile, course, gender, dob, doj,doa, id, pic, department,bgroup,caste,fname,mname,
            parent_mobile,city,state,paddress,taddress,enrol_no,session,ssc,hsc,deploma,schlorship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__act_profile);

//        gum_details = (ArrayList<Student>) getIntent().getSerializableExtra("ARRAY");


        iv_back = findViewById(R.id.iv_back);
        iv_profile_image = findViewById(R.id.iv_profile_image);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_course=findViewById(R.id.tv_course);
        tv_gender = findViewById(R.id.tv_gender);
        tv_dob = findViewById(R.id.tv_dob);
        tv_doa = findViewById(R.id.tv_doa);
//        tv_id = findViewById(R.id.tv_id);
        tv_dept = findViewById(R.id.tv_dept);

        tv_bgroup = findViewById(R.id.tv_bgroup);
        tv_caste = findViewById(R.id.tv_caste);
        tv_fname = findViewById(R.id.tv_fname);
        tv_mname = findViewById(R.id.tv_mname);
        tv_parent_mobile = findViewById(R.id.tv_parent_mobile);
        tv_city = findViewById(R.id.tv_city);
        tv_state = findViewById(R.id.tv_state);

        tv_paddress = findViewById(R.id.tv_paddress);
        tv_taddress = findViewById(R.id.tv_taddress);
        tv_enrol_no = findViewById(R.id.tv_enrol_no);
        tv_session = findViewById(R.id.tv_session);
        tv_ssc = findViewById(R.id.tv_ssc);
        tv_hsc = findViewById(R.id.tv_hsc);
        tv_deploma = findViewById(R.id.tv_deploma);
        tv_schlorship = findViewById(R.id.tv_schlorship);



        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        email = intent.getStringExtra("Email");
        mobile = intent.getStringExtra("Mobile");
//        id = intent.getStringExtra("ClgId");
        pic = intent.getStringExtra("Pic");
        dob = intent.getStringExtra("Dob");
        gender = intent.getStringExtra("Gender");
        course = intent.getStringExtra("Course");
        department = intent.getStringExtra("Department");

        doa = intent.getStringExtra("doa");
        bgroup = intent.getStringExtra("bgroup");
        caste = intent.getStringExtra("caste");
        fname = intent.getStringExtra("fname");
        mname = intent.getStringExtra("mname");
        parent_mobile = intent.getStringExtra("parent_mobile");
        city = intent.getStringExtra("city");
        state = intent.getStringExtra("state");
        paddress = intent.getStringExtra("paddress");
        taddress = intent.getStringExtra("taddress");
        enrol_no = intent.getStringExtra("enrol_no");
        session = intent.getStringExtra("session");

        ssc = intent.getStringExtra("ssc");
        hsc = intent.getStringExtra("hsc");
        deploma = intent.getStringExtra("deploma");
        schlorship = intent.getStringExtra("schlorship");

        tv_name.setText(name);
        tv_email.setText(email);
        tv_course.setText(course);
//        tv_id.setText(id);
        tv_dob.setText(dob);
        tv_gender.setText(gender);
        tv_mobile.setText(mobile);
        tv_dept.setText(department);

        tv_doa.setText(doa);
        tv_bgroup.setText(bgroup);
        tv_caste.setText(caste);
        tv_fname.setText(fname);
        tv_mname.setText(mname);
        tv_parent_mobile.setText(parent_mobile);
        tv_city.setText(city);
        tv_state.setText(state);
        tv_paddress.setText(paddress);
        tv_taddress.setText(taddress);
        tv_enrol_no.setText(enrol_no);

        tv_session.setText(session);
        tv_ssc.setText(ssc);
        tv_hsc.setText(hsc);
        tv_deploma.setText(deploma);
        tv_schlorship.setText(schlorship);


        Picasso.get().load(pic).into(iv_profile_image);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
