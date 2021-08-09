package zero.programmer.data.kendaraan.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.activity.EditProfileActivity;
import zero.programmer.data.kendaraan.activity.LoginActivity;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.User;
import zero.programmer.data.kendaraan.response.ResponseOneData;
import zero.programmer.data.kendaraan.session.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button buttonLogout, buttonEditProfile;
    private TextView textViewUsername, textViewFullName, textViewEmployeeNumber,
                textViewPosition, textViewWorkUnit, textViewRoleId;
    private SessionManager sessionManager;
    private String username;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewUsername = view.findViewById(R.id.tv_profil_username);
        textViewFullName = view.findViewById(R.id.tv_profil_full_name);
        textViewEmployeeNumber = view.findViewById(R.id.tv_profil_employee_number);
        textViewPosition = view.findViewById(R.id.tv_profil_position);
        textViewWorkUnit = view.findViewById(R.id.tv_profil_work_unit);
        textViewRoleId = view.findViewById(R.id.tv_profil_role_id);
        buttonLogout = view.findViewById(R.id.button_logout);
        buttonEditProfile = view.findViewById(R.id.button_edit_profile);

        // ambil username dari session
        sessionManager = new SessionManager(getContext());
        username = sessionManager.getUserSessionDetail().get(SessionManager.KEY_USERNAME);

        // set Data profile from server
        setProfileFromServer();
        
        // logout
        buttonLogout.setOnClickListener(v -> logoutUser());
        
        // edit profil
        buttonEditProfile.setOnClickListener(v -> editProfile());

        return view;
    }
    
    private void editProfile(){
        // kirim data lewat intent ke edit profile agar tidak perlu call ke server lagi di edit profile
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("fullName", textViewFullName.getText().toString());
        intent.putExtra("employeeNumber", textViewEmployeeNumber.getText().toString());
        intent.putExtra("position", textViewPosition.getText().toString());
        intent.putExtra("workUnit", textViewWorkUnit.getText().toString());
        intent.putExtra("roleId", textViewRoleId.getText().toString());
        startActivity(intent);
    }

    private void setProfileFromServer(){

        Call<ResponseOneData<User>> getDetailUser = GetConnection.apiRequest.getUser(ApiKeyData.getApiKey(), username);
        getDetailUser.enqueue(new Callback<ResponseOneData<User>>() {
            @Override
            public void onResponse(Call<ResponseOneData<User>> call, Response<ResponseOneData<User>> response) {
                try {

                    User detailUser = response.body().getData();

                    textViewUsername.setText(detailUser.getUsername());
                    textViewFullName.setText(detailUser.getFullName());
                    textViewEmployeeNumber.setText(detailUser.getEmployeeNumber());
                    textViewPosition.setText(detailUser.getPosition());
                    textViewWorkUnit.setText(detailUser.getWorkUnit());
                    textViewRoleId.setText(String.valueOf(detailUser.getRoleId()));

                } catch (NullPointerException e){
                    Toast.makeText(getContext(), "Error : " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOneData<User>> call, Throwable t) {
                Toast.makeText(getContext(), "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setMessage("Yakin anda ingin logout?");
        alertDialog.setCancelable(true);

        alertDialog.setPositiveButton("Tidak", (dialog, which) -> dialog.dismiss());

        alertDialog.setNegativeButton("Ya", (dialog, which) -> {
            new SessionManager(getContext()).createLogoutSession();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
            Toast.makeText(getContext(), "Logout berhasil", Toast.LENGTH_SHORT).show();
        });
        alertDialog.show();
    }
}