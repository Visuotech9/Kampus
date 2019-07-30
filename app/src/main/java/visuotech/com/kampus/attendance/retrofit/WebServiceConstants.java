package visuotech.com.kampus.attendance.retrofit;


public class WebServiceConstants {

    //----------LIVE---------------
    public static final String BASE_URL = "https://thevarsity.in/";

    //----------TEST---------------
//    public static final String BASE_URL = "http://192.168.1.64/";

    public static final String METHOD_SEND = "postdetails/";
    public static final String METHOD_GET = "getuserrecord?device_id=";
    public static final String METHOD_RESEND = "resendemailbyid?changeit_id=";
    public static final String METHOD_INBOX = "getinboxitem?email_id=";
    public static final String METHOD_POST_MESSAGE = "sendchatmsg/";
    public static final String METHOD_GET_MESSAGE = "getusermessages?changeit_id=";
    public static final String METHOD_SIGN_UP = "signup/";
    public static final String METHOD_FEEDBACK = "getquestion?/";
    public static final String METHOD_ANS = "getuseranser/";
}
