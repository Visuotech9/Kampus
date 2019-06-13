package visuotech.com.kampus.attendance.Adapter;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import visuotech.com.kampus.attendance.Model.Course;
import visuotech.com.kampus.attendance.Model.Subjects;
import visuotech.com.kampus.attendance.R;


public class Ad_subject extends RecyclerView.Adapter<Ad_subject.MyViewHolder> {
    ArrayList<Subjects> list;
    Context context;

    public Ad_subject(ArrayList<Subjects> list, Context context) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_course,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        holder.tv_name.setText(list.get(i).getSubject_name());
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
//            tv_course =  itemView.findViewById(R.id.tv_course);
//            tv_id =  itemView.findViewById(R.id.tv_id);
            iv_profile_image =  itemView.findViewById(R.id.iv_profile_image);
            lin_layout=itemView.findViewById(R.id.lin_layout);






        }
    }
    public void filterList(ArrayList<Subjects> course_list2) {
        this.list = course_list2;
        notifyDataSetChanged();
    }}
