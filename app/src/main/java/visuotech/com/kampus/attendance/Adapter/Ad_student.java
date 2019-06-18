package visuotech.com.kampus.attendance.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import visuotech.com.kampus.attendance.Activities.Administrator.Act_Student_profile;
import visuotech.com.kampus.attendance.Model.Student;
import visuotech.com.kampus.attendance.R;
import visuotech.com.kampus.attendance.SessionParam;


public class Ad_student extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String ingUrl = "http://production.chetaru.co.uk/ct_tracker/public/uploads/";
    SessionParam sessionParam;
    private List<Student> list;
    private Context context;

    private boolean isLoadingAdded = false;

    public Ad_student(List<Student> list, Context context) {
        this.context = context;
        this.list = list;
        sessionParam = new SessionParam(context);
    }

    public List<Student> getMovies() {
        return list;
    }

    public void setMovies(List<Student> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.list_student, parent, false);
        viewHolder = new VH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Student student = list.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final VH vh = (VH) holder;

                vh.tv_name.setText(student.getFull_name());
                vh.tv_dept.setText(student.getStudent_department_name());
                vh.tv_enrolment.setText(student.getEnrollment_no());
                vh.tv_section.setText(student.getStudent_section());
                vh.tv_sem.setText(student.getStudent_semester());
                Picasso.get().load(student.getStudent_pic()).into(vh.iv_profile_image);


                vh.lin_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Act_Student_profile.class);
//                        intent.putExtra("ID",String.valueOf(dat.getId()));
                        context.startActivity(intent);
                    }
                });


                break;

            case LOADING:
//                Do nothing
                break;
        }


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    //-----------------------Helper------------------------

    public void add(Student r) {
        list.add(r);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<Student> moveResults) {
        for (Student result : moveResults) {
            add(result);
        }
    }

    public void remove(Student r) {
        int position = list.indexOf(r);
        if (position > -1) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Student());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = list.size() - 1;
        Student result = getItem(position);

        if (result != null) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Student getItem(int position) {
        return list.get(position);
    }


    // ------------------View Holder--------------------

    public void filterList(List<Student> student_list2) {
        this.list = student_list2;
        notifyDataSetChanged();
    }

    protected class VH extends RecyclerView.ViewHolder {
        TextView tv_name, tv_dept, tv_enrolment, tv_sem, tv_section;
        ImageView iv_profile_image;
        LinearLayout lin_layout;

        public VH(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            iv_profile_image = itemView.findViewById(R.id.iv_profile_image);
            tv_dept = itemView.findViewById(R.id.tv_dept);
            tv_enrolment = itemView.findViewById(R.id.tv_enrolment);
            tv_sem = itemView.findViewById(R.id.tv_sem);
            tv_section = itemView.findViewById(R.id.tv_section);
            lin_layout = itemView.findViewById(R.id.lin_layout);

        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
