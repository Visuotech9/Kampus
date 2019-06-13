package visuotech.com.kampus.attendance.Activities.Faculty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import visuotech.com.kampus.attendance.Activities.Hod.Act_student_list3;
import visuotech.com.kampus.attendance.Activities.Hod.HOD_Act_home;
import visuotech.com.kampus.attendance.Adapter.Ad_student;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.ModelResponse;
import visuotech.com.kampus.attendance.Model.Section;
import visuotech.com.kampus.attendance.Model.Semister;
import visuotech.com.kampus.attendance.Model.Student;
import visuotech.com.kampus.attendance.PaginationScrollListener;
import visuotech.com.kampus.attendance.R;
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
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;
    ArrayList<Student> student_list=new ArrayList<>();
    ApiInterface apiInterface;
    Context context;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    EditText inputSearch;
    ArrayList<Director> director_list;
    ArrayList<String>director_name_list=new ArrayList<>();
    List<Student> students;
    List<Student>results;
    ArrayList<Semister>sem_list1;
    ArrayList<Section>section_list1;
    ImageView iv_add;
    private boolean isLoading;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int currentPage=PAGE_START;
    private BaseRequest baseRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_student_list4);

        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Student List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//-------------------------classes------------------------------------------
        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

//-------------------------recyclerview------------------------------------------
        rv_list=findViewById(R.id.rv_list);
        progressbar=findViewById(R.id.progressbar);
        spinner_section = findViewById(R.id.spinner_section);
        spinner_sem = findViewById(R.id.spinner_sem);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        students=new ArrayList<>();
        results=new ArrayList<>();

        inputSearch = findViewById(R.id.inputSearch);
        iv_add =  findViewById(R.id.iv_add);
        iv_add.setVisibility(View.GONE);

        spinner_section.setOnItemSelectedListener(this);
        spinner_sem.setOnItemSelectedListener(this);



        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
//                filter(editable.toString());
            }
        });

        rv_list.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        ApigetStudent2(currentPage);
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

//        apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        ApigetStudent1();



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




    private void filter(String text) {
        //new array list that will hold the filtered data
//        ArrayList<Student> student_list2 = new ArrayList<>();
        ArrayList<Student>student_list2 = new ArrayList<>();



        //looping through existing elements
        for (int i=0;i<student_list.size();i++){
            if (student_list.get(i).getFull_name().toLowerCase().contains(text.toLowerCase())){
                Student student=new Student();
                student.setFull_name(student_list.get(i).getFull_name());
                student.setStudent_department_name(student_list.get(i).getStudent_department_name());
                student.setEnrollment_no(student_list.get(i).getEnrollment_no());
                student.setStudent_pic(student_list.get(i).getStudent_pic());
                student.setStudent_semester(student_list.get(i).getStudent_semester());
                student.setStudent_section(student_list.get(i).getStudent_section());


                student_list2.add(student);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(student_list2);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Act_student_list4.this, Faculty_Act_home.class);
        startActivity(i);
        finish();
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
