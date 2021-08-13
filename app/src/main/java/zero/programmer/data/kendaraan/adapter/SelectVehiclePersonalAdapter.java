package zero.programmer.data.kendaraan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.activity.AddBorrowVehiclePersonalActivity;
import zero.programmer.data.kendaraan.entitites.Vehicle;
import zero.programmer.data.kendaraan.model.SendCustomData;

public class SelectVehiclePersonalAdapter extends RecyclerView.Adapter<SelectVehiclePersonalAdapter.HolderData>{

    private final Context context;
    private List<Vehicle> listVehicle;

    private String registrationNumber, name, merk, policeNumber, isBorrow;

    public SelectVehiclePersonalAdapter(Context context, List<Vehicle> listVehicle) {
        this.context = context;
        this.listVehicle = listVehicle;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_select_vehicle_personal, parent, false);
        return new HolderData(view);
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
        try{
            return listVehicle.size();
        } catch (NullPointerException e){
            Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public class HolderData extends RecyclerView.ViewHolder{

        private TextView textViewRegistrationNumber, textViewName, textViewMerk, textViewPoliceNumber,
                textViewIsBorrow;
        private CardView cardViewSelectVehiclePersonal;
        private Vibrator vibrator;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            // get view layout
            textViewRegistrationNumber = itemView.findViewById(R.id.card_select_vehicle_registration_number_personal);
            textViewName = itemView.findViewById(R.id.card_select_vehicle_name_personal);
            textViewMerk = itemView.findViewById(R.id.card_select_vehicle_merk_personal);
            textViewPoliceNumber = itemView.findViewById(R.id.card_select_vehicle_police_number_personal);
            textViewIsBorrow = itemView.findViewById(R.id.card_select_vehicle_is_borrow_personal);
            cardViewSelectVehiclePersonal = itemView.findViewById(R.id.card_select_vehicle_personal);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            // click card
            cardViewSelectVehiclePersonal.setOnClickListener(v -> selectThisVehicle());
        }

        private void selectThisVehicle(){
            // set data to variable
            registrationNumber = textViewRegistrationNumber.getText().toString();
            name = textViewName.getText().toString();
            merk = textViewMerk.getText().toString();
            policeNumber = textViewPoliceNumber.getText().toString();
            isBorrow = textViewIsBorrow.getText().toString();

            if (isBorrow.equals("DIPINJAM")){
                vibrator.vibrate(80);
                Toast.makeText(context, "Kendaraan sedang dipinjam", Toast.LENGTH_SHORT).show();
            } else if (policeNumber.equals("")){
                vibrator.vibrate(80);
                Toast.makeText(
                        context,
                        "Kendaraan tidak bisa dipinjam karena belum memiliki nomor polisi",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                // set data untuk dikirim ke input pinjam kendaraan
                SendCustomData.registrationNumber = registrationNumber;
                SendCustomData.vehicleName = name;
                SendCustomData.merk = merk;
                SendCustomData.policeNumber = policeNumber;

                context.startActivity(new Intent(context, AddBorrowVehiclePersonalActivity.class));
            }
        }
    }
}
