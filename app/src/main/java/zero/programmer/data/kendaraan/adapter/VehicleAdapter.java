package zero.programmer.data.kendaraan.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
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

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.activity.EditVehicleActivity;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.Vehicle;
import zero.programmer.data.kendaraan.response.ResponseOneData;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.HolderData> {

    private final Context context;
    private final List<Vehicle> listVehicle;

    private String registrationNumber;

    private final ApiRequest apiRequest = RetroServer.getRetrofit().create(ApiRequest.class);

    public VehicleAdapter(Context context, List<Vehicle> listVehicle){
        this.context = context;
        this.listVehicle = listVehicle;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_vehicle, parent, false);

        return new HolderData(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        Vehicle vehicle = listVehicle.get(position);

        holder.textViewRegistrationNumber.setText(vehicle.getRegistrationNumber());
        holder.textViewName.setText(vehicle.getName());
        holder.textViewMerk.setText(vehicle.getMerk());
        holder.textViewPoliceNumber.setText(vehicle.getPoliceNumber());

        if (!vehicle.getBorrow()){
            holder.textViewIsBorrow.setText(R.string.borrow_false);
        } else {
            holder.textViewIsBorrow.setText(R.string.borrow_true);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return listVehicle.size();
        } catch (NullPointerException e){
            Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public class HolderData extends RecyclerView.ViewHolder{

        TextView textViewName, textViewMerk, textViewPoliceNumber, textViewIsBorrow, textViewRegistrationNumber;
        CardView cardViewVehicle;
        TextView textViewDetailRegistrationNumber, textViewDetailName, textViewDetailMerk, textViewDetailChassisNumber,
                textViewDetailMachineNumber, textViewDetailPoliceNumber, textViewDetailPurchaseDate, textViewDetailValue,
                textViewDetailLocation, textViewDetailCondition, textViewDetailBorrowStatus;
        Vibrator vibrator;
        Button buttonUpdateVehicle, buttonDeleteVehicle;
        BottomSheetDialog bottomSheetDialog;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            // get view
            textViewName = itemView.findViewById(R.id.card_vehicle_name);
            textViewMerk = itemView.findViewById(R.id.card_vehicle_merk);
            textViewPoliceNumber = itemView.findViewById(R.id.card_vehicle_police_number);
            textViewIsBorrow = itemView.findViewById(R.id.card_vehicle_is_borrow);
            textViewRegistrationNumber = itemView.findViewById(R.id.card_vehicle_registration_number);

            cardViewVehicle = itemView.findViewById(R.id.card_vehicle);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            cardViewVehicle.setOnClickListener(v -> detailVehicle());
        }

        private void detailVehicle(){

            registrationNumber = textViewRegistrationNumber.getText().toString();

            Call<ResponseOneData<Vehicle>> getDetailVehicle = apiRequest.getVehicle(ApiKeyData.getApiKey(), registrationNumber);

            getDetailVehicle.enqueue(new Callback<ResponseOneData<Vehicle>>() {
                @Override
                public void onResponse(Call<ResponseOneData<Vehicle>> call, Response<ResponseOneData<Vehicle>> response) {
                    try {

                        Vehicle vehicleDetail = response.body().getData();

                        bottomSheetDialog = new BottomSheetDialog(
                                context, R.style.BottomSheetDialogTheme
                        );
                        View bottomSheetView = LayoutInflater.from(context)
                                .inflate(
                                        R.layout.layout_bottom_sheet,
                                        itemView.findViewById(R.id.bottomSheetContainer)
                                );

                        // get view
                        textViewDetailRegistrationNumber = bottomSheetView.findViewById(R.id.tv_vehicle_registration_number);
                        textViewDetailName = bottomSheetView.findViewById(R.id.tv_vehicle_name);
                        textViewDetailMerk = bottomSheetView.findViewById(R.id.tv_vehicle_merk);
                        textViewDetailChassisNumber = bottomSheetView.findViewById(R.id.tv_vehicle_chassis_number);
                        textViewDetailMachineNumber = bottomSheetView.findViewById(R.id.tv_vehicle_machine_number);
                        textViewDetailPoliceNumber = bottomSheetView.findViewById(R.id.tv_vehicle_police_number);
                        textViewDetailPurchaseDate = bottomSheetView.findViewById(R.id.tv_vehicle_purchase_date);
                        textViewDetailValue = bottomSheetView.findViewById(R.id.tv_vehicle_value);
                        textViewDetailLocation = bottomSheetView.findViewById(R.id.tv_vehicle_location);
                        textViewDetailCondition = bottomSheetView.findViewById(R.id.tv_vehicle_condition);
                        textViewDetailBorrowStatus = bottomSheetView.findViewById(R.id.tv_vehicle_is_borrow);
                        buttonUpdateVehicle = bottomSheetView.findViewById(R.id.button_update_vehicle);
                        buttonDeleteVehicle = bottomSheetView.findViewById(R.id.button_delete_vehicle);

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

                        // set data detail kendaraan dari database
                        textViewDetailRegistrationNumber.setText(vehicleDetail.getRegistrationNumber());
                        textViewDetailName.setText(vehicleDetail.getName());
                        textViewDetailMerk.setText(vehicleDetail.getMerk());
                        textViewDetailChassisNumber.setText(vehicleDetail.getChassisNumber());
                        textViewDetailMachineNumber.setText(vehicleDetail.getMachineNumber());
                        textViewDetailPoliceNumber.setText(vehicleDetail.getPoliceNumber());

                        // cek jika tanggal pembelian kosong
                        if (vehicleDetail.getPurchaseDate() == null){
                            textViewDetailPurchaseDate.setText("");
                        } else {
                            textViewDetailPurchaseDate.setText(simpleDateFormat.format(vehicleDetail.getPurchaseDate()));
                        }
                        textViewDetailValue.setText(String.valueOf(vehicleDetail.getAcquisitionValue()));
                        textViewDetailLocation.setText(vehicleDetail.getLocation());
                        textViewDetailCondition.setText(vehicleDetail.getCondition());

                        if (!vehicleDetail.getBorrow()){
                            textViewDetailBorrowStatus.setText(R.string.borrow_false);
                        } else {
                            textViewDetailBorrowStatus.setText(R.string.borrow_true);
                        }
                        // -------

                        // click button update
                        buttonUpdateVehicle.setOnClickListener(v -> updateVehicle());
                        // click button delete
                        buttonDeleteVehicle.setOnClickListener(v -> deleteVehicle());

                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();

                    } catch (Exception e){
                        Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseOneData<Vehicle>> call, Throwable t) {
                    Toast.makeText(context, "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void updateVehicle(){

            bottomSheetDialog.dismiss();

            Intent intent = new Intent(context, EditVehicleActivity.class);

            intent.putExtra("registrationNumber", registrationNumber);
            context.startActivity(intent);

        }

        private void deleteVehicle(){
            // membuat alert dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage("Yakin data dihapus?");
            alertDialog.setCancelable(true);

            alertDialog.setPositiveButton("Tidak", (dialog, which) -> dialog.dismiss());

            alertDialog.setNegativeButton("Ya", (dialog, which) -> {

                Call<ResponseOneData<Vehicle>> callDeleteVehicle = apiRequest.deleteVehicle(ApiKeyData.getApiKey(), registrationNumber);

                callDeleteVehicle.enqueue(new Callback<ResponseOneData<Vehicle>>() {
                    @Override
                    public void onResponse(Call<ResponseOneData<Vehicle>> call, Response<ResponseOneData<Vehicle>> response) {
                        try{
                            List<String> messages = response.body().getMessages();
                            Toast.makeText(context, messages.get(0), Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                        } catch (NullPointerException e){
                            Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseOneData<Vehicle>> call, Throwable t) {
                        Toast.makeText(context, "Error : " + t, Toast.LENGTH_SHORT).show();
                    }
                });

            });

            alertDialog.show();
        }
    }

}
