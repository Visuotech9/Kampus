package visuotech.com.kampus.attendance.Model;

public class Student {
    String full_name,enrollment_no,student_department_name,student_semester,student_section,student_pic,total_pages;

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public Student() {
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEnrollment_no() {
        return enrollment_no;
    }

    public void setEnrollment_no(String enrollment_no) {
        this.enrollment_no = enrollment_no;
    }

    public String getStudent_department_name() {
        return student_department_name;
    }

    public void setStudent_department_name(String student_department_name) {
        this.student_department_name = student_department_name;
    }

    public String getStudent_semester() {
        return student_semester;
    }

    public void setStudent_semester(String student_semester) {
        this.student_semester = student_semester;
    }

    public String getStudent_section() {
        return student_section;
    }

    public void setStudent_section(String student_section) {
        this.student_section = student_section;
    }

    public String getStudent_pic() {
        return student_pic;
    }

    public void setStudent_pic(String student_pic) {
        this.student_pic = student_pic;
    }
}
