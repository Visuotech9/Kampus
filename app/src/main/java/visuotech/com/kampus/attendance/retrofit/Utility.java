package visuotech.com.kampus.attendance.retrofit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import visuotech.com.kampus.attendance.R;




public class Utility {
    String result = "";
    StringBuilder sb;
    String startdate="";
    public static ProgressDialog progressDialog =null;
    public static AlertDialog.Builder alertbox;
    public static AlertDialog alertDialog;
    public static LayoutInflater _inflater;
    public static View progressdialogview;
    public static Dialog progress_dialog;
    public static RotateAnimation rAnim;
//    GoogleMap googleMap;
    public static String _clientName = "";
    public static String _clientMessageCount = "";
    public static String _clientDisc = "";
    public static String _time = "";
    public static int _clientId = 0;
    public static int _clientFollowStatus = -1;
    public static String _clientImage = "";
    public Context activity;
    public static String _user_image_base64 = "";
    public static String firs_char = "";



    //in this class we will writing code which we need to use more often
    //for eg: fetching current date or showing toast

    //    //change status bar code on lollipop
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void changeStatusBarColor(Activity con)

    {
        Window window = con.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(con.getResources().getColor(R.color.Black));
    }

    public static boolean getAPIVerison() {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;

    }

    public static void cancelDialog()
    {
        if(progress_dialog!=null)
        {
            //System.out.println("Dialog is Canceling");
            progress_dialog.cancel();
            progress_dialog = null;
        }
    }


    //    // ///////////////////show Progress dialog
    public static void showProgressDialog(final Context context)
 {
        progressDialog = new ProgressDialog(context,
                android.R.style.Theme_Panel);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        //		System.out.println("Dialog is Showing");
    }
 ///////////////////show Progress dialog
   /*
   public static void showProgressDialog(final Context context)
    {
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressdialogview = _inflater.inflate(R.layout.progress_bar_layout,null);

        progress_dialog = new Dialog(context,android.R.style.Theme_Panel);
        progress_dialog.setContentView(progressdialogview);
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        progressDialog = new ProgressDialog(context,R.layout.progress_bar_layout);
        View v = View.inflate(context,R.layout.progress_bar_layout, null);
        ImageView rotate = (ImageView) progressdialogview.findViewById(R.id.rotateImage);
    progressDialog.setMessage("Please wait...");
        rAnim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rAnim.setRepeatCount(Animation.INFINITE);
        rAnim.setInterpolator(new LinearInterpolator());
        rAnim.setDuration(700);
    *//* refreshIcon is object of an imageView *//*
        rotate.startAnimation(rAnim);
    		System.out.println("Dialog is Showing");
    }
    */

    public static void sucessDialog(String message,Context context) {

        final Dialog mDialog=new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //without extar space of title
        mDialog.setContentView(R.layout.notification_dailog);
        mDialog.setCanceledOnTouchOutside(false);

        Button btn_ok;
        TextView tv_notification;
        btn_ok= mDialog.findViewById(R.id.btn_ok);
        tv_notification= mDialog.findViewById(R.id.tv_notification);
        tv_notification.setText(message);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();

            }
        });
        mDialog.show();


    }

    public static boolean checkEmail(String email)
    {
        String expression = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*"
                + "+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern emailPattern = Pattern.compile(expression);
        return emailPattern.matcher(email).matches();
    }

//                        //hide key board
    public static void hideKeyBoard(View view, Activity context)
    {
        View focusedView = context.getCurrentFocus();
        if (focusedView != null) {
            ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void showAlertDialog(final String title, String message, final Context context, final boolean redirectToPreviousScreen)
    {
        if (alertDialog != null && alertDialog.isShowing())
        {

        }
        else
        {
            alertbox = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
            alertbox.setMessage(message);
            alertbox.setTitle(title);
            /*alertbox.setTitle(Gravity.CENTER);*/
            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface arg0, int arg1)
                {
                    alertDialog.dismiss();
                }
            });
            alertDialog = alertbox.create();
            alertDialog.show();
        }
    }
    ///////// key board hide



    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

  /*
    public static boolean showDialog(final Context context, String heading, String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(heading);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //return true;

                result = "yes";

                // Write your code here to invoke YES event
                //Toast.makeText(context, "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result = "no";
                // Write your code here to invoke NO event
                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
        if (result.equals("yes")) {
            return true;
        } else {
            return false;
        }

    }

*/
    //----------- hide KeyBoard------------

    public static void hideKeyBoard(Activity context)
    {
        View focusedView = context.getCurrentFocus();
        //	 Toast.makeText(context,"not hide", 1).show();
        if (focusedView != null) {
            //       Toast.makeText(context,"hide", 1).show();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(context.getWindow().getCurrentFocus().getWindowToken(), 0);
        }
    }


    //----------- convert Bitmap To Base64------------

/*
    public static String convertBitmapToBase64(Bitmap image)
    {
        String imageEncoded = "";
        try{
            Bitmap immagex=image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
            //						Log.e("LOOK", imageEncoded);
        }catch(OutOfMemoryError e)
        {
            e.printStackTrace();
            System.out.println("Error 1 : " + e);
        }catch(Exception e1){
            e1.printStackTrace();
            System.out.println("Error 2 : " + e1);
        }

        return imageEncoded;
    }
*/


    //----------- bitmap To Base64------------

    public static String bitmapToBase64(Bitmap bitmap)
    {
        String base64=null;
        try{
            if(bitmap!=null)
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,90, baos);
                byte [] _byteArray = baos.toByteArray();
                base64 = Base64.encodeToString(_byteArray, Base64.DEFAULT);
//				base64 = Base64.encodeToString(_byteArray,Base64.NO_WRAP);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }catch (OutOfMemoryError e)
        {
            e.printStackTrace();
        }
        return base64;
    }

    //----------- image_to_Base64------------

    public static String image_to_Base64(Context context, String filePath) {
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        File sourceFile;
        Compressor compressedImageBitmap;

        try {
            sourceFile = new File(filePath);

            bmp = BitmapFactory.decodeFile(filePath);
            bmp = new Compressor(context).compressToBitmap(sourceFile);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError ome) {

        }
        return encodeString;
    }

    //----------- show Calender ------------

    //public TextView showCalender(Context context,TextView tv) {
  /*
    public static String showCalender(Context context) {
        //here we are calling calendar
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        //tv.setText("");


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //tv_startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        startdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
        //tv.setText(startdate);
        //return tv;
        return startdate;
    }
*/
   /* public String getAddressFromLatlong(Context context,double lat, double longi) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        if (InternetDetect.isConnected(getApplicationContext())) {
            //locationPresenter.runGeocodeAPI(latLng.latitude + "," + latLng.longitude, AppConstants.GEOCODE_API_KEY);
            List<Address> addresses = new ArrayList<>();
            try {
                addresses = geocoder.getFromLocation(lat, longi, 1);

                //latitude= latLng.latitude;
                //longitude = latLng.longitude;
            } catch (IOException e) {
                e.printStackTrace();
            }

                Address address = addresses.get(0);


            if (address != null) {
                sb = new StringBuilder();
                if(address.getMaxAddressLineIndex()>0){
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i) + "\n");
                    }
                }else {
                    try {
                        sb.append(address.getAddressLine(0));
                    } catch (Exception ignored) {}
                }
            }
        }
        return sb.toString();

    }*/

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (activity.getContentResolver() != null) {
            Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    Bitmap ShrinkBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }




    //-----------decode Sampled Bitmap From Resource------------

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
    //-----------calculate In Sample Size------------

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //-----------get image uri------------

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    //-----------Get image path------------

    public String getPath(Uri uri, Context context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    //-----------change Date YMD to DMY------------

    public String changeDateYMDtoDMY(String date) {
        Date myDate;
        String outputDateStr = "";
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            myDate = inputFormat.parse(date);
            outputDateStr = outputFormat.format(myDate);
            System.out.println(outputDateStr);
        } catch (Exception e) {
            //java.text.ParseException: Unparseable date: Geting error
            System.out.println("Excep" + e);
        }
        return outputDateStr;
    }

    //-----------change Date month Name------------

    public String changeDatemonthName(String date) {
        Date myDate;
        String outputDateStr = "";
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            DateFormat outputFormat = new SimpleDateFormat("MMM-dd, yyyy HH:mm");
            myDate = inputFormat.parse(date);
            outputDateStr = outputFormat.format(myDate);
            System.out.println(outputDateStr);
        } catch (Exception e) {
            //java.text.ParseException: Unparseable date: Geting error
            System.out.println("Excep" + e);
        }
        return outputDateStr;
    }
    //-----------changeDate DMY to YMD-----------

    public static String changeDateDMYtoYMD(String date) {

        Date myDate;
        String outputDateStr = "";
        try {
            DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            myDate = inputFormat.parse(date);
            outputDateStr = outputFormat.format(myDate);
            System.out.println(outputDateStr);

        } catch (Exception e) {
            //java.text.ParseException: Unparseable date: Geting error
            System.out.println("Excep" + e);
        }
        return outputDateStr;
    }
    //-----------Email validation------------

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //-----------convert To String------------

    public static String convertToString(ArrayList<String> list) {

        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (String s : list)
        {
            sb.append(delim);
            sb.append(s);
            delim = ",";
        }
        return sb.toString();
    }
    //-----------convert To Array------------

    private ArrayList<String> convertToArray(String string) {
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(",")));
        return list;
    }

    //-----------CONVERT meter To KM--------------

    public String meterToKM(float meters) {
        float km;
        km = meters / 1000;
        return format(km);
    }

    //-----------CONVERT Km To meter--------------

    public String kmToMeter(float km) {
        float meter;
        meter = km * 1000;
        return format(meter);
    }

    //-----------FORMAT------------

    public static String format(Number n) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(2);
        return format.format(n);
    }


    //-----------Dist in km------------

    public static double distanceInKM(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

//    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
//        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
//        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }
    public float distanceFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        Log.e("DISTANCE RADIUS: ", String.valueOf(dist));

        int meterConversion = 1609;
        return new Float(dist * meterConversion).floatValue();
    }

//    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {
//
//        return googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(latitude, longitude))
//                .anchor(0.5f, 0.5f)
//                .title(title)
//                .snippet(snippet)
//                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
//    }
//    private void drawMarker(LatLng latLng,String address) {
//        // Creating an instance of MarkerOptions
//        MarkerOptions markerOptions = new MarkerOptions();
//
//        // Setting latitude and longitude for the marker
//        markerOptions.position(latLng);
//        markerOptions.title(address);
//
//        // Adding marker on the Google Map
//        googleMap.addMarker(markerOptions);
//    }
         public String removeFirstChar(String s){
             return s.substring(1);
            }
    public static String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if(matcher.find()){
            return matcher.group();
        } else {
            return "error";
        }
    }


    public int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i ++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length)?1:-1;
        }

        return res;
    }


}

