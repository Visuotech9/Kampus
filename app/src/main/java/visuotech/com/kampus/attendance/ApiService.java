package visuotech.com.kampus.attendance;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Akshay Raj on 05/02/18.
 * akshay@snowcorp.org
 * www.snowcorp.org
 */

public interface ApiService {
    @Multipart
    @POST("Kampus/Api2.php?apicall=add_assignment")
    Call<ResponseBody> uploadMultiple(
            @Part List<MultipartBody.Part> file,
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
}

