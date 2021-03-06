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
import visuotech.com.kampus.attendance.Activities.Administrator.Act_Hod_profile;
import visuotech.com.kampus.attendance.Activities.Faculty.Act_assignment_details;
import visuotech.com.kampus.attendance.Activities.Faculty.Act_studymat_detals;
import visuotech.com.kampus.attendance.Model.Assignment;
import visuotech.com.kampus.attendance.Model.Director;
import visuotech.com.kampus.attendance.Model.HOD;
import visuotech.com.kampus.attendance.R;


public class Ad_assignment extends RecyclerView.Adapter<Ad_assignment.MyViewHolder> {
    ArrayList<Assignment> list;
    Context context;

    public Ad_assignment (ArrayList<Assignment> list, Context context) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_hod,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        holder.tv_name.setText(list.get(i).getTitle());
        holder.tv_course.setText(list.get(i).getAssgDepartmentName());
        holder.tv_id.setText("Semister :"+list.get(i).getAssgSemester()+"("+list.get(i).getAssgSection()+")");


        holder.lin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Act_assignment_details.class);
                intent.putExtra("title",list.get(i).getTitle());
                intent.putExtra("description",list.get(i).getDescription());
                intent.putExtra("subject_name",list.get(i).getAssgSubjectName());
                intent.putExtra("section",list.get(i).getAssgSection());
                intent.putExtra("semester",list.get(i).getAssgSemester());
                intent.putExtra("department_name",list.get(i).getAssgDepartmentName());
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
    public void filterList(ArrayList<Assignment> assignment_list2) {
        this.list = assignment_list2;
        notifyDataSetChanged();
    }
}
