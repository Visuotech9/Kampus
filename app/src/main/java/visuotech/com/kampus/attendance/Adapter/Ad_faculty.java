package visuotech.com.kampus.attendance.Adapter;



import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_Director_profile;
import visuotech.com.kampus.attendance.Activities.Administrator.Act_faculty_profile;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.Faculty;
import visuotech.com.kampus.attendance.R;


public class Ad_faculty extends RecyclerView.Adapter<Ad_faculty.MyViewHolder> {
    ArrayList<Faculty> list;
    Context context;

    public Ad_faculty (ArrayList<Faculty> list, Context context) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_faculty,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        holder.tv_name.setText(list.get(i).getFaculty_name());
        holder.tv_course.setText(list.get(i).getFaculty_department_name());
        holder.tv_id.setText(list.get(i).getFaculty_username());
        Picasso.get().load(list.get(i).getFaculty_pic()).into(holder.iv_profile_image);

        holder.lin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Act_faculty_profile.class);
                intent.putExtra("Name",list.get(i).getFaculty_name());
                intent.putExtra("Mobile",list.get(i).getF_mobile_no());
                intent.putExtra("Email",list.get(i).getF_email_id());
                intent.putExtra("Dob",list.get(i).getF_dob());
                intent.putExtra("Doj",list.get(i).getF_date_of_joining());
                intent.putExtra("Address",list.get(i).getF_address());
                intent.putExtra("Gender",list.get(i).getF_gender());
                intent.putExtra("Course",list.get(i).getFaculty_course_name());
                intent.putExtra("Department",list.get(i).getFaculty_department_name());
                intent.putExtra("Pic",list.get(i).getFaculty_pic());
                intent.putExtra("ClgId",list.get(i).getFaculty_username());
                intent.putExtra("Designation",list.get(i).getDesignation());
                intent.putExtra("Experience",list.get(i).getExperience());
                intent.putExtra("HodName",list.get(i).getFaculty_hod_name());
                context.startActivity(intent);

            }
        });


//        holder.lin_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(context,GalleryActivity.class);
//                intent.putExtra("position",list.get(i).getEvent_no());
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


        TextView tv_name,tv_course,tv_id;
        LinearLayout lin_layout;
        CircleImageView iv_profile_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_course =  itemView.findViewById(R.id.tv_course);
            tv_id =  itemView.findViewById(R.id.tv_id);
            iv_profile_image =  itemView.findViewById(R.id.iv_profile_image);
            lin_layout=itemView.findViewById(R.id.lin_layout);






        }
    }
    public void filterList(ArrayList<Faculty> director_list2) {
        this.list = director_list2;
        notifyDataSetChanged();
    }}
