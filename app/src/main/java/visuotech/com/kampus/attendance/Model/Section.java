package visuotech.com.kampus.attendance.Model;

public class Section {
    String section_id,section,sec_sem_id,sec_course_id,sec_department_id;

    public Section() {
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSec_sem_id() {
        return sec_sem_id;
    }

    public void setSec_sem_id(String sec_sem_id) {
        this.sec_sem_id = sec_sem_id;
    }

    public String getSec_course_id() {
        return sec_course_id;
    }

    public void setSec_course_id(String sec_course_id) {
        this.sec_course_id = sec_course_id;
    }

    public String getSec_department_id() {
        return sec_department_id;
    }

    public void setSec_department_id(String sec_department_id) {
        this.sec_department_id = sec_department_id;
    }
}
