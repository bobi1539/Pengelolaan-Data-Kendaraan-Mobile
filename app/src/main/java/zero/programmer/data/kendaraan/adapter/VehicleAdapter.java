package zero.programmer.data.kendaraan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.entitites.Vehicle;
import zero.programmer.data.kendaraan.model.ResponseGetVehicle;
import zero.programmer.data.kendaraan.model.ResponseVehicle;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.HolderData> {

    private Context context;
    private List<Vehicle> listVehicle;
    private Vehicle vehicle;

    private String registrationNumber;

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
        vehicle = listVehicle.get(position);

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
        return listVehicle.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{

        TextView textViewName, textViewMerk, textViewPoliceNumber, textViewIsBorrow, textViewRegistrationNumber;
        CardView cardViewVehicle;
        TextView textViewDetailRegistrationNumber, textViewDetailName, textViewDetailMerk, textViewDetailChassisNumber,
                textViewDetailMachineNumber, textViewDetailPoliceNumber, textViewDetailPurchaseDate, textViewDetailValue,
                textViewDetailLocation, textViewDetailCondition, textViewDetailBorrowStatus;
        Vibrator vibrator;
        Button buttonUpdateVehicle, buttonDeleteVehicle;

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

            cardViewVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailVehicle();
                }
            });
        }

        private void detailVehicle(){

            // getar
            vibrator.vibrate(80);

            registrationNumber = textViewRegistrationNumber.getText().toString();

            ApiRequest apiRequest = RetroServer.getRetrofit().create(ApiRequest.class);
            Call<ResponseGetVehicle> getVehicle = apiRequest.getVehicle(registrationNumber);

            getVehicle.enqueue(new Callback<ResponseGetVehicle>() {
                @Override
                public void onResponse(Call<ResponseGetVehicle> call, Response<ResponseGetVehicle> response) {

                    try {

                        Vehicle vehicle = response.body().getData();

                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
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
                        textViewDetailRegistrationNumber.setText(vehicle.getRegistrationNumber());
                        textViewDetailName.setText(vehicle.getName());
                        textViewDetailMerk.setText(vehicle.getMerk());
                        textViewDetailChassisNumber.setText(vehicle.getChassisNumber());
                        textViewDetailMachineNumber.setText(vehicle.getMachineNumber());
                        textViewDetailPoliceNumber.setText(vehicle.getPoliceNumber());

                        // cek jika tanggal pembelian kosong
                        if (vehicle.getPurchaseDate() == null){
                            textViewDetailPurchaseDate.setText("");
                        } else {
                            textViewDetailPurchaseDate.setText(simpleDateFormat.format(vehicle.getPurchaseDate()));
                        }
                        textViewDetailValue.setText(String.valueOf(vehicle.getAcquisitionValue()));
                        textViewDetailLocation.setText(vehicle.getLocation());
                        textViewDetailCondition.setText(vehicle.getCondition());

                        if (!vehicle.getBorrow()){
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
                public void onFailure(Call<ResponseGetVehicle> call, Throwable t) {
                    Toast.makeText(context, "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void updateVehicle(){
            Toast.makeText(context, "Update Vehicle : " + registrationNumber, Toast.LENGTH_SHORT).show();
        }

        private void deleteVehicle(){
            Toast.makeText(context, "Delete Vehicle : " + registrationNumber, Toast.LENGTH_SHORT).show();
        }
    }

}
