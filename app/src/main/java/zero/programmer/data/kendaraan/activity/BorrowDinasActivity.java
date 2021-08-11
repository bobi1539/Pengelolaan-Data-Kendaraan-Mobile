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
import zero.programmer.data.kendaraan.adapter.BorrowVehicleAdapter;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.BorrowVehicle;
import zero.programmer.data.kendaraan.response.ResponseListData;
import zero.programmer.data.kendaraan.session.SessionManager;
import zero.programmer.data.kendaraan.utils.RoleId;

public class BorrowDinasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBorrow;
    private RecyclerView.Adapter recyclerViewBorrowAdapter;
    private RecyclerView.LayoutManager recyclerViewBorrowLayoutManager;
    private List<BorrowVehicle> listBorrowVehicle = new ArrayList<>();
    private ProgressBar progressBarBorrowDinas;
    private FloatingActionButton floatingActionButtonAddBorrowDinas;
    private SwipeRefreshLayout swipeRefreshLayoutBorrowDinas;
    private SessionManager sessionManager;

    private String roleId;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_dinas);

        // get view layout
        recyclerViewBorrow = findViewById(R.id.recycler_view_borrow_dinas);
        progressBarBorrowDinas = findViewById(R.id.borrow_dinas_progress_bar);
        floatingActionButtonAddBorrowDinas = findViewById(R.id.button_add_borrow_dinas);
        swipeRefreshLayoutBorrowDinas = findViewById(R.id.swipe_refresh_borrow_dinas);

        recyclerViewBorrowLayoutManager = new LinearLayoutManager(this);
        recyclerViewBorrow.setLayoutManager(recyclerViewBorrowLayoutManager);

        // session manager
        sessionManager = new SessionManager(this);
        roleId = sessionManager.getUserSessionDetail().get(SessionManager.KEY_ROLE_ID);
        username = sessionManager.getUserSessionDetail().get(SessionManager.KEY_USERNAME);

        // swipe refresh layout
        swipeRefreshLayoutBorrowDinas.setOnRefreshListener(() -> {
            swipeRefreshLayoutBorrowDinas.setRefreshing(true);
            retrieveData();
            swipeRefreshLayoutBorrowDinas.setRefreshing(false);
        });

        // click floating add borrow dinas
        floatingActionButtonAddBorrowDinas.setOnClickListener(v -> startActivity(new Intent(this, SelectVehicleDinasActivity.class)));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        retrieveData();
    }

    private void retrieveData(){

        progressBarBorrowDinas.setVisibility(View.VISIBLE);

        if (roleId.equals(RoleId.ADMIN.toString()) || roleId.equals(RoleId.KABID.toString())){
            getBorrowDataAdmin();
        } else if (roleId.equals(RoleId.KARYAWAN.toString())){
            getBorrowDataEmployee();
        } else {
            progressBarBorrowDinas.setVisibility(View.GONE);
            Toast.makeText(this, "Your access is forbidden", Toast.LENGTH_SHORT).show();
        }

    }

    private void getBorrowDataAdmin(){
        Call<ResponseListData<BorrowVehicle>> getAllListBorrowDinas = GetConnection.apiRequest.listBorrowVehicleDinas(
                ApiKeyData.getApiKey()
        );

        getAllListBorrowDinas.enqueue(new Callback<ResponseListData<BorrowVehicle>>() {
            @Override
            public void onResponse(Call<ResponseListData<BorrowVehicle>> call, Response<ResponseListData<BorrowVehicle>> response) {
                try {
                    if (response.code() == 404){
                        progressBarBorrowDinas.setVisibility(View.GONE);
                        Toast.makeText(BorrowDinasActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                    } else {
                        listBorrowVehicle = response.body().getData();
                        recyclerViewBorrowAdapter = new BorrowVehicleAdapter(BorrowDinasActivity.this, listBorrowVehicle);
                        recyclerViewBorrow.setAdapter(recyclerViewBorrowAdapter);
                        recyclerViewBorrowAdapter.notifyDataSetChanged();
                        progressBarBorrowDinas.setVisibility(View.GONE);
                    }

                } catch (NullPointerException e){
                    progressBarBorrowDinas.setVisibility(View.GONE);
                    Toast.makeText(BorrowDinasActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseListData<BorrowVehicle>> call, Throwable t) {
                progressBarBorrowDinas.setVisibility(View.GONE);
                Toast.makeText(BorrowDinasActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBorrowDataEmployee(){
        Call<ResponseListData<BorrowVehicle>> getListBorrowByUsername = GetConnection
                .apiRequest
                .listBorrowVehicleDinasByUsername(ApiKeyData.getApiKey(),username);

        getListBorrowByUsername.enqueue(new Callback<ResponseListData<BorrowVehicle>>() {
            @Override
            public void onResponse(Call<ResponseListData<BorrowVehicle>> call, Response<ResponseListData<BorrowVehicle>> response) {
                try {
                    if (response.code() == 404){
                        progressBarBorrowDinas.setVisibility(View.GONE);
                        Toast.makeText(BorrowDinasActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                    } else {
                        listBorrowVehicle = response.body().getData();
                        recyclerViewBorrowAdapter = new BorrowVehicleAdapter(BorrowDinasActivity.this, listBorrowVehicle);
                        recyclerViewBorrow.setAdapter(recyclerViewBorrowAdapter);
                        recyclerViewBorrowAdapter.notifyDataSetChanged();
                        progressBarBorrowDinas.setVisibility(View.GONE);
                    }

                } catch (NullPointerException e){
                    progressBarBorrowDinas.setVisibility(View.GONE);
                    Toast.makeText(BorrowDinasActivity.this, "Error : ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseListData<BorrowVehicle>> call, Throwable t) {
                progressBarBorrowDinas.setVisibility(View.GONE);
                Toast.makeText(BorrowDinasActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}