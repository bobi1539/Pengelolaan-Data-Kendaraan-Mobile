package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.adapter.SelectVehiclePersonalAdapter;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.Vehicle;
import zero.programmer.data.kendaraan.response.ResponseListData;

public class SelectVehiclePersonalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private List<Vehicle> listVehicle = new ArrayList<>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle_personal);

        // get view layout
        recyclerView = findViewById(R.id.recycler_view_select_vehicle_personal);
        progressBar = findViewById(R.id.progress_bar_select_vehicle_personal);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_select_vehicle_personal);

        // set recycler view to layout manager
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            retrieveData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
    }

    private void retrieveData(){

        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseListData<Vehicle>> getListVehicle = GetConnection.apiRequest.listVehicle(ApiKeyData.getApiKey());

        getListVehicle.enqueue(new Callback<ResponseListData<Vehicle>>() {
            @Override
            public void onResponse(Call<ResponseListData<Vehicle>> call, Response<ResponseListData<Vehicle>> response) {

                if (response.code() == 404){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SelectVehiclePersonalActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        listVehicle = response.body().getData();
                        recyclerViewAdapter = new SelectVehiclePersonalAdapter(SelectVehiclePersonalActivity.this, listVehicle);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    } catch (NullPointerException e){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SelectVehiclePersonalActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseListData<Vehicle>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SelectVehiclePersonalActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });

    }
}