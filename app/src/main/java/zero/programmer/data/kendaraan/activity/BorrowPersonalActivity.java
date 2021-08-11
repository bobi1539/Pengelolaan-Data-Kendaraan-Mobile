package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class BorrowPersonalActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBorrow;
    private RecyclerView.Adapter recyclerViewBorrowAdapter;
    private RecyclerView.LayoutManager recyclerViewBorrowLayoutManager;
    private List<BorrowVehicle> listBorrowVehicle = new ArrayList<>();
    private ProgressBar progressBarBorrowPersonal;
    private FloatingActionButton floatingActionButtonAddBorrowPersonal;
    private SwipeRefreshLayout swipeRefreshLayoutBorrowPersonal;
    private SessionManager sessionManager;

    private String roleId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_personal);

        // get view layout
        recyclerViewBorrow = findViewById(R.id.recycler_view_borrow_personal);
        progressBarBorrowPersonal = findViewById(R.id.borrow_personal_progress_bar);
        floatingActionButtonAddBorrowPersonal = findViewById(R.id.button_add_borrow_personal);
        swipeRefreshLayoutBorrowPersonal = findViewById(R.id.swipe_refresh_borrow_personal);

        // set layout manager
        recyclerViewBorrowLayoutManager = new LinearLayoutManager(this);
        recyclerViewBorrow.setLayoutManager(recyclerViewBorrowLayoutManager);

        // session manager
        sessionManager = new SessionManager(this);
        roleId = sessionManager.getUserSessionDetail().get(SessionManager.KEY_ROLE_ID);
        username = sessionManager.getUserSessionDetail().get(SessionManager.KEY_USERNAME);

        // swipe refresh layout
        swipeRefreshLayoutBorrowPersonal.setOnRefreshListener(() -> {
            swipeRefreshLayoutBorrowPersonal.setRefreshing(true);
            retrieveData();
            swipeRefreshLayoutBorrowPersonal.setRefreshing(false);
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        retrieveData();
    }

    private void retrieveData(){
        progressBarBorrowPersonal.setVisibility(View.VISIBLE);

        if (roleId.equals(RoleId.ADMIN.toString()) || roleId.equals(RoleId.KABID.toString())){
            getBorrowDataAdmin();
        } else if (roleId.equals(RoleId.KARYAWAN.toString())){
            getBorrowDataEmployee();
        } else {
            Toast.makeText(this, "Your access is forbidden", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBorrowDataAdmin(){
        Call<ResponseListData<BorrowVehicle>> getAllBorrowPersonal = GetConnection.apiRequest.listBorrowVehiclePersonal(
                ApiKeyData.getApiKey()
        );
        getAllBorrowPersonal.enqueue(new Callback<ResponseListData<BorrowVehicle>>() {
            @Override
            public void onResponse(Call<ResponseListData<BorrowVehicle>> call, Response<ResponseListData<BorrowVehicle>> response) {
                try {

                    if (response.code() == 404){
                        progressBarBorrowPersonal.setVisibility(View.GONE);
                        Toast.makeText(BorrowPersonalActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                    } else {
                        listBorrowVehicle = response.body().getData();
                        setRecyclerViewBorrow(listBorrowVehicle);
                    }

                } catch (NullPointerException e){
                    progressBarBorrowPersonal.setVisibility(View.GONE);
                    Toast.makeText(BorrowPersonalActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseListData<BorrowVehicle>> call, Throwable t) {
                progressBarBorrowPersonal.setVisibility(View.GONE);
                Toast.makeText(BorrowPersonalActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBorrowDataEmployee(){
        Call<ResponseListData<BorrowVehicle>> getBorrowPersonalByUsername = GetConnection
                .apiRequest
                .listBorrowVehiclePersonalByUsername(ApiKeyData.getApiKey(), username);

        getBorrowPersonalByUsername.enqueue(new Callback<ResponseListData<BorrowVehicle>>() {
            @Override
            public void onResponse(Call<ResponseListData<BorrowVehicle>> call, Response<ResponseListData<BorrowVehicle>> response) {
                try {
                    if (response.code() == 404){
                        progressBarBorrowPersonal.setVisibility(View.GONE);
                        Toast.makeText(BorrowPersonalActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                    } else {
                        listBorrowVehicle = response.body().getData();
                        setRecyclerViewBorrow(listBorrowVehicle);
                    }
                } catch (NullPointerException e){
                    progressBarBorrowPersonal.setVisibility(View.GONE);
                    Toast.makeText(BorrowPersonalActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseListData<BorrowVehicle>> call, Throwable t) {
                progressBarBorrowPersonal.setVisibility(View.GONE);
                Toast.makeText(BorrowPersonalActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * untuk set recycler view
     * @param listBorrowVehicle
     */
    private void setRecyclerViewBorrow(List<BorrowVehicle> listBorrowVehicle){
        recyclerViewBorrowAdapter = new BorrowVehicleAdapter(BorrowPersonalActivity.this, listBorrowVehicle);
        recyclerViewBorrow.setAdapter(recyclerViewBorrowAdapter);
        recyclerViewBorrowAdapter.notifyDataSetChanged();
        progressBarBorrowPersonal.setVisibility(View.GONE);

    }
}