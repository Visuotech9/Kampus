package visuotech.com.kampus.attendance.retrofit;

import android.app.Application;
import android.text.TextUtils;

import com.google.gson.JsonObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/*import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.FacebookSdk;*/

/**
 * Created by lenovo on 11/19/2016.
 */
public class Functions extends Application {

    private static Functions functions;

    public static Functions getClient() {
        if (functions == null) {
            functions = new Functions();
        }
        return functions;
    }

    @Override
    public void onCreate() {
        super.onCreate();
       // FacebookSdk.sdkInitialize(this.getApplicationContext());
    }

    public HashMap<String, String> getHashMapObject(String... nameValuePair) {
        HashMap<String, String> hashMap = null;

        if (null != nameValuePair && nameValuePair.length % 2 == 0) {

            hashMap = new HashMap<>();

            int i = 0;
            while (i < nameValuePair.length) {
                hashMap.put(nameValuePair[i], nameValuePair[i + 1]);
                i += 2;
            }

        }

        return hashMap;
    }

    public JsonObject getJsonMapObject(String... nameValuePair) {
        JsonObject jsonObject = null;

        if (null != nameValuePair && nameValuePair.length % 2 == 0) {

            jsonObject = new JsonObject();

            int i = 0;
            while (i < nameValuePair.length) {
                jsonObject.addProperty(nameValuePair[i], nameValuePair[i + 1]);
                i += 2;
            }

        }

        return jsonObject;
    }

    public String getNextDayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        return sdf.format(tomorrow);
    }

    /* Input in yyyy-MM-dd */
    public String DateInMobileView_(String dateStr) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM yyyy hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(dateStr);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    /* Input in yyyy-MM-dd */
    public String DateInMobileView2(String dateStr) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(dateStr);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /* Input in dd MMM yyyy */
    public String DateInServerFormat(String dateStr) {
        String inputPattern = "dd MMM yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(dateStr);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getTodaysDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        return sdf.format(today);
    }

    public double getRoundToNextInt(String rate) {

        return TextUtils.isEmpty(rate) ? 0 : (int) Math.ceil(Float.valueOf(rate));
    }

    /*public void showConfirmationDialog(Context context, String msg, final
    OnItemClickCustom onItemClickAdapter) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }

        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemClickAdapter.onClick(0, 0, 0);
                    }
                });

        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemClickAdapter.onClick(1, 1, 1);
                    }


                });
        AlertDialog dialog = builder.create();

        builder.show();

    }*/

   /* public void showOkDialog(Context context, String msg, final
    OnItemClickCustom onItemClickAdapter) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }

        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemClickAdapter.onClick(0, 0, 0);
                    }
                });


        AlertDialog dialog = builder.create();

        builder.show();

    }*/


    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();


            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) hexString.append("0");
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException nsae) {
        }

        return hexString.toString();


    }

    private String txnID() {
        Random rand = new Random();
        String rndm = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        return hashCal("SHA-256", rndm).substring(0, 20);
    }
    /*public void displayImage(final Context context, String picturePath, final ImageView imageView) {
        if (TextUtils.isEmpty(picturePath)) {
            return;
        }
        Glide.with(context).load(new File(picturePath)).asBitmap().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }*/

    /*public void showListSelection(Context context, int titleResId, final
    OnItemClickCustom onItemClickAdapter, final ArrayList<String> items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (0 != titleResId) {
            builder.setTitle(titleResId);
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_list_item_1);
        arrayAdapter.addAll(items);

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                String item = arrayAdapter.getItem(position);
                if (null != onItemClickAdapter) {
                    onItemClickAdapter.onClick(position,position, item);

                }

            }
        });

        AlertDialog dialog = builder.create();

        builder.show();

    }*/
/*
    public void showListSelectionWithSearch__(Context context, int titleResId, final
    OnItemClickAdapter onItemClickAdapter, final ArrayList<VendorInfoModel> items) {

        final MyDialog dialog = new MyDialog(context);
        dialog.setContentView(R.layout.search_and_list);
        EditText input = (EditText) dialog.findViewById(R.id.editText);
        RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.genric_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);

        final VendorListAdapter arrayAdapter = new VendorListAdapter(context, new OnItemClickAdapter() {
            @Override
            public void click(int idofClickedItem, Object object, int position) {
                VendorInfoModel vendorInfoModel = (VendorInfoModel) object;

              //  String item = arrayAdapter.getItem(position);
                if (null != onItemClickAdapter) {
                    onItemClickAdapter.click(position, vendorInfoModel, position);
                    dialog.dismiss();
                }
            }
        });


        listView.setAdapter(arrayAdapter);

        arrayAdapter.setList(items);

//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                context, android.R.layout.simple_list_item_1);
//        arrayAdapter.addAll(items);
        listView.setAdapter(arrayAdapter);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayAdapter.filterSet(s.toString());
            }
        });
        arrayAdapter.setList(items);

        *//*AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (0 != titleResId) {
            builder.setTitle(titleResId);
        }
*//*
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialog.show();

    }




    private void ShowKey(EditText ct, Context ctc) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) ctc.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                ct.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideKeyboard(EditText ct, Context ctc) {
        InputMethodManager imm = (InputMethodManager) ctc.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ct.getWindowToken(), 0);

    }

    public void shoEditField__(final Context context, String title, final
    OnItemClickAdapter onItemClickAdapter, String previousRate) {
        final MyDialog dialog = new MyDialog(context);
        dialog.setContentView(R.layout.item_edit_txt_number);


        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        TextView titletv = (TextView) dialog.findViewById(R.id.title);

        titletv.setText(title);

        final EditText rate_et = (EditText) dialog.findViewById(R.id.rate_et);
        if (Double.parseDouble(previousRate) == 0) {
            //  rate_et.setText("" + previousRate);
        } else {
            rate_et.setText("" + previousRate);
        }

        rate_et.setSelection((rate_et.getText().toString().length()));

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(rate_et, context);
                dialog.dismiss();
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(rate_et.getText().toString())) {
                    hideKeyboard(rate_et, context);
                    onItemClickAdapter.click(0, rate_et.getText().toString(), 0);


                    dialog.dismiss();
                }

            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ShowKey(rate_et, context);
            }
        });
        dialog.show();

       *//* AlertDialog.Builder builder = new AlertDialog.Builder(context);
b
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setPadding(20,20,20,20);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!input.getText().toString().equals("")) {
                    onItemClickAdapter.click(0, input.getText().toString().trim(), 0);
                }

            }
        });

        AlertDialog dialog = builder.create();

        builder.show();
*//*
    }

    public double getRoundoff(double i2) {
        return Double.parseDouble(new DecimalFormat("##.##").format(i2));
    }


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void showCalendar(Context ctx, boolean isTodayMax, final DatePickerListner datePickerListner) {

        Calendar mcurrentTime = Calendar.getInstance();
        int year = mcurrentTime.get(Calendar.YEAR);

        final int month = mcurrentTime.get(Calendar.MONTH);
        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mTimePicker;

        mTimePicker = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date dt = new Date();
                String formatedDate = sdf.format(new Date((year - 1900), monthOfYear, dayOfMonth));
                if (null != datePickerListner) {
                    datePickerListner.onPick(formatedDate, Functions.getClient().DateInMobileView(formatedDate));

                }
            }
        }, year, month, day);


        Date dt = new Date();

        if (isTodayMax) {
            mTimePicker.getDatePicker().setMaxDate(dt.getTime());
        }
        mTimePicker.show();
    }*/
}
