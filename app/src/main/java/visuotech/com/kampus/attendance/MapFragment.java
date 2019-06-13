package visuotech.com.kampus.attendance;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {


    public GoogleMap map;
    MapFragment context;
    public double longitude1;
    public double latitude1;
    public double longitude2;
    public double latitude2;
    public String address;
    public String add;
    private static final String PREF_NAME = "logininf";

    public String user_id, date,date_map;
    ImageView iv_back, iv_map, iv_list;
    //------------------------------
    private int TOTAL_PAGES;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private boolean isLoading;
    private static final String TAG = "USERLOCATION";
    private List<Address> addresses;
    ArrayList<String> prgmName;
    LatLng location1;
    double radius = 100; // 100 meters
    private String mParam1;
    SharedPreferences shared;
    ArrayList<String> arrPackage;

    @SuppressLint("ValidFragment")
    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                map = mMap;
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setZoomGesturesEnabled(true);


                latitude1= Double.parseDouble("22.7533");
                longitude1= Double.parseDouble("75.8937");
//                createMarker(latitude1, longitude1, "indore", "22-03-2019");//                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(list.get(0).getLatitude()), Double.parseDouble(list.get(0).getLongitude()))));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude1,longitude1), 17.0f));


            }

        });

        return rootView;
    }




//    protected Marker createMarker(double latitude, double longitude, String title, String snippet) {
//
//        return map.addMarker(new MarkerOptions()
//                .position(new LatLng(latitude, longitude))
//                .anchor(0.5f, 0.5f)
//                .title(title)
//                .snippet(snippet));
////                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
//    }


}
