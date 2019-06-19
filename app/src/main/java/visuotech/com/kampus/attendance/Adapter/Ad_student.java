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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Student student = list.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final VH vh = (VH) holder;

                vh.tv_name.setText(student.getFullName());
                vh.tv_dept.setText(student.getStudentDepartmentName());
                vh.tv_enrolment.setText(student.getEnrollmentNo());
                vh.tv_section.setText(student.getStudentSection());
                vh.tv_sem.setText(student.getStudentSemester());
                Picasso.get().load(student.getStudentPic()).into(vh.iv_profile_image);


                vh.lin_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, Act_Student_profile.class);

                        intent.putExtra("ssc", String.valueOf(list.get(position).getSscResult()));
                        intent.putExtra("hsc", String.valueOf(list.get(position).getHscResult()));
                        intent.putExtra("deploma", String.valueOf(list.get(position).getDiploma()));
                        intent.putExtra("schlorship", String.valueOf(list.get(position).getScholarship()));

                        intent.putExtra("fname", String.valueOf(list.get(position).getFathersName()));
                        intent.putExtra("mname", String.valueOf(list.get(position).getMothersName()));
                        intent.putExtra("parent_mobile", String.valueOf(list.get(position).getSEmergencyContactNo()));
                        intent.putExtra("city", String.valueOf(list.get(position).getSCity()));
                        intent.putExtra("state", String.valueOf(list.get(position).getSState()));
                        intent.putExtra("enrol_no", String.valueOf(list.get(position).getEnrollmentNo()));
                        intent.putExtra("session", String.valueOf(list.get(position).getSession()));


                        intent.putExtra("Name", String.valueOf(list.get(position).getFullName()));
                        intent.putExtra("Email", String.valueOf(list.get(position).getSEmailId()));
                        intent.putExtra("Mobile", String.valueOf(list.get(position).getSMobileNo()));
                        intent.putExtra("ClgId", String.valueOf(list.get(position).getStudentId()));
                        intent.putExtra("Pic", String.valueOf(list.get(position).getStudentPic()));
                        intent.putExtra("Dob", String.valueOf(list.get(position).getSDob()));
                        intent.putExtra("Gender", String.valueOf(list.get(position).getSGender()));
                        intent.putExtra("Course", String.valueOf(list.get(position).getStudentCourseName()));
                        intent.putExtra("paddress", String.valueOf(list.get(position).getPermanentAddress()));
                        intent.putExtra("taddress", String.valueOf(list.get(position).getTemporaryAddress()));
                        intent.putExtra("Department", String.valueOf(list.get(position).getStudentDepartmentName()));
                        intent.putExtra("doa", String.valueOf(list.get(position).getAdmissionDate()));
                        intent.putExtra("bgroup", String.valueOf(list.get(position).getSBloodGroup()));
                        intent.putExtra("caste", String.valueOf(list.get(position).getSCaste()));

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
