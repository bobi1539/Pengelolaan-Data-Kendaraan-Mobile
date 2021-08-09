package zero.programmer.data.kendaraan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import zero.programmer.data.kendaraan.activity.EditDriverActivity;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.Driver;
import zero.programmer.data.kendaraan.response.ResponseOneData;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.HolderData>{

    private final Context context;
    private List<Driver> listDriver;

    private String idDriver;

    private final ApiRequest apiRequest = RetroServer.getRetrofit().create(ApiRequest.class);

    public DriverAdapter(Context context, List<Driver> listDriver) {
        this.context = context;
        this.listDriver = listDriver;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view card ke rycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_driver, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        // set data dari class user ke card driver
        Driver driver = listDriver.get(position);
        holder.textViewIdDriver.setText(driver.getIdDriver());
        holder.textViewFullName.setText(driver.getFullName());
        holder.textViewPhoneNumber.setText(driver.getPhoneNumber());

        if (!driver.getOnDuty()){
            holder.textViewIsOnDuty.setText(R.string.driver_not_on_duty);
        } else {
            holder.textViewIsOnDuty.setText(R.string.driver_on_duty);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return listDriver.size();
        } catch (NullPointerException e){
            Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public class HolderData extends RecyclerView.ViewHolder{

        TextView textViewIdDriver, textViewFullName, textViewPhoneNumber, textViewIsOnDuty;
        TextView textViewDetailIdDriver, textViewDetailFullName, textViewDetailPhoneNumber,
                textViewDetailAddress, textViewDetailIsOnDuty;
        CardView cardViewDriver;
        Button buttonEditDriver, buttonDeleteDriver;
        BottomSheetDialog bottomSheetDialog;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            // get view recycler dari card
            textViewIdDriver = itemView.findViewById(R.id.card_driver_id);
            textViewFullName = itemView.findViewById(R.id.card_driver_full_name);
            textViewPhoneNumber = itemView.findViewById(R.id.card_driver_phone_number);
            textViewIsOnDuty = itemView.findViewById(R.id.card_driver_is_on_duty);
            cardViewDriver = itemView.findViewById(R.id.card_driver);

            cardViewDriver.setOnClickListener(v -> detailDriver());
        }

        private void detailDriver(){
            idDriver = textViewIdDriver.getText().toString();

            Call<ResponseOneData<Driver>> getDetailDriver = apiRequest.getDriver(ApiKeyData.getApiKey(), idDriver);

            getDetailDriver.enqueue(new Callback<ResponseOneData<Driver>>() {
                @Override
                public void onResponse(Call<ResponseOneData<Driver>> call, Response<ResponseOneData<Driver>> response) {
                    try {

                        Driver driverDetail = response.body().getData();

                        // bottom sheet dialog
                        bottomSheetDialog = new BottomSheetDialog(
                                context, R.style.BottomSheetDialogTheme
                        );
                        View bottomSheetView = LayoutInflater.from(context)
                                .inflate(
                                        R.layout.bottom_sheet_driver,
                                        itemView.findViewById(R.id.bottom_sheet_driver)
                                );

                        // get view driver detail
                        textViewDetailIdDriver = bottomSheetView.findViewById(R.id.tv_driver_id);
                        textViewDetailFullName = bottomSheetView.findViewById(R.id.tv_driver_full_name);
                        textViewDetailPhoneNumber = bottomSheetView.findViewById(R.id.tv_driver_phone_number);
                        textViewDetailIsOnDuty = bottomSheetView.findViewById(R.id.tv_driver_status);
                        textViewDetailAddress = bottomSheetView.findViewById(R.id.tv_driver_address);
                        buttonEditDriver = bottomSheetView.findViewById(R.id.button_edit_driver);
                        buttonDeleteDriver = bottomSheetView.findViewById(R.id.button_delete_driver);

                        // set detail driver from reponse
                        textViewDetailIdDriver.setText(driverDetail.getIdDriver());
                        textViewDetailFullName.setText(driverDetail.getFullName());
                        textViewDetailPhoneNumber.setText(driverDetail.getPhoneNumber());

                        // cek status driver
                        if (!driverDetail.getOnDuty()){
                            textViewDetailIsOnDuty.setText(R.string.driver_not_on_duty);
                        } else {
                            textViewDetailIsOnDuty.setText(R.string.driver_on_duty);
                        }
                        textViewDetailAddress.setText(driverDetail.getAddress());

                        // set button update
                        buttonEditDriver.setOnClickListener(v -> editDriver());
                        // set button delete
                        buttonDeleteDriver.setOnClickListener(v -> deleteDriver());

                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();


                    } catch (NullPointerException e){
                        Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseOneData<Driver>> call, Throwable t) {
                    Toast.makeText(context, "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void editDriver(){
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(context, EditDriverActivity.class);
            intent.putExtra("idDriver", idDriver);
            context.startActivity(intent);

        }

        private void deleteDriver(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage("Yakin data dihapus?");
            alertDialog.setCancelable(true);

            alertDialog.setPositiveButton("Tidak", ((dialog, which) -> dialog.dismiss()));

            alertDialog.setNegativeButton("Ya", (dialog, which) -> {

                Call<ResponseOneData<Driver>> deleteDataDriver = apiRequest.deleteDriver(ApiKeyData.getApiKey(), idDriver);
                deleteDataDriver.enqueue(new Callback<ResponseOneData<Driver>>() {
                    @Override
                    public void onResponse(Call<ResponseOneData<Driver>> call, Response<ResponseOneData<Driver>> response) {
                        try {
                            Toast.makeText(context, response.body().getMessages().get(0), Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                        } catch (NullPointerException e){
                            Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseOneData<Driver>> call, Throwable t) {
                        Toast.makeText(context, "Error : " + t, Toast.LENGTH_SHORT).show();
                    }
                });
            });
            alertDialog.show();
        }
    }
}
