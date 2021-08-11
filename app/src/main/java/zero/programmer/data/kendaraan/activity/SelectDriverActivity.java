package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.adapter.SelectDriverAdapter;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.Driver;
import zero.programmer.data.kendaraan.response.ResponseListData;

public class SelectDriverActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private List<Driver> listDriver = new ArrayList<>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Intent intent;

    private String registrationNumber, name, merk, policeNumber, isBorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_driver);

        // get view layout
        recyclerView = findViewById(R.id.recycler_view_select_driver);
        progressBar = findViewById(R.id.progress_bar_select_driver);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_select_driver);

        // get data from intent
        intent = getIntent();
        registrationNumber = intent.getStringExtra("registrationNumber");
        name = intent.getStringExtra("name");
        merk = intent.getStringExtra("merk");
        policeNumber = intent.getStringExtra("policeNumber");
        isBorrow = intent.getStringExtra("isBorrow");

        // set layout manager
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        // set swipe layout
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

        Call<ResponseListData<Driver>> getListDriver = GetConnection.apiRequest.listDriver(ApiKeyData.getApiKey());
        getListDriver.enqueue(new Callback<ResponseListData<Driver>>() {
            @Override
            public void onResponse(Call<ResponseListData<Driver>> call, Response<ResponseListData<Driver>> response) {
                if (response.code() == 404){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SelectDriverActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        listDriver = response.body().getData();
                        recyclerViewAdapter = new SelectDriverAdapter(SelectDriverActivity.this, listDriver);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    } catch (NullPointerException e){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SelectDriverActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseListData<Driver>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SelectDriverActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}