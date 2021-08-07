package zero.programmer.data.kendaraan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.User;
import zero.programmer.data.kendaraan.response.ResponseOneData;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.HolderData>{

    private final Context context;
    private List<User> listUser;

    private String username;

    private final ApiRequest apiRequest = RetroServer.getRetrofit().create(ApiRequest.class);

    public UserAdapter(Context context, List<User> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent,false);
        return new HolderData(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        User user = listUser.get(position);
        holder.textViewFullName.setText(user.getFullName());
        holder.textViewUsername.setText(user.getUsername());
        holder.textViewEmployeeNumber.setText(user.getEmployeeNumber());
        holder.textViewPosition.setText(user.getPosition());
    }

    @Override
    public int getItemCount() {
        try{
            return listUser.size();
        } catch (NullPointerException e){
            Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public class HolderData extends RecyclerView.ViewHolder{

        TextView textViewFullName, textViewUsername, textViewEmployeeNumber, textViewPosition;
        TextView textViewDetailFullName, textViewDetailUsername, textViewDetailEmployeeNumber,
                textViewDetailPosition, textViewDetailWorkUnit, textViewDetailRoleId;
        Button buttonUpdateUser, buttonDeleteUser;
        CardView cardViewUser;
        BottomSheetDialog bottomSheetDialog;


        public HolderData(@NonNull View itemView) {
            super(itemView);
            textViewFullName = itemView.findViewById(R.id.card_user_full_name);
            textViewUsername = itemView.findViewById(R.id.card_user_username);
            textViewEmployeeNumber = itemView.findViewById(R.id.card_user_employee_number);
            textViewPosition = itemView.findViewById(R.id.card_user_position);

            cardViewUser = itemView.findViewById(R.id.card_user);

            cardViewUser.setOnClickListener(v -> {
                detailUser();
            });
        }

        private void detailUser(){
            username = textViewUsername.getText().toString();

            Call<ResponseOneData<User>> getDetailUser = apiRequest.getUser(ApiKeyData.getApiKey(), username);

            getDetailUser.enqueue(new Callback<ResponseOneData<User>>() {
                @Override
                public void onResponse(Call<ResponseOneData<User>> call, Response<ResponseOneData<User>> response) {
                    try {

                        User userDetail = response.body().getData();

                        // bottom sheet dialog
                        bottomSheetDialog = new BottomSheetDialog(
                                context, R.style.BottomSheetDialogTheme
                        );
                        View bottomSheetView = LayoutInflater.from(context)
                                .inflate(
                                        R.layout.bottom_sheet_user,
                                        itemView.findViewById(R.id.bottom_sheet_user)
                                );

                        // get view user detail
                        textViewDetailUsername = bottomSheetView.findViewById(R.id.tv_user_username);
                        textViewDetailFullName = bottomSheetView.findViewById(R.id.tv_user_full_name);
                        textViewDetailEmployeeNumber = bottomSheetView.findViewById(R.id.tv_user_employee_number);
                        textViewDetailPosition = bottomSheetView.findViewById(R.id.tv_user_position);
                        textViewDetailWorkUnit = bottomSheetView.findViewById(R.id.tv_user_work_unit);
                        textViewDetailRoleId = bottomSheetView.findViewById(R.id.tv_user_role_id);
                        buttonUpdateUser = bottomSheetView.findViewById(R.id.button_edit_user);
                        buttonDeleteUser = bottomSheetView.findViewById(R.id.button_delete_user);

                        // set detail user dari response
                        textViewDetailUsername.setText(userDetail.getUsername());
                        textViewDetailFullName.setText(userDetail.getFullName());
                        textViewDetailEmployeeNumber.setText(userDetail.getEmployeeNumber());
                        textViewDetailPosition.setText(userDetail.getPosition());
                        textViewDetailWorkUnit.setText(userDetail.getWorkUnit());
                        textViewDetailRoleId.setText(String.valueOf(userDetail.getRoleId()));

                        // button update
                        buttonUpdateUser.setOnClickListener(v -> editUser());
                        // button delete
                        buttonDeleteUser.setOnClickListener(v -> deleteUser());

                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();

                    } catch (Exception e){
                        Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseOneData<User>> call, Throwable t) {
                    Toast.makeText(context, "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void editUser(){
            Toast.makeText(context, "Update user", Toast.LENGTH_SHORT).show();
        }

        private void deleteUser(){
            // membuat alert dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage("Yakin data dihapus?");
            alertDialog.setCancelable(true);

            alertDialog.setPositiveButton("Tidak", (dialog, which) -> dialog.dismiss());

            alertDialog.setNegativeButton("Ya", ((dialog, which) -> {

                Call<ResponseOneData<User>> deleteUserData = apiRequest.deleteUser(ApiKeyData.getApiKey(), username);

                deleteUserData.enqueue(new Callback<ResponseOneData<User>>() {
                    @Override
                    public void onResponse(Call<ResponseOneData<User>> call, Response<ResponseOneData<User>> response) {

                        try {
                            Toast.makeText(context, response.body().getMessages().get(0), Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                        } catch (NullPointerException e){
                            Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseOneData<User>> call, Throwable t) {
                        Toast.makeText(context, "Error : " + t, Toast.LENGTH_SHORT).show();
                    }
                });

            }));
            alertDialog.show();
        }
    }

}
