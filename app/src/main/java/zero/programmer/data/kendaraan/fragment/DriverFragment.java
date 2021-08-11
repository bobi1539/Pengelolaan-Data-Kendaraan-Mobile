package zero.programmer.data.kendaraan.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.activity.AddDriverActivity;
import zero.programmer.data.kendaraan.adapter.DriverAdapter;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.Driver;
import zero.programmer.data.kendaraan.response.ResponseListData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewDriver;
    private RecyclerView.Adapter recyclerViewAdapterDriver;
    private RecyclerView.LayoutManager recyclerViewLayoutManagerDriver;
    private List<Driver> listDriver = new ArrayList<>();
    private ProgressBar progressBarDriver;
    private FloatingActionButton floatingActionButtonAddDriver;
    private SwipeRefreshLayout swipeRefreshLayoutDriver;

    public DriverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverFragment newInstance(String param1, String param2) {
        DriverFragment fragment = new DriverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver, container, false);

        recyclerViewDriver = view.findViewById(R.id.recycler_view_driver);
        floatingActionButtonAddDriver = view.findViewById(R.id.button_add_driver);
        progressBarDriver = view.findViewById(R.id.driver_progress_bar);
        swipeRefreshLayoutDriver = view.findViewById(R.id.swipe_refresh_driver);

        recyclerViewLayoutManagerDriver = new LinearLayoutManager(getContext());
        recyclerViewDriver.setLayoutManager(recyclerViewLayoutManagerDriver);

        // swipe refresh layout
        swipeRefreshLayoutDriver.setOnRefreshListener(() -> {
            swipeRefreshLayoutDriver.setRefreshing(true);
            retrieveData();
            swipeRefreshLayoutDriver.setRefreshing(false);
        });

        floatingActionButtonAddDriver.setOnClickListener(v -> startActivity(new Intent(getContext(), AddDriverActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveData();
    }

    // ambil data dari server
    private void retrieveData(){

        progressBarDriver.setVisibility(View.VISIBLE);

        Call<ResponseListData<Driver>> getListDriver = GetConnection.apiRequest.listDriver(ApiKeyData.getApiKey());

        getListDriver.enqueue(new Callback<ResponseListData<Driver>>() {
            @Override
            public void onResponse(Call<ResponseListData<Driver>> call, Response<ResponseListData<Driver>> response) {
                if (response.code() == 404){
                    progressBarDriver.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        listDriver = response.body().getData();
                        recyclerViewAdapterDriver = new DriverAdapter(getContext(), listDriver);
                        recyclerViewDriver.setAdapter(recyclerViewAdapterDriver);
                        recyclerViewAdapterDriver.notifyDataSetChanged();
                        progressBarDriver.setVisibility(View.GONE);
                    } catch (NullPointerException e){
                        progressBarDriver.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseListData<Driver>> call, Throwable t) {
                progressBarDriver.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });

    }
}