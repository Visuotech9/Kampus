package visuotech.com.kampus.attendance;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection
{
    public static boolean checkNetworkStatus(Context context)
    {
        boolean HaveConnectedWifi = false;
        boolean HaveConnectedMobile = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo)
            {
                if(ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        HaveConnectedWifi = true;
                if(ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        HaveConnectedMobile = true;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return HaveConnectedWifi || HaveConnectedMobile;
    }
}
