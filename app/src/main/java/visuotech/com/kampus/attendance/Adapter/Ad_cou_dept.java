package visuotech.com.kampus.attendance.Adapter;



import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_Director_profile;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Department;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;
import visuotech.com.kampus.attendance.retrofit.BaseRequest;
import visuotech.com.kampus.attendance.retrofit.RequestReciever;


public class Ad_cou_dept extends RecyclerView.Adapter<Ad_cou_dept.MyViewHolder> {
    ArrayList<Course> list;

    ArrayList<Department> department_list;
    ArrayList<String>department_name_list=new ArrayList<>();

    Context context;
    private BaseRequest baseRequest;
    SessionParam sessionParam;

    public Ad_cou_dept (ArrayList<Course> list, Context context) {
        this.list = list;
        this.context=context;
        sessionParam=new SessionParam(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_cou_dept,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        holder.tv_name.setText(list.get(i).getCourse_name());

        holder.rv_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.rv_list.setLayoutManager(linearLayoutManager);
        holder.rv_list.setItemAnimator(new DefaultItemAnimator());
        ApigetDepartment(holder.rv_list,list.get(i).getCourse_id());

//        holder.tv_course.setText(list.get(i).getDir_course_name());
//        holder.tv_id.setText(list.get(i).getDirector_username());
//        Picasso.get().load(list.get(i).getDirector_pic()).into(holder.iv_profile_image);

//        holder.lin_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(context,Act_Director_profile.class);
//                intent.putExtra("Name",list.get(i).getDirector_name());
//                intent.putExtra("Mobile",list.get(i).getDirector_mobile_no());
//                intent.putExtra("Email",list.get(i).getDirector_email_id());
//                intent.putExtra("Dob",list.get(i).getDiretor_dob());
//                intent.putExtra("Doj",list.get(i).getDirector_date_of_joining());
//                intent.putExtra("Address",list.get(i).getDirector_address());
//                intent.putExtra("Gender",list.get(i).getDirector_gender());
//                intent.putExtra("Course",list.get(i).getDir_course_name());
//                intent.putExtra("Pic",list.get(i).getDirector_pic());
//                intent.putExtra("ClgId",list.get(i).getDirector_username());
//                context.startActivity(intent);
//
//            }
//        });
/*
        Glide.with(context)
                .load("https://raw.githubusercontent.com/bumptech/glide/master/static/glide_logo.png")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.iv_gallery_image);
*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView tv_name;
        LinearLayout lin_layout;
        CircleImageView iv_profile_image;
        RecyclerView rv_list;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            rv_list =  itemView.findViewById(R.id.rv_list);
//            tv_id =  itemView.findViewById(R.id.tv_id);
//            iv_profile_image =  itemView.findViewById(R.id.iv_profile_image);
//            lin_layout=itemView.findViewById(R.id.lin_layout);






        }
    }


    private void ApigetDepartment(final RecyclerView rv_list, String course_id){
        baseRequest = new BaseRequest();
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int requestCode, String Json, Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(Json);
                    JSONArray jsonArray=jsonObject.optJSONArray("user");

                    department_list=baseRequest.getDataList(jsonArray,Department.class);
                    for (int i=0;i<department_list.size();i++){
                        department_name_list.add(department_list.get(i).getDepartment_name());
//                       department_id.add(department_list1.get(i).getDepartment_id());
                    }

                   Ad_department adapter=new Ad_department(department_list,context);
                    rv_list.setAdapter(adapter);


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
        String remainingUrl2="/Kampus/Api2.php?apicall=department_list&organization_id="+sessionParam.org_id+"&course_id="+course_id;
        baseRequest.callAPIGETData(1, remainingUrl2);
    }



}
