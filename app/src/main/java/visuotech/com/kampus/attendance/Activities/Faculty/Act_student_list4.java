package visuotech.com.kampus.attendance.Activities.Faculty;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import visuotech.com.kampus.attendance.Activities.Hod.Act_student_list3;
import visuotech.com.kampus.attendance.Activities.Hod.HOD_Act_home;
import visuotech.com.kampus.attendance.Adapter.Ad_Section_list;
import visuotech.com.kampus.attendance.Adapter.Ad_Semister_list;
import visuotech.com.kampus.attendance.Adapter.Ad_hod;
import visuotech.com.kampus.attendance.Adapter.Ad_student;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Assignment;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.HOD;
import visuotech.com.kampus.attendance.Model.ModelResponse;
import visuotech.com.kampus.attendance.Model.Section;
import visuotech.com.kampus.attendance.Model.Semister;
import visuotech.com.kampus.attendance.Model.Student;
import visuotech.com.kampus.attendance.NetworkConnection;
import visuotech.com.kampus.attendance.PaginationScrollListener;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.RecyclerTouchListener;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.ApiClient;
import visuotech.com.kampus.attendance.retrofit.ApiInterface;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_student_list4 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG ="UserListActivity";
    Spinner spinner_sem,spinner_section,spinner_subject;

    private static final int PAGE_START = 1;
    Ad_student adapter;
    Ad_Semister_list adapter1;
    Ad_Section_list adapter2;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;
    ArrayList<Student> student_list=new ArrayList<>();
    ArrayList<Student> student_list2=new ArrayList<>();
    ApiInterface apiInterface;
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    EditText inputSearch;
    ArrayList<Director> director_list;
    ArrayList<Semister> sem_list = new ArrayList<>();
    ArrayList<Section> sec_list = new ArrayList<>();


    ArrayList<String>director_name_list=new ArrayList<>();
    List<Student> students;
    List<Student>results;
    ArrayList<Semister>sem_list1;
    ArrayList<Section>section_list1;
    ImageView iv_add;
    TextView tv_sem,tv_sec;
    private boolean isLoading;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int currentPage=PAGE_START;
    private BaseRequest baseRequest;
    String dept_name,sem_id,sem_name,sec_id,sec_name;
    Button btn_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Student List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_student_list4, null);
        container.addView(rowView, container.getChildCount());

        Intent intent=getIntent();
        dept_name=intent.getStringExtra("DEPT_NAME");

        rv_list=rowView.findViewById(R.id.rv_list);
        btn_view=rowView.findViewById(R.id.btn_view);
        tv_sem = rowView.findViewById(R.id.tv_sem);
        tv_sec = rowView.findViewById(R.id.tv_sec);
        progressbar=rowView.findViewById(R.id.progressbar);
        spinner_section = rowView.findViewById(R.id.spinner_section);
        spinner_sem = rowView.findViewById(R.id.spinner_sem);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        students=new ArrayList<>();
        results=new ArrayList<>();

        inputSearch = rowView.findViewById(R.id.inputSearch);
        iv_add =  rowView.findViewById(R.id.iv_add);
        iv_add.setVisibility(View.GONE);




        rv_list.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
                            ApigetStudent2(currentPage);
                        } else {
                            sucessDialog(getResources().getString(R.string.Internet_connection), context);
                        }

                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        apiInterface = ApiClient.getClient().create(ApiInterface.class);




        tv_sem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sucessDialog3(context, tv_sem, sem_list);

            }
        });
        tv_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sucessDialog4(context, tv_sec, sec_list);

            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callApi();
            }
        });




//        apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        ApigetStudent1();

        ApigetSemister(dept_name);
        ApigetSection();
    }

    private void callApi() {

        if (NetworkConnection.checkNetworkStatus(getApplicationContext()) == true) {
            ApigetStudent1();
        } else {
            sucessDialog(getResources().getString(R.string.Internet_connection), context);
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private List<Student> fetchResults(Response<ModelResponse> response) {
        ModelResponse topRatedMovies = response.body();
        return topRatedMovies.getmData();
    }
    private Call<ModelResponse> callTopRatedMoviesApi() {
        return apiInterface.getUserList(Integer.parseInt(sessionParam.org_id),currentPage);
    }

    private void ApigetStudent1(){
        baseRequest = new BaseRequest(context);
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    student_list=baseRequest.getDataListreverse(jsonArray,Student.class);
                    adapter=new Ad_student(student_list,context);
                    rv_list.setAdapter(adapter);

                    int TOTAL_PAGES= Integer.parseInt(student_list.get(0).getTotal_pages());
                    if (currentPage != TOTAL_PAGES)
                        adapter.addLoadingFooter();
                    else isLastPage = true;

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
        String remainingUrl2="/Kampus/Api2.php?apicall=student_list&organization_id="+sessionParam.org_id+"&department_id="+sessionParam.dept_id+"&currentpage="+currentPage;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetStudent2(final int currentPage){
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    student_list=baseRequest.getDataListreverse(jsonArray,Student.class);
                    adapter.addAll(student_list);
                    int TOTAL_PAGES= Integer.parseInt(student_list.get(0).getTotal_pages());


                    adapter.removeLoadingFooter();
                    isLoading = false;
                    adapter.addAll(student_list);
                    if (currentPage != TOTAL_PAGES)
                        adapter.addLoadingFooter();
                    else isLastPage = true;

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
        String remainingUrl2="/Kampus/Api2.php?apicall=student_list&organization_id="+sessionParam.org_id+"&department_id="+sessionParam.dept_id+"&currentpage="+currentPage;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    private void ApigetSemister(String dept_name) {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");


                    sem_list = baseRequest.getDataList(jsonArray, Semister.class);


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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=sem_list&organization_id=" + sessionParam.org_id + "&course_id=" + sessionParam.course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }

    private void ApigetSection() {
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray = jsonObject.optJSONArray("user");


                    sec_list = baseRequest.getDataList(jsonArray, Section.class);


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
        String remainingUrl2 = "/Kampus/Api2.php?apicall=section_list&organization_id=" + sessionParam.org_id + "&course_id=" + sessionParam.course_id+"&department_id="+sessionParam.dept_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }


    public void sucessDialog3(Context context, final TextView tv_station1, final ArrayList<Semister> semister_list) {

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
        tv_title.setText("Select Semister");
        tv_retry.setText("OK");
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter1 = new Ad_Semister_list(semister_list, context, dept_name);
        rv.setAdapter(adapter1);

        rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int j = position + 1;

                tv_station1.setText(semister_list.get(position).getSem());
                sem_id = semister_list.get(position).getSem_id();
                sem_name = semister_list.get(position).getSem();

//                ApigetSemister(course_id, course_name);
                mDialog.cancel();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
//                tv_station1.setEnabled(true);


            }
        });
        mDialog.show();


    }

    public void sucessDialog4(Context context, final TextView tv_station1, final ArrayList<Section> section_list) {

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
        tv_title.setText("Select Section");
        tv_retry.setText("OK");
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter2 = new Ad_Section_list(section_list, context, dept_name);
        rv.setAdapter(adapter2);

        rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int j = position + 1;

                tv_station1.setText(sec_list.get(position).getSection());
                sec_id = sec_list.get(position).getSection_id();
                sec_name = sec_list.get(position).getSection();

//                ApigetSemister(course_id, course_name);
                mDialog.cancel();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
//                tv_station1.setEnabled(true);


            }
        });
        mDialog.show();


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem search_item = menu.findItem(R.id.mi_search);

        SearchView searchView = (SearchView) search_item.getActionView();


        searchView.setFocusable(false);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {
                student_list2.clear();


                for (int i = 0; i < student_list.size(); i++) {
                    if (student_list.get(i).getFull_name().toLowerCase().contains(s.toLowerCase())) {
                        Student student = new Student();
                        student.setFull_name(student_list.get(i).getFull_name());
                        student.setStudent_department_name(student_list.get(i).getStudent_department_name());
                        student.setEnrollment_no(student_list.get(i).getEnrollment_no());
                        student.setStudent_pic(student_list.get(i).getStudent_pic());
                        student.setStudent_semester(student_list.get(i).getStudent_semester());
                        student.setStudent_section(student_list.get(i).getStudent_section());


                        student_list2.add(student);
                    }
                }
                adapter = new Ad_student(student_list2, context);
                rv_list.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                student_list2.clear();


                for (int i = 0; i < student_list.size(); i++) {
                    if (student_list.get(i).getFull_name().toLowerCase().contains(s.toLowerCase())) {
                        Student student = new Student();
                        student.setFull_name(student_list.get(i).getFull_name());
                        student.setStudent_department_name(student_list.get(i).getStudent_department_name());
                        student.setEnrollment_no(student_list.get(i).getEnrollment_no());
                        student.setStudent_pic(student_list.get(i).getStudent_pic());
                        student.setStudent_semester(student_list.get(i).getStudent_semester());
                        student.setStudent_section(student_list.get(i).getStudent_section());


                        student_list2.add(student);
                    }
                }
                adapter = new Ad_student(student_list2, context);
                rv_list.setAdapter(adapter);

                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(Act_student_list4.this, Faculty_Act_home.class);
                startActivity(i);
                finish();
                break;

            case R.id.add_user:
//                Intent i1 = new Intent(Act_student_list4.this, Act_add_student3.class);
//                startActivity(i1);
//                finish();
                break;
        }

        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_student_list4.this, Faculty_Act_home.class);
        startActivity(i);
        finish();
    }


}
