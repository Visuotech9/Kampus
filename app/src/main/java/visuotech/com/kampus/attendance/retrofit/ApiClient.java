package visuotech.com.kampus.attendance.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final String ROOT_URL = "https://collectorexpress.in/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientChatimage() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(WebServiceConstants.chatUrl)
                    .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static Retrofit getCustomClient(String baseURL) {
//        String url="http://collectorexpress.in/SOH/PHP/Api2.php?apicall=";
//        String baseUrl=baseURL.split(".in/")[0]+".in/";
//        String queryUrl=baseURL.split(".in")[1];


        Retrofit retrofit_ = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(getRequestHeader())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit_;
    }

    public static OkHttpClient okHttpClient = null;

    public static OkHttpClient getRequestHeader() {
        if (null == okHttpClient) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
}
