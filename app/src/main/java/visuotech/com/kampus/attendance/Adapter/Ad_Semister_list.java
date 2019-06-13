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
import visuotech.com.kampus.attendance.Model.Semister;
import visuotech.com.kampus.attendance.R;


public class Ad_Semister_list extends RecyclerView.Adapter<Ad_Semister_list.MyViewHolder> {
    ArrayList<Semister> list;
    Context context;
    String dept_name;
    TextView tv_station1;


    public Ad_Semister_list(ArrayList<Semister> list, Context context, String dept_name) {
        this.list = list;
        this.context=context;
        this.tv_station1=tv_station1;
        this.dept_name=dept_name;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_station,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        int j=i+1;
        holder.tv_station.setText(list.get(i).getSem());
        holder.tv_name.setText(dept_name);
        holder.tv_station_no.setText(String.valueOf(i+1));
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


        TextView tv_station,tv_name,tv_station_no;
        LinearLayout lin_layout;
        CircleImageView iv_profile_image;
        LinearLayout lay_call,lay_message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_station =  itemView.findViewById(R.id.tv_station);
            tv_name =  itemView.findViewById(R.id.tv_name);
            tv_station_no =  itemView.findViewById(R.id.tv_station_no);

            }
    }
    public void filterList(ArrayList<Semister> list) {
        this.list = list;
        notifyDataSetChanged();
    }}
