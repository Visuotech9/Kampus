package visuotech.com.kampus.attendance.Activities.Administrator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import visuotech.com.kampus.attendance.Adapter.Ad_faculty;
import visuotech.com.kampus.attendance.Adapter.Ad_student;
import visuotech.com.kampus.attendance.MarshMallowPermission;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.Model.ModelResponse;
import visuotech.com.kampus.attendance.Model.Student;
import visuotech.com.kampus.attendance.PaginationScrollListener;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.ApiClient;
import visuotech.com.kampus.attendance.retrofit.ApiInterface;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;

public class Act_student_list extends AppCompatActivity {
    private static final String TAG ="UserListActivity";
    Ad_student adapter;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressbar;
    ArrayList<Student>student_list=new ArrayList<>();;
    ArrayList<Student>student_list2=new ArrayList<>();;


    private boolean isLoading;
    ApiInterface apiInterface;
    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;

    private int currentPage=PAGE_START;

    Context context;
    LinearLayout container;
    Activity activity;
    SessionParam sessionParam;
    MarshMallowPermission marshMallowPermission;
    private BaseRequest baseRequest;
    EditText inputSearch;

    ArrayList<Director> director_list;
    ArrayList<String>director_name_list=new ArrayList<>();
    List<Student> students;
    List<Student>results;

    ImageView iv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        //-------------------------toolbar------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor((Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("Students");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        activity = this;
        sessionParam = new SessionParam(getApplicationContext());
        marshMallowPermission = new MarshMallowPermission(activity);

        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.content_main_student_list, null);
        container.addView(rowView, container.getChildCount());

//-------------------------recyclerview------------------------------------------
        rv_list=rowView.findViewById(R.id.rv_list);
        progressbar=rowView.findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        students=new ArrayList<>();
        results=new ArrayList<>();

        inputSearch = (EditText) rowView.findViewById(R.id.inputSearch);
        iv_add =  rowView.findViewById(R.id.iv_add);



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
                        ApigetStudent2(currentPage);
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
        ApigetStudent1();



    }

/*

    private void loadFirstPage(final int currentPage) {
        Log.d(TAG, "loadFirstPage: ");

        callTopRatedMoviesApi().enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {

                results = fetchResults(response);
                students = (List<Student>) response.body().getmData();
//                Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
//                businessLogic(movies);
                progressbar.setVisibility(View.GONE);
                adapter.addAll(students);

                int TOTAL_PAGES= Integer.parseInt(students.get(0).getTotal_pages());
//                int current_page=response.body().getCurrent_page();
                if (currentPage != TOTAL_PAGES)
                    adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void loadNextPage(final int currentPage) {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                adapter.removeLoadingFooter();
                isLoading = false;
                results = fetchResults(response);
                students = (List<Student>) response.body().getmData();
//                businessLogic(movies);
                int TOTAL_PAGES= Integer.parseInt(students.get(0).getTotal_pages());
                adapter.addAll(students);


                if (currentPage != TOTAL_PAGES)
                    adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

*/



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
        String remainingUrl2="/Kampus/Api2.php?apicall=student_list&organization_id="+sessionParam.org_id+"&currentpage="+currentPage+"&department_id="+sessionParam.dept_id;
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
        String remainingUrl2="/Kampus/Api2.php?apicall=student_list&organization_id="+sessionParam.org_id+"&currentpage="+currentPage+"&department_id="+sessionParam.dept_id;
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



                for (int i=0;i<student_list.size();i++){
                    if (student_list.get(i).getFull_name().toLowerCase().contains(s.toLowerCase())){
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
                adapter = new Ad_student(student_list2, context);
                rv_list.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                student_list2.clear();



                for (int i=0;i<student_list.size();i++){
                    if (student_list.get(i).getFull_name().toLowerCase().contains(s.toLowerCase())){
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
                adapter = new Ad_student(student_list2, context);
                rv_list.setAdapter(adapter);

                return false;
            }
        });


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(Act_student_list.this, Administrator_Act_home.class);
                startActivity(i);
                finish();
                break;

            case R.id.add_user:
                Intent i1 = new Intent(Act_student_list.this, Act_add_student.class);
                startActivity(i1);
                finish();
                break;
        }

        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Act_student_list.this, Administrator_Act_home.class);
        startActivity(i);
        finish();
    }
}
