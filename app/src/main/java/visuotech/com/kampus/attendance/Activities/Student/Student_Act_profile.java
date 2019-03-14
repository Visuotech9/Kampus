package visuotech.com.kampus.attendance.Activities.Student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import visuotech.com.kampus.attendance.R;

public class Student_Act_profile extends AppCompatActivity {
         ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__act_profile);

        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Student_Act_profile.this, Student_Act_home.class);
                startActivity(i);
                finish();
            }
        });
    }
}
