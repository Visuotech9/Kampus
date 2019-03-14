package visuotech.com.kampus.attendance.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.College;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;
import visuotech.com.kampus.attendance.retrofit.Utility;

public class Act_College_list extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;
    private SearchableSpinner mSearchableSpinner;

    private final ArrayList<String> mStrings = new ArrayList<>();

//    Spinner spinner;
    SearchableSpinner spinner;
    ArrayList<College> college_list1;
    String college,collegeId,collegeLogo;

    ArrayList<String>  college_list= new ArrayList<String>();
    ArrayList<String>  college_id= new ArrayList<String>();




    Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__college_list);

        spinner =  findViewById(R.id.spinner);
        btn_ok =  findViewById(R.id.btn_ok);

//        layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ready) );

        if (Utility.getAPIVerison()){
            btn_ok.setBackgroundResource(R.drawable.background_btn);
        }
        college_list1=new ArrayList<>();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
       LinearLayout lin_spl_layout=findViewById(R.id.lin_spl_layout);

        ApigetCollege();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!college.isEmpty()){
                    Intent intent=new Intent(Act_College_list.this,Act_Login.class);
                    intent.putExtra("COLLEGE ID",collegeId);
                    intent.putExtra("COLLEGE NAME",college);
                    intent.putExtra("COLLEGE LOGO",collegeLogo);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Select college", Toast.LENGTH_LONG).show();
                }

            }
        });

//        if (NetworkConnection.checkNetworkStatus(context) == true) {
//
//        } else {
//            Snackbar.make(lin_spl_layout, "No internet connection", Snackbar.LENGTH_LONG).show();
//        }
        spinner.setOnItemSelectedListener(this);







    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.spinner :
                //Your Action Here.
                college=college_list1.get(i).getOrganization_name();
                collegeId=college_list1.get(i).getOrganization_id();
                collegeLogo=college_list1.get(i).getLogo();
//                Toast.makeText(getApplicationContext(),college, Toast.LENGTH_LONG).show();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void ApigetCollege(){
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    college_list1=baseRequest.getDataList(jsonArray,College.class);

                    for (int i=0;i<college_list1.size();i++){
                        college_list.add(college_list1.get(i).getOrganization_name());
                        college_id.add(college_list1.get(i).getOrganization_id());

                    }
                    ArrayAdapter<CharSequence> adapter_department = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,college_list);
                        adapter_department.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter_department);

//                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
//                            college_list, android.R.layout.simple_spinner_item);
//                    // Specify layout to be used when list of choices appears
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    // Applying the adapter to our spinner
//                    spinner.setAdapter(adapter);


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
        String remainingUrl2="Kampus/Api2.php?apicall=college_list";
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


}
