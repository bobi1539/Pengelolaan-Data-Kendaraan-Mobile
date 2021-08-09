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
import zero.programmer.data.kendaraan.activity.AddUserActivity;
import zero.programmer.data.kendaraan.adapter.UserAdapter;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.User;
import zero.programmer.data.kendaraan.response.ResponseListData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewUser;
    private RecyclerView.Adapter recyclerViewAdapterUser;
    private RecyclerView.LayoutManager recyclerViewLayoutManagerUser;
    private List<User> listUser = new ArrayList<>();
    private ProgressBar progressBarUser;
    private FloatingActionButton floatingActionButtonAddUser;
    private SwipeRefreshLayout swipeRefreshLayoutUser;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        recyclerViewUser = view.findViewById(R.id.recycler_view_user);
        progressBarUser = view.findViewById(R.id.user_progress_bar);
        swipeRefreshLayoutUser = view.findViewById(R.id.swipe_refresh_user);
        floatingActionButtonAddUser = view.findViewById(R.id.button_add_user);

        recyclerViewLayoutManagerUser = new LinearLayoutManager(getContext());
        recyclerViewUser.setLayoutManager(recyclerViewLayoutManagerUser);

        // swipe refresh layout
        swipeRefreshLayoutUser.setOnRefreshListener(() -> {
            swipeRefreshLayoutUser.setRefreshing(true);
            retrieveData();
            swipeRefreshLayoutUser.setRefreshing(false);
        });

        floatingActionButtonAddUser.setOnClickListener(v -> startActivity(new Intent(getContext(), AddUserActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveData();
    }

    private void retrieveData(){
        progressBarUser.setVisibility(View.VISIBLE);

        Call<ResponseListData<User>> getDataUser = GetConnection.apiRequest.listUser(ApiKeyData.getApiKey());

        getDataUser.enqueue(new Callback<ResponseListData<User>>() {
            @Override
            public void onResponse(Call<ResponseListData<User>> call, Response<ResponseListData<User>> response) {

                try {
                    listUser = response.body().getData();
                } catch (NullPointerException e){
                    progressBarUser.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error : " + e, Toast.LENGTH_SHORT).show();
                }

                recyclerViewAdapterUser = new UserAdapter(getContext(), listUser);
                recyclerViewUser.setAdapter(recyclerViewAdapterUser);
                recyclerViewAdapterUser.notifyDataSetChanged();
                progressBarUser.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseListData<User>> call, Throwable t) {
                progressBarUser.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}