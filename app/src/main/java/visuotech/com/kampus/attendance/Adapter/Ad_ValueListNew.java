package visuotech.com.kampus.attendance.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import visuotech.com.kampus.attendance.Model.SemList;
import visuotech.com.kampus.attendance.R;

public class Ad_ValueListNew extends RecyclerView.Adapter<Ad_ValueListNew.ViewHolder> {

    ArrayList<SemList> list = new ArrayList<>();
    ArrayList<SemList> list11 = new ArrayList<>();
    List<String> selectedValueLsit = new ArrayList<>();
    int beliefIndex;
    LinearLayout lay1;
    CheckBox checkbox_all;
    private Context context;


    public Ad_ValueListNew(Context context, ArrayList<SemList> list, TextView tv_station1, TextView tv_select_all, CheckBox checkbox_all, ArrayList<SemList> list11, LinearLayout lay1) {
        this.list = list;
        this.list11 = list11;
        this.lay1 = lay1;
        this.checkbox_all = checkbox_all;

       /* list.get(0).setChecked(true);
        list.get(2).setChecked(true);*/


        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_semister, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SemList semList = new SemList();
        holder.tv_station.setText(list.get(position).getSem());

        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getChecked()) {
                    holder.checkbox1.setChecked(false);
                    list.get(position).setChecked(false);
                    for (int j = 0; j < list11.size(); j++) {
                        if (list11.get(j).getSem().equals(list.get(position).getSem())) {
                            list11.remove(j);
                            break;
                        }

                    }
                } else {
                    list.get(position).setChecked(true);
                    holder.checkbox1.setChecked(true);
                    semList.setSem(list.get(position).getSem());
                    list11.add(semList);
                }
            }
        });

/*
        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (semList.isselectAll) {
                    checkbox_all.setChecked(false);
                    holder.checkbox1.setChecked(false);
                    semList.setIsselectAll(false);
                    diselectAllItem(false);
                } else {
                    checkbox_all.setChecked(true);
                    holder.checkbox1.setChecked(true);
                    semList.setIsselectAll(true);
                    list11.clear();
                    selectAllItem(true);
                }
            }
        });

        if (semList.isselectAll) {
            checkbox_all.setChecked(true);
            semList.setIsselectAll(true);
            selectAllItem(true);
        } else {
            checkbox_all.setChecked(false);
            semList.setIsselectAll(false);
            diselectAllItem(false);
        }
*/

        for (int i = 0; i < list.size(); i++) {
            if (list.get(position).getChecked()) {
                holder.checkbox1.setChecked(true);
            } else {
                holder.checkbox1.setChecked(false);
            }
        }
    }
  /*  public void selectAllItem(boolean isSelectedAll,) {
        if (list != null) {

            list11.clear();
            for (int index = 0; index < list.size(); index++) {
                SemList semList=new SemList();
                list.get(index).setChecked(isSelectedAll);
                semList.setSem(list.get(index).getSem());
                list11.add(semList);
            }
            Log.e("LIST222_LENGTH", String.valueOf(list11.size()));
            notifyDataSetChanged();
        }

    }
*/


    public void selectAllItem(boolean isSelectedAll) {
        if (list != null) {
            for (int index = 0; index < list.size(); index++) {
                SemList semList=new SemList();
                list.get(index).setChecked(isSelectedAll);
                semList.setSem(list.get(index).getSem());
                semList.setChecked(isSelectedAll);
                list11.add(semList);
            }
            notifyDataSetChanged();
        }

    }


    public void diselectAllItem(boolean isSelectedAll) {
        if (list != null) {
            for (int index = 0; index < list.size(); index++) {
                SemList semList=new SemList();
                list.get(index).setSelected(isSelectedAll);
                semList.setChecked(isSelectedAll);
            }
            list11.clear();
            notifyDataSetChanged();
        }

    }


    public ArrayList<SemList> getList() {
        return list11;
    }

    @Override
    public int getItemCount() {
        //return jobsNearYouList == null ? 0 : jobsNearYouList.size();
        return list.size();
    }

    /* public List<SemList> getList() {
         return list;
     }
 */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_station, tv_value;
        LinearLayout lay_linear, lay;
        ImageView iv_cb;
        CircleImageView iv_profile_image;
        LinearLayout lay_call, lay_message;
        CheckBox checkbox1;

        public ViewHolder(View v) {
            super(v);
            tv_station = itemView.findViewById(R.id.tv_station);
            checkbox1 = itemView.findViewById(R.id.checkbox1);
            lay_linear = itemView.findViewById(R.id.lay_linear);
            tv_value = itemView.findViewById(R.id.tv_value);
            iv_cb = itemView.findViewById(R.id.iv_cb);
            lay = itemView.findViewById(R.id.lay);

        }
    }
}
