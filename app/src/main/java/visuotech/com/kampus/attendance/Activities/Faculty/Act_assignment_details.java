package visuotech.com.kampus.attendance.Activities.Faculty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import visuotech.com.kampus.attendance.R;

public class Act_assignment_details extends AppCompatActivity {
    String title,description,subject_name,section,semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_assignment_details);

        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        description=intent.getStringExtra("description");
        subject_name=intent.getStringExtra("subject_name");
        section=intent.getStringExtra("section");
        semester=intent.getStringExtra("semester");
    }
}
