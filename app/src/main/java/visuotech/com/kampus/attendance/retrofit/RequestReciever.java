package visuotech.com.kampus.attendance.retrofit;

/**
 * Created by lenovo on 11/17/2016.
 */
public interface RequestReciever {
    void onSuccess(int requestCode, String Json, Object object);

    void onFailure(int requestCode, String errorCode, String message);

    void onNetworkFailure(int requestCode, String message);
}
