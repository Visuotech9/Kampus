package visuotech.com.kampus.attendance.retrofit;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 11/18/2016.
 */
public abstract class BaseRequestParser {
    public String message;
    public String mResponseCode = "0";
    private JSONObject mRespJSONObject = null;

//    public boolean parseJson(String json) {
//        if (!TextUtils.isEmpty(json)) {
//            try {
//                mRespJSONObject = new JSONObject(json);
//                if (null != mRespJSONObject) {
//                    mResponseCode = mRespJSONObject.optString("code",
//                            "Response code not found");
//                    /*message = mRespJSONObject.optString("message",
//                            "Something going wrong."); */
//                    message = mRespJSONObject.optString("message",
//                            "Something going wrong.");
//
//                    //-------------------this block is for tellsid api---------------
//                    if (mRespJSONObject.toString().equals("{\"response\":\"Succ\"}")) {
//                        return true;
//                    }
//                    if (mResponseCode.equalsIgnoreCase("200")) {
//                        return true;
//                    }
//                    if (mResponseCode.equalsIgnoreCase("400")) {
//                        return false;
//                    }
//                    if (mResponseCode.equalsIgnoreCase("404")) {
//                        /*message = mRespJSONObject.optString("error",
//                                "No Data Found");*/
//                        message = mRespJSONObject.optString("message", "Something going wrong.");
//                        return false;
//                    } else {
//                        return false;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

    public boolean parseJson(String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                mRespJSONObject = new JSONObject(json);
                if (null!=mRespJSONObject){
                    boolean error=false;
                    if (mRespJSONObject.optBoolean("error")==error){
                        return true;
                    } else {
                        message=mRespJSONObject.optString("message");
                        return false;
                    }
                }else{
                    message=mRespJSONObject.optString("message");
                    return false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("ERROR :",e.getMessage());
            }

        }
        message=mRespJSONObject.optString("message");
        return false;
    }

    public Object getDataObject() {
        if (null == mRespJSONObject) {
            return null;
        }
        try {
            return mRespJSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public JSONArray getDataArray() {
        if (null == mRespJSONObject) {
            return null;
        }
        try {
            return mRespJSONObject.getJSONArray("");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



//    public JSONArray getDataArray() {
//        if (null == mRespJSONObject) {
//            return null;
//        }
//        try {
//            if(mRespJSONObject.optJSONArray("data")!=null){
//                return mRespJSONObject.optJSONArray("data");
//            }else {
//                return mRespJSONObject.optJSONArray("response");
//            }
//        } catch (Exception e) {
//            return mRespJSONObject.optJSONArray("data");
//            //e.printStackTrace();
//            //return null;
//        }
//    }
//
//    public Object getDataObject() {
//        if (null == mRespJSONObject) {
//            return null;
//        }
//        try {
//            return mRespJSONObject.optJSONObject("data");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}
