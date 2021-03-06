package visuotech.com.kampus.attendance.Activities.Administrator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import visuotech.com.kampus.attendance.Adapter.Ad_Semister_adapter;
import visuotech.com.kampus.attendance.Adapter.Ad_ValueListNew;
import visuotech.com.kampus.attendance.Adapter.Ad_course;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.SemList;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.RecyclerTouchListener;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.Functions;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_add_semister extends AppCompatActivity {

    SwipeRefreshLayout mSwipeRefreshLayout;
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    LinearLayout container;
    TextView tv_course, tv_sem;
    ArrayList<Course> course_list = new ArrayList<>();
    ArrayList<String> course_list1 = new ArrayList<>();
    ArrayList<String> sem_list_string = new ArrayList<>();
    ArrayList<SemList> sem_list = new ArrayList<>();
    ArrayList<SemList> list=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    Ad_ValueListNew adapter1;
    Ad_course adapter;
    Button btn_view;
    String course_id, course_name;
    int[] arr = {1, 2};
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Add Semister");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.activity_act_add_semister, null);


        addList();

        tv_course = rowView.findViewById(R.id.tv_course);
        tv_sem = rowView.findViewById(R.id.tv_sem);
        btn_view = rowView.findViewById(R.id.btn_view);

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();
            }
        });

        tv_sem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sem)));
                sucessDialog2(context, tv_sem, sem_list);
            }
        });


        tv_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sucessDialog1(context, tv_course, course_list);
            }
        });


        ApigetCourse();
        container.addView(rowView, container.getChildCount());
    }

    private void callApi() {

        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
            ApiaddSemList();
        } else {
            sucessDialog(getResources().getString(R.string.Internet_connection), context);
        }

    }

    private void ApiaddSemList() {
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {

                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    Toast.makeText(getApplicationContext(), "sucess", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        JsonArray array = new JsonArray();
        for (int i = 0; i < sem_list_string.size(); i++) {
            array.add(sem_list_string.get(i));
        }
        JsonObject object = new JsonObject();
        object.add("semester_id", array);
        object.addProperty("course_id", course_id);
        object.addProperty("organization_id", sessionParam.org_id);

        baseRequest.callAPIPostCustomURL(1, object, "Kampus/Api2.php?apicall=add_sem");

    }

    private void addList() {
        SemList semList = new SemList("I");
        sem_list.add(semList);

        semList = new SemList("II");
        sem_list.add(semList);

        semList = new SemList("III");
        sem_list.add(semList);

        semList = new SemList("IV");
        sem_list.add(semList);

        semList = new SemList("V");
        sem_list.add(semList);

        semList = new SemList("VI");
        sem_list.add(semList);

        semList = new SemList("VII");
        sem_list.add(semList);

        semList = new SemList("VIII");
        sem_list.add(semList);


    }

    public void sucessDialog1(Context context, final TextView tv_station1, final ArrayList<Course> courses_list) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.search_layout_dialog);
        mDialog.setCanceledOnTouchOutside(true);


        TextView tv_retry;
        LinearLayout lay;
        EditText inputSearch;
        RecyclerView rv;
        rv = mDialog.findViewById(R.id.rv_list);
        tv_retry = mDialog.findViewById(R.id.tv_retry);
        inputSearch = mDialog.findViewById(R.id.inputSearch);
        TextView tv_title = mDialog.findViewById(R.id.tv_title);
        LinearLayout lay1 = mDialog.findViewById(R.id.lay1);
        lay1.setVisibility(View.GONE);
        tv_title.setText("Select Course");
        tv_retry.setText("OK");
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new Ad_course(courses_list, context);
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int j = position + 1;

                tv_station1.setText(courses_list.get(position).getCourse_name());
                course_id = courses_list.get(position).getCourse_id();
                course_name = courses_list.get(position).getCourse_name();

//                ApigetSemister(course_id, course_name);
                mDialog.cancel();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

//        inputSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                filter1(editable.toString(), adapter, sp_list);
//            }
//        });


        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
//                tv_station1.setEnabled(true);


            }
        });
        mDialog.show();


    }


    public void sucessDialog2(Context context, final TextView tv_station1, final ArrayList<SemList> thana_list1) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.search_layout_dialog22);
        mDialog.setCanceledOnTouchOutside(true);


        TextView tv_retry, tv_select_all;
        EditText inputSearch;
        CheckBox checkbox_all;
        LinearLayout lay;
        rv = mDialog.findViewById(R.id.rv_list);
        tv_retry = mDialog.findViewById(R.id.tv_retry);
        inputSearch = mDialog.findViewById(R.id.inputSearch);
        TextView tv_title = mDialog.findViewById(R.id.tv_title);
        tv_select_all = mDialog.findViewById(R.id.tv_select_all);
        checkbox_all = mDialog.findViewById(R.id.checkbox_all);
        LinearLayout lay1 = mDialog.findViewById(R.id.lay1);
         lay = mDialog.findViewById(R.id.lay);
        lay1.setVisibility(View.GONE);
        tv_title.setText("Semister ");
        tv_retry.setText("OK");

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        sem_list_string.clear();
        adapter1 = new Ad_ValueListNew(context, thana_list1, tv_station1, tv_select_all, checkbox_all,list,lay);
        rv.setAdapter(adapter1);


//        rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                int j = position + 1;
//                tv_station1.setText(sp_list.get(position));
//
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                filter2(editable.toString(), thana_list1);
            }
        });


        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();

                String data = "";

                list = adapter1.getList();

                for (int j = 0; j < list.size(); j++) {
                    sem_list_string.add(list.get(j).getSem());

                }

                try {
                    for (int i = 0; i < list.size(); i++) {
                        arr[i] = Integer.parseInt(sem_list_string.get(i));
                    }

                } catch (NumberFormatException ex) {

                }
                System.out.println(Arrays.toString(arr));
                if (list.size() != 0) {
                    tv_station1.setText(list.get(0).getSem());
                } else {
                    tv_station1.setHint("Select Semister");
                }

                Log.e("LIST_LENGTH", String.valueOf(sem_list_string.size()));
//                tv_station1.setText(list.get(0).getTi_branch_name()+","+list.get(0).getTi_city());
            }
        });
        mDialog.show();


    }


    private void ApigetCourse() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");

                    course_list = baseRequest.getDataListreverse(jsonArray, Course.class);
                    for (int i = 0; i < course_list.size(); i++) {
                        course_list1.add(course_list.get(i).getCourse_name());
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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=course_list&organization_id=" + sessionParam.org_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    public void sucessDialog(String message, Context context) {
//        textView.setText(getResources().getString(R.string.txt_hello));

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
        mDialog.setContentView(R.layout.notification_dailog2);
        mDialog.setCanceledOnTouchOutside(true);

        Button btn_ok;
        TextView tv_retry;
        TextView tv_notification;
        btn_ok = mDialog.findViewById(R.id.btn_ok);
        tv_retry = mDialog.findViewById(R.id.tv_retry);
        tv_notification = mDialog.findViewById(R.id.tv_notification);
        tv_notification.setText(message);
        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
                callApi();


            }
        });
        mDialog.show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_add_semister.this, Act_semister_list.class);
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_add_semister.this, Act_semister_list.class);
        startActivity(i);
        finish();
    }


}
