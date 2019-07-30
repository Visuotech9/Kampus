package visuotech.com.kampus.attendance.retrofit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import visuotech.com.kampus.attendance.Model.ModelResponse;

import static visuotech.com.kampus.attendance.Constants.ADD_ASSIGNMENT;
import static visuotech.com.kampus.attendance.Constants.ADD_COURSE;
import static visuotech.com.kampus.attendance.Constants.ADD_DEPT;
import static visuotech.com.kampus.attendance.Constants.ADD_DIRECTOR;
import static visuotech.com.kampus.attendance.Constants.ADD_FACULTY;
import static visuotech.com.kampus.attendance.Constants.ADD_HOD;
import static visuotech.com.kampus.attendance.Constants.ADD_SEM;
import static visuotech.com.kampus.attendance.Constants.ADD_STUDENT;
import static visuotech.com.kampus.attendance.Constants.ADD_STUDY_MATERIAL;
import static visuotech.com.kampus.attendance.Constants.ADD_TIME_TABLE;
import static visuotech.com.kampus.attendance.Constants.CHANGE_PSWD;
import static visuotech.com.kampus.attendance.Constants.LOGIN_STATUS;
import static visuotech.com.kampus.attendance.Constants.METHOD_LOGIN;
import static visuotech.com.kampus.attendance.Constants.METHOD_LOGOUT;

/**
 * Created by Himanshu choudhary on 15/10/2018
 */

public interface ApiInterface {

    @GET()
    @Streaming
    Call<ResponseBody> downloadImage(@Url String fileUrl);


    @POST
    Call<JsonElement> postData(@Url String remainingURL, @Body JsonObject jsonObject, @Header("Authorization") String token);

    @GET
    Call<JsonElement> postDataGET(@Url String remainingURL, @QueryMap Map<String, String> map, @Header("Authorization") String token);

    @GET
    Call<JsonElement> postDataTellSid(@Url String remainingURL, @QueryMap Map<String, String> map);

    @GET
    Call<JsonElement> getDataWithoutMap(@Url String remainingURL);

    @GET
    Call<JsonElement> getImageUrl(@Url String remainingURL);


    @DELETE
    Call<JsonElement> callAPIDELETE(@Url String remainingURL, @QueryMap Map<String, String> map, @Header("Authorization") String token);


    @POST
    Call<JsonElement> postDataCustomURL(@Url String remainingURL, @Body JsonObject jsonObject);

    @POST
    Call<JsonElement> postDataCustomURL1(@Url String remainingURL, @Body JsonObject jsonObject);

    @GET
    Call<JsonElement> postDataGET(@Url String remainingURL, @QueryMap Map<String, String> map);


    @Multipart
    @POST(METHOD_LOGIN)
    Call<JsonElement> postLogin(
            @Part("device_id") RequestBody device_id_,
            @Part("user_name") RequestBody email_,
            @Part("password") RequestBody password_);

    @Multipart
    @POST(LOGIN_STATUS)
    Call<JsonElement> postLoginStatus(@Part("user_type") RequestBody user_type_,
                                      @Part("device_id") RequestBody device_id_,
                                      @Part("user_id") RequestBody user_id_,
                                      @Part("organization_id") RequestBody organization_id_);

    @Multipart
    @POST(METHOD_LOGOUT)
    Call<JsonElement> postLogout(@Part("user_type") RequestBody user_type_,
                                 @Part("device_id") RequestBody device_id_,
                                 @Part("user_id") RequestBody user_id_,
                                 @Part("organization_id") RequestBody organization_id_);

    @Multipart
    @POST(CHANGE_PSWD)
    Call<JsonElement> postchngpswd(@Part("user_type") RequestBody user_type_,
                                   @Part("old_pswd") RequestBody old_pswd,
                                   @Part("new_pswd") RequestBody new_pswd,
                                   @Part("cnfirm_pswd") RequestBody confrm_pswd,
                                   @Part("user_id") RequestBody user_id_,
                                   @Part("organization_id") RequestBody organization_id_);

    @Multipart
    @POST(ADD_COURSE)
    Call<JsonElement> postCourse(@Part("course_name") RequestBody courseName_,
                                 @Part("organization_id") RequestBody org_id_);

    @Multipart
    @POST(ADD_DEPT)
//courseName_,org_id_
    Call<JsonElement> postDept(@Part("department_name") RequestBody dept_name_,
                               @Part("organization_id") RequestBody org_id_,//dept_name_,org_id_,cour_id_
                               @Part("course_id") RequestBody cour_id_);


    @Multipart
    @POST(ADD_DIRECTOR)
    Call<JsonElement> addDirector(@Part MultipartBody.Part file,
                                  @Part("director_name") RequestBody name_,
                                  @Part("director_email_id") RequestBody email_,
                                  @Part("director_mobile_no") RequestBody mobile_no_,
                                  @Part("director_address") RequestBody address_,
                                  @Part("diretor_dob") RequestBody dob_,
                                  @Part("director_date_of_joining") RequestBody doj_,
                                  @Part("director_clg_id") RequestBody dir_id_,
                                  @Part("course_id") RequestBody course_id,
                                  @Part("organization_id") RequestBody organization_id_,
                                  @Part("director_gender") RequestBody gender_);

    @Multipart
    @POST(ADD_ASSIGNMENT)
    Call<JsonElement> addAssignment(@Part List<MultipartBody.Part> files,
                                    @Part("title") RequestBody title_,
                                    @Part("description") RequestBody description_,
                                    @Part("start_time") RequestBody starttime_,
                                    @Part("end_time") RequestBody endtime_,
                                    @Part("start_date") RequestBody startdate_,
                                    @Part("end_date") RequestBody enddate_,
                                    @Part("department_id") RequestBody dept_id_,
                                    @Part("course_id") RequestBody course_id_,
                                    @Part("faculty_id") RequestBody userId_,
                                    @Part("hod_id") RequestBody hod_id_,
                                    @Part("director_id") RequestBody director_id_,
                                    @Part("organization_id") RequestBody org_id_,
                                    @Part("sem_id") RequestBody semId_,
                                    @Part("subject_id") RequestBody subId_,
                                    @Part("section_id") RequestBody sectionId_);

    @Multipart
    @POST(ADD_STUDY_MATERIAL)
    Call<JsonElement> addStudymat(@Part List<MultipartBody.Part> files,
                                  @Part("title") RequestBody title_,
                                  @Part("description") RequestBody description_,
                                  @Part("department_id") RequestBody dept_id_,
                                  @Part("course_id") RequestBody course_id_,
                                  @Part("faculty_id") RequestBody userId_,
                                  @Part("hod_id") RequestBody hod_id_,
                                  @Part("director_id") RequestBody director_id_,
                                  @Part("organization_id") RequestBody org_id_,
                                  @Part("sem_id") RequestBody semId_,
                                  @Part("subject_id") RequestBody subId_,
                                  @Part("section_id") RequestBody sectionId_);

    @Multipart
    @POST(ADD_TIME_TABLE)
    Call<JsonElement> addTimeTable(
            @Part("sem_id") RequestBody semId_,
            @Part("section_id") RequestBody sectionId_,
            @Part("day") RequestBody day_,
            @Part("organization_id") RequestBody org_id_,
            @Part("course_id") RequestBody course_id_,
            @Part("department_id") RequestBody dept_id_,
            @Part("hod_id") RequestBody userId_,
            @Part("director_id") RequestBody hod_director_id_,
            @Part("starting_hour[]") ArrayList<String> starthrs,
            @Part("ending_hour[]") ArrayList<String> endhrs,
            @Part("starting_min[]") ArrayList<String> startmin,
            @Part("ending_min[]") ArrayList<String> endmin,
            @Part("faculty_id[]") ArrayList<String> facId,
            @Part("subject_id[]") ArrayList<String> subId);


    @Multipart
    @POST(ADD_SEM)
    Call<JsonElement> addSemister(
            @Part("course_id") RequestBody course_id_,
            @Part("organization_id") RequestBody org_id_,
            @Query("semester_id[]") ArrayList<String> sem_list_string);


    @Multipart
    @POST("./")
    Call<JsonElement> addSubEvent(@Part("") RequestBody useCase,
                                  @Query("event_id[]") ArrayList<String> event_id,
                                  @Query("user_id[]") ArrayList<String> user_id,
                                  @Query("name[]") ArrayList<String> name,
                                  @Query("date_time[]") ArrayList<String> date_time,
                                  @Part("token") RequestBody token,
                                  @Part MultipartBody.Part... profilePic);

    @Multipart
    @POST(ADD_HOD)
    Call<JsonElement> addhOD(@Part MultipartBody.Part file,
                             @Part("hod_name") RequestBody name_,
                             @Part("hod_email_id") RequestBody email_,
                             @Part("hod_mobile_no") RequestBody mobile_no_,
                             @Part("hod_address") RequestBody address_,
                             @Part("hod_dob") RequestBody dob_,
                             @Part("hod_date_of_joining") RequestBody doj_,
                             @Part("hod_clg_id") RequestBody hod_clg_id_,
                             @Part("department_id") RequestBody dept_id_,
                             @Part("organization_id") RequestBody organization_id_,
                             @Part("hod_gender") RequestBody gender_,
                             @Part("course_id") RequestBody course_id_);

    @Multipart
    @POST(ADD_FACULTY)
    Call<JsonElement> addFaculty(@Part MultipartBody.Part file,
                                 @Part("faculty_name") RequestBody name_,
                                 @Part("f_email_id") RequestBody email_,
                                 @Part("f_mobile_no") RequestBody mobile_no_,
                                 @Part("f_address") RequestBody address_,
                                 @Part("f_dob") RequestBody dob_,
                                 @Part("f_date_of_joining") RequestBody doj_,
                                 @Part("faculty_clg_id") RequestBody faculty_clg_id_,
                                 @Part("department_id") RequestBody dept_id_,
                                 @Part("organization_id") RequestBody organization_id_,
                                 @Part("f_gender") RequestBody gender_,
                                 @Part("course_id") RequestBody courseId_,
                                 @Part("hod_id") RequestBody hodId__,
                                 @Part("experience") RequestBody experience_,
                                 @Part("designation") RequestBody designation_);


    @Multipart
    @POST(ADD_STUDENT)
    Call<JsonElement> addStudent(@Part MultipartBody.Part file,
                                 @Part("first_name") RequestBody fname_,
                                 @Part("middle_name") RequestBody mname_,
                                 @Part("last_name") RequestBody lname_,
                                 @Part("s_email_id") RequestBody email_,
                                 @Part("s_mobile_no") RequestBody mobile_no_,
                                 @Part("s_emergency_contact_no") RequestBody emer_mobbile_,
                                 @Part("permanent_address") RequestBody paddress_,
                                 @Part("temporary_address") RequestBody taddress_,
                                 @Part("s_dob") RequestBody dob_,
                                 @Part("admission_date") RequestBody admission_date_,
                                 @Part("fathers_name") RequestBody fat_name_,
                                 @Part("mothers_name") RequestBody mot_name_,
                                 @Part("enrollment_no") RequestBody enrol_no_,
                                 @Part("session_start") RequestBody session_start_,
                                 @Part("session_end") RequestBody session_end_,
                                 @Part("scholarship") RequestBody scholarship_,
                                 @Part("s_caste") RequestBody caste_,
                                 @Part("s_blood_group") RequestBody blood_,
                                 @Part("s_gender") RequestBody gender_,
                                 @Part("department_id") RequestBody dept_id_,
                                 @Part("organization_id") RequestBody organization_id_,
                                 @Part("course_id") RequestBody courseId_,
                                 @Part("hod_id") RequestBody hodId__,
                                 @Part("sem_id") RequestBody semId_,
                                 @Part("section_id") RequestBody sectionId_,
                                 @Part("s_city") RequestBody city_,
                                 @Part("s_state") RequestBody state_,
                                 @Part("hsc_result") RequestBody hsc_,
                                 @Part("ssc_result") RequestBody ssc_,
                                 @Part("diploma") RequestBody diploma_);
/*

    @Multipart
    @POST("addDotEvidence")
    Call<MultipartAddEvidence> addEvidence(@Header("Authorization") String token, @Part("file\"; filename=\"pp.png\" ") RequestBody file, @Part("dotId") RequestBody dotId, @Part("description") RequestBody description);
*/

}

