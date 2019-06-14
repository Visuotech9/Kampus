package visuotech.com.kampus.attendance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BrandsFilterActivity extends AppCompatActivity{
    private RecyclerView brandRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_brand_item);

        brandRecyclerView = (RecyclerView) findViewById(R.id.brands_lst);

        //RecyclerView layout manager
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        brandRecyclerView.setLayoutManager(recyclerLayoutManager);

        //RecyclerView item decorator
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(brandRecyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        brandRecyclerView.addItemDecoration(dividerItemDecoration);

        //RecyclerView adapater
        ProductFilterRecyclerViewAdapter recyclerViewAdapter = new
                ProductFilterRecyclerViewAdapter(getBrands(),this);
        brandRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private List<FilterModel> getBrands(){
        List<FilterModel> modelList = new ArrayList<FilterModel>();
        modelList.add(new FilterModel("Adidas", true));
        modelList.add(new FilterModel("Nike", false));
        modelList.add(new FilterModel("Reebok", true));
        modelList.add(new FilterModel("Boss", false));
        modelList.add(new FilterModel("Wrangler", true));
        modelList.add(new FilterModel("Lee", false));
        modelList.add(new FilterModel("Levis", false));
        modelList.add(new FilterModel("Polo", false));
        modelList.add(new FilterModel("Tommy Hil", false));
        modelList.add(new FilterModel("Nautica", false));
        modelList.add(new FilterModel("Gas", false));
        modelList.add(new FilterModel("Diesel", false));
        modelList.add(new FilterModel("Gap", false));
        modelList.add(new FilterModel("Flying Machine", false));
        modelList.add(new FilterModel("Pepe Jeans", true));
        modelList.add(new FilterModel("Jack Jones", false));
        modelList.add(new FilterModel("Puma",false));

        return modelList;
    }

}
