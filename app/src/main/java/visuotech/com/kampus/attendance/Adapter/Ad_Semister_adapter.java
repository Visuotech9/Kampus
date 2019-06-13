package visuotech.com.kampus.attendance.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import visuotech.com.kampus.attendance.Model.SemList;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;


public class Ad_Semister_adapter extends RecyclerView.Adapter<Ad_Semister_adapter.MyViewHolder> {
    ArrayList<SemList> list;
    ArrayList<SemList> list11;

    ArrayList<String> list2;
    ArrayList<Integer> position_list;
    Context context;
    String samitee_name;
    TextView tv_station1;
    SessionParam sessionParam;
    TextView tv_select_all;
    CheckBox checkbox_all;
    boolean flagSelectAll = false;
    String con;
    int post;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();


    public Ad_Semister_adapter(Context context, ArrayList<SemList> list, TextView tv_station1, TextView tv_select_all, CheckBox checkbox_all, String con) {
        this.list = list;
        this.context = context;
        this.tv_station1 = tv_station1;
        this.tv_select_all = tv_select_all;
        this.checkbox_all = checkbox_all;
        this.con = con;
        sessionParam = new SessionParam(context);
        list2 = new ArrayList<>();
        list11 = new ArrayList<>();
        position_list = new ArrayList<>();


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_semister, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        final SemList thana = list.get(i);
        holder.checkbox1.setOnCheckedChangeListener(null);
        checkbox_all.setOnCheckedChangeListener(null);

        holder.checkbox1.setChecked(thana.isSelected());
        checkbox_all.setChecked(thana.isSelected());
        holder.tv_station.setText(list.get(i).getSem());

        holder.checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                thana.setSelected(isChecked);

                if (isChecked==true){
                    SemList thana1 = new SemList();
                    thana1.setSem(list.get(i).getSem());
                    list11.add(thana1);
                }
            }
        });
        checkbox_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                thana.setSelected(isChecked);

                if (isChecked==false){
                    flagSelectAll = true;
                    diselectAllItem(false);
                    checkbox_all.setChecked(false);
                    list11.clear();


                }else {
                    selectAllItem(true);
                    checkbox_all.setChecked(true);
                }

            }
        });

        tv_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list11.clear();
                if (list.get(i).isSelected()) {

                    flagSelectAll = true;
                    diselectAllItem(false);
                    checkbox_all.setChecked(false);
                    list11.clear();

                } else {

                    selectAllItem(true);
                    checkbox_all.setChecked(true);

                }

            }
        });


        holder.tv_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SemList thana=new SemList();
                if (list.get(i).isSelected()){
                    flagSelectAll = true;
                    list.get(i).setSelected(false);
                    holder.checkbox1.setChecked(false);
                    list11.add(thana);

                }else {

                    holder.checkbox1.setChecked(true);
                    list.get(i).setSelected(true);
                    thana.setSelected(true);
                    thana.setSem(list.get(i).getSem());
                    list11.add(thana);

                }



            }
        });
    }

    public void selectAllItem(boolean isSelectedAll) {
        if (list != null) {

            list11.clear();
            for (int index = 0; index < list.size(); index++) {
                SemList thana2 = new SemList();
                list.get(index).setSelected(isSelectedAll);
                thana2.setSem(list.get(index).getSem());
                list11.add(thana2);
            }
            Log.e("LIST222_LENGTH", String.valueOf(list11.size()));
            notifyDataSetChanged();
        }

    }

    public void diselectAllItem(boolean isSelectedAll) {
        if (list != null) {
            SemList thana2 = new SemList();
            for (int index = 0; index < list.size(); index++) {
                list.get(index).setSelected(isSelectedAll);
            }
            list11.clear();
            notifyDataSetChanged();
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<SemList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<SemList> getList() {
        return list11;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_station;
        LinearLayout lay_linear;
        CircleImageView iv_profile_image;
        LinearLayout lay_call, lay_message;
        CheckBox checkbox1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_station = itemView.findViewById(R.id.tv_station);
            checkbox1 = itemView.findViewById(R.id.checkbox1);
            lay_linear = itemView.findViewById(R.id.lay_linear);
        }
    }
}
