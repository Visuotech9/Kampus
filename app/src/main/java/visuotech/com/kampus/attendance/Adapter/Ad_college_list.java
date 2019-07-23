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
import visuotech.com.kampus.attendance.Model.College;
import visuotech.com.kampus.attendance.R;


public class Ad_college_list extends RecyclerView.Adapter<Ad_college_list.MyViewHolder> {
    ArrayList<College> list;
    Context context;
    String samitee_name;
    TextView tv_station1;


    public Ad_college_list(Context context, ArrayList<College> list, TextView tv_station1) {
        this.list = list;
        this.context=context;
        this.tv_station1=tv_station1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_college,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        int j=i+1;
        holder.tv_station.setText(list.get(i).getOrganization_name());
//        holder.tv_station.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tv_station1.setText(i+" -"+ list.get(i));
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView tv_station;
        LinearLayout lin_layout;
        CircleImageView iv_profile_image;
        LinearLayout lay_call,lay_message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_station =  itemView.findViewById(R.id.tv_station);

            }
    }
    public void filterList(ArrayList<College> list) {
        this.list = list;
        notifyDataSetChanged();
    }}
