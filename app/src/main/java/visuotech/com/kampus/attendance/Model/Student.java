
package visuotech.com.kampus.attendance.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Student implements Serializable {

    @SerializedName("admission_date")
    private String mAdmissionDate;
    @SerializedName("course_id")
    private String mCourseId;
    @SerializedName("creation_date")
    private String mCreationDate;
    @SerializedName("department_id")
    private String mDepartmentId;
    @SerializedName("diploma")
    private String mDiploma;
    @SerializedName("director_id")
    private String mDirectorId;
    @SerializedName("enrollment_no")
    private String mEnrollmentNo;
    @SerializedName("faculty_id")
    private String mFacultyId;
    @SerializedName("fathers_name")
    private String mFathersName;
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("full_name")
    private String mFullName;
    @SerializedName("hod_id")
    private String mHodId;
    @SerializedName("hsc_result")
    private String mHscResult;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("middle_name")
    private String mMiddleName;
    @SerializedName("mothers_name")
    private String mMothersName;
    @SerializedName("organization_id")
    private String mOrganizationId;
    @SerializedName("permanent_address")
    private String mPermanentAddress;
    @SerializedName("s_active_status")
    private String mSActiveStatus;
    @SerializedName("s_blood_group")
    private String mSBloodGroup;
    @SerializedName("s_caste")
    private String mSCaste;
    @SerializedName("s_city")
    private String mSCity;
    @SerializedName("s_dob")
    private String mSDob;
    @SerializedName("s_email_id")
    private String mSEmailId;
    @SerializedName("s_emergency_contact_no")
    private String mSEmergencyContactNo;
    @SerializedName("s_gender")
    private String mSGender;
    @SerializedName("s_mobile_no")
    private String mSMobileNo;
    @SerializedName("s_prefix")
    private String mSPrefix;
    @SerializedName("s_state")
    private String mSState;
    @SerializedName("scholarship")
    private String mScholarship;
    @SerializedName("section_id")
    private String mSectionId;
    @SerializedName("sem_id")
    private String mSemId;
    @SerializedName("session")
    private String mSession;
    @SerializedName("session_end")
    private String mSessionEnd;
    @SerializedName("session_start")
    private String mSessionStart;
    @SerializedName("ssc_result")
    private String mSscResult;
    @SerializedName("student_course_name")
    private String mStudentCourseName;
    @SerializedName("student_department_name")
    private String mStudentDepartmentName;
    @SerializedName("student_director_name")
    private String mStudentDirectorName;
    @SerializedName("student_hod_name")
    private String mStudentHodName;
    @SerializedName("student_id")
    private String mStudentId;
    @SerializedName("student_organization_name")
    private String mStudentOrganizationName;
    @SerializedName("student_pic")
    private String mStudentPic;
    @SerializedName("student_section")
    private String mStudentSection;
    @SerializedName("student_semester")
    private String mStudentSemester;
    @SerializedName("student_username")
    private String mStudentUsername;
    @SerializedName("temporary_address")
    private String mTemporaryAddress;
    @SerializedName("total_pages")
    private String mTotalPages;

    public String getmFacultyId() {
        return mFacultyId;
    }

    public void setmFacultyId(String mFacultyId) {
        this.mFacultyId = mFacultyId;
    }

    public String getmTotalPages() {
        return mTotalPages;
    }

    public void setmTotalPages(String mTotalPages) {
        this.mTotalPages = mTotalPages;
    }

    public String getAdmissionDate() {
        return mAdmissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        mAdmissionDate = admissionDate;
    }

    public String getCourseId() {
        return mCourseId;
    }

    public void setCourseId(String courseId) {
        mCourseId = courseId;
    }

    public String getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(String creationDate) {
        mCreationDate = creationDate;
    }

    public String getDepartmentId() {
        return mDepartmentId;
    }

    public void setDepartmentId(String departmentId) {
        mDepartmentId = departmentId;
    }

    public String getDiploma() {
        return mDiploma;
    }

    public void setDiploma(String diploma) {
        mDiploma = diploma;
    }

    public String getDirectorId() {
        return mDirectorId;
    }

    public void setDirectorId(String directorId) {
        mDirectorId = directorId;
    }

    public String getEnrollmentNo() {
        return mEnrollmentNo;
    }

    public void setEnrollmentNo(String enrollmentNo) {
        mEnrollmentNo = enrollmentNo;
    }


    public String getFathersName() {
        return mFathersName;
    }

    public void setFathersName(String fathersName) {
        mFathersName = fathersName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getHodId() {
        return mHodId;
    }

    public void setHodId(String hodId) {
        mHodId = hodId;
    }

    public String getHscResult() {
        return mHscResult;
    }

    public void setHscResult(String hscResult) {
        mHscResult = hscResult;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getMiddleName() {
        return mMiddleName;
    }

    public void setMiddleName(String middleName) {
        mMiddleName = middleName;
    }

    public String getMothersName() {
        return mMothersName;
    }

    public void setMothersName(String mothersName) {
        mMothersName = mothersName;
    }

    public String getOrganizationId() {
        return mOrganizationId;
    }

    public void setOrganizationId(String organizationId) {
        mOrganizationId = organizationId;
    }

    public String getPermanentAddress() {
        return mPermanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        mPermanentAddress = permanentAddress;
    }

    public String getSActiveStatus() {
        return mSActiveStatus;
    }

    public void setSActiveStatus(String sActiveStatus) {
        mSActiveStatus = sActiveStatus;
    }

    public String getSBloodGroup() {
        return mSBloodGroup;
    }

    public void setSBloodGroup(String sBloodGroup) {
        mSBloodGroup = sBloodGroup;
    }

    public String getSCaste() {
        return mSCaste;
    }

    public void setSCaste(String sCaste) {
        mSCaste = sCaste;
    }

    public String getSCity() {
        return mSCity;
    }

    public void setSCity(String sCity) {
        mSCity = sCity;
    }

    public String getSDob() {
        return mSDob;
    }

    public void setSDob(String sDob) {
        mSDob = sDob;
    }

    public String getSEmailId() {
        return mSEmailId;
    }

    public void setSEmailId(String sEmailId) {
        mSEmailId = sEmailId;
    }

    public String getSEmergencyContactNo() {
        return mSEmergencyContactNo;
    }

    public void setSEmergencyContactNo(String sEmergencyContactNo) {
        mSEmergencyContactNo = sEmergencyContactNo;
    }

    public String getSGender() {
        return mSGender;
    }

    public void setSGender(String sGender) {
        mSGender = sGender;
    }

    public String getSMobileNo() {
        return mSMobileNo;
    }

    public void setSMobileNo(String sMobileNo) {
        mSMobileNo = sMobileNo;
    }

    public String getSPrefix() {
        return mSPrefix;
    }

    public void setSPrefix(String sPrefix) {
        mSPrefix = sPrefix;
    }

    public String getSState() {
        return mSState;
    }

    public void setSState(String sState) {
        mSState = sState;
    }

    public String getScholarship() {
        return mScholarship;
    }

    public void setScholarship(String scholarship) {
        mScholarship = scholarship;
    }

    public String getSectionId() {
        return mSectionId;
    }

    public void setSectionId(String sectionId) {
        mSectionId = sectionId;
    }

    public String getSemId() {
        return mSemId;
    }

    public void setSemId(String semId) {
        mSemId = semId;
    }

    public String getSession() {
        return mSession;
    }

    public void setSession(String session) {
        mSession = session;
    }

    public String getSessionEnd() {
        return mSessionEnd;
    }

    public void setSessionEnd(String sessionEnd) {
        mSessionEnd = sessionEnd;
    }

    public String getSessionStart() {
        return mSessionStart;
    }

    public void setSessionStart(String sessionStart) {
        mSessionStart = sessionStart;
    }

    public String getSscResult() {
        return mSscResult;
    }

    public void setSscResult(String sscResult) {
        mSscResult = sscResult;
    }

    public String getStudentCourseName() {
        return mStudentCourseName;
    }

    public void setStudentCourseName(String studentCourseName) {
        mStudentCourseName = studentCourseName;
    }

    public String getStudentDepartmentName() {
        return mStudentDepartmentName;
    }

    public void setStudentDepartmentName(String studentDepartmentName) {
        mStudentDepartmentName = studentDepartmentName;
    }

    public String getStudentDirectorName() {
        return mStudentDirectorName;
    }

    public void setStudentDirectorName(String studentDirectorName) {
        mStudentDirectorName = studentDirectorName;
    }

    public String getStudentHodName() {
        return mStudentHodName;
    }

    public void setStudentHodName(String studentHodName) {
        mStudentHodName = studentHodName;
    }

    public String getStudentId() {
        return mStudentId;
    }

    public void setStudentId(String studentId) {
        mStudentId = studentId;
    }

    public String getStudentOrganizationName() {
        return mStudentOrganizationName;
    }

    public void setStudentOrganizationName(String studentOrganizationName) {
        mStudentOrganizationName = studentOrganizationName;
    }

    public String getStudentPic() {
        return mStudentPic;
    }

    public void setStudentPic(String studentPic) {
        mStudentPic = studentPic;
    }

    public String getStudentSection() {
        return mStudentSection;
    }

    public void setStudentSection(String studentSection) {
        mStudentSection = studentSection;
    }

    public String getStudentSemester() {
        return mStudentSemester;
    }

    public void setStudentSemester(String studentSemester) {
        mStudentSemester = studentSemester;
    }

    public String getStudentUsername() {
        return mStudentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        mStudentUsername = studentUsername;
    }

    public String getTemporaryAddress() {
        return mTemporaryAddress;
    }

    public void setTemporaryAddress(String temporaryAddress) {
        mTemporaryAddress = temporaryAddress;
    }



}
