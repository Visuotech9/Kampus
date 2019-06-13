package visuotech.com.kampus.attendance.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import visuotech.com.kampus.attendance.Adapter.Ad_college_list;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.College;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.RecyclerTouchListener;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_College_list extends AppCompatActivity {
    private final ArrayList<String> mStrings = new ArrayList<>();
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    //    Spinner spinner;
    ArrayList<College> college_list1 = new ArrayList<>();
    ArrayList<String> college_list = new ArrayList<String>();
    ArrayList<College> college_list3 = new ArrayList<>();
    String college, collegeId, collegeLogo;
    TextView tv_station1;
    ArrayList<String> college_id = new ArrayList<String>();
    ArrayAdapter<CharSequence> adapter_department;
    Ad_college_list adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    String orgination,orgination_id;
    Button btn_ok;
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__college_list);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);


        tv_station1 = findViewById(R.id.tv_station1);
        btn_ok = findViewById(R.id.btn_ok);

//        layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ready) );

//        if (Utility.getAPIVerison()){
//            btn_ok.setBackgroundResource(R.drawable.background_btn);
//        }

        LinearLayout lin_spl_layout = findViewById(R.id.lin_spl_layout);

        ApigetCollege();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!college.isEmpty()) {
                    Intent intent = new Intent(Act_College_list.this, Act_Login.class);
                    intent.putExtra("COLLEGE ID", collegeId);
                    intent.putExtra("COLLEGE NAME", college);
                    intent.putExtra("COLLEGE LOGO", collegeLogo);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Select college", Toast.LENGTH_LONG).show();
                }

            }
        });

//        if (NetworkConnection.checkNetworkStatus(context) == true) {
//
//        } else {
//            Snackbar.make(lin_spl_layout, "No internet connection", Snackbar.LENGTH_LONG).show();
//        }


        tv_station1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sucessDialog1(context, tv_station1, college_list1);
            }
        });


    }


    private void ApigetCollege() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    college_list1 = baseRequest.getDataList(jsonArray, College.class);

                    for (int i = 0; i < college_list1.size(); i++) {
                        college_list.add(college_list1.get(i).getOrganization_name());
                        college_id.add(college_list1.get(i).getOrganization_id());

                    }

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
        String remainingUrl2 = "Kampus/Api2.php?apicall=college_list";
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    public void sucessDialog1(Context context, final TextView tv_station1, final ArrayList<College> college_list) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.search_layout_dialog);
        mDialog.setCanceledOnTouchOutside(true);


        TextView tv_retry;
        EditText inputSearch;
        rv = mDialog.findViewById(R.id.rv_list);
        tv_retry = mDialog.findViewById(R.id.tv_retry);
        inputSearch = mDialog.findViewById(R.id.inputSearch);
        TextView tv_title = mDialog.findViewById(R.id.tv_title);
        tv_title.setText("Select College");

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new Ad_college_list(context, college_list, tv_station1);
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int j = position + 1;
            /*    tv_station1.setText(college_list.get(position).getOrganization_name());
                college = college_list1.get(position).getOrganization_name();
                collegeId = college_list1.get(position).getOrganization_id();
                collegeLogo = college_list1.get(position).getLogo();
*/

                if (college_list3.size() != 0) {
                    tv_station1.setText(college_list3.get(position).getOrganization_name());
                    collegeId = college_list3.get(position).getOrganization_id();
                    college = college_list3.get(position).getOrganization_name();
                    collegeLogo = college_list1.get(position).getLogo();
                    mDialog.cancel();
                } else {
                    tv_station1.setText(college_list.get(position).getOrganization_name());
                    collegeId = college_list.get(position).getOrganization_id();
                    college = college_list.get(position).getOrganization_name();
                    collegeLogo = college_list.get(position).getLogo();
                    mDialog.cancel();
                }


                mDialog.cancel();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString(), adapter, college_list);
            }
        });


        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();


            }
        });
        mDialog.show();


    }

    private void filter(String text, Ad_college_list adapter, ArrayList<College> college_list) {

        college_list3.clear();
        for (int i = 0; i < college_list.size(); i++) {
            if (college_list.get(i).getOrganization_name().toLowerCase().contains(text.toLowerCase())) {
                College colleget = new College();
                colleget.setOrganization_name(college_list.get(i).getOrganization_name());
                college_list3.add(colleget);
            }
        }
        adapter.filterList(college_list3);
    }


}
