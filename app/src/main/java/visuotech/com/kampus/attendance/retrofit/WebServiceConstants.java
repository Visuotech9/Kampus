package visuotech.com.kampus.attendance.retrofit;


public class WebServiceConstants {
    //http://thechangeconsultancy.co/changeitapp/index.php/Api/postdetails/
//            public static final String URL = "http://demo.emajdoor.com/familytree/api/";
//    public static final String URL = "http://thechangeconsultancy.co/tellsid/index.php/apitellsid/";
    //public static final String URL = "http://live.thechangeconsultancy.co/tellsid/index.php/apitellsid/";  //live url
    public static final String URL = "https://tellsid.softintelligence.co.uk/index.php/apitellsid/postdetails/"; //test api


//    public static final String URL = "http://thechangeconsultancy.co/demo_tellsid/index.php/apitellsid/";
//http://thechangeconsultancy.co/changeitapp/index.php/Api/getuserrecord/
//

    //assets/upload/tellsid/
    //public static final String imgUrl = "http://live.thechangeconsultancy.co/tellsid/assets/upload/tellsid/";  //live
    //public static final String chatUrl = "http://live.thechangeconsultancy.co/tellsid/assets/upload/chatimg/"; //live


    public static final String imgUrl = "http://live.thechangeconsultancy.co/tellsid/index.php/apitellsid/";
    public static final String chatUrl = "http://live.thechangeconsultancy.co/tellsid/index.php/apitellsid/";


    //    public static final String imgUrl = "http://tellsid.softintelligence.co.uk/assets/upload/tellsid/";
//public static final String imgUrl = "http://thechangeconsultancy.co/demo_tellsid/assets/upload/tellsid/";

//    public static final String chatUrl = "http://tellsid.softintelligence.co.uk/assets/upload/chatimg/";
//    public static final String chatUrl = "http://thechangeconsultancy.co/demo_tellsid/assets/upload/chatimg/";


    public static final String METHOD_SEND = "postdetails/";
    public static final String METHOD_GET = "getuserrecord?device_id=";
    public static final String METHOD_RESEND = "resendemailbyid?changeit_id=";
    public static final String METHOD_INBOX = "getinboxitem?email_id=";
    public static final String METHOD_POST_MESSAGE = "sendchatmsg/";
    public static final String METHOD_GET_MESSAGE = "getusermessages?changeit_id=";
    public static final String METHOD_SIGN_UP = "signup/";
    public static final String METHOD_FEEDBACK = "getquestion?/";
    public static final String METHOD_ANS = "getuseranser/";


    public static String getMethodUrl(String methodName) {
        String url = "";
        url = URL + methodName;
        return url;
    }
}
